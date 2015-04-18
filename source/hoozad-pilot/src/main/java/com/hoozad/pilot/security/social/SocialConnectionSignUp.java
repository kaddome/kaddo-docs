package com.hoozad.pilot.security.social;

import com.hoozad.pilot.domain.ExternalAccount;
import com.hoozad.pilot.domain.ExternalAccountProvider;
import com.hoozad.pilot.domain.User;
import com.hoozad.pilot.repository.UserRepository;
import com.hoozad.pilot.service.UserService;
import com.hoozad.pilot.web.rest.dto.UserDTO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.ApiException;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * An implementation of ConnectionSignUp that resolves the User login for a social
 * Connection by searching for an ExternalAccount that matches the Connection.
 *
 * @see com.hoozad.pilot.domain.User#getLogin()
 * @see com.hoozad.pilot.domain.ExternalAccount
 */
@Component("socialConnectionSignUp")
public class SocialConnectionSignUp implements ConnectionSignUp {
    private final Logger log = LoggerFactory.getLogger(SocialConnectionSignUp.class);

    @Inject
    private UserRepository userRepository;
    @Inject
    private UserService userService;

    /**
     * Map a Connection to an existing User by searching for an ExternalAccount that matches
     * the Connection's {@link org.springframework.social.connect.ConnectionKey ConnectionKey}.  For example,
     * given a ConnectionKey with a providerId of "google" and a providerUserId of "12345691011",
     * search for an ExternalAccount that matches and return the {@link com.hoozad.pilot.domain.User#getLogin() login}
     * associated with the account.
     *
     * @param connection a non-null Connection
     * @return a User login if the Connection matched an existing User, null otherwise
     */
    @Transactional(readOnly = true)
    @Override
    public String execute(Connection<?> connection) {
        ConnectionKey key = connection.getKey();
        String providerName = key.getProviderId();
        String externalId = key.getProviderUserId();
        ExternalAccountProvider externalProvider = ExternalAccountProvider.caseInsensitiveValueOf(providerName);

        // try to find an internal user based on the social ConnectionKey.  for example, something like "google" "12345691011".
        User user = userRepository.getUserByExternalAccount(externalProvider, externalId).orElse(null);
        if (user == null) {
            user = registerExternalAccount(connection);
        }
        String internalLogin = user.getLogin();
        log.debug("Returning existing internal User '{}' for external login '{}' from {}", internalLogin, externalId, externalProvider);
        return internalLogin;

    }

    User registerExternalAccount(Connection<?> connection) {
        log.debug("Creating user from previous external authentication");
        UserDTO externalAuthDTO = externalAuthAsUserDTO(connection);
        ExternalAccount externalAccount = externalAuthDTO.getExternalAccounts().iterator().next();
        return userService.createUserInformation(
            getLoginForConnection(connection),
            externalAuthDTO.getFirstName(), externalAuthDTO.getLastName(),
            externalAuthDTO.getEmail(),
            null,
            externalAccount
        );
    }

    private String getLoginForConnection(Connection<?> connection) {
        ConnectionKey key = connection.getKey();
        String providerName = key.getProviderId();
        String externalId = key.getProviderUserId();
        return providerName.toLowerCase() + "_" + externalId;
    }

    UserDTO externalAuthAsUserDTO(Connection<?> connection) {

        // build a new UserDTO from the external provider's version of the User
        UserProfile profile = connection.fetchUserProfile();
        String firstName = profile.getFirstName();
        String lastName = profile.getLastName();
        String email = profile.getEmail();

        // build the ExternalAccount from the ConnectionKey
        String externalAccountProviderName = connection.getKey().getProviderId();
        ExternalAccountProvider externalAccountProvider = ExternalAccountProvider.caseInsensitiveValueOf(externalAccountProviderName);
        String externalUserId = connection.getKey().getProviderUserId();
        ExternalAccount externalAccount = new ExternalAccount(externalAccountProvider, externalUserId);

        // check that we got the information we needed
        if (StringUtils.isBlank(firstName) || StringUtils.isBlank(lastName) || StringUtils.isBlank(email))
            throw new ApiException(externalAccountProviderName, "provider failed to return required attributes");


        return new UserDTO(firstName, lastName, email, externalAccount);
    }
}

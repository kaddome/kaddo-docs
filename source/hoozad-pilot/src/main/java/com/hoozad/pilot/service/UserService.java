package com.hoozad.pilot.service;

import com.hoozad.pilot.domain.Authority;
import com.hoozad.pilot.domain.DeliveryDetails;
import com.hoozad.pilot.domain.ExternalAccount;
import com.hoozad.pilot.domain.User;
import com.hoozad.pilot.repository.AuthorityRepository;
import com.hoozad.pilot.repository.PersistentTokenRepository;
import com.hoozad.pilot.repository.UserRepository;
import com.hoozad.pilot.security.SecurityUtils;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

/**
 * Service class for managing users.
 */
@Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private PersistentTokenRepository persistentTokenRepository;

    @Inject
    private AuthorityRepository authorityRepository;

    void checkForDuplicateUser(User user) {
        if (userRepository.findOneByLogin(user.getLogin()).isPresent()) {
            throw new RegistrationException("login already in use");
        }
    }

    public User createUserInformation(String login, String firstName, String lastName,
                                      String langKey, ExternalAccount externalAccount) {
        User newUser = new User();
        Authority authority = authorityRepository.findOne("ROLE_USER");
        Set<Authority> authorities = new HashSet<>();
        authorities.add(authority);
        newUser.setAuthorities(authorities);
        newUser.getExternalAccounts().add(externalAccount);
        newUser.setLogin(login);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setLangKey(langKey);

        checkForDuplicateUser(newUser);
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public void updateUserInformation(String firstName, String lastName, DeliveryDetails deliveryDetails) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).ifPresent(u -> {
            u.setFirstName(firstName);
            u.setLastName(lastName);
            u.setDeliveryDetails(deliveryDetails);
            userRepository.save(u);
            log.debug("Changed Information for User: {}", u);
        });
    }

    public User getUserWithAuthorities() {
        User currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get();
        currentUser.getAuthorities().size(); // eagerly load the association
        return currentUser;
    }

    /**
     * Persistent Token are used for providing automatic authentication, they should be automatically deleted after
     * 30 days.
     * <p/>
     * <p>
     * This is scheduled to get fired everyday, at midnight.
     * </p>
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void removeOldPersistentTokens() {
        LocalDate now = new LocalDate();
        persistentTokenRepository.findByTokenDateBefore(now.minusMonths(1)).stream().forEach(token -> {
            log.debug("Deleting token {}", token.getSeries());
            persistentTokenRepository.delete(token);
        });
    }
}

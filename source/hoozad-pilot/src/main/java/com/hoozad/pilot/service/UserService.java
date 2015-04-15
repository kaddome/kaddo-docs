package com.hoozad.pilot.service;

import com.hoozad.pilot.domain.Authority;
import com.hoozad.pilot.domain.ExternalAccount;
import com.hoozad.pilot.domain.User;
import com.hoozad.pilot.repository.AuthorityRepository;
import com.hoozad.pilot.repository.PersistentTokenRepository;
import com.hoozad.pilot.repository.UserRepository;
import com.hoozad.pilot.security.SecurityUtils;
import com.hoozad.pilot.service.util.RandomUtil;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service class for managing users.
 */
@Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private UserRepository userRepository;

    @Inject
    private PersistentTokenRepository persistentTokenRepository;

    @Inject
    private AuthorityRepository authorityRepository;

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        userRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                userRepository.save(user);
                log.debug("Activated user: {}", user);
                return user;
            });
        return Optional.empty();
    }

    void checkForDuplicateUser(User user) {

        if (userRepository.findOneByLogin(user.getLogin()).isPresent()) {
            throw new RegistrationException("login already in use");
        } else if (userRepository.findOneByEmail(user.getEmail()).isPresent()) {
            new RegistrationException("e-mail address already in use");
        }
    }

    User createUserInformation(String login, String password, String firstName, String lastName,
                               String email, String langKey, ExternalAccount externalAccount) {
        User newUser = new User();
        Authority authority = authorityRepository.findOne("ROLE_USER");
        Set<Authority> authorities = new HashSet<>();
        authorities.add(authority);
        newUser.setAuthorities(authorities);

        if (StringUtils.isNotBlank(password)) {
            String encryptedPassword = passwordEncoder.encode(password);
            newUser.setPassword(encryptedPassword);
        } else {
            newUser.getExternalAccounts().add(externalAccount);
//            externalAccount.setUser(newUser);
        }

        newUser.setLogin(login);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setLangKey(langKey);

        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());

        checkForDuplicateUser(newUser);
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User createUserInformation(String login, String password, String firstName, String lastName, String email,
                                      String langKey) {
        return createUserInformation(login, password, firstName, lastName, email, langKey, null);
    }

    public User createUserInformation(String login, String firstName, String lastName, String email,
                                      String langKey, ExternalAccount externalAccount) {
        return createUserInformation(login, null, firstName, lastName, email, langKey, externalAccount);
    }

    public void updateUserInformation(String firstName, String lastName, String email) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).ifPresent(u -> {
            u.setFirstName(firstName);
            u.setLastName(lastName);
            u.setEmail(email);
            userRepository.save(u);
            log.debug("Changed Information for User: {}", u);
        });
    }

    public void changePassword(String password) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).ifPresent(u -> {
            String encryptedPassword = passwordEncoder.encode(password);
            u.setPassword(encryptedPassword);
            userRepository.save(u);
            log.debug("Changed password for User: {}", u);
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

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p/>
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     * </p>
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        DateTime now = new DateTime();
        List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3));
        for (User user : users) {
            log.debug("Deleting not activated user {}", user.getLogin());
            userRepository.delete(user);
        }
    }
}

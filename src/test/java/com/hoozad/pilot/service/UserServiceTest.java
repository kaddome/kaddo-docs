package com.hoozad.pilot.service;

import com.hoozad.pilot.Application;
import com.hoozad.pilot.config.MongoConfiguration;
import com.hoozad.pilot.domain.PersistentToken;
import com.hoozad.pilot.domain.User;
import com.hoozad.pilot.repository.PersistentTokenRepository;
import com.hoozad.pilot.repository.UserRepository;
import com.hoozad.pilot.security.SecurityUtils;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserService
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@Import(MongoConfiguration.class)
public class UserServiceTest {

    @Inject
    private PersistentTokenRepository persistentTokenRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserService userService;
    private User existingUser;

    @Before
    public void setup() {
        this.existingUser = existingUser("existing_user");
    }

    @After
    public void removeTestUser() {
        userRepository.delete(existingUser);
    }

    @Test
    public void testRemoveOldPersistentTokens() {
        int existingCount = persistentTokenRepository.findByUser(existingUser).size();
        generateUserToken(existingUser, "1111-1111", new LocalDate());
        LocalDate now = new LocalDate();
        generateUserToken(existingUser, "2222-2222", now.minusDays(32));
        assertThat(persistentTokenRepository.findByUser(existingUser)).hasSize(existingCount + 2);
        userService.removeOldPersistentTokens();
        assertThat(persistentTokenRepository.findByUser(existingUser)).hasSize(existingCount + 1);
    }

    private void generateUserToken(User user, String tokenSeries, LocalDate localDate) {
        PersistentToken token = new PersistentToken();
        token.setSeries(tokenSeries);
        token.setUser(user);
        token.setTokenValue(tokenSeries + "-data");
        token.setTokenDate(localDate);
        token.setIpAddress("127.0.0.1");
        token.setUserAgent("Test agent");
        persistentTokenRepository.save(token);
    }

    private User existingUser(String login) {
        return userService.createUserInformation(login, "First name", "Last name", "en", null);
    }
}

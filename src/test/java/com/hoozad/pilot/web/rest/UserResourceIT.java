package com.hoozad.pilot.web.rest;

import com.hoozad.pilot.Application;
import com.hoozad.pilot.config.MongoConfiguration;
import com.hoozad.pilot.domain.DeliveryDetails;
import com.hoozad.pilot.domain.SharingMode;
import com.hoozad.pilot.domain.User;
import com.hoozad.pilot.repository.AuthorityRepository;
import com.hoozad.pilot.repository.UserRepository;
import com.hoozad.pilot.security.AuthoritiesConstants;
import com.hoozad.pilot.service.UserService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;

import static com.hoozad.pilot.domain.SharingMode.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@Import(MongoConfiguration.class)
public class UserResourceIT {

    @Inject
    private UserRepository userRepository;
    @Inject
    private AuthorityRepository authorityRepository;
    @Inject
    private UserService userService;
    @Inject
    private WebApplicationContext wac;
    @Inject
    private FilterChainProxy springSecurityFilter;

    private User existingUser;
    private MockMvc mockMvc;
    private User ecommerceUser;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(wac).dispatchOptions(true).addFilter(springSecurityFilter).build();
        this.ecommerceUser = existingUser("e_commerce_user", AuthoritiesConstants.ECOMMERCE);
        this.existingUser = existingUser("existing_user", AuthoritiesConstants.USER);
    }

    @After
    public void removeTestUser() {
        userRepository.delete(existingUser);
        userRepository.delete(ecommerceUser);
    }

    @Test
    public void testGetExistingUser() throws Exception {
        mockMvc.perform(get("/api/users/existing_user")
                .header("Authorization", authorizationFor("existing_user"))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))

                .andExpect(jsonPath("$.lastName").value(existingUser.getLastName()))
                .andExpect(jsonPath("$.sharingMode").value(SHARE_WITH_FB_FRIENDS_ONLY.name()));

        /**
         * TODO Need to add more test coverage here for all the fields of the User domain object
         */
    }

    private String authorizationFor(String username) {
        return "Basic " + new String(Base64.encodeBase64((username + ":").getBytes()));
    }

    @Test
    public void testGetUnknownUser() throws Exception {
        mockMvc.perform(get("/api/users/unknown")
                .header("Authorization", authorizationFor("existing_user"))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private User existingUser(String login, String authority) {
        User user = userService.createUserInformation(login, "First name", "Last name", "en", null);
        user.setDeliveryDetails(testDeliveryDetails());
        user.getAuthorities().add(authorityRepository.findOne(authority));
        user.setSharingMode(SHARE_WITH_FB_FRIENDS_ONLY);
        userRepository.save(user);
        return user;
    }

    private DeliveryDetails testDeliveryDetails() {
        DeliveryDetails deliveryDetails = new DeliveryDetails();
        deliveryDetails.setAddressLine1("Address line 1");
        deliveryDetails.setCity("city");
        deliveryDetails.setPostcode("postcode");
        return deliveryDetails;
    }
}

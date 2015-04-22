package com.hoozad.pilot.web.rest;

import com.hoozad.pilot.Application;
import com.hoozad.pilot.config.MongoConfiguration;
import com.hoozad.pilot.domain.DeliveryDetails;
import com.hoozad.pilot.domain.User;
import com.hoozad.pilot.repository.UserRepository;
import com.hoozad.pilot.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.inject.Inject;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
public class UserResourceTest {

    @Inject
    private UserRepository userRepository;
    @Inject
    private UserService userService;
    private User existingUser;

    private MockMvc restUserMockMvc;

    @Before
    public void setup() {
        UserResource userResource = new UserResource();
        ReflectionTestUtils.setField(userResource, "userRepository", userRepository);
        this.restUserMockMvc = MockMvcBuilders.standaloneSetup(userResource).build();
        this.existingUser = existingUser("existing_user");
    }

    @After
    public void removeTestUser() {
        userRepository.delete(existingUser);
    }

    @Test
    public void testGetExistingUser() throws Exception {
        restUserMockMvc.perform(get("/api/users/existing_user")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.lastName").value(existingUser.getLastName()));
    }

    @Test
    public void testGetUnknownUser() throws Exception {
        restUserMockMvc.perform(get("/api/users/unknown")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testGetDeliveryDetails() throws Exception {
        restUserMockMvc.perform(get("/api/users/existing_user/delivery_details")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.city").value(existingUser.getDeliveryDetails().getCity()));
    }

    @Test
    public void testGetDeliveryDetailsForUnknownUser() throws Exception {
        restUserMockMvc.perform(get("/api/users/unknown/delivery_details")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    private User existingUser(String login) {
        User user = userService.createUserInformation(login, "First name", "Last name", "en", null);
        user.setDeliveryDetails(testDeliveryDetails());
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

package com.hoozad.pilot.web.rest;

import com.hoozad.pilot.Application;
import com.hoozad.pilot.config.MongoConfiguration;
import com.hoozad.pilot.domain.DeliveryDetails;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@Import(MongoConfiguration.class)
public class DeliveryDetailsResourceIT {

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

    private User anyExistingUser;
    private MockMvc mockMvc;
    private User ecommerceUser;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(wac).dispatchOptions(true).addFilter(springSecurityFilter).build();
        this.ecommerceUser = createUser("e_commerce_user", AuthoritiesConstants.ECOMMERCE);
        this.anyExistingUser = createUser("any_existing_user", AuthoritiesConstants.USER);
    }

    @After
    public void removeTestUser() {
        userRepository.delete(anyExistingUser);
        userRepository.delete(ecommerceUser);
    }


    private String authorizationFor(String username) {
        return "Basic " + new String(Base64.encodeBase64((username + ":").getBytes()));
    }

    @Test
    public void an_accepted_and_authenticated_e_commerce_platform_should_be_able_to_obtain_delivery_details_of_an_existing_user() throws Exception {
        mockMvc.perform(get("/api/users/any_existing_user/delivery_details")
                .header("Authorization", authorizationFor("e_commerce_user"))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.city").value(anyExistingUser.getDeliveryDetails().getCity()));
    }

    @Test
    public void a_non_e_commerce_authenticated_user_should_not_be_able_to_retrieve_user_delivery_details() throws Exception {
        mockMvc.perform(get("/api/users/any_existing_user/delivery_details")
                .header("Authorization", authorizationFor("any_existing_user"))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }

    @Test
    public void a_non_existing_user_should_not_be_able_to_retrieve_user_delivery_details() throws Exception {
        mockMvc.perform(get("/api/users/existing_user/delivery_details")
                .header("Authorization", authorizationFor("non_existing_e_commerce_user"))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void an_accepted_and_authenticated_e_commerce_platform_should_not_be_able_to_retrieve_delivery_details_for_non_existing_user() throws Exception {
        mockMvc.perform(get("/api/users/unknown/delivery_details")
                .header("Authorization", authorizationFor("e_commerce_user"))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private User createUser(String login, String authority) {
        User user = userService.createUserInformation(login, "First name", "Last name", "en", null);
        user.setDeliveryDetails(testDeliveryDetails());
        user.getAuthorities().add(authorityRepository.findOne(authority));
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

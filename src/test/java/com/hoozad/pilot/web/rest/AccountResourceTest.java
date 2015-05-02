package com.hoozad.pilot.web.rest;

import com.hoozad.pilot.Application;
import com.hoozad.pilot.domain.Authority;
import com.hoozad.pilot.domain.DeliveryDetails;
import com.hoozad.pilot.domain.User;
import com.hoozad.pilot.repository.UserRepository;
import com.hoozad.pilot.security.AuthoritiesConstants;
import com.hoozad.pilot.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.hoozad.pilot.web.rest.TestUserDataBuilder.buildTestData;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AccountResource REST controller.
 *
 * @see UserService
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class AccountResourceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    private MockMvc restUserMockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        AccountResource accountResource = new AccountResource();

        ReflectionTestUtils.setField(accountResource, "userRepository", userRepository);
        ReflectionTestUtils.setField(accountResource, "userService", userService);

        this.restUserMockMvc = MockMvcBuilders.standaloneSetup(accountResource).build();
    }

    @Test
    public void testNonAuthenticatedUser() throws Exception {
        restUserMockMvc.perform(get("/api/authenticate")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

    }

    @Test
    public void testAuthenticatedUser() throws Exception {
        restUserMockMvc.perform(get("/api/authenticate")
                .with(request -> {
                    request.setRemoteUser("test");
                    return request;
                })
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("test"));
    }

    @Test
    public void testGetExistingAccount() throws Exception {

        User mockUser = createMockUser();
        when(userService.getUserWithAuthorities()).thenReturn(mockUser);

        restUserMockMvc.perform(get("/api/account")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.login").value("test"))
                .andExpect(jsonPath("$.firstName").value("john"))
                .andExpect(jsonPath("$.roles").value(AuthoritiesConstants.ADMIN))
                .andExpect(jsonPath("$.openProfile").value(true));
    }

    @Test
    public void testPostForUpdatingAccountDetails() throws Exception {

        User mockUser = createMockUser();

        setCurrentlyLoggedInUser("test");

        when(userRepository.findOneByLogin(anyString())).thenReturn(Optional.of(mockUser));


        restUserMockMvc.perform(
                post("/api/account").accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE).content(updatedUserDetailsAsJSON())
        ).andExpect(status().isOk());

        verify(userService, times(1)).updateUserInformation(anyString(), anyString(), any(DeliveryDetails.class), anyBoolean());
    }

    @Test
    public void testGetUnknownAccount() throws Exception {
        when(userService.getUserWithAuthorities()).thenReturn(null);

        restUserMockMvc.perform(get("/api/account")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    private String updatedUserDetailsAsJSON() {
        return buildTestData().
                    login("test").
                    firstName("Foo").
                    lastName("Bar").
                    userRole("ROLE_USER").
                    country("UK").
                    postcode("E123456").
                    addressLine1("Street 1234").
                    addressLine2("Building 123").
                    city("London, UK").
                    openProfile(true).
                build();
    }

    private void setCurrentlyLoggedInUser(String credentials) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(credentials, credentials));
        SecurityContextHolder.setContext(securityContext);
    }


    private User createMockUser() {
        User user = new User();
        user.setLogin("test");
        user.setFirstName("john");
        user.setLastName("doe");
        user.setAuthorities(createAdminAuthority());
        user.setOpenProfile(true);
        return user;
    }

    private Set<Authority> createAdminAuthority() {
        Set<Authority> authorities = new HashSet<>();
        Authority authority = new Authority();
        authority.setName(AuthoritiesConstants.ADMIN);
        authorities.add(authority);
        return authorities;
    }
}

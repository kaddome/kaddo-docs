package com.hoozad.pilot.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hoozad.pilot.domain.Authority;
import com.hoozad.pilot.domain.PersistentToken;
import com.hoozad.pilot.repository.PersistentTokenRepository;
import com.hoozad.pilot.repository.UserRepository;
import com.hoozad.pilot.security.SecurityUtils;
import com.hoozad.pilot.service.UserService;
import com.hoozad.pilot.web.rest.dto.UserDTO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserService userService;

    @Inject
    private PersistentTokenRepository persistentTokenRepository;

    /**
     * GET  /authenticate -> check if the user is authenticated, and return its login.
     */
    @RequestMapping(value = "/authenticate",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * GET  /account -> get the current user.
     */
    @RequestMapping(value = "/account",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserDTO> getAccount() {
        return Optional.ofNullable(userService.getUserWithAuthorities())
            .map(user -> new ResponseEntity<>(
                new UserDTO(
                    user.getLogin(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getLangKey(),
                    user.getDeliveryDetails(),
                    user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toList()), user.getOpenProfile()),
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @RequestMapping(value = "/account",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> saveAccount(@RequestBody UserDTO userDTO) {
        return userRepository
            .findOneByLogin(userDTO.getLogin())
            .filter(user -> user.getLogin().equals(SecurityUtils.getCurrentLogin()))
            .map(user -> {
                userService.updateUserInformation(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getDeliveryDetails(), userDTO.isOpenProfile());
                return new ResponseEntity<String>(HttpStatus.OK);
            })
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * GET  /account/sessions -> get the current open sessions.
     */
    @RequestMapping(value = "/account/sessions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PersistentToken>> getCurrentSessions() {
        return userRepository.findOneByLogin(SecurityUtils.getCurrentLogin())
            .map(user -> new ResponseEntity<>(
                persistentTokenRepository.findByUser(user),
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * DELETE  /account/sessions?series={series} -> invalidate an existing session.
     * <p/>
     * - You can only delete your own sessions, not any other user's session
     * - If you delete one of your existing sessions, and that you are currently logged in on that session, you will
     * still be able to use that session, until you quit your browser: it does not work in real time (there is
     * no API for that), it only removes the "remember me" cookie
     * - This is also true if you invalidate your current session: you will still be able to use it until you close
     * your browser or that the session times out. But automatic login (the "remember me" cookie) will not work
     * anymore.
     * There is an API to invalidate the current session, but there is no API to check which session uses which
     * cookie.
     */
    @RequestMapping(value = "/account/sessions/{series}",
        method = RequestMethod.DELETE)
    @Timed
    public void invalidateSession(@PathVariable String series) throws UnsupportedEncodingException {
        String decodedSeries = URLDecoder.decode(series, "UTF-8");
        userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).ifPresent(u -> {
            persistentTokenRepository.findByUser(u).stream()
                .filter(persistentToken -> StringUtils.equals(persistentToken.getSeries(), decodedSeries))
                .findAny().ifPresent(t -> persistentTokenRepository.delete(decodedSeries));
        });
    }
}

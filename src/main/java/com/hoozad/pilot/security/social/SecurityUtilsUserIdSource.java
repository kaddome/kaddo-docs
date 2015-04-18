package com.hoozad.pilot.security.social;

import com.hoozad.pilot.security.SecurityUtils;
import org.springframework.social.UserIdSource;


/**
 * A UserIdSource that delegates to {@link com.hoozad.pilot.security.SecurityUtils#getCurrentLogin()}.
 * UserIdSource is used by Spring Security Social to link the
 * {@link org.springframework.social.connect.Connection Connections} stored in the
 * {@link org.springframework.social.connect.UsersConnectionRepository UsersConnectionRepository} to the
 * {@link com.hoozad.pilot.domain.User#getLogin() internal logins}.
 */
public class SecurityUtilsUserIdSource implements UserIdSource {
    @Override
    public String getUserId() {
        return SecurityUtils.getCurrentLogin();
    }
}

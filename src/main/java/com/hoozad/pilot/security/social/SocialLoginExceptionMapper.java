package com.hoozad.pilot.security.social;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.social.security.SocialAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * An AuthenticationFailureHandler that replaces Spring's
 */
public class SocialLoginExceptionMapper extends SimpleUrlAuthenticationFailureHandler {

    private final static String DELEGATED = "SocialLoginRejectedFailureHandler.delegated";

    protected Map<Class<? extends AuthenticationException>, String> map = new HashMap<>();
    protected SocialAuthenticationFailureHandler delegate = new SocialAuthenticationFailureHandler(this);

    public SocialLoginExceptionMapper(String defaultFailureUrl) {
        super.setDefaultFailureUrl(defaultFailureUrl);
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if (request.getAttribute(DELEGATED) == null) {
            request.setAttribute(DELEGATED, Boolean.TRUE);
            delegate.onAuthenticationFailure(request, response, exception);
        } else if (map.containsKey(exception.getClass())) {
            String url = map.get(exception.getClass());
            super.getRedirectStrategy().sendRedirect(request, response, url);
        } else {
            super.onAuthenticationFailure(request, response, exception);
        }

    }

    public SocialLoginExceptionMapper add(Class<? extends AuthenticationException> clazz, String url) {
        map.put(clazz, url);
        return this;
    }
}

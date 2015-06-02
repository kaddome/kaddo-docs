package com.hoozad.pilot.config;

import com.hoozad.pilot.security.AjaxAuthenticationFailureHandler;
import com.hoozad.pilot.security.AjaxAuthenticationSuccessHandler;
import com.hoozad.pilot.security.AjaxLogoutSuccessHandler;
import com.hoozad.pilot.security.AuthoritiesConstants;
import com.hoozad.pilot.security.Http401UnauthorizedEntryPoint;
import com.hoozad.pilot.security.social.SocialLoginExceptionMapper;
import com.hoozad.pilot.web.filter.CsrfCookieGeneratorFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.social.security.SocialAuthenticationException;
import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.inject.Inject;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Inject
    private Environment env;

    @Inject
    private AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler;

    @Inject
    private AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler;

    @Inject
    private AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;

    @Inject
    private Http401UnauthorizedEntryPoint authenticationEntryPoint;

    @Inject
    private UserDetailsService userDetailsService;

    @Inject
    private RememberMeServices rememberMeServices;

    @Inject
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }



    /**
     * Build a configurer that can be applied to an HttpSecurity instance. When the configurer is applied,
     * Spring Social Security's {@link org.springframework.social.security.SocialAuthenticationFilter}
     * will be added to the HttpSecurity's SecurityFilterChain.
     *
     * @returnauthenticationEntryPoint
     */
    protected SpringSocialConfigurer buildSpringSocialConfigurer() {
        // build an AuthenticationFailureHandler that is aware of our own exception types
        final SocialLoginExceptionMapper handler = new SocialLoginExceptionMapper("/#/register/external")
            .add(SocialAuthenticationException.class, "/#/register/external/rejected");

        SpringSocialConfigurer configurer = new SpringSocialConfigurer()
            .postLoginUrl("/")
            .alwaysUsePostLoginUrl(true);

        // configure options not available using the standard configurer
        configurer.addObjectPostProcessor(
            new ObjectPostProcessor<SocialAuthenticationFilter>() {
                public SocialAuthenticationFilter postProcess(SocialAuthenticationFilter object) {
                    // replace the default exception
                    object.setAuthenticationFailureHandler(handler);
                    return object;
                }
            }
        );

        return configurer;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
            .antMatchers("/scripts/**/*.{js,html}")
            .antMatchers("/bower_components/**")
            .antMatchers("/i18n/**")
            .antMatchers("/assets/**")
            .antMatchers("/swagger-ui/**")
            .antMatchers("/test/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .addFilterAfter(new CsrfCookieGeneratorFilter(), CsrfFilter.class)
            .exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPoint)
        .and()
            .rememberMe()
            .rememberMeServices(rememberMeServices)
            .key(env.getProperty("jhipster.security.rememberme.key"))
        .and()
            .httpBasic()
        .and()
            .logout()
            .logoutUrl("/api/logout")
            .logoutSuccessHandler(ajaxLogoutSuccessHandler)
            .deleteCookies("JSESSIONID", "hazelcast.sessionId", "CSRF-TOKEN")
            .permitAll()
        .and()
            .apply(buildSpringSocialConfigurer())
        .and()
            .headers()
            .frameOptions()
            .disable()
            .authorizeRequests()
                .antMatchers("/api/register").permitAll()
                .antMatchers("/api/authenticate").permitAll()
                .antMatchers("/api/users/*/delivery_details").hasAuthority(AuthoritiesConstants.ECOMMERCE)
                .antMatchers("/api/logs/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/api/**").authenticated()
                .antMatchers("/metrics/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/health/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/trace/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/dump/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/shutdown/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/beans/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/configprops/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/info/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/autoconfig/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/env/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/trace/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/api-docs/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/protected/**").authenticated();

    }

    @EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
    private static class GlobalSecurityConfiguration extends GlobalMethodSecurityConfiguration {
    }
}

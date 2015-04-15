package com.hoozad.pilot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.connect.web.ReconnectFilter;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

import javax.inject.Inject;
import javax.sql.DataSource;

/**
 * Spring Social Configuration.
 * This configuration is demonstrating the use of the simplified Spring Social configuration options from Spring Social 1.1.
 *
 * @author Craig Walls
 */
@Configuration
@EnableSocial
public class SocialConfig extends SocialConfigurerAdapter {

    @Inject
    private DataSource dataSource;

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer cfConfig, Environment env) {
        cfConfig.addConnectionFactory(new FacebookConnectionFactory(env.getProperty("facebook.appKey"), env.getProperty("facebook.appSecret")));
    }

    @Override
    public UserIdSource getUserIdSource() {
        return new UserIdSource() {
            @Override
            public String getUserId() {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication == null) {
                    throw new IllegalStateException("Unable to get a ConnectionRepository: no user signed in");
                }
                return authentication.getName();
            }
        };
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        return new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
    }

    @Bean
    public ConnectController connectController(ConnectionFactoryLocator connectionFactoryLocator, ConnectionRepository connectionRepository) {
        return new ConnectController(connectionFactoryLocator, connectionRepository);
    }

    @Bean
    public ReconnectFilter apiExceptionHandler(UsersConnectionRepository usersConnectionRepository, UserIdSource userIdSource) {
        return new ReconnectFilter(usersConnectionRepository, userIdSource);
    }

    @Bean
    @Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
    public Facebook facebook(ConnectionRepository repository) {
        Connection<Facebook> connection = repository.findPrimaryConnection(Facebook.class);
        return connection != null ? connection.getApi() : null;
    }
}

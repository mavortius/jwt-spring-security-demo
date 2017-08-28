package org.zerhusen.security;

import io.jsonwebtoken.lang.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.zerhusen.security.service.UserService;

public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final Log logger = LogFactory.getLog(getClass());

    private final UserService userService;

    public JwtAuthenticationProvider(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(JwtAuthenticationToken.class, authentication,
                "Only JwtAuthenticationToken is supported");

        String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED"
                : authentication.getName();
        UserDetails user;

        try {
            user = retrieveUser(username);
            ((JwtAuthenticationToken) authentication).setAuthorities(user.getAuthorities());
        } catch (UsernameNotFoundException notFound) {
            logger.debug("User '" + username + "' not found");

            throw new BadCredentialsException("Bad credentials");
        }

        Assert.notNull(user,
                "retrieveUser returned null - a violation of the interface contract");

        return createSuccessAuthentication(username, authentication, user);
    }

    private Authentication createSuccessAuthentication(String principal,
                                                       Authentication authentication, UserDetails user) {
        JwtAuthenticationToken result = new JwtAuthenticationToken(
                user.getAuthorities(), principal, authentication.getCredentials().toString());

        result.setDetails(authentication.getDetails());
        result.setAuthenticated(true);

        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private UserDetails retrieveUser(String username) throws AuthenticationException {
        UserDetails loadedUser;

        try {
            loadedUser = userService.loadUserByUsername(username);
        } catch (UsernameNotFoundException notFound) {

            throw notFound;
        } catch (Exception repositoryProblem) {
            throw new InternalAuthenticationServiceException(
                    repositoryProblem.getMessage(), repositoryProblem);
        }

        if (loadedUser == null) {
            throw new InternalAuthenticationServiceException(
                    "UserService returned null, which is an interface contract violation");
        }

        return loadedUser;
    }
}

package org.zerhusen.provider;

import org.springframework.data.jdbc.support.ConnectionUsernamePasswordProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextUserProvider implements ConnectionUsernamePasswordProvider {
    @Override
    public String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null) return null;

        Object principal = authentication.getPrincipal();

        if(principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    @Override
    public String getPassword() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null) return null;

        return authentication.getCredentials().toString();
    }
}

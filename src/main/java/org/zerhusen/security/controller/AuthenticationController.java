package org.zerhusen.security.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.zerhusen.security.AuthenticationRequest;
import org.zerhusen.security.JwtAuthenticationToken;
import org.zerhusen.security.JwtTokenUtil;
import org.zerhusen.security.JwtUserDetails;
import org.zerhusen.security.service.AuthenticationResponse;
import org.zerhusen.security.service.UserService;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AuthenticationController {

    @Value("${jwt.header}")
    private String tokenHeader;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    public AuthenticationController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @RequestMapping(value = "${jwt.route.authentication.path}", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest, Device device) throws Exception {

        // Perform the security
        final Authentication authentication = new JwtAuthenticationToken(
                                                    authenticationRequest.getUsername(),
                                                    authenticationRequest.getPassword()
                                                                        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        authenticationManager.authenticate(authentication);

        final String token = jwtTokenUtil.generateToken(authentication, device);

        // Return the token
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @RequestMapping(value = "${jwt.route.authentication.refresh}", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        final String requestHeader = request.getHeader(this.tokenHeader);
        String token = StringUtils.delete(requestHeader, "Bearer ");
        String username = jwtTokenUtil.getUsernameFromToken(token);

        if (jwtTokenUtil.canTokenBeRefreshed(token)) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(new AuthenticationResponse(refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

}

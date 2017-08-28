package org.zerhusen.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.zerhusen.model.security.Authority;
import org.zerhusen.model.security.AuthorityName;
import org.zerhusen.model.security.User;
import org.zerhusen.security.AuthenticationRequest;
import org.zerhusen.security.JwtTokenUtil;
import org.zerhusen.security.JwtUserDetails;
import org.zerhusen.security.JwtUserFactory;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthenticationControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private UserDetailsService userDetailsService;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithAnonymousUser
    public void successfulAuthenticationWithAnonymousUser() throws Exception {

        AuthenticationRequest authenticationRequest = new AuthenticationRequest("user", "password");

        this.mvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(authenticationRequest)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(roles = "USUARIO")
    public void successfulRefreshTokenWithUserRole() throws Exception {

        Authority authority = new Authority();
        authority.setId(0L);
        authority.setName(AuthorityName.USUARIO);

        User user = new User();
        user.setUsername("username");
        user.setAuthority(authority);
        user.setEnabled(Boolean.TRUE);

        JwtUserDetails jwtUserDetails = JwtUserFactory.create(user);

        when(this.jwtTokenUtil.getUsernameFromToken(any())).thenReturn(user.getUsername());

        when(this.userDetailsService.loadUserByUsername(eq(user.getUsername()))).thenReturn(jwtUserDetails);

        when(this.jwtTokenUtil.canTokenBeRefreshed(any())).thenReturn(true);

        this.mvc.perform(get("/refresh"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void successfulRefreshTokenWithAdminRole() throws Exception {

        Authority authority = new Authority();
        authority.setId(1L);
        authority.setName(AuthorityName.ADMINISTRADOR);

        User user = new User();
        user.setUsername("admin");
        user.setAuthority(authority);
        user.setEnabled(Boolean.TRUE);

        JwtUserDetails jwtUserDetails = JwtUserFactory.create(user);

        when(this.jwtTokenUtil.getUsernameFromToken(any())).thenReturn(user.getUsername());

        when(this.userDetailsService.loadUserByUsername(eq(user.getUsername()))).thenReturn(jwtUserDetails);

        when(this.jwtTokenUtil.canTokenBeRefreshed(any())).thenReturn(true);

        this.mvc.perform(get("/refresh"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithAnonymousUser
    public void shouldGetUnauthorizedWithAnonymousUser() throws Exception {

        this.mvc.perform(get("/refresh"))
                .andExpect(status().isUnauthorized());

    }

}


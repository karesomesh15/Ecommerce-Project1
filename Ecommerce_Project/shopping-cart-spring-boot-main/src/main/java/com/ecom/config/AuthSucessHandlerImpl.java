package com.ecom.config;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthSucessHandlerImpl implements AuthenticationSuccessHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthSucessHandlerImpl.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        
        String username = authentication.getName();  // Get the username of the authenticated user
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Set<String> roles = AuthorityUtils.authorityListToSet(authorities);
        
        if (roles.contains("ROLE_ADMIN")) {
            logger.info("Admin '{}' successfully logged in.", username);  // Log successful login for admin
            response.sendRedirect("/admin/");
        } else {
            logger.info("User '{}' successfully logged in.", username);   // Log successful login for user
            response.sendRedirect("/");
        }
    }
}

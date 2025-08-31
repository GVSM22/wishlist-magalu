package com.example.magalu_wishlist.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class UserAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private ExternalAuthComponent authComponent;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional.ofNullable(request.getHeader("Authorization"))
                .flatMap(authComponent::externalAuth)
                .ifPresent(dt -> {
                    var token = new UsernamePasswordAuthenticationToken(dt.getUsername(), null, dt.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(token);
                    request.setAttribute("email", dt.getUsername());
                });

        filterChain.doFilter(request, response);
    }
}

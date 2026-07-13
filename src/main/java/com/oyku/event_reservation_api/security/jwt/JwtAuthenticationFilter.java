package com.oyku.event_reservation_api.security.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
    	
    	System.out.println("JWT filter triggered for URI:" + request.getRequestURI());
    	
    	final String authHeader = request.getHeader("Authorization");

    	System.out.println("AUTH HEADER: " + authHeader);
    	
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);

        System.out.println("TOKEN: " + jwt);
        
        final String userEmail =
                jwtService.extractUsername(jwt);
        
        System.out.println("EMAIL FROM TOKEN: " + userEmail);

        if (userEmail != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

        	System.out.println("USER DETAILS LOADING");
        	
            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(userEmail);

            if (jwtService.isTokenValid(jwt, userDetails)) {

            	System.out.println("TOKEN VALID");
                System.out.println("AUTHORITIES : " + userDetails.getAuthorities());
                
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request));

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authToken);

            }

        }

        filterChain.doFilter(request, response);

    }

}
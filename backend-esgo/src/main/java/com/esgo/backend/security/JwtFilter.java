package com.esgo.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // DEBUG LOG: Print the incoming request URL and Header
        System.out.println("Processing Request: " + request.getMethod() + " " + request.getRequestURI());

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                // Check if token is "null" string (common frontend bug)
                if (token.equals("null") || token.equals("undefined")) {
                    System.out.println("ERROR: Token string is 'null' or 'undefined'. Frontend issue.");
                } else {
                    username = jwtUtil.extractUsername(token);
                    System.out.println("DEBUG: Token valid. Extracted Username: " + username);
                }
            } catch (Exception e) {
                System.out.println("ERROR: Token Validation Failed: " + e.getMessage());
            }
        } else {
            System.out.println("WARNING: No valid Authorization header found.");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // DEBUG: User validated
            if (jwtUtil.validateToken(token, username)) {
                UserDetails userDetails = new User(username, "", new ArrayList<>());
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("SUCCESS: User authenticated in SecurityContext.");
            } else {
                System.out.println("ERROR: Token expired or username mismatch.");
            }
        }

        chain.doFilter(request, response);
    }
}
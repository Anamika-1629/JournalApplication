package dev.anamika.journalApp.filter;

import dev.anamika.journalApp.services.UserDetailsServiceImpl;
import dev.anamika.journalApp.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // If no Bearer token, skip — SecurityConfig will reject if the route needs auth
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7); //strip "Bearer " <-- len 7

        // Only process if SecurityContext is empty (don't overwrite existing auth)
        if (SecurityContextHolder.getContext().getAuthentication() == null){
            try{
                if (jwtUtils.isTokenValid(token)){
                    String username = jwtUtils.extractUsername(token);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    // Create authentication token and set it in context
                    var authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            catch (Exception e){
                // Invalid token — context stays empty, request proceeds to be rejected
                // Don't log the exception message (could leak info); log a generic message
                logger.warn("JWT validation failed for request to: "+request.getRequestURI());
            }
        }

        filterChain.doFilter(request, response);

    }
}

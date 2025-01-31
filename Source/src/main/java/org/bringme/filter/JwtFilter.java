package org.bringme.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.bringme.exceptions.CustomException;
import org.bringme.service.impl.JwtService;
import org.bringme.service.impl.MyUserDetailService;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter for handling JWT authentication.
 * <p>
 * This filter intercepts incoming HTTP requests, extracts the JWT token from the "Authorization" header,
 * validates the token, and sets the authentication details in the Spring Security context.
 * </p>
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final ApplicationContext context;

    /**
     * Constructs a JwtFilter with dependencies.
     *
     * @param jwtService The service used to handle JWT operations (validation, extraction).
     * @param context    The application context to retrieve user details.
     */
    public JwtFilter(JwtService jwtService, ApplicationContext context) {
        this.jwtService = jwtService;
        this.context = context;
    }

    /**
     * Filters incoming requests to check for a valid JWT token.
     * <p>
     * This method extracts the token from the "Authorization" header, validates it,
     * retrieves the user details, and sets the authentication in the SecurityContext.
     * </p>
     *
     * @param request     The HTTP request.
     * @param response    The HTTP response.
     * @param filterChain The filter chain.
     * @throws ServletException If a servlet-specific error occurs.
     * @throws IOException      If an input or output error occurs.
     * @throws CustomException  as response {@link org.bringme.controller.ControllerAdvice} for every exception handled.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String id = null;
        String emailOrPhone = null;

        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                emailOrPhone = jwtService.extractEmailOrPhone(token);
                id = jwtService.extractIdAsString(token);
            }


            if (id != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = context.getBean(MyUserDetailService.class).loadUserByUsername(emailOrPhone);
                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (CustomException ex) {
            // If there's a JWT error, send the response with the CustomException
            response.setStatus(ex.getStatus().value());
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"" + ex.getMessage() + "\"}");
            return; // Stop the filter chain here since JWT validation failed
        } catch (Exception ex) {
            // If any other exception occurs, handle it here
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"message\":"+ex.getMessage()+"}");
        }
    }
}

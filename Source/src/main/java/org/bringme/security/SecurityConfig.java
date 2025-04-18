package org.bringme.security;


import org.bringme.filter.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration of the application powered by {@link org.springframework.security}
 * <p> This configuration defines security rules, including request authorization,
 * <p>  CSRF protection, session management, and JWT authentication.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailService;
    private final JwtFilter jwtFilter;

    /**
     * Constructor to be used in Spring-boot
     *
     * @param userDetailService class to handle user's data
     * @param jwtFilter         Filter to check the JWT token
     */

    public SecurityConfig(UserDetailsService userDetailService, JwtFilter jwtFilter) {
        this.userDetailService = userDetailService;
        this.jwtFilter = jwtFilter;
    }

    /**
     * Configures the security filter chain for handling authentication and authorization.
     * <ul>
     *     <li>Disables CSRF protection (since we are using JWT-based authentication).</li>
     *     <li>Defines public and protected API endpoints.</li>
     *     <li>Requires authentication for all other endpoints.</li>
     *     <li>Uses stateless session management.</li>
     *     <li>Adds a custom JWT filter before the {@link UsernamePasswordAuthenticationFilter}.</li>
     * </ul>
     *
     * @param http The {@link HttpSecurity} object used to configure security settings.
     * @return The configured {@link SecurityFilterChain}.
     * @throws Exception If an error occurs while configuring security.
     */
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/bring-me/auth/signup", "/bring-me/auth/login",
                                "/bring-me/trips/all", "/bring-me/trips/filter/**", "/bring-me/trips/show/**", "/bring-me/items/all", "/bring-me/ver/**", "/bring-me/items/show/**", "/bring-me/items/filter/**").permitAll()
                        .requestMatchers("/bring-me/p/a/**", "/bring-me/report/a/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Defines the authentication provider bean.
     * <p>
     * This provider:
     * <ul>
     *     <li>Uses a {@link DaoAuthenticationProvider} for authentication.</li>
     *     <li>Retrieves user details from the configured {@link UserDetailsService}.</li>
     *     <li>Uses the configured {@link BCryptPasswordEncoder} to verify credentials.</li>
     * </ul>
     * </p>
     *
     * @return An {@link AuthenticationProvider} configured with user details and password encoding.
     */
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailService);
        return provider;
    }

    /**
     * Defines the authentication manager bean.
     * <p>
     * This retrieves the {@link AuthenticationManager} from the {@link AuthenticationConfiguration}.
     * </p>
     *
     * @param config The authentication configuration.
     * @return The {@link AuthenticationManager} used for handling authentication requests.
     * @throws Exception If an error occurs while retrieving the authentication manager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Tool to encode entered password.
     *
     * @return class of {@link BCryptPasswordEncoder}
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

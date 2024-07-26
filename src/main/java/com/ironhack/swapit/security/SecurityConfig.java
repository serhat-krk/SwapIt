package com.ironhack.swapit.security;

import com.ironhack.swapit.security.filters.CustomAuthenticationFilter;
import com.ironhack.swapit.security.filters.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity // Indicates it is a security configuration class using spring web security
@RequiredArgsConstructor
public class SecurityConfig {

    // UserDetailService is an interface provided by Spring Security that defines a way to retrieve user information
    private final UserDetailsService userDetailsService;

    // Autowired instance of the AuthenticationManagerBuilder (provided by Spring Security)
    private final AuthenticationManagerBuilder authManagerBuilder;

    // Bean Definition for AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Bean definition for SecurityFilterChain
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws  Exception {
        // CustomAuthenticationFilter instance created
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authManagerBuilder.getOrBuild());

        // Set the URL that the filter should process
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");

        // Disable CSRF protection because we are using tokens, not session
        http.csrf().disable();

        // Set the session creation policy to stateless; to not maintain sessions in the server
        http.sessionManagement().sessionCreationPolicy(STATELESS);


        // Set up authorization for different request matchers and user roles
        // TODO: Update for new requests
        http.authorizeHttpRequests((requests) -> requests

                // Public endpoint for visitors
                .requestMatchers("/api/login/**").permitAll()
                .requestMatchers("api/register").permitAll()

                // TODO: Add role based requests here
                .requestMatchers(GET, "/api/users/**").hasAnyAuthority("ROLE_ADMIN")

                // Any other endpoint requires authentication
                .anyRequest().authenticated());


        // Add the custom authentication filter to the http security object
        http.addFilter(customAuthenticationFilter);

        // Add the custom authorization filter before the standard authentication filter
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        // Build the security filter chain to be returned
        return http.build();
    }
}

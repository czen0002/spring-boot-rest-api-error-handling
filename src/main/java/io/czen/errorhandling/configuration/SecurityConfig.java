package io.czen.errorhandling.configuration;

import io.czen.errorhandling.model.handler.RestAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    @Bean
    @Autowired
    public SecurityFilterChain filterChain(HttpSecurity http, RestAuthenticationEntryPoint restAuthenticationEntryPoint) throws Exception {
        http
            .csrf()
            .disable()
            .authorizeRequests()
            .anyRequest()
            .authenticated()
            .and()
            .httpBasic()
            .authenticationEntryPoint(restAuthenticationEntryPoint);
        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails apiUser = User
                .withUsername("apiuser")
                .password("{noop}password")
                .roles("USER")
                .build();
        UserDetails admin = User
                .withUsername("admin")
                .password("{noop}password")
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(Arrays.asList(apiUser, admin));
    }
}

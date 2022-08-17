package io.czen.errorhandling.configuration;

import io.czen.errorhandling.model.handler.RestAccessDeniedHandler;
import io.czen.errorhandling.model.handler.RestAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    @Autowired
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            RestAuthenticationEntryPoint restAuthenticationEntryPoint,
            RestAccessDeniedHandler restAccessDeniedHandler) throws Exception {
        http


            .authorizeRequests()
            .antMatchers(HttpMethod.POST, "/v1/student").hasRole("ADMIN")
            .and()
                .httpBasic()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
            .exceptionHandling()
            .accessDeniedHandler(restAccessDeniedHandler)
            .and()
            .csrf()
            .disable();
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("apiuser").password("{noop}password").roles("USER")
                .and()
                .withUser("admin").password("{noop}password").roles("USE", "ADMIN");
    }

//    @Bean
//    public InMemoryUserDetailsManager userDetailsService() {
//        UserDetails apiUser = User
//                .withUsername("apiuser")
//                .password("{noop}password")
//                .roles("USER")
//                .build();
//        UserDetails admin = User
//                .withUsername("admin")
//                .password("{noop}password")
//                .roles("ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(apiUser, admin);
//    }
}

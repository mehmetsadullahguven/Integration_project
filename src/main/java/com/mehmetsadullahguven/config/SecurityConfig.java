package com.mehmetsadullahguven.config;

import com.mehmetsadullahguven.jwt.AuthEntryPoint;
import com.mehmetsadullahguven.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public static final String  AUTHENTICATE= "/authenticate";
    public static final String  REGISTER= "/register";
    public static final String  REFRESH_TOKEN= "/refreshToken";
    public static final String  WEBHOOK= "/webhook/**";
    public static final String[]  SWAGGER_PATHS= {
            "/swagger-ui/**",
            "/v3/api-docs/**",
//            "/swagger-ui.html"
    };


    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private AuthEntryPoint authEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request-> request
                        .requestMatchers(AUTHENTICATE, REGISTER, REFRESH_TOKEN, WEBHOOK).permitAll()
                        .requestMatchers(SWAGGER_PATHS).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPoint));

        return http.build();
    }
}


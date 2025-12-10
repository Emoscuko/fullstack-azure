package com.in28minutes.rest.basic.auth;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SpringSecurityConfigurationBasicAuth {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> auth
				// 1. Allow Preflight checks (for Frontend)
				.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

				// 2. Allow Swagger UI and API Docs (This fixes the download issue)
				.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

				// 3. Authenticate everything else
				.anyRequest().authenticated());

		// Disable CSRF (Standard for Stateless REST APIs)
		http.csrf(csrf -> csrf.disable());

		// Use Basic Auth
		http.httpBasic(withDefaults());

		// Set Session to Stateless (Stops browser from caching the login state)
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}
}
package com.secure.notes.security;

import org.springframework.security.config.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
@EnableWebSecurity
public class SecurityConfig
{
	
	  @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

	        http
	                .authorizeHttpRequests(
	                        auth ->
	                                auth.
	                                        requestMatchers("/home").permitAll()
	                                        .requestMatchers("/admin").denyAll()
	                                        .anyRequest().authenticated()
	                )
	                .httpBasic(Customizer.withDefaults());
	                http.sessionManagement(session->
	                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	            );
	               //http.formLogin(Customizer.withDefaults());

	        return http.build();
	    }


}

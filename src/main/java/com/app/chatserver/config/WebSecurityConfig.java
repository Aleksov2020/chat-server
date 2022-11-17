package com.app.chatserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        // This is not for websocket authorization, and this should most likely not be altered.
        http
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .csrf().disable()
                .authorizeRequests()

                // websoket endpoint
                .antMatchers("/ruchat/**").permitAll()

                // websoket test requests
                .antMatchers( "/", "/app.js").permitAll()

                // webjars packages
                .antMatchers("/webjars/**").permitAll()

                // chat requests
                .antMatchers("/chat/v1.0/**").permitAll()

                .antMatchers(HttpMethod.POST,"/chat/v1.0/**").permitAll()

                // post requests
                .antMatchers("/post/v1.0/**").permitAll()

                .antMatchers(HttpMethod.POST,"/post/v1.0/**").permitAll()

                // deny other
                .anyRequest().denyAll();

    }
}

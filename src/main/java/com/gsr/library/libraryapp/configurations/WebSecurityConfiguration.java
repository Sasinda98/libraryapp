package com.gsr.library.libraryapp.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .mvcMatchers(HttpMethod.OPTIONS, "/**");
        web
                .ignoring()
                .mvcMatchers("/swagger-ui.html/**",
                        "/configuration/**",
                        "/swagger-resources/**",
                        "/v2/api-docs",
                        "/webjars/**"
                );

    }
}

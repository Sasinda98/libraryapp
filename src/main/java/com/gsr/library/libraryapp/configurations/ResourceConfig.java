package com.gsr.library.libraryapp.configurations;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.web.bind.annotation.CrossOrigin;

@Configuration
@EnableResourceServer
@SecurityScheme(name = "Library Authorization", type = SecuritySchemeType.OAUTH2, flows = @OAuthFlows(clientCredentials = @OAuthFlow(tokenUrl = "http://localhost:8080/oauth/token", scopes = {
        @OAuthScope(name = "WRITE", description = "Any operation that writes to API."),
        @OAuthScope(name = "READ", description = "Any operation that reads from API.") })))
public class ResourceConfig implements ResourceServerConfigurer {
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("book");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers( "/swagger-ui.html/**",   "/v3/api-docs")
                .permitAll();
    }
}

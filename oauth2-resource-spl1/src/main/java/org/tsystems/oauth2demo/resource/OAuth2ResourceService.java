package org.tsystems.oauth2demo.resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@EnableWebSecurity

public class OAuth2ResourceService extends WebMvcConfigurerAdapter {
	public static void main(final String[] args) {
		SpringApplication.run(OAuth2ResourceService.class, args);
	}
}

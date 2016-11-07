package org.tsystems.oauth2demo.server.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class OAuth2ServerSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;

	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Override
    @Bean
    public AuthenticationManager authenticationManagerBean() 
      throws Exception {
        return super.authenticationManagerBean();
    }
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(this.dataSource).passwordEncoder(passwordEncoder);
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.requestMatchers()
			   .antMatchers("/oauth/authorize", "/oauth/confirm_access")
			   .and()
			   .authorizeRequests()
			.anyRequest()
			.authenticated();

		// Disable session creation in Spring Security
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
}
package org.tsystems.oauth2demo.server.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

@EnableAuthorizationServer
@Configuration
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private DataSource dataSource;
	
	@Value("${authorization.accessTokenValiditySeconds}")
	private int accessTokenValiditySeconds;
	
	@Value("${authorization.refreshTokenValiditySeconds}")
	private int refreshTokenValiditySeconds;
	
	@Value("${authorization.reuseRefreshToken}")
	private boolean reuseRefreshToken;
	
	@Value("${authorization.supportRefreshToken}")
	private boolean supportRefreshToken;
	
	@Bean
	public TokenStore tokenStore() {
	    return new JdbcTokenStore(dataSource);
	}
	
	@Bean
	protected AuthorizationCodeServices authorizationCodeServices() {
		return new JdbcAuthorizationCodeServices(dataSource);
    }
	
	@Bean
	public DefaultAccessTokenConverter defaultAccessTokenConverter() {
		return new DefaultAccessTokenConverter();
	}
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer)
			throws Exception {
		oauthServer
			.tokenKeyAccess("permitAll()")
//			.checkTokenAccess("isAuthenticated()")
			.checkTokenAccess("permitAll()");
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients)
			throws Exception {
			clients.jdbc(dataSource);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints)
			throws Exception {
		endpoints.authorizationCodeServices(this.authorizationCodeServices())
				 .tokenStore(tokenStore())
				 .tokenServices(customTokenServices())
				 .authenticationManager(authenticationManager)
				 .accessTokenConverter(defaultAccessTokenConverter());
	}

	private AuthorizationServerTokenServices customTokenServices(){
	      DefaultTokenServices tokenServices = new DefaultTokenServices();
		  tokenServices.setTokenStore(tokenStore());
		  tokenServices.setSupportRefreshToken(supportRefreshToken);
		  tokenServices.setAccessTokenValiditySeconds(accessTokenValiditySeconds);
		  tokenServices.setRefreshTokenValiditySeconds(refreshTokenValiditySeconds);
//		  tokenServices.setClientDetailsService(clientDetailsService);
		  tokenServices.setReuseRefreshToken(reuseRefreshToken);
		  return tokenServices;
		}
}
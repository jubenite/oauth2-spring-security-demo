package org.tsystems.oauth2demo.proxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy

public class OAuth2DemoProxyApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(OAuth2DemoProxyApplication.class, args);
	}
}

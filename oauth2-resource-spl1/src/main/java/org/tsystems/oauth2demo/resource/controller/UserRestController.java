package org.tsystems.oauth2demo.resource.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {

	@RequestMapping(value = "user", method = RequestMethod.GET)
	public OAuth2User getAuthenticatedUser() {
		OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext()
				.getAuthentication();
		OAuth2User user = new OAuth2User((long) 1, (String) authentication.getPrincipal(), "", "", "email", "",
				authentication.getAuthorities(), true, null);
		return user;

	}
}
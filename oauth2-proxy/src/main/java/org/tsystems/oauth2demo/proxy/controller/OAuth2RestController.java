package org.tsystems.oauth2demo.proxy.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Configuration
@RestController
public class OAuth2RestController {

	@Value("${oauth2.clientId}")
	private String clientId;

	@Value("${oauth2.clientSecret}")
	private String clientSecret;

	@Value("${oauth2.accessTokenUri}")
	private String accessTokenUri;

	@Value("${oauth2.checkTokenUri}")
	private String checkTokenUri;

	private RestOperations restTemplate;

	public OAuth2RestController() {
		restTemplate = new RestTemplate();
		((RestTemplate) restTemplate).setErrorHandler(new DefaultResponseErrorHandler() {
			@Override
			// Ignore 400
			public void handleError(ClientHttpResponse response) throws IOException {
				if (response.getRawStatusCode() != 400) {
					super.handleError(response);
				}
			}
		});
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody OAuth2AuthenticationRequest authenticationRequest)
			throws AuthenticationException {

		OAuth2AuthenticationResponse response = null;

		// get access_token
		MultiValueMap<String, String> formAccessData = new LinkedMultiValueMap<String, String>();
		formAccessData.set("grant_type", "password");
		formAccessData.set("username", authenticationRequest.getUsername());
		formAccessData.set("password", authenticationRequest.getPassword());

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", getAuthorizationHeader(clientId, clientSecret));

		Map<String, Object> map = postForMap(accessTokenUri, formAccessData, headers);
		if (!map.containsKey("error")) {
			String tokenType = (String) map.get("token_type") + " ";
			String accessToken = (String) map.get("access_token");
			String refreshToken = (String) map.get("refresh_token");
			response = new OAuth2AuthenticationResponse(tokenType.concat(accessToken), tokenType.concat(refreshToken));
			// get roles
			// MultiValueMap<String, String> formCheckData = new
			// LinkedMultiValueMap<String, String>();
			// formCheckData.set("token", token);
			// Map<String, Object> checkTokenMap = postForMap(checkTokenUri,
			// formCheckData, headers);
			// map.put("authorities", checkTokenMap.get("authorities"));
			// map.put("user_name", checkTokenMap.get("user_name"));
		}

		// Return the token
		return ResponseEntity.ok(response);
	}

	@RequestMapping(value = "/refreshToken", method = RequestMethod.POST)
	public Map<String, Object> refresh(String refreshToken) {

		// get access_token
		MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
		form.set("grant_type", "refresh_token");
		form.set("refresh_token", refreshToken);

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", getAuthorizationHeader(clientId, clientSecret));

		return postForMap(accessTokenUri, form, headers);
	}

	private String getAuthorizationHeader(String clientId, String clientSecret) {
		String creds = String.format("%s:%s", clientId, clientSecret);
		try {
			return "Basic " + new String(Base64.encode(creds.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("Could not convert String");
		}
	}

	private Map<String, Object> postForMap(String path, MultiValueMap<String, String> formData, HttpHeaders headers) {
		if (headers.getContentType() == null) {
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		}
		@SuppressWarnings("rawtypes")
		Map map = restTemplate.exchange(path, HttpMethod.POST,
				new HttpEntity<MultiValueMap<String, String>>(formData, headers), Map.class).getBody();
		@SuppressWarnings("unchecked")
		Map<String, Object> result = map;
		return result;
	}
}
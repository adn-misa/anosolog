/*
 * Author : AdNovum Informatik AG
 */

package com.adnovum.anosolog.service;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TrinsicService {

	@Value("${trinsic.apiKey}")
	private String trinsicApiKey;

	@Value("${trinsic.credentialDefId}")
	private String credentialDef;

	@Value("${trinsic.verificationPolicyId}")
	private String verificationPolicyId;

	private final RestTemplate restTemplate = new RestTemplate();
	private final ObjectMapper objectMapper = new ObjectMapper();

	public String getCredentialOffer(String origin, String email, String firstname, String lastname) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(List.of(MediaType.APPLICATION_JSON));
		headers.setBearerAuth(trinsicApiKey);
		headers.set("Content-Type", "application/*+json");

		JSONObject body = new JSONObject();
		body.put("definitionId", credentialDef);
		body.put("automaticIssuance", true);

		JSONObject credValues = new JSONObject();
		credValues.put("origin", origin);
		credValues.put("email", email);
		credValues.put("firstname", firstname);
		credValues.put("lastname", lastname);

		body.put("credentialValues", credValues);

		HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);

		String credOfferResultStr = restTemplate.postForObject("https://api.trinsic.id/credentials/v1/credentials", entity,
				String.class);

		JsonNode root;
		try {
			root = objectMapper.readTree(credOfferResultStr);
			return root.get("offerUrl").textValue();
		}
		catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return null;
	}

	public String getVerification() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(List.of(MediaType.TEXT_PLAIN));
		headers.setBearerAuth(trinsicApiKey);

		HttpEntity<String> entity = new HttpEntity<>(headers);

		String url = String.format("https://api.trinsic.id/credentials/v1/verifications/policy/%s", verificationPolicyId);
		String verificationResultStr = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class).getBody();

		JsonNode root;
		try {
			root = objectMapper.readTree(verificationResultStr);
			return root.get("verificationRequestUrl").textValue();
		}
		catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return null;
	}
}

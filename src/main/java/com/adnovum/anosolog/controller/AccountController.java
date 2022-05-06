/*
 * Author : AdNovum Informatik AG
 */

package com.adnovum.anosolog.controller;

import java.awt.image.BufferedImage;

import com.adnovum.anosolog.service.QrService;
import com.adnovum.anosolog.service.TrinsicService;
import com.google.zxing.WriterException;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AccountController {

	private final TrinsicService trinsicService;
	private final QrService qrService;

	public AccountController(TrinsicService trinsicService, QrService qrService) {
		this.trinsicService = trinsicService;
		this.qrService = qrService;
	}

	@GetMapping("/oidc-principal")
	public OidcUser getOidcUserPrincipal(
			@AuthenticationPrincipal OidcUser principal) {
		return principal;
	}

	@GetMapping(value = "/credential", produces = MediaType.IMAGE_PNG_VALUE)
	public BufferedImage getCredentialOffer(@AuthenticationPrincipal OidcUser principal) throws WriterException {
		String result = trinsicService.getCredentialOffer(
				"Google",
				principal.getEmail(),
				principal.getGivenName(),
				principal.getFamilyName()
		);

		return qrService.generateQrCode(result);
	}

	@GetMapping(value = "/verify", produces = MediaType.IMAGE_PNG_VALUE)
	public BufferedImage getVerification(@AuthenticationPrincipal OidcUser principal) throws WriterException {
		String result = trinsicService.getVerification();

		return qrService.generateQrCode(result);
	}
}

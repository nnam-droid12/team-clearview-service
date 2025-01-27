package com.clearview.docusign.hackathon.Agreement.controller;

import com.clearview.docusign.hackathon.Agreement.service.DocumentService;
import com.clearview.docusign.hackathon.auth.config.DocuSignConfig;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.client.auth.OAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/api/v1/docusign")
public class OAuthController {

    private static final Logger log = LoggerFactory.getLogger(OAuthController.class);

    @Value("${docusign.integration-key}")
    private String integrationKey;

    @Value("${docusign.private-key}")
    private String privateKey;

    @Value("${docusign.user-id}")
    private String userId;

    private String accessToken;
    private long tokenExpiresAt;

    @GetMapping("/consent-url")
    public ResponseEntity<Map<String, String>> generateConsentUrl() {
        String consentUrl = String.format(
                "https://account-d.docusign.com/oauth/auth?response_type=code&scope=signature%%20impersonation%%20adm_store_unified_repo_read&client_id=%s&redirect_uri=https://contract-image-latest.onrender.com/api/v1/docusign/callback",
                integrationKey
        );

        Map<String, String> response = new HashMap<>();
        response.put("consent_url", consentUrl);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/callback")
    public ResponseEntity<String> handleCallback(@RequestParam String code) {
        try {
            ApiClient apiClient = new ApiClient(ApiClient.DEMO_REST_BASEPATH);
            List<String> scopes = Arrays.asList(
                    OAuth.Scope_SIGNATURE,
                    OAuth.Scope_IMPERSONATION,
                    "adm_store_unified_repo_read"
            );

            OAuth.OAuthToken oAuthToken = apiClient.requestJWTUserToken(
                    integrationKey,
                    userId,
                    scopes,
                    privateKey.getBytes(StandardCharsets.UTF_8),
                    3600
            );

            OAuth.UserInfo userInfo = apiClient.getUserInfo(oAuthToken.getAccessToken());
            return ResponseEntity.ok("Authentication successful");
        } catch (Exception e) {
            log.error("Token exchange failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }



    public String getAccessToken() {
        return this.accessToken;
    }


    private String createThankYouPage(String title, String message) {
        return "<!DOCTYPE html>" +
                "<html><head><title>" + title + "</title>" +
                "<style>body{font-family:Arial,sans-serif;text-align:center;padding-top:50px;}</style>" +
                "</head><body>" +
                "<h1>" + title + "</h1>" +
                "<p>" + message + "</p>" +
                "<p>You can now close this window.</p>" +
                "</body></html>";
    }
}
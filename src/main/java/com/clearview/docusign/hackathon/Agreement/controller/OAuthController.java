package com.clearview.docusign.hackathon.Agreement.controller;

import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.client.auth.OAuth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/docusign")
public class OAuthController {

    @Value("${docusign.integration-key}")
    private String integrationKey;

    @Value("${docusign.private-key}")
    private String privateKey;

    private static final List<String> SCOPES = Arrays.asList(
            OAuth.Scope_SIGNATURE,
            OAuth.Scope_IMPERSONATION,
            "https://api.docusign.com/v1",
            "adm_store_unified_repo_read"
    );

    @GetMapping("/consent-url")
    public String generateConsentUrl() {
        String baseUrl = "https://account-d.docusign.com/oauth/auth";
        String redirectUri = "https://contract-image-latest.onrender.com/api/v1/docusign/callback";

        String scopeString = String.join(" ",
                OAuth.Scope_SIGNATURE,
                OAuth.Scope_IMPERSONATION,
                "https://api.docusign.com/v1",
                "adm_store_unified_repo_read"
        );

        return String.format("%s?response_type=code&scope=%s&client_id=%s&redirect_uri=%s",
                baseUrl,
                scopeString,
                integrationKey,
                redirectUri
        );
    }


    @GetMapping("/callback")
    public ResponseEntity<String> handleCallback(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String error) {

        if (error != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(createResponsePage("Authorization Failed", "There was an error during authorization: " + error));
        }

        if (code != null) {
            try {
                // Exchange authorization code for access token
                ApiClient apiClient = new ApiClient("https://account-d.docusign.com");
                OAuth.OAuthToken token = apiClient.generateAccessToken(
                        integrationKey,
                        new String(privateKey.getBytes(), StandardCharsets.UTF_8),
                        code
                );

                return ResponseEntity.status(HttpStatus.OK)
                        .body(createResponsePage("Access Granted", "You can now close this page. Authorization was successful."));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(createResponsePage("Authorization Failed", "Token exchange failed: " + e.getMessage()));
            }
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(createResponsePage("No Authorization", "No authorization code received."));
    }

    private String createResponsePage(String title, String message) {
        return "<!DOCTYPE html>" +
                "<html><head>" +
                "<title>" + title + "</title>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; text-align: center; padding-top: 50px; }" +
                "</style>" +
                "</head><body>" +
                "<h1>" + title + "</h1>" +
                "<p>" + message + "</p>" +
                "<p>You can now close this page.</p>" +
                "</body></html>";
    }

}
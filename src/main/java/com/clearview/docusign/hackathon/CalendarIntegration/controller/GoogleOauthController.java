package com.clearview.docusign.hackathon.CalendarIntegration.controller;

import com.clearview.docusign.hackathon.CalendarIntegration.service.GoogleCalendarSyncService;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.services.calendar.CalendarScopes;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Collections;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/googleOauth")
public class GoogleOauthController {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(GoogleOauthController.class);
    private final AuthorizationCodeFlow authorizationCodeFlow;

    @Value("${google.oauth.redirectUri}")
    private String redirectUri;

    @Value("${google.credentials.token-store-path:/app/tokens}")
    private String tokenStorePath;

    public GoogleOauthController(AuthorizationCodeFlow authorizationCodeFlow) {
        this.authorizationCodeFlow = authorizationCodeFlow;
    }

    @GetMapping("/oauth2/authorize")
    public ResponseEntity<String> authorize() throws Exception {
        Set<String> scopes = Collections.singleton("https://www.googleapis.com/auth/calendar");

        log.error("Generated Scopes: {}", scopes);
        log.error("Redirect URI: {}", redirectUri);

        AuthorizationCodeRequestUrl authorizationUrl = authorizationCodeFlow.newAuthorizationUrl()
                .setRedirectUri(redirectUri)
                .setScopes(scopes);

        log.error("Full Authorization URL: {}", authorizationUrl.toString());

        return ResponseEntity.ok(authorizationUrl.toString());
    }


    @GetMapping("/google-callback")
    public ResponseEntity<String> handleCallback(
            @RequestParam("code") String authorizationCode
    ) throws Exception {
        try {
            TokenResponse tokenResponse = authorizationCodeFlow.newTokenRequest(authorizationCode)
                    .setRedirectUri(redirectUri)
                    .execute();

            // Force offline access to get refresh token
            tokenResponse.setRefreshToken(tokenResponse.getRefreshToken());

            String userId = "williamekene700@gmail.com";

            Credential credential = authorizationCodeFlow.createAndStoreCredential(tokenResponse, userId);

            log.info("OAuth Successful - Tokens Stored");

            return ResponseEntity.ok("Authorization successful");
        } catch (Exception e) {
            log.error("OAuth Callback Error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Authorization failed: " + e.getMessage());
        }
    }


    @GetMapping("/generate-auth-url")
    public ResponseEntity<String> generateAuthorizationUrl() throws Exception {
        AuthorizationCodeRequestUrl authorizationUrl = authorizationCodeFlow.newAuthorizationUrl()
                .setRedirectUri(redirectUri)
                .setScopes(Collections.singleton("https://www.googleapis.com/auth/calendar"));

        System.out.println("Redirect URI: " + redirectUri);
        System.out.println("Authorization URL: " + authorizationUrl.toString());

        return ResponseEntity.ok(authorizationUrl.toString());
    }

    @GetMapping("/check-auth")
    public ResponseEntity<String> checkAuthentication() throws Exception {
        Credential credential = authorizationCodeFlow.loadCredential("williamekene700@gmail.com");
        return credential != null
                ? ResponseEntity.ok("Authenticated")
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Authorization required. Visit /generate-auth-url");
    }
}
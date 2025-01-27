package com.clearview.docusign.hackathon.Agreement.service;

import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.auth.OAuth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Service
public class TokenService {
    private String accessToken;
    private long expiresAt;

    @Value("${docusign.integration-key}")
    private String integrationKey;

    @Value("${docusign.user-id}")
    private String userId;

    @Value("${docusign.private-key}")
    private String privateKey;

    public synchronized String getAccessToken() throws Exception {
        if (accessToken == null || System.currentTimeMillis() >= expiresAt) {
            refreshToken();
        }
        return accessToken;
    }

    private void refreshToken() throws Exception {
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

        this.accessToken = oAuthToken.getAccessToken();
        this.expiresAt = System.currentTimeMillis() + (oAuthToken.getExpiresIn() * 1000L);
    }
}

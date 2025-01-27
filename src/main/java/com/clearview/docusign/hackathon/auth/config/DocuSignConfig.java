package com.clearview.docusign.hackathon.auth.config;

import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.client.auth.OAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Configuration
public class DocuSignConfig {
    private static final Logger log = LoggerFactory.getLogger(DocuSignConfig.class);

    @Value("${docusign.private-key}")
    private String privateKey;

    @Value("${docusign.integration-key}")
    private String integrationKey;

    @Value("${docusign.user-id}")
    private String userId;
        @Value("${docusign.auth-server}")
        private String authServer;

    @Bean
    @ConditionalOnProperty(name = "docusign.consent-granted", havingValue = "true")
    public ApiClient docuSignApiClient() throws Exception {
        ApiClient apiClient = new ApiClient(ApiClient.DEMO_REST_BASEPATH);
        List<String> scopes = Arrays.asList(
                OAuth.Scope_SIGNATURE,
                OAuth.Scope_IMPERSONATION,
                "adm_store_unified_repo_read"
        );

        try {
            OAuth.OAuthToken oAuthToken = apiClient.requestJWTUserToken(
                    integrationKey,
                    userId,
                    scopes,
                    privateKey.getBytes(StandardCharsets.UTF_8),
                    3600
            );
            apiClient.setAccessToken(oAuthToken.getAccessToken(), oAuthToken.getExpiresIn());
            return apiClient;
        } catch (ApiException e) {
            String consentUrl = String.format(
                    "https://account-d.docusign.com/oauth/auth?response_type=code&scope=signature%%20impersonation%%20adm_store_unified_repo_read&client_id=%s&redirect_uri=https://contract-image-latest.onrender.com/api/v1/docusign/callback",
                    integrationKey
            );
            throw new RuntimeException("DocuSign consent required. Visit: " + consentUrl);
        }

    }

    @Bean
    @ConditionalOnMissingBean(ApiClient.class)
    public ApiClient fallbackDocuSignApiClient() {
        return new ApiClient(ApiClient.DEMO_REST_BASEPATH);
    }
}
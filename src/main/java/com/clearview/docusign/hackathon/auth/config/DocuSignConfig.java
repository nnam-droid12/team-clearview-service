package com.clearview.docusign.hackathon.auth.config;

import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.auth.OAuth;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;


import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.security.Security;

@Configuration
public class DocuSignConfig {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DocuSignConfig.class);

    @Value("${docusign.private-key}")
    private String privateKey;

    @Value("${docusign.integration-key}")
    private String integrationKey;

    @Value("${docusign.user-id}")
    private String userId;

    @Value("${docusign.auth-server}")
    private String authServer;

    @PostConstruct
    public void init() {

        Security.addProvider(new BouncyCastleProvider());
    }


    @Bean
    public ApiClient apiClient() throws Exception {
        ApiClient apiClient = new ApiClient();

        apiClient.setBasePath("https://" + authServer);

        try {

            String cleanedPrivateKey = privateKey.trim();

            java.util.List<String> scopes = Arrays.asList(
                    OAuth.Scope_SIGNATURE,
                    OAuth.Scope_IMPERSONATION
            );


            OAuth.OAuthToken oAuthToken = apiClient.requestJWTUserToken(
                    integrationKey,
                    userId,
                    scopes,
                    cleanedPrivateKey.getBytes(),
                    3600
            );


            log.debug("Received JWT Access Token: " + oAuthToken.getAccessToken());


            apiClient.setAccessToken(oAuthToken.getAccessToken(), oAuthToken.getExpiresIn());

            return apiClient;
        } catch (Exception e) {
            log.error("Error while creating DocuSign ApiClient: ", e);
            throw new Exception("DocuSign authentication failed", e);
        }
    }

}
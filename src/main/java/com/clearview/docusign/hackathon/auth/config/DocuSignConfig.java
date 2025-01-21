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

//    @PostConstruct
//    public void initFile() throws IOException {
//
//        if (privateKey == null || privateKey.trim().isEmpty()) {
//            Resource resource = new ClassPathResource("docusign-private-key.txt");
//            privateKey = new String(Files.readAllBytes(resource.getFile().toPath()));
//        }
//    }

    @Bean
    public ApiClient apiClient() throws Exception {
        ApiClient apiClient = new ApiClient();

        // Check if you're using the correct base path for auth-server (production or demo)
        apiClient.setBasePath("https://" + authServer);

        try {
            // Ensure that scopes and privateKey are set properly
            String cleanedPrivateKey = privateKey.trim();  // Clean the private key string

            // Prepare OAuth token request parameters
            java.util.List<String> scopes = Arrays.asList(
                    OAuth.Scope_SIGNATURE,
                    OAuth.Scope_IMPERSONATION
            );

            // Request the access token with JWT
            OAuth.OAuthToken oAuthToken = apiClient.requestJWTUserToken(
                    integrationKey,  // Integration key (Client ID)
                    userId,          // User ID
                    scopes,          // Scopes
                    cleanedPrivateKey.getBytes(),  // Private Key as bytes
                    3600  // Token validity in seconds
            );

            // Log the token received for debugging purposes
            log.debug("Received JWT Access Token: " + oAuthToken.getAccessToken());

            // Set access token
            apiClient.setAccessToken(oAuthToken.getAccessToken(), oAuthToken.getExpiresIn());

            return apiClient;
        } catch (Exception e) {
            log.error("Error while creating DocuSign ApiClient: ", e);
            throw new Exception("DocuSign authentication failed", e);
        }
    }

}
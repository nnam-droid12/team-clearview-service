package com.clearview.docusign.hackathon.NavigatorAgreement.service;

import com.clearview.docusign.hackathon.NavigatorAgreement.utils.AgreementsList;
import com.clearview.docusign.hackathon.NavigatorAgreement.utils.NavigatorAgreement;
import com.docusign.esign.client.ApiClient;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NavigatorApiService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(NavigatorApiService.class);
    private final String accountId;
    private final ApiClient apiClient;
    private static final String NAVIGATOR_BASE_URL = "https://navigator.docusign.net/v1";

    public NavigatorApiService(
            ApiClient apiClient,
            @Value("${docusign.account-id}") String accountId) {
        this.apiClient = apiClient;
        this.accountId = accountId;
    }

    public NavigatorAgreement getAgreement(String agreementId) throws Exception {
        try {
            String accessToken = apiClient.getAccessToken();

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            String url = String.format("%s/accounts/%s/agreements/%s",
                    NAVIGATOR_BASE_URL, accountId, agreementId);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<NavigatorAgreement> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    NavigatorAgreement.class
            );

            return response.getBody();
        } catch (Exception e) {
            log.error("Error fetching agreement details: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch agreement details", e);
        }
    }


    public AgreementsList getAgreementsList() throws Exception {
        try {
            String accessToken = apiClient.getAccessToken();

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            String url = String.format("%s/accounts/%s/agreements",
                    NAVIGATOR_BASE_URL, accountId);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<AgreementsList> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    AgreementsList.class
            );

            return response.getBody();
        } catch (Exception e) {
            log.error("Error fetching agreements list: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch agreements list", e);
        }
    }
}

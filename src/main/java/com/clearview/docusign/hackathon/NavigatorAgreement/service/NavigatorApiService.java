package com.clearview.docusign.hackathon.NavigatorAgreement.service;

import com.clearview.docusign.hackathon.Agreement.controller.OAuthController;
import com.clearview.docusign.hackathon.Agreement.entities.Agreement;
import com.clearview.docusign.hackathon.Agreement.service.TokenService;
import com.clearview.docusign.hackathon.NavigatorAgreement.utils.AgreementNavigatorDto;
import com.docusign.esign.client.ApiClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NavigatorApiService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(NavigatorApiService.class);

    private static final String ESIGNATURE_BASE_URL = "https://demo.docusign.net/restapi/v2.1";

    @Value("${docusign.integration-key}")
    private String integrationKey;

    @Value("${docusign.user-id}")
    private String userId;

    @Value("${docusign.private-key}")
    private String privateKey;
    private final OAuthController oAuthController;

    private final String accountId;
    private final AgreementMapperService mapperService;
    private final ApiClient apiClient;
    private final TokenService tokenService;
    private final RestTemplate restTemplate;
    private static final String NAVIGATOR_BASE_URL = "https://api-d.docusign.com/v1";

    public NavigatorApiService(
            OAuthController oAuthController, ApiClient apiClient,
            @Value("${docusign.account-id}") String accountId, AgreementMapperService mapperService, TokenService tokenService, RestTemplate restTemplate) {
        this.oAuthController = oAuthController;
        this.apiClient = apiClient;
        this.accountId = accountId;
        this.mapperService = mapperService;
        this.tokenService = tokenService;
        this.restTemplate = restTemplate;
    }

    public AgreementNavigatorDto getNavigatorAgreement(String agreementId) throws Exception {
        verifyEnvelopeExists(agreementId);

        String accessToken = tokenService.getAccessToken();

        verifyNavigatorAccess();

        String url = NAVIGATOR_BASE_URL + "/accounts/" + accountId + "/agreements/" + agreementId;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("DS-API-VERSION", "1.0");
        headers.set("Accept", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            log.debug("Making request to Navigator API with URL: {}", url);
            ResponseEntity<String> rawResponse = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            return parseResponse(rawResponse);

        } catch (HttpClientErrorException e) {
            handleNavigatorApiError(e, agreementId);
            throw e;
        }
    }

    private void verifyEnvelopeExists(String envelopeId) throws Exception {
        String accessToken = tokenService.getAccessToken();
        String url = ESIGNATURE_BASE_URL + "/accounts/" + accountId + "/envelopes/" + envelopeId;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            log.debug("Envelope {} exists in eSignature API", envelopeId);
        } catch (HttpClientErrorException e) {
            log.error("Envelope {} not found in eSignature API: {}", envelopeId, e.getResponseBodyAsString());
            throw new IllegalArgumentException("Envelope not found in DocuSign eSignature system");
        }
    }

    private void verifyNavigatorAccess() throws Exception {
        String accessToken = tokenService.getAccessToken();
        String url = NAVIGATOR_BASE_URL + "/accounts/" + accountId;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("DS-API-VERSION", "1.0");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            log.debug("Navigator API access verified");
        } catch (HttpClientErrorException e) {
            log.error("Navigator API access error: {}", e.getResponseBodyAsString());
            if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                throw new SecurityException("No access to Navigator API. Please verify Navigator is enabled for your account");
            }
            throw e;
        }
    }

    private void handleNavigatorApiError(HttpClientErrorException e, String agreementId) {
        String errorBody = e.getResponseBodyAsString();
        log.error("Navigator API error: {} - {}", e.getStatusCode(), errorBody);

        if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
            log.error("Agreement {} not found in Navigator. This could mean:", agreementId);
            log.error("1. The envelope hasn't been indexed by Navigator yet");
            log.error("2. Navigator is not enabled for your account");
            log.error("3. The agreement is not available in Navigator");
        }
    }

    private AgreementNavigatorDto parseResponse(ResponseEntity<String> rawResponse) throws JsonProcessingException, JsonProcessingException {
        if (rawResponse.getBody() == null) {
            throw new IllegalStateException("Received null response body from DocuSign API");
        }

        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

        return mapper.readValue(rawResponse.getBody(), AgreementNavigatorDto.class);
    }

//    public Agreement getAgreement(String agreementId) throws Exception {
//        AgreementNavigatorDto navigatorDto = getNavigatorAgreement(agreementId);
//        return mapperService.mapToEntity(navigatorDto);
//    }



//    public AgreementsList getAgreementsList() throws Exception {
//        try {
//            String accessToken = apiClient.getAccessToken();
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setBearerAuth(accessToken);
//            headers.setContentType(MediaType.APPLICATION_JSON);
//
//            String url = String.format("%s/accounts/%s/agreements",
//                    NAVIGATOR_BASE_URL, accountId);
//
//            HttpEntity<?> entity = new HttpEntity<>(headers);
//            RestTemplate restTemplate = new RestTemplate();
//
//            ResponseEntity<AgreementsList> response = restTemplate.exchange(
//                    url,
//                    HttpMethod.GET,
//                    entity,
//                    AgreementsList.class
//            );
//
//            return response.getBody();
//        } catch (Exception e) {
//            log.error("Error fetching agreements list: {}", e.getMessage());
//            throw new RuntimeException("Failed to fetch agreements list", e);
//        }
//    }
}

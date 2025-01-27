package com.clearview.docusign.hackathon.NavigatorAgreement.service;

import com.clearview.docusign.hackathon.Agreement.entities.Agreement;
import com.clearview.docusign.hackathon.Agreement.service.TokenService;
import com.clearview.docusign.hackathon.NavigatorAgreement.utils.EnvelopDto;
import com.fasterxml.jackson.databind.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class EnvelopService {

    private static final String ESIGNATURE_BASE_URL = "https://demo.docusign.net/restapi/v2.1";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(EnvelopService.class);

    @Value("${docusign.account-id}")
    private String accountId;

    private final TokenService tokenService;
    private final RestTemplate restTemplate;
    private final AgreementMapperService mapperService;

    public EnvelopService(TokenService tokenService, RestTemplate restTemplate,
                          AgreementMapperService mapperService) {
        this.tokenService = tokenService;
        this.restTemplate = restTemplate;
        this.mapperService = mapperService;
    }

    public EnvelopDto getEnvelope(String envelopeId) throws Exception {
        String accessToken = tokenService.getAccessToken();

        String url = ESIGNATURE_BASE_URL + "/accounts/" + accountId + "/envelopes/" + envelopeId
                + "?include=recipients,documents";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            log.debug("Making request to eSignature API with URL: {}", url);
            ResponseEntity<String> rawResponse = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            log.info("Raw DocuSign response: {}", rawResponse.getBody());

            ObjectMapper mapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);  // Use PropertyNamingStrategies instead

            mapper.enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            return mapper.readValue(rawResponse.getBody(), EnvelopDto.class);

        } catch (HttpClientErrorException e) {
            log.error("eSignature API error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw e;
        }
    }

    public Agreement getAgreement(String envelopeId) throws Exception {
        EnvelopDto envelopeDto = getEnvelope(envelopeId);
        return mapperService.mapToEntity(envelopeDto);
    }
}

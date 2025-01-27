package com.clearview.docusign.hackathon.NavigatorAgreement.controller;


import com.clearview.docusign.hackathon.Agreement.entities.Agreement;
import com.clearview.docusign.hackathon.NavigatorAgreement.service.EnvelopService;
import com.clearview.docusign.hackathon.NavigatorAgreement.service.NavigatorApiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("/api/v1/navigator")
public class NavigatorApiController {


    private static final Logger log = org.slf4j.LoggerFactory.getLogger(NavigatorApiController.class);
    private final NavigatorApiService navigatorApiService;
    private final EnvelopService envelopService;

    public NavigatorApiController(NavigatorApiService navigatorApiService, EnvelopService envelopService) {
        this.navigatorApiService = navigatorApiService;
        this.envelopService = envelopService;
    }


    @GetMapping("/get-envelop/{envelopeId}")
    public ResponseEntity<Agreement> getEnvelopAgreement(@PathVariable String envelopeId) {
        try {
            Agreement agreement = envelopService.getAgreement(envelopeId);
            return ResponseEntity.ok(agreement);
        } catch (HttpClientErrorException e) {
            log.error("DocuSign API error for envelope {}: {} - {}",
                    envelopeId, e.getStatusCode(), e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (Exception e) {
            log.error("Error getting agreement for envelope {}", envelopeId, e);
            return ResponseEntity.internalServerError().build();
        }
    }



//    @GetMapping("/agreements-navigator/{agreementId}")
//    public ResponseEntity<Agreement> getAgreement(@PathVariable String agreementId) {
//        try {
//            Agreement agreement = navigatorApiService.getAgreement(agreementId);
//            if (agreement == null) {
//                log.warn("No agreement found for ID: {}", agreementId);
//                return ResponseEntity.notFound().build();
//            }
//            return ResponseEntity.ok(agreement);
//        } catch (HttpClientErrorException e) {
//            log.error("DocuSign API error for agreement {}: {} - {}",
//                    agreementId, e.getStatusCode(), e.getResponseBodyAsString());
//            return ResponseEntity.status(e.getStatusCode()).body(null);
//        } catch (JsonProcessingException e) {
//            log.error("Error parsing agreement data for ID {}: {}", agreementId, e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
//        } catch (Exception e) {
//            log.error("Unexpected error while fetching agreement {}: {}",
//                    agreementId, e.getMessage(), e);
//            return ResponseEntity.internalServerError().build();
//        }
//    }

//    @GetMapping("/agreements-navigator")
//    public ResponseEntity<AgreementsList> getAgreementsList() {
//        try {
//            AgreementsList agreements = navigatorApiService.getAgreementsList();
//            return ResponseEntity.ok(agreements);
//        } catch (Exception e) {
//            log.error("Error fetching agreements list: {}", e.getMessage());
//            return ResponseEntity.internalServerError().build();
//        }
//    }

}

package com.clearview.docusign.hackathon.NavigatorAgreement.controller;


import com.clearview.docusign.hackathon.NavigatorAgreement.service.NavigatorApiService;
import com.clearview.docusign.hackathon.NavigatorAgreement.utils.AgreementsList;
import com.clearview.docusign.hackathon.NavigatorAgreement.utils.NavigatorAgreement;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/navigator")
public class NavigatorApiController {


    private static final Logger log = org.slf4j.LoggerFactory.getLogger(NavigatorApiController.class);
    private final NavigatorApiService navigatorApiService;

    public NavigatorApiController(NavigatorApiService navigatorApiService) {
        this.navigatorApiService = navigatorApiService;
    }

    @GetMapping("/agreements-navigator/{agreementId}")
    public ResponseEntity<NavigatorAgreement> getAgreement(@PathVariable String agreementId) {
        try {
            NavigatorAgreement agreement = navigatorApiService.getAgreement(agreementId);
            return ResponseEntity.ok(agreement);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/agreements-navigator")
    public ResponseEntity<AgreementsList> getAgreementsList() {
        try {
            AgreementsList agreements = navigatorApiService.getAgreementsList();
            return ResponseEntity.ok(agreements);
        } catch (Exception e) {
            log.error("Error fetching agreements list: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}

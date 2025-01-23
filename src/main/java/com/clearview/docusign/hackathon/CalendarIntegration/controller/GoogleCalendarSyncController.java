package com.clearview.docusign.hackathon.CalendarIntegration.controller;

import com.clearview.docusign.hackathon.CalendarIntegration.service.GoogleCalendarSyncService;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agreements/calendar")
public class GoogleCalendarSyncController {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(GoogleCalendarSyncController.class);
    private final GoogleCalendarSyncService googleCalendarSyncService;

    public GoogleCalendarSyncController(GoogleCalendarSyncService googleCalendarSyncService) {
        this.googleCalendarSyncService = googleCalendarSyncService;
    }

    @PostMapping("/{agreementId}/sync-google")
    public ResponseEntity<String> syncAgreementToGoogleCalendar(@PathVariable Long agreementId) {
        try {
            googleCalendarSyncService.syncAgreementToGoogleCalendar(agreementId);
            return ResponseEntity.ok("Agreement synced to Google Calendar successfully");
        } catch (Exception e) {
            log.error("Error syncing agreement to Google Calendar", e);
            return ResponseEntity.internalServerError()
                    .body("Failed to sync agreement to Google Calendar: " + e.getMessage());
        }
    }
}
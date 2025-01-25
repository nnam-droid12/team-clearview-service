package com.clearview.docusign.hackathon.CalendarIntegration.service;

import com.clearview.docusign.hackathon.Agreement.entities.Agreement;
import com.clearview.docusign.hackathon.Agreement.repository.AgreementRepository;
import com.clearview.docusign.hackathon.CalendarIntegration.controller.GoogleCalendarSyncController;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

import java.security.GeneralSecurityException;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GoogleCalendarSyncService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(GoogleCalendarSyncService.class);

    private final AgreementRepository agreementRepository;
    private final AuthorizationCodeFlow authorizationCodeFlow;
    private final String userId;
    private final Calendar calendarService;

    public GoogleCalendarSyncService(
            AgreementRepository agreementRepository,
            AuthorizationCodeFlow authorizationCodeFlow,
            @Value("${google.calendar.user-id}") String userId,
            @Autowired(required = false) Calendar calendarService
    ) {
        this.agreementRepository = agreementRepository;
        this.authorizationCodeFlow = authorizationCodeFlow;
        this.userId = userId;
        this.calendarService = calendarService;
    }

    private Calendar getCalendarService() throws Exception {
        try {
            Credential credential = authorizationCodeFlow.loadCredential(userId);

            if (credential == null) {
                log.error("CRITICAL: No credential found for user: {}", userId);
                throw new IllegalStateException("No credentials available for user: " + userId);
            }

            // Log credential details for debugging
            log.info("Credential details:");
            log.info("Access Token: {}", credential.getAccessToken());
            log.info("Refresh Token: {}", credential.getRefreshToken());
            log.info("Token Expiration: {}", credential.getExpirationTimeMilliseconds());

            // Attempt token refresh if needed
            if (credential.getAccessToken() == null || credential.getExpirationTimeMilliseconds() <= 60) {
                log.info("Attempting to refresh token...");
                credential.refreshToken();
            }

            return new Calendar.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    credential
            ).setApplicationName("Agreement Management System").build();
        } catch (Exception e) {
            log.error("Calendar Service Initialization Failure", e);
            throw new IllegalStateException("Failed to initialize calendar service: " + e.getMessage(), e);
        }
    }

    public void syncAgreementToGoogleCalendar(Long agreementId) throws IOException {
        try {
            log.error("Starting sync for Agreement ID: {}", agreementId);

            Calendar calendar = getCalendarService();

            Agreement agreement = agreementRepository.findByIdWithMilestones(agreementId)
                    .orElseThrow(() -> new RuntimeException("Agreement not found"));

            BatchRequest batch = calendar.batch();

            List<Event> milestoneEvents = createMilestoneEvents(agreement);
            log.error("Milestone Events count: {}", milestoneEvents.size());

            insertEventsIntoBatch(batch, milestoneEvents);


        } catch (Exception e) {
            log.error("Detailed sync error:", e);
            log.error("Exception class: {}", e.getClass().getName());
            log.error("Exception message: {}", e.getMessage());

            if (e.getCause() != null) {
                log.error("Cause class: {}", e.getCause().getClass().getName());
                log.error("Cause message: {}", e.getCause().getMessage());
            }

            throw new IOException("Failed to sync agreement to Google Calendar", e);
        }
    }

    private List<Event> createMilestoneEvents(Agreement agreement) {
        log.error("Agreement ID: {}", agreement.getAgreementId());
        log.error("Milestones count: {}", agreement.getMilestones().size());

        return agreement.getMilestones().stream()
                .filter(milestone -> milestone.getDueDate() != null)
                .map(milestone -> {
                    Event event = new Event()
                            .setSummary("Milestone: " + milestone.getTitle())
                            .setDescription(
                                    "Milestone for Agreement: " + agreement.getTitle() + "\n" +
                                            "Status: " + milestone.getStatus()
                            );

                    DateTime startDateTime = new DateTime(milestone.getDueDate().toInstant(ZoneOffset.UTC).toEpochMilli());
                    EventDateTime start = new EventDateTime()
                            .setDateTime(startDateTime)
                            .setTimeZone("UTC");

                    DateTime endDateTime = new DateTime(
                            milestone.getDueDate().plusHours(1).toInstant(ZoneOffset.UTC).toEpochMilli()
                    );
                    EventDateTime end = new EventDateTime()
                            .setDateTime(endDateTime)
                            .setTimeZone("UTC");

                    event.setStart(start)
                            .setEnd(end)
                            .setColorId(getColorForMilestoneStatus(milestone.getStatus()));

                    return event;
                })
                .collect(Collectors.toList());
    }



    private List<Event> createObligationEvents(Agreement agreement) {
        return agreement.getObligations().stream()
                .filter(obligation -> obligation.getDueDate() != null)
                .map(obligation -> {
                    Event event = new Event()
                            .setSummary("Obligation: " + obligation.getDescription())
                            .setDescription(
                                    "Obligation for Agreement: " + agreement.getTitle() + "\n" +
                                            "Status: " + obligation.getStatus()
                            );


                    DateTime startDateTime = new DateTime(obligation.getDueDate().toInstant(ZoneOffset.UTC).toEpochMilli());
                    EventDateTime start = new EventDateTime()
                            .setDateTime(startDateTime)
                            .setTimeZone("UTC");


                    DateTime endDateTime = new DateTime(
                            obligation.getDueDate().plusHours(1).toInstant(ZoneOffset.UTC).toEpochMilli()
                    );
                    EventDateTime end = new EventDateTime()
                            .setDateTime(endDateTime)
                            .setTimeZone("UTC");

                    event.setStart(start)
                            .setEnd(end)
                            .setColorId(getColorForObligationStatus(obligation.getStatus()));

                    return event;
                })
                .collect(Collectors.toList());
    }


    private Event createAgreementContractEvent(Agreement agreement) {
        if (agreement.getSignedDate() == null) {
            return null;
        }

        Event event = new Event()
                .setSummary("Contract: " + agreement.getTitle())
                .setDescription(
                        "Agreement Details:\n" +
                                "Status: " + agreement.getStatus() + "\n" +
                                "Signed Date: " + agreement.getSignedDate()
                );


        DateTime startDateTime = new DateTime(agreement.getSignedDate().toInstant(ZoneOffset.UTC).toEpochMilli());
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("UTC");

        DateTime endDateTime = new DateTime(
                agreement.getSignedDate().plusHours(1).toInstant(ZoneOffset.UTC).toEpochMilli()
        );
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("UTC");

        event.setStart(start)
                .setEnd(end)
                .setColorId("10");

        return event;
    }


    private void insertEventsIntoBatch(BatchRequest batch, List<Event> events) throws IOException {
        for (Event event : events) {
            calendarService.events().insert("primary", event)
                    .queue(batch, new JsonBatchCallback<Event>() {
                        @Override
                        public void onSuccess(Event event, HttpHeaders responseHeaders) {
                            log.info("Event created: {}", event.getSummary());
                        }

                        @Override
                        public void onFailure(GoogleJsonError e, HttpHeaders responseHeaders) {
                            log.error("Error creating event: {}", e.getMessage());
                        }
                    });
        }
    }


    private String getColorForMilestoneStatus(String status) {
        switch (status) {
            case "PENDING":
                return "11";
            case "IN_PROGRESS":
                return "6";
            case "COMPLETED":
                return "10";
            default:
                return "7";
        }
    }


    private String getColorForObligationStatus(String status) {
        switch (status) {
            case "PENDING":
                return "5";
            case "IN_PROGRESS":
                return "6";
            case "COMPLETED":
                return "10";
            default:
                return "7";
        }
    }
}
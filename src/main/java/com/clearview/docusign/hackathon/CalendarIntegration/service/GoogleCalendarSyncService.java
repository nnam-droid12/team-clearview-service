package com.clearview.docusign.hackathon.CalendarIntegration.service;

import com.clearview.docusign.hackathon.Agreement.entities.Agreement;
import com.clearview.docusign.hackathon.Agreement.repository.AgreementRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoogleCalendarSyncService {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(GoogleCalendarSyncService.class);
    private final GoogleCredential googleCredential;
    private final AgreementRepository agreementRepository;
    private final Calendar calendarService;

    public GoogleCalendarSyncService(
            AgreementRepository agreementRepository,
            @Value("${google.calendar.primary-calendar-id:primary}") String primaryCalendarId, GoogleCredential googleCredential
    ) throws IOException, GeneralSecurityException {
        this.agreementRepository = agreementRepository;
        this.googleCredential = googleCredential;


        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        this.calendarService = new Calendar.Builder(
                httpTransport,
                jsonFactory,
                googleCredential
        ).setApplicationName("Agreement Management System").build();
    }


    public void syncAgreementToGoogleCalendar(Long agreementId) throws IOException {

        Agreement agreement = agreementRepository.findById(agreementId)
                .orElseThrow(() -> new RuntimeException("Agreement not found"));


        BatchRequest batch = calendarService.batch();


        List<Event> milestoneEvents = createMilestoneEvents(agreement);
        insertEventsIntoBatch(batch, milestoneEvents);


        List<Event> obligationEvents = createObligationEvents(agreement);
        insertEventsIntoBatch(batch, obligationEvents);


        Event agreementEvent = createAgreementContractEvent(agreement);
        if (agreementEvent != null) {
            insertEventsIntoBatch(batch, List.of(agreementEvent));
        }


        batch.execute();

        log.info("Synced events for Agreement ID {} to Google Calendar", agreementId);
    }


    private List<Event> createMilestoneEvents(Agreement agreement) {
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
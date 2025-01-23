package com.clearview.docusign.hackathon.CalendarIntegration.config;

import com.google.api.services.calendar.CalendarScopes;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class GoogleCalendarConfig {
    @Value("${google.credentials.path}")
    private String credentialsPath;

    @Bean
    public GoogleCredentials googleCredentials() throws IOException {
        try (InputStream credentialsStream = new FileInputStream(credentialsPath)) {
            return GoogleCredentials.fromStream(credentialsStream)
                    .createScoped(CalendarScopes.all());
        }
    }
}

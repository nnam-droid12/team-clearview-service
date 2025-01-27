package com.clearview.docusign.hackathon.CalendarIntegration.config;

import com.clearview.docusign.hackathon.CalendarIntegration.service.GoogleCalendarSyncService;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.AbstractDataStoreFactory;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.*;
import java.util.*;

@Configuration
public class GoogleCalendarConfig {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(GoogleCalendarSyncService.class);



    @Value("${AWS.S3_BUCKET_NAME}")
    private String s3BucketName;

    @Bean
    public AuthorizationCodeFlow authorizationCodeFlow(
            S3Client s3Client,
            GoogleClientSecrets clientSecrets
    ) throws Exception {
        return new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                clientSecrets,
                // Explicitly list full scope
                Arrays.asList("https://www.googleapis.com/auth/calendar",
                        "https://www.googleapis.com/auth/calendar.events")

        )
                .setAccessType("offline")
                .setApprovalPrompt("force")
                .setDataStoreFactory(new S3DataStoreFactory(s3Client, s3BucketName))
                .build();
    }
    private static class S3DataStoreFactory implements DataStoreFactory {
        private final S3Client s3Client;
        private final String s3BucketName;
        private final Map<String, S3DataStore<?>> dataStores = new HashMap<>();

        public S3DataStoreFactory(S3Client s3Client, String s3BucketName) {
            this.s3Client = s3Client;
            this.s3BucketName = s3BucketName;
        }

        @Override
        public <V extends Serializable> DataStore<V> getDataStore(String id) {
            return (DataStore<V>) dataStores.computeIfAbsent(id,
                    k -> new S3DataStore<>(this, s3Client, s3BucketName, k));
        }
    }

    private static class S3DataStore<V extends Serializable> implements DataStore<V> {
        private final DataStoreFactory dataStoreFactory;
        private final S3Client s3Client;
        private final String s3BucketName;
        private final String id;
        private final Map<String, V> inMemoryStore = new HashMap<>();

        public S3DataStore(DataStoreFactory factory, S3Client s3Client, String s3BucketName, String id) {
            this.dataStoreFactory = factory;
            this.s3Client = s3Client;
            this.s3BucketName = s3BucketName;
            this.id = id;
        }

        @Override
        public DataStoreFactory getDataStoreFactory() {
            return dataStoreFactory;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public V get(String key) {
            try {

                if (inMemoryStore.containsKey(key)) {
                    return inMemoryStore.get(key);
                }


                GetObjectRequest request = GetObjectRequest.builder()
                        .bucket(s3BucketName)
                        .key(id + "/" + key)
                        .build();

                byte[] data = s3Client.getObjectAsBytes(request).asByteArray();
                V value = deserialize(data);

                inMemoryStore.put(key, value);
                return value;
            } catch (Exception e) {
                log.error("Error retrieving from S3: {}", e.getMessage());
                return null;
            }
        }

        @Override
        public DataStore<V> set(String key, V value) {
            try {

                inMemoryStore.put(key, value);


                PutObjectRequest request = PutObjectRequest.builder()
                        .bucket(s3BucketName)
                        .key(id + "/" + key)
                        .build();

                s3Client.putObject(request, RequestBody.fromBytes(serialize(value)));
                return this;
            } catch (Exception e) {
                log.error("Error storing in S3: {}", e.getMessage());
                throw new RuntimeException("Failed to save to S3", e);
            }
        }

        @Override
        public int size() { return inMemoryStore.size(); }
        @Override
        public boolean isEmpty() { return inMemoryStore.isEmpty(); }
        @Override
        public boolean containsKey(String key) { return inMemoryStore.containsKey(key); }
        @Override
        public boolean containsValue(V value) { return inMemoryStore.containsValue(value); }
        @Override
        public Set<String> keySet() { return inMemoryStore.keySet(); }
        @Override
        public Collection<V> values() { return inMemoryStore.values(); }
        @Override
        public DataStore<V> clear() {
            inMemoryStore.clear();
            return this;
        }
        @Override
        public DataStore<V> delete(String key) {
            inMemoryStore.remove(key);
            return this;
        }

        private byte[] serialize(V value) throws IOException {
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                 ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(value);
                return baos.toByteArray();
            }
        }

        private V deserialize(byte[] data) throws IOException, ClassNotFoundException {
            try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
                 ObjectInputStream ois = new ObjectInputStream(bais)) {
                return (V) ois.readObject();
            }
        }
    }

    @Bean
    public GoogleClientSecrets googleClientSecrets() throws IOException {
        return GoogleClientSecrets.load(
                GsonFactory.getDefaultInstance(),
                new InputStreamReader(new ClassPathResource("credentials.json").getInputStream())
        );
    }

    @Bean
    public Calendar calendarService(
            AuthorizationCodeFlow authorizationCodeFlow,
            @Value("${google.calendar.user-id}") String userId
    ) throws Exception {
        Credential credential = authorizationCodeFlow.loadCredential(userId);

        if (credential == null || credential.getAccessToken() == null) {
            throw new IllegalStateException("No valid credentials found");
        }

        return new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                credential
        ).setApplicationName("Agreement Management System").build();
    }
}
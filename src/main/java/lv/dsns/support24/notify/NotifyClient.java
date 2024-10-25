package lv.dsns.support24.notify;

import com.fasterxml.jackson.databind.ObjectMapper;
import lv.dsns.support24.notify.dto.request.NotifyRequestDTO;
import lv.dsns.support24.notify.dto.response.NotifyResponseDTO;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class NotifyClient {

    @Value("${nabat.notify.voice}")
    private String VOICE;

    @Value("${nabat.notify.dtmfLen}")
    private String DTMFLEN;

    @Value("${nabat.url}")
    private String URL;

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public NotifyClient() {
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.HOURS)
                .readTimeout(1, TimeUnit.HOURS)
                .writeTimeout(1, TimeUnit.HOURS)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public NotifyResponseDTO notifyUsers(List<Object[]> usersToNotify, NotifyRequestDTO requestDTO) throws IOException {
        // Build JSON body
        NotifyRequestBody notifyRequestBody = buildNotifyRequest(usersToNotify, requestDTO.getMessage());

        // Convert Java object to JSON string
        String jsonRequest = objectMapper.writeValueAsString(notifyRequestBody);

        // Build HTTP request
        RequestBody body = RequestBody.create(
                jsonRequest, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(URL)
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build();

        // Execute the request
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            // Parse response body to Notify object
            String responseBody = response.body().string();
            return objectMapper.readValue(responseBody, NotifyResponseDTO.class);
        }
    }

    // Helper method to construct the NotifyRequest from users and message
    private NotifyRequestBody buildNotifyRequest(List<Object[]> usersToNotify, String message) {
        List<NotifyUser> notifyUsers = usersToNotify.stream()
                .map(user -> new NotifyUser(1414141415, (String) user[1]))
                .toList();

        return NotifyRequestBody.builder()
                .text(message)
                .voice(VOICE)
                .dtmfLen(DTMFLEN)
                .notifyUsers(notifyUsers)
                .build();
    }
}

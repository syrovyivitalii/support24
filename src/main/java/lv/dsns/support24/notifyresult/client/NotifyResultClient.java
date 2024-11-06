package lv.dsns.support24.notifyresult.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lv.dsns.support24.notifyresult.model.NotifyResult;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Component
public class NotifyResultClient {
    @Value("${nabat.result.url}")
    private String URL;

    private final ObjectMapper mapper;
    private final OkHttpClient client;

    public NotifyResultClient() {
        this.client = new OkHttpClient();
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
    }

    public NotifyResult getNotifyResult(UUID eventId) {
        HttpUrl urlWithParameters = Objects.requireNonNull(HttpUrl.parse(URL)).newBuilder()
                .addQueryParameter("event_id", String.valueOf(eventId))
                .build();

        // Create an empty POST request body
        RequestBody body = RequestBody.create("", MediaType.parse("application/json"));

        // Build the POST request
        Request request = new Request.Builder()
                .url(urlWithParameters)
                .post(body) // Use POST method with empty body
                .addHeader("accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                return mapper.readValue(responseBody, NotifyResult.class);
            } else {
                throw new RuntimeException("Response was not successful. Code: " + response.code() + ", Message: " + response.message());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error executing request: " + e.getMessage(), e);
        }
    }
}

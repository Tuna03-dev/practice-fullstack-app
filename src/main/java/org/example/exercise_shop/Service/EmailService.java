package org.example.exercise_shop.Service;

import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.dto.request.EmailRequest;
import org.example.exercise_shop.dto.response.EmailResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
public class EmailService {
    private final RestTemplate restTemplate =new RestTemplate();
    private final String apiKey = "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";
    private final String brevoApiUrl = "https://api.brevo.com/v3/smtp/email";


    public void sendEmail(EmailRequest emailRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json");
        headers.set("api-key", apiKey);
        headers.set("content-Type", "application/json");
        HttpEntity<EmailRequest> requestHttpEntity = new HttpEntity<>(emailRequest, headers);
        ResponseEntity<String> response = restTemplate.exchange(brevoApiUrl, HttpMethod.POST, requestHttpEntity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()){
            throw new RuntimeException("Failed to send email: " + response.getBody());
        }

    }
}

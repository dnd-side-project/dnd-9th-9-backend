package com.dnd.Exercise.global.slack;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Slf4j
public class SlackMessageSender {

    private final String SLACK_LOGGER_WEBHOOK_URI;
    public final ObjectMapper objectMapper;

    public SlackMessageSender(
            ObjectMapper objectMapper,
            @Value("${slack.webhook.uri}") String slack_webhook_uri) {
        this.objectMapper = objectMapper;
        SLACK_LOGGER_WEBHOOK_URI = slack_webhook_uri;
    }

    public void send(String message) {
        WebClient.create(SLACK_LOGGER_WEBHOOK_URI)
                .post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(toJson(message))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    private String toJson(String message) {
        try {
            Map<String, String> values = new HashMap<>();
            values.put("text", message);

            return objectMapper.writeValueAsString(values);
        } catch (JsonProcessingException ignored) {
        }
        return "{\"text\" : \"슬랙으로 보낼 데이터를 제이슨으로 변경하는데 에러가 발생함.\"}";
    }
}
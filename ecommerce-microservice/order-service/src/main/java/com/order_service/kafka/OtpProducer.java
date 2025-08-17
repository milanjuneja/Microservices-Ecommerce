package com.order_service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OtpProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendOtpEvent(OtpEmailEvent event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("otp-email-topic", json);
            System.out.println("OTP event sent to Kafka: " + json);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send OTP event", e);
        }
    }
}
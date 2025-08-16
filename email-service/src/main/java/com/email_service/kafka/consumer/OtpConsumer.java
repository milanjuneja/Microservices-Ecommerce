package com.email_service.kafka.consumer;

import com.email_service.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.user_service.kafka.dto.OtpEmailEvent;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

//@Service
//@AllArgsConstructor
//public class OtpConsumer {
//
//    private final EmailService emailService;
//    private final ObjectMapper objectMapper;
//
//    @KafkaListener(topics = "otp-email-topic", groupId = "email-group")
//    public void consume(String message) {
//        System.out.println("Received raw message: " + message);  // Add this line
//        try {
//            OtpEmailEvent event = objectMapper.readValue(message, OtpEmailEvent.class);
//            emailService.sendVerificationOtpEmail(
//                event.getEmail(),
//                event.getOtp(),
//                event.getSubject(),
//                event.getText()
//            );
//            System.out.println("OTP email sent: " + event.getEmail());
//        } catch (Exception e) {
//            System.err.println("Error processing message: " + message);
//            e.printStackTrace();
//
//        }
//    }
//    @PostConstruct
//    public void init() {
//        System.out.println("OtpConsumer initialized and ready to receive messages");
//    }
//}

@Service
@AllArgsConstructor
public class OtpConsumer {
    private final EmailService emailService;
    private final ObjectMapper objectMapper;  // ... existing fields ...

    @PostConstruct
    public void init() {
        System.out.println("✅ OtpConsumer initialized and ready for messages");
    }

    @KafkaListener(topics = "otp-email-topic", groupId = "email-group")
    public void consume(String message) {
        System.out.println("📩 Received raw message: " + message);
        try {
            OtpEmailEvent event = objectMapper.readValue(message, OtpEmailEvent.class);
            System.out.println("🔍 Parsed event: " + event);
            emailService.sendVerificationOtpEmail(
                    event.getEmail(),
                    event.getOtp(),
                    event.getSubject(),
                    event.getText()
            );
            System.out.println("📧 Email sent to: " + event.getEmail());
        } catch (Exception e) {
            System.err.println("❌ Error processing message: " + message);
            e.printStackTrace();
        }
    }
}

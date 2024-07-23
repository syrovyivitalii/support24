package lv.dsns.support24.common.smtp;

import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public interface EmailNotificationService {
    void send(String email, String subject, String text);
    void sendNotification(String to, String subject, Map<String, Object> properties, String templateName);
}

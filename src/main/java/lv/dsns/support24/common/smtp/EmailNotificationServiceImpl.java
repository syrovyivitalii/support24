package lv.dsns.support24.common.smtp;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@Slf4j
public class EmailNotificationServiceImpl implements EmailNotificationService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private FreeMarkerConfigurationFactoryBean freemarkerConfig;

    @Value("${spring.mail.username}")
    private String fromAddress;

    @Value("${spring.mail.solveProblemUrl}")
    private String baseUrl;


    @Async
    @Override
    public void send(String email, String subject, String text) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);
        mailMessage.setFrom(fromAddress);
        javaMailSender.send(mailMessage);
    }

    @Async
    @Override
    public void sendNotification(String to, String subject, Map<String, Object> properties, String templateName) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            Template template = freemarkerConfig.getObject().getTemplate(templateName);
            properties.put("baseUrl", baseUrl);
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, properties);

            helper.setFrom(fromAddress);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
            javaMailSender.send(message);
        } catch (IOException | MessagingException | TemplateException e) {
            log.error("Error sending email with template", e);
        }
    }
}

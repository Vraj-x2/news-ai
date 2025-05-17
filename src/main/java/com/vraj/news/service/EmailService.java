package com.vraj.news.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);

            // Convert plain text (Gemini output) to basic HTML
            String htmlContent = convertToHtml(body);

            helper.setText(htmlContent, true); // enable HTML

            mailSender.send(message);
            log.info("📧 HTML email sent to {}", to);
        } catch (MessagingException e) {
            log.error("❌ Failed to send email: {}", e.getMessage(), e);
        }
    }

    private String convertToHtml(String raw) {
        StringBuilder html = new StringBuilder();
        html.append("<html><body>");
        html.append("<h2>📰 Gemini-Picked News Summary</h2>");
        html.append("<ul>");

        String[] lines = raw.split("\\n");
        String currentTitle = null;

        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("*") || line.startsWith("-")) {
                // Detect markdown link: [Title](URL)
                int linkStart = line.indexOf('[');
                int linkEnd = line.indexOf(']');
                int urlStart = line.indexOf('(', linkEnd);
                int urlEnd = line.indexOf(')', urlStart);

                if (linkStart != -1 && linkEnd != -1 && urlStart != -1 && urlEnd != -1) {
                    String title = line.substring(linkStart + 1, linkEnd);
                    String url = line.substring(urlStart + 1, urlEnd);
                    html.append("<li><b><a href='").append(url).append("'>")
                        .append(title).append("</a></b></li>");
                    currentTitle = title;
                }
            } else if (!line.isEmpty()) {
                // Summary line
                html.append("<p style='margin-left:20px;'>").append(line).append("</p>");
            }
        }

        html.append("</ul></body></html>");
        return html.toString();
    }

}

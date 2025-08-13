package com.example.todolist.service.impl;

import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${FRONTEND-URL}")
    private String frontendUrl;

    @Value("${FROM-EMAIL}")
    private String fromEmail;

    @Value("${FROM-NAME}")
    private String fromName;

    @Value("${spring.mail.username}")
    private String gmailUsername;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    @PostConstruct
    public void debugConfig() {
        System.out.println("=== EMAIL SERVICE CONFIG DEBUG ===");
        System.out.println("Gmail Username: " + (gmailUsername != null && !gmailUsername.isEmpty() ?
                "SET (" + gmailUsername.substring(0, Math.min(5, gmailUsername.length())) + "...)" : "NOT SET"));
        System.out.println("Frontend URL: " + frontendUrl);
        System.out.println("From Email: " + fromEmail);
        System.out.println("From Name: " + fromName);
        System.out.println("================================");
    }

    public void sendPasswordResetEmail(String toEmail, String firstName, String resetToken) {
        try {
            System.out.println("=== SENDING EMAIL DEBUG ===");
            System.out.println("To: " + toEmail);
            System.out.println("From: " + fromEmail);
            System.out.println("Token: " + resetToken);

            String resetLink = frontendUrl + "/reset-password?token=" + resetToken;
            System.out.println("Reset Link: " + resetLink);

            String htmlContent = createPasswordResetEmailTemplate(firstName, resetLink);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, fromName);
            helper.setTo(toEmail);
            helper.setSubject("Reset Your Password - Todo App");
            helper.setText(htmlContent, true); // true indicates HTML content

            System.out.println("Sending email via Gmail SMTP...");
            javaMailSender.send(message);
            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            System.err.println("MessagingException in sendPasswordResetEmail: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error sending password reset email: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error in sendPasswordResetEmail: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error sending password reset email: " + e.getMessage());
        }
    }

    private String createPasswordResetEmailTemplate(String firstName, String resetLink) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <style>
                        .email-container {
                            font-family: Arial, sans-serif;
                            max-width: 600px;
                            margin: 0 auto;
                            padding: 20px;
                            border: 1px solid #ddd;
                            border-radius: 10px;
                        }
                        .header {
                            background-color: #007bff;
                            color: white;
                            padding: 20px;
                            text-align: center;
                            border-radius: 10px 10px 0 0;
                        }
                        .content {
                            padding: 30px 20px;
                        }
                        .reset-button {
                            display: inline-block;
                            background-color: #28a745;
                            color: white;
                            padding: 12px 30px;
                            text-decoration: none;
                            border-radius: 5px;
                            margin: 20px 0;
                            font-weight: bold;
                        }
                        .footer {
                            text-align: center;
                            color: #666;
                            font-size: 12px;
                            margin-top: 30px;
                        }
                    </style>
                </head>
                <body>
                    <div class="email-container">
                        <div class="header">
                            <h1>Password Reset Request</h1>
                        </div>
                        <div class="content">
                            <h2>Hello %s,</h2>
                            <p>You requested to reset your password for your Todo App account.</p>
                            <p>Click the button below to reset your password:</p>
                            <a href="%s" class="reset-button">Reset Password</a>
                            <p>Or copy and paste this link in your browser:</p>
                            <p style="word-break: break-all; color: #007bff;">%s</p>
                            <p><strong>This link will expire in 1 hour.</strong></p>
                            <p>If you didn't request this password reset, please ignore this email.</p>
                            <p>Best regards,<br>Todo App Team</p>
                        </div>
                        <div class="footer">
                            <p>This is an automated email. Please do not reply.</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(firstName, resetLink, resetLink);
    }
}

package dev.fernando.dscatalog.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import dev.fernando.dscatalog.dto.EmailDTO;
import dev.fernando.dscatalog.services.exceptions.EmailException;

@Service
public class EmailService {
    @Value("${spring.mail.username}")
    private String from;
    private final JavaMailSender javaMailSender;
    public EmailService(final JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
    public void send(EmailDTO email) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(email.to());
            message.setSubject(email.subject());
            message.setText(email.body());
            javaMailSender.send(message);
        } catch (MailException e) {
            throw new EmailException("Erro ao enviar o e-mail: " + e.getMessage());
        }
    }
}

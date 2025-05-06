package com.example.email_sending_task.service.impl;

import com.example.email_sending_task.dto.EmailDto;
import com.example.email_sending_task.models.EmailOutbox;
import com.example.email_sending_task.models.Status;
import com.example.email_sending_task.repository.EmailOutRepository;
import com.example.email_sending_task.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final EmailOutRepository emailOutRepository;


    @Override
    public void addEmail(EmailDto emailDto) {
        EmailOutbox emailOutbox = new EmailOutbox();
        emailOutbox.setTo_address(emailDto.getTo_address());
        emailOutbox.setSubject(emailDto.getSubject());
        emailOutbox.setBody(emailDto.getBody());
        emailOutbox.setStatus(Status.Pending);
        emailOutRepository.save(emailOutbox);
    }

    @Override
    public void sendEmail() {
        List<EmailOutbox> emailList = emailOutRepository
                .findByStatusIn(List.of(Status.Pending, Status.Retrying));
        for (EmailOutbox email : emailList) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(email.getTo_address());
                message.setSubject(email.getSubject());
                message.setText(email.getBody());

                mailSender.send(message);
                email.setStatus(Status.Success);
                email.setLast_updated_at(LocalDateTime.now());
            } catch (Exception e) {
                int retryCount = email.getRetryCount();
                if (retryCount < 5) {
                    email.setRetryCount(retryCount + 1);
                    email.setStatus(Status.Retrying);
                    email.setLast_updated_at(LocalDateTime.now());
                } else {
                    email.setStatus(Status.Failed);
                    email.setLast_updated_at(LocalDateTime.now());
                }
            }
            emailOutRepository.save(email);
        }
    }

    @Scheduled(fixedRate = 60000)
    public void scheduleEmailSend() {
        sendEmail();
    }
}

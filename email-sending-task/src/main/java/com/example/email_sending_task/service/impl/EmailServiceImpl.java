package com.example.email_sending_task.service.impl;

import com.example.email_sending_task.dto.EmailDto;
import com.example.email_sending_task.models.EmailOutbox;
import com.example.email_sending_task.models.Status;
import com.example.email_sending_task.repository.EmailOutRepository;
import com.example.email_sending_task.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    private final JavaMailSender mailSender;
    private final EmailOutRepository emailOutRepository;


    @Override
    public void addEmail(EmailDto emailDto) {
        logger.info("Adding new email request: {}", emailDto.getTo_address());
        EmailOutbox emailOutbox = new EmailOutbox();
        emailOutbox.setTo_address(emailDto.getTo_address());
        emailOutbox.setSubject(emailDto.getSubject());
        emailOutbox.setBody(emailDto.getBody());
        emailOutbox.setStatus(Status.Pending);
        emailOutRepository.save(emailOutbox);
        logger.info("Email request added with ID: {}", emailOutbox.getId());
    }

    @Scheduled(fixedRate = 60000)
    public void scheduleEmailSend() {
        logger.info("Fetching email requests for status");
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
                logger.info("Email sent succesfully to: {}", email.getTo_address());
            } catch (Exception e) {
                int retryCount = email.getRetryCount();
                if (retryCount < 5) {
                    email.setRetryCount(retryCount + 1);
                    email.setStatus(Status.Retrying);
                    logger.info("Email request trying to sent {} retryCount is: {}",email.getTo_address() ,email.getRetryCount());
                    email.setLast_updated_at(LocalDateTime.now());
                } else {
                    email.setStatus(Status.Failed);
                    email.setLast_updated_at(LocalDateTime.now());
                    logger.info("Email can't sent to: {}", email.getTo_address());
                }
            }
            emailOutRepository.save(email);
        }
    }
}

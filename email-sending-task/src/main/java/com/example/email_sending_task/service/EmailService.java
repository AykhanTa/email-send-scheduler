package com.example.email_sending_task.service;

import com.example.email_sending_task.dto.EmailDto;

public interface EmailService {
    void addEmail(EmailDto emailDto);
    void sendEmail();
}

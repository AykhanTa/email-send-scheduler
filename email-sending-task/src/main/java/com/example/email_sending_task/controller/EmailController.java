package com.example.email_sending_task.controller;

import com.example.email_sending_task.dto.EmailDto;
import com.example.email_sending_task.models.EmailOutbox;
import com.example.email_sending_task.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/add")
    public void addEmail(@RequestBody EmailDto emailDto) {
        emailService.addEmail(emailDto);
    }
}

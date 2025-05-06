package com.example.email_sending_task.dto;

import lombok.Data;

@Data
public class EmailDto {
    private String to_address;
    private String subject;
    private String body;
}

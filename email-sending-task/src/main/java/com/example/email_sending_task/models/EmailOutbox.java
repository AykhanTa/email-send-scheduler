package com.example.email_sending_task.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "email_outbox")
public class EmailOutbox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String to_address;
    private String subject;
    private String body;
    private Status status;
    private int retryCount=0;
    private LocalDateTime created_at;
    private LocalDateTime last_updated_at;

    public EmailOutbox() {
        created_at = LocalDateTime.now();
    }

}

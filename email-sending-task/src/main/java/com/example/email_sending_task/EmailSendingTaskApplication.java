package com.example.email_sending_task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EmailSendingTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailSendingTaskApplication.class, args);
	}

}

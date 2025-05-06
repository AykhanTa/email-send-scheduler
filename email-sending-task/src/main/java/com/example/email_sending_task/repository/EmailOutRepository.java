package com.example.email_sending_task.repository;

import com.example.email_sending_task.models.EmailOutbox;
import com.example.email_sending_task.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailOutRepository extends JpaRepository<EmailOutbox,Integer> {
    List<EmailOutbox> findByStatusIn(List<Status> statusList);
}

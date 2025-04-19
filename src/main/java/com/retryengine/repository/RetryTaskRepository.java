package com.retryengine.repository;

import com.retryengine.model.RetryTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RetryTaskRepository extends JpaRepository<RetryTask, Long> {

    List<RetryTask> findByNextRetryTimeBeforeAndStatus(LocalDateTime time, String status);
    List<RetryTask> findByReplayedIsTrue();
}

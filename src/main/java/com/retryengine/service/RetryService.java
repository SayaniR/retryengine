package com.retryengine.service;

import com.retryengine.model.RetryTask;
import com.retryengine.repository.RetryTaskRepository;
import com.retryengine.retry.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class RetryService {
    @Autowired
    private RetryTaskRepository retryTaskRepository;


    public void executeRetry(Long taskId) {
        RetryTask task = retryTaskRepository.findById(taskId).orElseThrow();
        RetryStrategy strategy = resolveStrategy(task.getStrategy());

        try {
           
            if (Math.random() < 0.7) {
                throw new RuntimeException("Simulated failure");
            }
            task.setStatus("SUCCESS");
            task.setErrorMessage(null);
        } catch (Exception e) {
            if (strategy.shouldRetry(task.getAttempts(), e)) {
                task.setAttempts(task.getAttempts() + 1);
                task.setNextRetryTime(LocalDateTime.now().plusSeconds(strategy.getNextDelay(task.getAttempts())));
                task.setErrorMessage(e.getMessage());
            } else {
                task.setStatus("FAILED");
                task.setErrorMessage(e.getMessage());
            }
        }
        retryTaskRepository.save(task);
    }

    
    @Scheduled(fixedRate = 5000)
    public void pollAndRetry() {
        List<RetryTask> tasks = retryTaskRepository.findByNextRetryTimeBeforeAndStatus(LocalDateTime.now(), "PENDING");
        tasks.forEach(task -> executeRetry(task.getId()));
    }

    public List<RetryTask> getReplayedTasks() {
        return retryTaskRepository.findByReplayedIsTrue();
    }

    
    public void replayTask(Long taskId) {
        RetryTask task = retryTaskRepository.findById(taskId).orElseThrow();
        task.setAttempts(0);
        task.setStatus("PENDING");
        task.setNextRetryTime(LocalDateTime.now());
        task.setReplayed(true);
        task.setErrorMessage(null);
        retryTaskRepository.save(task);
    }

    private RetryStrategy resolveStrategy(String strategyName) {
        return switch (strategyName) {
            case "FIXED" -> new FixedIntervalStrategy(3000);
            case "EXPONENTIAL" -> new ExponentialBackoffStrategy(2000, 5);
            case "JITTER" -> new JitterStrategy(2000, 1000, 5);
            case "CIRCUIT" -> new CircuitBreakerStrategy(3, 10000);
            default -> throw new IllegalArgumentException("Unknown strategy: " + strategyName);
        };
    }

    public List<RetryTask> getPendingTasks() {
        return retryTaskRepository.findByNextRetryTimeBeforeAndStatus(LocalDateTime.now(), "PENDING");
    }
}

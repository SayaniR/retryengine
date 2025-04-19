package com.retryengine.controller;

import com.retryengine.model.RetryTask;
import com.retryengine.service.RetryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/retries")
public class RetryController {

    @Autowired
    private RetryService retryService;


    @PostMapping("/execute/{taskId}")
    public ResponseEntity<String> executeRetry(@PathVariable Long taskId) {
        retryService.executeRetry(taskId);
        return ResponseEntity.ok("Retry executed successfully");
    }


    @GetMapping("/replayed")
    public ResponseEntity<List<RetryTask>> getReplayedTasks() {
        List<RetryTask> tasks = retryService.getReplayedTasks();
        return ResponseEntity.ok(tasks);
    }


    @PostMapping("/replay/{taskId}")
    public ResponseEntity<String> replayTask(@PathVariable Long taskId) {
        retryService.replayTask(taskId);
        return ResponseEntity.ok("Task replayed successfully");
    }

 
    @GetMapping("/pending")
    public ResponseEntity<List<RetryTask>> getPendingTasks() {
        List<RetryTask> tasks = retryService.getPendingTasks();
        return ResponseEntity.ok(tasks);
    }
}

package com.example.smarttask.controller;

import com.example.smarttask.dto.TaskDto;
import com.example.smarttask.entity.User;
import com.example.smarttask.security.CustomUserDetails;
import com.example.smarttask.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // GET /tasks
    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        List<TaskDto> tasks = taskService.getAllTasks(userDetails.getUser());
        return ResponseEntity.ok(tasks);
    }

    // GET /tasks/my-tasks
    @GetMapping("/my-tasks")
    public ResponseEntity<List<TaskDto>> getMyTasks(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        List<TaskDto> tasks = taskService.getMyTasks(userDetails.getUser());
        return ResponseEntity.ok(tasks);
    }

    // POST /tasks
    @PostMapping
    public ResponseEntity<TaskDto> createTask(
            @Valid @RequestBody TaskDto taskDto,
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        TaskDto createdTask = taskService.createTask(taskDto, userDetails.getUser());
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    // PUT /tasks/{id}
    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskDto taskDto,
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        TaskDto updatedTask = taskService.updateTask(id, taskDto, userDetails.getUser());
        return ResponseEntity.ok(updatedTask);
    }

    // DELETE /tasks/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id,
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        taskService.deleteTask(id, userDetails.getUser());
        return ResponseEntity.noContent().build();
    }

    // POST /tasks/{taskId}/assign/{userId}
    @PostMapping("/{taskId}/assign/{userId}")
    public ResponseEntity<TaskDto> assignTask(
            @PathVariable Long taskId,
            @PathVariable Long userId,
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        TaskDto updatedTask = taskService.assignTask(taskId, userId, userDetails.getUser());
        return ResponseEntity.ok(updatedTask);
    }

    // POST /tasks/{taskId}/unassign
    @PostMapping("/{taskId}/unassign")
    public ResponseEntity<TaskDto> unassignTask(
            @PathVariable Long taskId,
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        TaskDto updatedTask = taskService.assignTask(taskId, null, userDetails.getUser());
        return ResponseEntity.ok(updatedTask);
    }
}

package com.example.smarttask.dto;

import com.example.smarttask.entity.TaskStatus;
import com.example.smarttask.entity.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TaskDto {

    private Long id;

    @NotBlank(message = "Task title is required")
    private String title;

    @NotBlank(message = "Task description is required")
    private String description;

    @NotNull(message = "Task status is required")
    private TaskStatus status;

    @NotNull(message = "Project ID is required")
    private Long projectId;

    @NotNull(message = "Deadline is required")
    @FutureOrPresent(message = "Deadline cannot be in the past")
    private LocalDate deadline;

    private Long assignedToId;
    private String assignedToName;
    private Long createdById;
    private String createdByName;
    private LocalDateTime createdAt;
    private TaskPriority priority;

    public TaskDto() {
    }

    public TaskDto(Long id, String title, TaskStatus status, Long projectId) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.projectId = projectId;
    }

    public TaskDto(Long id, String title, String description, TaskStatus status, Long projectId, Long assignedToId, String assignedToName, Long createdById, String createdByName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.projectId = projectId;
        this.assignedToId = assignedToId;
        this.assignedToName = assignedToName;
        this.createdById = createdById;
        this.createdByName = createdByName;
    }

    public TaskDto(Long id, String title, String description, TaskStatus status, Long projectId, Long assignedToId, String assignedToName, Long createdById, String createdByName, LocalDate deadline, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.projectId = projectId;
        this.assignedToId = assignedToId;
        this.assignedToName = assignedToName;
        this.createdById = createdById;
        this.createdByName = createdByName;
        this.deadline = deadline;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getAssignedToId() {
        return assignedToId;
    }

    public void setAssignedToId(Long assignedToId) {
        this.assignedToId = assignedToId;
    }

    public String getAssignedToName() {
        return assignedToName;
    }

    public void setAssignedToName(String assignedToName) {
        this.assignedToName = assignedToName;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public java.time.LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(java.time.LocalDate deadline) {
        this.deadline = deadline;
    }

    public java.time.LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.time.LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }
}

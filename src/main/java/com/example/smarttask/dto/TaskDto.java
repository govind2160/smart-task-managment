package com.example.smarttask.dto;

import com.example.smarttask.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TaskDto {

    private Long id;

    @NotBlank(message = "Task title is required")
    private String title;

    @NotNull(message = "Task status is required")
    private TaskStatus status;

    @NotNull(message = "Project ID is required")
    private Long projectId;

    private Long assignedToId;
    private String assignedToName;
    private Long createdById;
    private String createdByName;
    private java.time.LocalDate deadline;
    private java.time.LocalDateTime createdAt;

    public TaskDto() {
    }

    public TaskDto(Long id, String title, TaskStatus status, Long projectId) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.projectId = projectId;
    }

    public TaskDto(Long id, String title, TaskStatus status, Long projectId, Long assignedToId, String assignedToName, Long createdById, String createdByName) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.projectId = projectId;
        this.assignedToId = assignedToId;
        this.assignedToName = assignedToName;
        this.createdById = createdById;
        this.createdByName = createdByName;
    }

    public TaskDto(Long id, String title, TaskStatus status, Long projectId, Long assignedToId, String assignedToName, Long createdById, String createdByName, java.time.LocalDate deadline, java.time.LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
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
}

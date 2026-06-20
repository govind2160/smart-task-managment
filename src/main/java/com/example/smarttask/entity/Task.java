package com.example.smarttask.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT DEFAULT ''")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status = TaskStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to_id")
    private User assignedTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id", nullable = false)
    private User createdBy;

    @Column(name = "deadline")
    private java.time.LocalDate deadline;

    @Column(name = "created_at", nullable = false, updatable = false)
    private java.time.LocalDateTime createdAt = java.time.LocalDateTime.now();

    public Task() {
    }

    public Task(String title, String description, TaskStatus status, Project project) {
        this.title = title;
        this.description = description;
        this.status = (status != null) ? status : TaskStatus.PENDING;
        this.project = project;
    }

    public Task(String title, String description, TaskStatus status, Project project, User createdBy, User assignedTo) {
        this.title = title;
        this.description = description;
        this.status = (status != null) ? status : TaskStatus.PENDING;
        this.project = project;
        this.createdBy = createdBy;
        this.assignedTo = assignedTo;
    }

    // Getters and Setters
    public TaskPriority getPriority() {
        if (this.deadline == null) {
            return TaskPriority.LOW;
        }
        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(java.time.LocalDate.now(), this.deadline);
        if (daysBetween <= 15) {
            return TaskPriority.HIGH;
        } else if (daysBetween <= 30) {
            return TaskPriority.MEDIUM;
        } else {
            return TaskPriority.LOW;
        }
    }

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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
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

package com.example.smarttask.dto;

import com.example.smarttask.entity.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;
import java.time.LocalDate;

public class ProjectDto {

    private Long id;

    @NotBlank(message = "Project name is required")
    private String name;

    @NotBlank(message = "Project description is required")
    private String description;

    @NotNull(message = "Project status is required")
    private ProjectStatus status;

    @NotNull(message = "Deadline is required")
    @FutureOrPresent(message = "Deadline cannot be in the past")
    private LocalDate deadline;

    private Long ownerId;
    private String ownerName;
    private int memberCount;

    public ProjectDto() {
    }

    public ProjectDto(Long id, String name, String description, ProjectStatus status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public ProjectDto(Long id, String name, String description, ProjectStatus status, Long ownerId, String ownerName, int memberCount, LocalDate deadline) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.memberCount = memberCount;
        this.deadline = deadline;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public java.time.LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(java.time.LocalDate deadline) {
        this.deadline = deadline;
    }
}

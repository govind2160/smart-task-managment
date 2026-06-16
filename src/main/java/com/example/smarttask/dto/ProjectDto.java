package com.example.smarttask.dto;

import com.example.smarttask.entity.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProjectDto {

    private Long id;

    @NotBlank(message = "Project name is required")
    private String name;

    private String description;

    @NotNull(message = "Project status is required")
    private ProjectStatus status;

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

    public ProjectDto(Long id, String name, String description, ProjectStatus status, Long ownerId, String ownerName, int memberCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.memberCount = memberCount;
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
}

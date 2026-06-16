package com.example.smarttask.dto;

public class DashboardStatsDto {

    private long ownedProjectsCount;
    private long memberProjectsCount;
    private long assignedTasksCount;
    private long completedTasksCount;
    private long pendingTasksCount;

    public DashboardStatsDto() {
    }

    public DashboardStatsDto(long ownedProjectsCount, long memberProjectsCount, long assignedTasksCount, long completedTasksCount, long pendingTasksCount) {
        this.ownedProjectsCount = ownedProjectsCount;
        this.memberProjectsCount = memberProjectsCount;
        this.assignedTasksCount = assignedTasksCount;
        this.completedTasksCount = completedTasksCount;
        this.pendingTasksCount = pendingTasksCount;
    }

    // Getters and Setters
    public long getOwnedProjectsCount() {
        return ownedProjectsCount;
    }

    public void setOwnedProjectsCount(long ownedProjectsCount) {
        this.ownedProjectsCount = ownedProjectsCount;
    }

    public long getMemberProjectsCount() {
        return memberProjectsCount;
    }

    public void setMemberProjectsCount(long memberProjectsCount) {
        this.memberProjectsCount = memberProjectsCount;
    }

    public long getAssignedTasksCount() {
        return assignedTasksCount;
    }

    public void setAssignedTasksCount(long assignedTasksCount) {
        this.assignedTasksCount = assignedTasksCount;
    }

    public long getCompletedTasksCount() {
        return completedTasksCount;
    }

    public void setCompletedTasksCount(long completedTasksCount) {
        this.completedTasksCount = completedTasksCount;
    }

    public long getPendingTasksCount() {
        return pendingTasksCount;
    }

    public void setPendingTasksCount(long pendingTasksCount) {
        this.pendingTasksCount = pendingTasksCount;
    }
}

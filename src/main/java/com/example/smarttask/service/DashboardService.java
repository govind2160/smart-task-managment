package com.example.smarttask.service;

import com.example.smarttask.dto.DashboardStatsDto;
import com.example.smarttask.entity.TaskStatus;
import com.example.smarttask.entity.User;
import com.example.smarttask.repository.ProjectRepository;
import com.example.smarttask.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public DashboardService(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    public DashboardStatsDto getDashboardStats(User user) {
        long ownedProjectsCount = projectRepository.countByOwnerId(user.getId());
        long memberProjectsCount = projectRepository.countByMembersIdAndOwnerIdNot(user.getId());
        long assignedTasksCount = taskRepository.countByAssignedToId(user.getId());
        long completedTasksCount = taskRepository.countByStatusAndAssignedToId(TaskStatus.COMPLETED, user.getId());
        
        // Pending tasks include both PENDING and IN_PROGRESS statuses (everything not COMPLETED) assigned to the user
        long pendingTasksCount = taskRepository.countByStatusAndAssignedToId(TaskStatus.PENDING, user.getId()) 
                               + taskRepository.countByStatusAndAssignedToId(TaskStatus.IN_PROGRESS, user.getId());

        return new DashboardStatsDto(ownedProjectsCount, memberProjectsCount, assignedTasksCount, completedTasksCount, pendingTasksCount);
    }
}

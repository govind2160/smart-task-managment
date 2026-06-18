package com.example.smarttask.service;

import com.example.smarttask.dto.TaskDto;
import com.example.smarttask.entity.Project;
import com.example.smarttask.entity.Role;
import com.example.smarttask.entity.Task;
import com.example.smarttask.entity.User;
import com.example.smarttask.exception.ResourceNotFoundException;
import com.example.smarttask.repository.ProjectRepository;
import com.example.smarttask.repository.TaskRepository;
import com.example.smarttask.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public TaskDto createTask(TaskDto taskDto, User creator) {
        Project project = projectRepository.findById(taskDto.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + taskDto.getProjectId()));

        // Check if creator is a member of the project
        boolean isMember = project.getMembers().stream()
                .anyMatch(m -> m.getId().equals(creator.getId()));
        if (creator.getRole() != Role.ROLE_ADMIN && !isMember) {
            throw new IllegalArgumentException("Access denied: You are not a member of this project");
        }

        User assignee = null;
        if (taskDto.getAssignedToId() != null) {
            assignee = userRepository.findById(taskDto.getAssignedToId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assignee user not found"));
            
            // Check if assignee is a member of the project
            boolean isAssigneeMember = project.getMembers().stream()
                    .anyMatch(m -> m.getId().equals(taskDto.getAssignedToId()));
            if (!isAssigneeMember) {
                throw new IllegalArgumentException("Assignee must be a member of the project");
            }
        }

        Task task = new Task(taskDto.getTitle(), taskDto.getStatus(), project, creator, assignee);
        task.setDeadline(taskDto.getDeadline());
        Task savedTask = taskRepository.save(task);
        return convertToDto(savedTask);
    }

    @Transactional(readOnly = true)
    public List<TaskDto> getAllTasks(User user) {
        List<Task> tasks;
        if (user.getRole() == Role.ROLE_ADMIN) {
            tasks = taskRepository.findAll();
        } else {
            tasks = taskRepository.findByProjectMembersId(user.getId());
        }
        return tasks.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TaskDto getTaskById(Long id, User user) {
        Task task;
        if (user.getRole() == Role.ROLE_ADMIN) {
            task = taskRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        } else {
            task = taskRepository.findByIdAndProjectMembersId(id, user.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id + " or access denied"));
        }
        return convertToDto(task);
    }

    public TaskDto updateTask(Long id, TaskDto taskDto, User user) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        Project project = task.getProject();
        boolean isAdmin = user.getRole() == Role.ROLE_ADMIN;
        boolean isMember = isAdmin || project.getMembers().stream()
                .anyMatch(m -> m.getId().equals(user.getId()));
        if (!isMember) {
            throw new IllegalArgumentException("Access denied: You are not a member of this project");
        }

        boolean isOwner = isAdmin || project.getOwner().getId().equals(user.getId());
        boolean isAssignee = task.getAssignedTo() != null && task.getAssignedTo().getId().equals(user.getId());
        boolean isCreator = task.getCreatedBy() != null && task.getCreatedBy().getId().equals(user.getId());

        if (!isOwner) {
            // Non-owner member can only update the status if they are assignee or creator
            if (!(isAssignee || isCreator)) {
                throw new IllegalArgumentException("Access denied: You can only update tasks assigned to or created by you");
            }
            task.setStatus(taskDto.getStatus());
        } else {
            // Owner can update everything
            task.setTitle(taskDto.getTitle());
            task.setStatus(taskDto.getStatus());
            task.setDeadline(taskDto.getDeadline());

            if (taskDto.getProjectId() != null && !taskDto.getProjectId().equals(project.getId())) {
                Project newProject = projectRepository.findById(taskDto.getProjectId())
                        .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + taskDto.getProjectId()));
                // Verify owner is also owner/member of the new project
                boolean isNewMember = isAdmin || newProject.getMembers().stream()
                        .anyMatch(m -> m.getId().equals(user.getId()));
                if (!isNewMember) {
                    throw new IllegalArgumentException("Cannot move task to a project you don't belong to");
                }
                task.setProject(newProject);
                project = newProject;
            }

            if (taskDto.getAssignedToId() != null) {
                User newAssignee = userRepository.findById(taskDto.getAssignedToId())
                        .orElseThrow(() -> new ResourceNotFoundException("Assignee user not found"));
                boolean isAssigneeMember = project.getMembers().stream()
                        .anyMatch(m -> m.getId().equals(taskDto.getAssignedToId()));
                if (!isAssigneeMember) {
                    throw new IllegalArgumentException("Assignee must be a member of the project");
                }
                task.setAssignedTo(newAssignee);
            } else {
                task.setAssignedTo(null);
            }
        }

        Task updatedTask = taskRepository.save(task);
        return convertToDto(updatedTask);
    }

    public void deleteTask(Long id, User user) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        Project project = task.getProject();
        if (user.getRole() != Role.ROLE_ADMIN && !project.getOwner().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Only the project owner can delete tasks");
        }

        taskRepository.delete(task);
    }

    public TaskDto assignTask(Long taskId, Long assigneeId, User user) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        Project project = task.getProject();
        boolean isMember = user.getRole() == Role.ROLE_ADMIN || project.getMembers().stream()
                .anyMatch(m -> m.getId().equals(user.getId()));
        if (!isMember) {
            throw new IllegalArgumentException("Access denied: You are not a member of this project");
        }

        User assignee = null;
        if (assigneeId != null) {
            assignee = userRepository.findById(assigneeId)
                    .orElseThrow(() -> new ResourceNotFoundException("Assignee user not found"));
            boolean isAssigneeMember = project.getMembers().stream()
                    .anyMatch(m -> m.getId().equals(assigneeId));
            if (!isAssigneeMember) {
                throw new IllegalArgumentException("Assignee must be a member of the project");
            }
        }

        task.setAssignedTo(assignee);
        Task updatedTask = taskRepository.save(task);
        return convertToDto(updatedTask);
    }

    @Transactional(readOnly = true)
    public List<TaskDto> getMyTasks(User user) {
        return taskRepository.findByAssignedToId(user.getId())
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Mapper helper
    private TaskDto convertToDto(Task task) {
        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.getStatus(),
                task.getProject().getId(),
                task.getAssignedTo() != null ? task.getAssignedTo().getId() : null,
                task.getAssignedTo() != null ? task.getAssignedTo().getName() : null,
                task.getCreatedBy() != null ? task.getCreatedBy().getId() : null,
                task.getCreatedBy() != null ? task.getCreatedBy().getName() : null,
                task.getDeadline(),
                task.getCreatedAt()
        );
    }
}

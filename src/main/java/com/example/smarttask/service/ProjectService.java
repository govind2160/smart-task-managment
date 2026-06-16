package com.example.smarttask.service;

import com.example.smarttask.dto.ProjectDto;
import com.example.smarttask.dto.UserDto;
import com.example.smarttask.entity.Project;
import com.example.smarttask.entity.User;
import com.example.smarttask.exception.ResourceNotFoundException;
import com.example.smarttask.repository.ProjectRepository;
import com.example.smarttask.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public ProjectDto createProject(ProjectDto projectDto, User owner) {
        Project project = new Project(
                projectDto.getName(), 
                projectDto.getDescription(), 
                projectDto.getStatus(), 
                owner
        );
        Project savedProject = projectRepository.save(project);
        return convertToDto(savedProject);
    }

    @Transactional(readOnly = true)
    public List<ProjectDto> getAllProjects(User user) {
        return projectRepository.findByMembersId(user.getId())
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ProjectDto updateProject(Long id, ProjectDto projectDto, User owner) {
        Project project = projectRepository.findByIdAndOwnerId(id, owner.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id + " for current user"));

        project.setName(projectDto.getName());
        project.setDescription(projectDto.getDescription());
        if (projectDto.getStatus() != null) {
            project.setStatus(projectDto.getStatus());
        }

        Project updatedProject = projectRepository.save(project);
        return convertToDto(updatedProject);
    }

    public void deleteProject(Long id, User owner) {
        Project project = projectRepository.findByIdAndOwnerId(id, owner.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id + " for current user"));
        projectRepository.delete(project);
    }

    public List<UserDto> getMembers(Long projectId, User user) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        boolean isMember = project.getMembers().stream()
                .anyMatch(m -> m.getId().equals(user.getId()));
        if (!isMember) {
            throw new IllegalArgumentException("Access denied: You are not a member of this project");
        }

        return project.getMembers().stream()
                .map(m -> new UserDto(m.getId(), m.getName(), m.getEmail()))
                .collect(Collectors.toList());
    }

    public List<UserDto> addMember(Long projectId, Long userId, User owner) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        if (!project.getOwner().getId().equals(owner.getId())) {
            throw new IllegalArgumentException("Only the project owner can manage members");
        }

        User userToAdd = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        project.getMembers().add(userToAdd);
        projectRepository.save(project);

        return project.getMembers().stream()
                .map(m -> new UserDto(m.getId(), m.getName(), m.getEmail()))
                .collect(Collectors.toList());
    }

    public List<UserDto> removeMember(Long projectId, Long userId, User owner) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        if (!project.getOwner().getId().equals(owner.getId())) {
            throw new IllegalArgumentException("Only the project owner can manage members");
        }

        if (project.getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("Cannot remove the project owner from the project");
        }

        project.getMembers().removeIf(m -> m.getId().equals(userId));
        projectRepository.save(project);

        return project.getMembers().stream()
                .map(m -> new UserDto(m.getId(), m.getName(), m.getEmail()))
                .collect(Collectors.toList());
    }

    // Mapper helper
    private ProjectDto convertToDto(Project project) {
        return new ProjectDto(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getStatus(),
                project.getOwner() != null ? project.getOwner().getId() : null,
                project.getOwner() != null ? project.getOwner().getName() : null,
                project.getMembers() != null ? project.getMembers().size() : 0
        );
    }
}

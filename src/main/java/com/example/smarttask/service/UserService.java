package com.example.smarttask.service;

import com.example.smarttask.dto.UserDto;
import com.example.smarttask.entity.Project;
import com.example.smarttask.entity.Task;
import com.example.smarttask.entity.User;
import com.example.smarttask.exception.ResourceNotFoundException;
import com.example.smarttask.repository.ProjectRepository;
import com.example.smarttask.repository.TaskRepository;
import com.example.smarttask.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            ProjectRepository projectRepository,
            TaskRepository taskRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto createUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email is already in use: " + userDto.getEmail());
        }

        User user = new User(
                userDto.getName(),
                userDto.getEmail(),
                passwordEncoder.encode(userDto.getPassword()),
                null
        );
        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return convertToDto(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // 1. Unassign tasks assigned to this user
        List<Task> assignedTasks = taskRepository.findByAssignedToId(id);
        for (Task task : assignedTasks) {
            task.setAssignedTo(null);
            taskRepository.save(task);
        }

        // 2. Delete tasks created by this user
        List<Task> createdTasks = taskRepository.findByCreatedById(id);
        for (Task task : createdTasks) {
            taskRepository.delete(task);
        }

        // 3. Remove user from all projects where they are a member
        List<Project> memberProjects = projectRepository.findByMembersId(id);
        for (Project project : memberProjects) {
            project.getMembers().remove(user);
            projectRepository.save(project);
        }

        // 4. Delete projects owned by this user
        List<Project> ownedProjects = projectRepository.findByOwnerId(id);
        for (Project project : ownedProjects) {
            projectRepository.delete(project);
        }

        // 5. Delete the user
        userRepository.delete(user);
    }

    // Mapper helper
    private UserDto convertToDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getRole().name());
    }
}

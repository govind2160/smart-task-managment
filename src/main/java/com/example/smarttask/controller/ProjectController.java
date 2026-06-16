package com.example.smarttask.controller;

import com.example.smarttask.dto.ProjectDto;
import com.example.smarttask.dto.UserDto;
import com.example.smarttask.entity.User;
import com.example.smarttask.security.CustomUserDetails;
import com.example.smarttask.service.ProjectService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/projects")
public class ProjectController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // GET /projects
    @GetMapping
    public ResponseEntity<List<ProjectDto>> getAllProjects(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        List<ProjectDto> projects = projectService.getAllProjects(userDetails.getUser());
        return ResponseEntity.ok(projects);
    }

    // POST /projects
    @PostMapping
    public ResponseEntity<ProjectDto> createProject(
            @Valid @RequestBody ProjectDto projectDto, 
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        ProjectDto createdProject = projectService.createProject(projectDto, userDetails.getUser());
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    // PUT /projects/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ProjectDto> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectDto projectDto,
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        ProjectDto updatedProject = projectService.updateProject(id, projectDto, userDetails.getUser());
        return ResponseEntity.ok(updatedProject);
    }

    // DELETE /projects/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(
            @PathVariable Long id,
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        projectService.deleteProject(id, userDetails.getUser());
        return ResponseEntity.noContent().build();
    }

    // GET /projects/{projectId}/members
    @GetMapping("/{projectId}/members")
    public ResponseEntity<List<UserDto>> getMembers(
            @PathVariable Long projectId,
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        logger.info("GET /projects/{}/members called by user: {}", projectId, userDetails.getUsername());
        List<UserDto> members = projectService.getMembers(projectId, userDetails.getUser());
        return ResponseEntity.ok(members);
    }

    // POST /projects/{projectId}/members/{userId}
    @PostMapping("/{projectId}/members/{userId}")
    public ResponseEntity<List<UserDto>> addMember(
            @PathVariable Long projectId,
            @PathVariable Long userId,
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        logger.info("POST /projects/{}/members/{} called by user: {}", projectId, userId, userDetails.getUsername());
        List<UserDto> members = projectService.addMember(projectId, userId, userDetails.getUser());
        return ResponseEntity.ok(members);
    }

    // DELETE /projects/{projectId}/members/{userId}
    @DeleteMapping("/{projectId}/members/{userId}")
    public ResponseEntity<List<UserDto>> removeMember(
            @PathVariable Long projectId,
            @PathVariable Long userId,
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        logger.info("DELETE /projects/{}/members/{} called by user: {}", projectId, userId, userDetails.getUsername());
        List<UserDto> members = projectService.removeMember(projectId, userId, userDetails.getUser());
        return ResponseEntity.ok(members);
    }
}

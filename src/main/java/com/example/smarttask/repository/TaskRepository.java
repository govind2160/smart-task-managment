package com.example.smarttask.repository;

import com.example.smarttask.entity.Task;
import com.example.smarttask.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProjectId(Long projectId);
    long countByStatus(TaskStatus status);

    @Query("SELECT t FROM Task t JOIN t.project.members m WHERE m.id = :userId")
    List<Task> findByProjectMembersId(@Param("userId") Long userId);

    @Query("SELECT DISTINCT t FROM Task t LEFT JOIN t.project.members m WHERE t.project.owner.id = :userId OR m.id = :userId")
    List<Task> findByProjectOwnerIdOrMembersId(@Param("userId") Long userId);

    @Query("SELECT t FROM Task t JOIN t.project.members m WHERE t.id = :taskId AND m.id = :userId")
    Optional<Task> findByIdAndProjectMembersId(@Param("taskId") Long taskId, @Param("userId") Long userId);

    @Query("SELECT t FROM Task t LEFT JOIN t.project.members m WHERE t.id = :taskId AND (t.project.owner.id = :userId OR m.id = :userId)")
    Optional<Task> findByIdAndProjectOwnerIdOrMembersId(@Param("taskId") Long taskId, @Param("userId") Long userId);

    List<Task> findByAssignedToId(Long userId);

    @Query("SELECT t FROM Task t WHERE t.createdBy.id = :userId")
    List<Task> findByCreatedById(@Param("userId") Long userId);

    long countByAssignedToId(Long userId);

    long countByStatusAndAssignedToId(TaskStatus status, Long userId);
}

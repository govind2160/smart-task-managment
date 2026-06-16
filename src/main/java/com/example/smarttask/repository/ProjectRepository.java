package com.example.smarttask.repository;

import com.example.smarttask.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByOwnerId(Long ownerId);
    Optional<Project> findByIdAndOwnerId(Long id, Long ownerId);
    long countByOwnerId(Long ownerId);

    @Query("SELECT DISTINCT p FROM Project p JOIN p.members m WHERE m.id = :userId")
    List<Project> findByMembersId(@Param("userId") Long userId);

    @Query("SELECT p FROM Project p JOIN p.members m WHERE p.id = :projectId AND m.id = :userId")
    Optional<Project> findByIdAndMembersId(@Param("projectId") Long projectId, @Param("userId") Long userId);

    @Query("SELECT COUNT(p) FROM Project p JOIN p.members m WHERE m.id = :userId AND p.owner.id != :userId")
    long countByMembersIdAndOwnerIdNot(@Param("userId") Long userId);
}

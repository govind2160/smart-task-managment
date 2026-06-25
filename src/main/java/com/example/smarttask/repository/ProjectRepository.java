package com.example.smarttask.repository;

import com.example.smarttask.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Override
    @EntityGraph(attributePaths = {"owner", "members"})
    List<Project> findAll();

    @Override
    @EntityGraph(attributePaths = {"owner", "members"})
    Optional<Project> findById(Long id);

    @EntityGraph(attributePaths = {"owner", "members"})
    List<Project> findByOwnerId(Long ownerId);

    @EntityGraph(attributePaths = {"owner", "members"})
    Optional<Project> findByIdAndOwnerId(Long id, Long ownerId);

    long countByOwnerId(Long ownerId);

    @Query("SELECT DISTINCT p FROM Project p LEFT JOIN FETCH p.owner LEFT JOIN FETCH p.members WHERE :userId MEMBER OF p.members")
    List<Project> findByMembersId(@Param("userId") Long userId);

    @Query("SELECT DISTINCT p FROM Project p LEFT JOIN FETCH p.owner LEFT JOIN FETCH p.members WHERE p.owner.id = :userId OR :userId MEMBER OF p.members")
    List<Project> findByOwnerIdOrMembersId(@Param("userId") Long userId);

    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.owner LEFT JOIN FETCH p.members WHERE p.id = :projectId AND :userId MEMBER OF p.members")
    Optional<Project> findByIdAndMembersId(@Param("projectId") Long projectId, @Param("userId") Long userId);

    @Query("SELECT COUNT(p) FROM Project p JOIN p.members m WHERE m.id = :userId AND p.owner.id != :userId")
    long countByMembersIdAndOwnerIdNot(@Param("userId") Long userId);
}

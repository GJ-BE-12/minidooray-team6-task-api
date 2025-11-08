package com.nhnacademy.taskAPI.repository;

import com.nhnacademy.taskAPI.entity.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

    boolean existsByProjectIdAndAccountId(Long projectId, Long accountId);

    List<ProjectMember> findByAccountId(Long accountId);

    List<ProjectMember> findByProjectId(Long projectId);

    Optional<ProjectMember> findByProjectIdAndAccountId(Long projectId, Long accountId);
}
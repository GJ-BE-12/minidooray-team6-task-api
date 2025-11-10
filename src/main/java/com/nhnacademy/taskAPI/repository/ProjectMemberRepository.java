package com.nhnacademy.taskAPI.repository;

import com.nhnacademy.taskAPI.entity.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    List<ProjectMember> findByAccountId(Long accountId);
    List<ProjectMember> findByProjectId(Long projectId);
    boolean existsByAccountIdAndProjectId(Long accountId, Long projectId);
    Optional<ProjectMember> findByProjectIdAndAccountId(Long projectId, Long accountId);

//    boolean existsByProjectIdAndAccountId(Long projectId, Long accountId);
}

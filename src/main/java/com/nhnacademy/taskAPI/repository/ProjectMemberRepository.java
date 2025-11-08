package com.nhnacademy.taskAPI.repository;

import com.nhnacademy.taskAPI.entity.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    List<ProjectMember> findByAccountId(Long accountId);
    List<ProjectMember> findByProjectId(Long projectId);
    void deleteByProjectIdAndAccountId(Long projectId, Long accountId);
    boolean existsByProjectIdAndAccountId(Long projectId, Long accountId);
}

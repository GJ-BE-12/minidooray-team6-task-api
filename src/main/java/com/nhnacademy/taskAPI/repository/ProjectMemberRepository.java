package com.nhnacademy.taskAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberRepository<ProjectMember> extends JpaRepository<ProjectMember, Long>{}

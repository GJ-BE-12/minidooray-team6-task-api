package com.nhnacademy.taskAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository<Project> extends JpaRepository<Project, Long>{}

package com.nhnacademy.taskAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MilestoneRepository<Milestone> extends JpaRepository<Milestone, Long>{}

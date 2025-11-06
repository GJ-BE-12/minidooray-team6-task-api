package com.nhnacademy.taskAPI.repository;

import com.nhnacademy.taskAPI.entity.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MilestoneRepository extends JpaRepository<Milestone, Long>{}

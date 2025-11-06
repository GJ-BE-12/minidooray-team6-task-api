package com.nhnacademy.taskAPI.repository;

import com.nhnacademy.taskAPI.entity.TaskTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskTagRepository extends JpaRepository<TaskTag, Long>{}

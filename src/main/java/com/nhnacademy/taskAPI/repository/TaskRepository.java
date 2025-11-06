package com.nhnacademy.taskAPI.repository;

import com.nhnacademy.taskAPI.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long>{}

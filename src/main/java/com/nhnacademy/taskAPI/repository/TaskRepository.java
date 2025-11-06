package com.nhnacademy.taskAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository<Task> extends JpaRepository<Task, Long>{}

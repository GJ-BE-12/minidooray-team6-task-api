package com.nhnacademy.taskAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskTagRepository<TaskTag> extends JpaRepository<TaskTag, Long>{}

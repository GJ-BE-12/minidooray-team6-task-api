package com.nhnacademy.taskAPI.repository;

import com.nhnacademy.taskAPI.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskTagRepository extends JpaRepository<Tag, Long>{}

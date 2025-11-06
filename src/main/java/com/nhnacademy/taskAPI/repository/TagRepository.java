package com.nhnacademy.taskAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository<Tag> extends JpaRepository<Tag, Long>{}

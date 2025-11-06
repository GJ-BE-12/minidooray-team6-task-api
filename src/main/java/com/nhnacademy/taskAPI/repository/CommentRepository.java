package com.nhnacademy.taskAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository<Comment> extends JpaRepository<Comment, Long>{}

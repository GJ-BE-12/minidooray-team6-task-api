package com.nhnacademy.taskAPI.repository;

import com.nhnacademy.taskAPI.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>{}

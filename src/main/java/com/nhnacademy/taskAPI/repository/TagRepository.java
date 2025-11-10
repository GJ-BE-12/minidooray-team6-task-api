package com.nhnacademy.taskAPI.repository;

import com.nhnacademy.taskAPI.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long>{
    List<Tag> findByProjectId(Long projectId);}

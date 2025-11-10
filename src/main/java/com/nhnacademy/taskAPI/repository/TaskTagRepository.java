package com.nhnacademy.taskAPI.repository;


import com.nhnacademy.taskAPI.entity.Task;
import com.nhnacademy.taskAPI.entity.TaskTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskTagRepository extends JpaRepository<TaskTag, Long>{
    boolean existsByTask_IdAndTag_Id(Long taskId, Long tagId);

    Optional<TaskTag> findByTask_IdAndTag_Id(Long taskId, Long tagId);

    List<TaskTag> getTaskTagsByTask(Task task);
}

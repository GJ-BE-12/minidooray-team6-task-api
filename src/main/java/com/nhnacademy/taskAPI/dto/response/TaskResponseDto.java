package com.nhnacademy.taskAPI.dto.response;

import com.nhnacademy.taskAPI.entity.Project;
import com.nhnacademy.taskAPI.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDto {
    private Long id;
    private String title;
    private String content;
    private Long creatorId;
    private Long milestoneId;


    public static TaskResponseDto fromEntity(Task task) {
        return new TaskResponseDto(
                task.getId(),
                task.getTitle(),
                task.getContent(),
                task.getCreatorId(),
                task.getMilestone().getId()
        );
    }
}

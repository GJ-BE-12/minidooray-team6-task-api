package com.nhnacademy.taskAPI.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDetailsDto {
    private ProjectResponseDto project;
    private List<MileStoneResponseDto> mileStone;
    
    private List<TaskResponseDto> tasks;
    private List<TagResponseDto> tags;
    private List<Long> members;
}
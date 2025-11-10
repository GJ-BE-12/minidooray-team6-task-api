package com.nhnacademy.taskAPI.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Task 상세조회 시 보여줄 tag 목록과 mileStones목록 comment목록
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDetailsResponseDto {
    private TaskResponseDto task;
    private MileStoneResponseDto mileStone;

    private List<TagResponseDto> tags;
    private List<CommentResponseDto>  comments;
}

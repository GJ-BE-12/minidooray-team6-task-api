package com.nhnacademy.taskAPI.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Task의 기본 정보를 담는 곳, 주로 projectDetailsDto 내의 task목록에서 사용할 예정
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDto {
    private Long id;
    private String title;
    private String content;
    private Long creatorId;
    private Long milestoneId;
}

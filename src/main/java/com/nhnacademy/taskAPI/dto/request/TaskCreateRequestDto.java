package com.nhnacademy.taskAPI.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Project내에서 새로운 Task를 생성하기 위한 요청 본문 Task 생성시 creator과 project id가 필요함
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreateRequestDto {
//    private Long creatorId; //세션에서 가져오지 말고 taskAPI에서 받아오기
//    task 생성자 xuserid
//    private Long projectId;
//    url에 존재
    private String title;
    private String content;
}

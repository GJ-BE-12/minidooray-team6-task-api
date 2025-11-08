//package com.nhnacademy.taskAPI.service;
//
//import com.nhnacademy.taskAPI.dto.TaskResponseDto;
//
//import java.util.List;
//
//public interface TaskService {
//
//    /**
//     * [C] 특정 프로젝트에 새 태스크를 생성합니다.
//     */
//    TaskResponseDto createTask(Long accountId, Long projectId, TaskCreateRequestDto requestDto);
//
//    /**
//     * [R] 특정 프로젝트에 속한 모든 태스크 목록을 조회합니다.
//     */
//    List<TaskResponseDto> findTasksByProjectId(Long accountId, Long projectId);
//
//    /**
//     * [R] 단일 태스크의 상세 정보를 조회합니다.
//     */
//    TaskResponseDto findTaskDetails(Long accountId, Long taskId);
//
//    /**
//     * [U] 태스크의 내용, 마일스톤을 수정합니다.
//     */
//    TaskResponseDto updateTask(Long accountId, Long taskId, TaskUpdateDto requestDto);
//
//    /**
//     * [D] 태스크를 삭제합니다.
//     */
//    void deleteTask(Long accountId, Long taskId);
//
//    /**
//     * [C] 특정 태스크에 특정 태그를 할당(연결)합니다.
//     */
//    void assignTagToTask(Long accountId, Long taskId, Long tagId);
//
//    /**
//     * [D] 특정 태스크에 연결된 태그를 해제(제거)합니다.
//     */
//    void removeTagFromTask(Long accountId, Long taskId, Long tagId);
//}
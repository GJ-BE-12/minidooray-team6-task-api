package com.nhnacademy.taskAPI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.taskAPI.dto.request.TaskAddTagRequestDto;
import com.nhnacademy.taskAPI.dto.request.TaskUpdateRequestDto;
import com.nhnacademy.taskAPI.dto.response.MileStoneResponseDto;
import com.nhnacademy.taskAPI.dto.response.TaskDetailsResponseDto;
import com.nhnacademy.taskAPI.dto.response.TaskResponseDto;
import com.nhnacademy.taskAPI.exception.GlobalExceptionHandler;
import com.nhnacademy.taskAPI.exception.MemberAccessDeniedException;
import com.nhnacademy.taskAPI.exception.TaskNotFoundException;
import com.nhnacademy.taskAPI.service.TaskService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {TaskController.class, GlobalExceptionHandler.class})
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TaskService taskService;

    // --- 공통 변수 ---
    private final Long userId = 101L;
    private final Long projectId = 1L;
    private final Long taskId = 1L;
    private TaskResponseDto taskResponseDto;

    @BeforeEach
    void setUp() {
        // TaskResponseDto 구조에 맞게 Mock 객체 생성
        taskResponseDto = new TaskResponseDto(taskId, "Test Task", "Content", userId, null, List.of());
    }

    @Nested
    @DisplayName("POST /projects/{projectId}/tasks (태스크 생성)")
    class CreateTaskTests {
        
        @Test
        @DisplayName("성공 (201 Created)")
        void createTask_Success() throws Exception {
            // TaskCreateRequestDto는 AllArgsConstructor를 사용하므로 Map으로 생성
            Map<String, String> requestMap = Map.of(
                    "title", "Test Task",
                    "content", "Content"
            );
            
            given(taskService.createTask(eq(userId), eq(projectId), any()))
                    .willReturn(taskResponseDto);

            mockMvc.perform(post("/projects/{projectId}/tasks", projectId)
                            .header("X-USER-ID", userId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestMap)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(taskId));
        }
    }

    @Nested
    @DisplayName("GET /projects/{projectId}/tasks (태스크 목록 조회)")
    class FindTasksByProjectIdTests {

        @Test
        @DisplayName("성공 (200 OK)")
        void findTasksByProjectId_Success() throws Exception {
            given(taskService.findTasksByProjectId(userId, projectId)).willReturn(List.of(taskResponseDto));

            mockMvc.perform(get("/projects/{projectId}/tasks", projectId)
                            .header("X-USER-ID", userId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1));
        }

        @Test
        @DisplayName("실패 - 프로젝트 접근 권한 없음 (403 Forbidden)")
        void findTasksByProjectId_AccessDenied() throws Exception {
            given(taskService.findTasksByProjectId(999L, projectId))
                    .willThrow(new MemberAccessDeniedException("Access Denied"));

            mockMvc.perform(get("/projects/{projectId}/tasks", projectId)
                            .header("X-USER-ID", 999L)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("GET /projects/{projectId}/tasks/{taskId} (태스크 상세 조회)")
    class FindTaskDetailsTests {

        @Test
        @DisplayName("성공 (200 OK)")
        void findTaskDetails_Success() throws Exception {
            MileStoneResponseDto milestone = new MileStoneResponseDto(1L, "Milestone");
            TaskDetailsResponseDto detailsDto = new TaskDetailsResponseDto(taskResponseDto, milestone, List.of(), List.of());
            
            given(taskService.findTaskDetails(userId, taskId)).willReturn(detailsDto);

            mockMvc.perform(get("/projects/{projectId}/tasks/{taskId}", projectId, taskId)
                            .header("X-USER-ID", userId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.task.id").value(taskId));
        }

        @Test
        @DisplayName("실패 - 태스크 없음 (404 Not Found)")
        void findTaskDetails_NotFound() throws Exception {
            given(taskService.findTaskDetails(userId, 999L)).willThrow(new TaskNotFoundException("Not Found"));

            mockMvc.perform(get("/projects/{projectId}/tasks/{taskId}", projectId, 999L)
                            .header("X-USER-ID", userId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("PUT /projects/{projectId}/tasks/{taskId} (태스크 수정)")
    class UpdateTaskTests {

        @Test
        @DisplayName("성공 (200 OK)")
        void updateTask_Success() throws Exception {
            // TaskUpdateRequestDto는 AllArgsConstructor 사용
            Map<String, Object> requestMap = Map.of(
                    "title", "Updated",
                    "content", "...",
                    "mileStoneId", 1L
            );
            TaskResponseDto responseDto = new TaskResponseDto(taskId, "Updated", "...", userId, 1L, List.of());
            
            given(taskService.updateTask(eq(userId), eq(taskId), any(TaskUpdateRequestDto.class)))
                    .willReturn(responseDto);

            mockMvc.perform(put("/projects/{projectId}/tasks/{taskId}", projectId, taskId)
                            .header("X-USER-ID", userId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestMap)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").value("Updated"));
        }
    }

    @Nested
    @DisplayName("DELETE /projects/{projectId}/tasks/{taskId} (태스크 삭제)")
    class DeleteTaskTests {
        
        @Test
        @DisplayName("성공 (204 No Content)")
        void deleteTask_Success() throws Exception {
            willDoNothing().given(taskService).deleteTask(userId, taskId);

            mockMvc.perform(delete("/projects/{projectId}/tasks/{taskId}", projectId, taskId)
                            .header("X-USER-ID", userId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }
    }

    @Nested
    @DisplayName("POST /projects/{projectId}/tasks/{taskId}/tags (태그 할당)")
    class AddTagToTaskTests {
        
        @Test
        @DisplayName("성공 (201 Created)")
        void addTagToTask_Success() throws Exception {
            TaskAddTagRequestDto requestDto = new TaskAddTagRequestDto(1L);
            willDoNothing().given(taskService).addTagToTask(eq(userId), eq(taskId), any(TaskAddTagRequestDto.class));

            mockMvc.perform(post("/projects/{projectId}/tasks/{taskId}/tags", projectId, taskId)
                            .header("X-USER-ID", userId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isCreated());
        }
    }

    @Nested
    @DisplayName("DELETE /projects/{projectId}/tasks/{taskId}/tags/{tagId} (태그 해제)")
    class RemoveTagFromTaskTests {

        @Test
        @DisplayName("성공 (204 No Content)")
        void removeTagFromTask_Success() throws Exception {
            Long tagId = 1L;
            willDoNothing().given(taskService).removeTagFromTask(userId, taskId, tagId);
            
            mockMvc.perform(delete("/projects/{projectId}/tasks/{taskId}/tags/{tagId}", projectId, taskId, tagId)
                            .header("X-USER-ID", userId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }
    }
}
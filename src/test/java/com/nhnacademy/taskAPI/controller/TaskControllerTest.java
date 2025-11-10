package com.nhnacademy.taskAPI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.taskAPI.dto.request.TaskAddTagRequestDto;
import com.nhnacademy.taskAPI.dto.request.TaskCreateRequestDto;
import com.nhnacademy.taskAPI.dto.request.TaskUpdateRequestDto;
import com.nhnacademy.taskAPI.dto.response.TaskDetailsResponseDto;
import com.nhnacademy.taskAPI.dto.response.TaskResponseDto;
import com.nhnacademy.taskAPI.exception.GlobalExceptionHandler;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
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

    private final Long userId = 101L;
    private final Long projectId = 1L;
    private final Long taskId = 1L;
    private TaskResponseDto taskResponseDto;

    @BeforeEach
    void setUp() {
        taskResponseDto = new TaskResponseDto(taskId, "Test Task", "Content", userId, null, List.of());
    }

    @Nested
    @DisplayName("POST /projects/{projectId}/tasks (태스크 생성)")
    class CreateTaskTests {

        @Test
        @DisplayName("성공 (201 Created)")
        void createTask_Success() throws Exception {
            TaskCreateRequestDto requestDto = new TaskCreateRequestDto("Test Task", "Content");
            given(taskService.createTask(eq(userId), eq(projectId), any(TaskCreateRequestDto.class)))
                    .willReturn(taskResponseDto);

            mockMvc.perform(post("/projects/{projectId}/tasks", projectId)
                            .header("X-USER-ID", userId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
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
    }

    @Nested
    @DisplayName("GET /tasks/{taskId} (태스크 상세 조회)")
    class FindTaskDetailsTests {

        @Test
        @DisplayName("성공 (200 OK)")
        void findTaskDetails_Success() throws Exception {
            TaskDetailsResponseDto detailsDto = new TaskDetailsResponseDto(taskResponseDto, null, List.of(), List.of());
            given(taskService.findTaskDetails(userId, taskId)).willReturn(detailsDto);

            mockMvc.perform(get("/tasks/{taskId}", taskId)
                            .header("X-USER-ID", userId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.task.id").value(taskId));
        }

        @Test
        @DisplayName("실패 - 태스크 없음 (404 Not Found)")
        void findTaskDetails_NotFound() throws Exception {
            given(taskService.findTaskDetails(userId, 999L)).willThrow(new TaskNotFoundException("Not Found"));

            mockMvc.perform(get("/tasks/{taskId}", 999L)
                            .header("X-USER-ID", userId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("PUT /tasks/{taskId} (태스크 수정)")
    class UpdateTaskTests {

        @Test
        @DisplayName("성공 (200 OK)")
        void updateTask_Success() throws Exception {
            TaskUpdateRequestDto requestDto = new TaskUpdateRequestDto("Updated", "...", 1L);
            TaskResponseDto responseDto = new TaskResponseDto(taskId, "Updated", "...", userId, 1L, List.of());

            given(taskService.updateTask(eq(userId), eq(taskId), any(TaskUpdateRequestDto.class)))
                    .willReturn(responseDto);

            mockMvc.perform(put("/tasks/{taskId}", taskId)
                            .header("X-USER-ID", userId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").value("Updated"));
        }
    }

    @Nested
    @DisplayName("DELETE /tasks/{taskId} (태스크 삭제)")
    class DeleteTaskTests {

        @Test
        @DisplayName("성공 (204 No Content)")
        void deleteTask_Success() throws Exception {
            willDoNothing().given(taskService).deleteTask(userId, taskId);

            mockMvc.perform(delete("/tasks/{taskId}", taskId)
                            .header("X-USER-ID", userId))
                    .andExpect(status().isNoContent());
        }
    }

    @Nested
    @DisplayName("POST /tasks/{taskId}/tags (태그 할당)")
    class AddTagToTaskTests {

        @Test
        @DisplayName("성공 (201 Created)")
        void addTagToTask_Success() throws Exception {
            TaskAddTagRequestDto requestDto = new TaskAddTagRequestDto(1L);
            willDoNothing().given(taskService).addTagToTask(eq(userId), eq(taskId), any(TaskAddTagRequestDto.class));

            mockMvc.perform(post("/tasks/{taskId}/tags", taskId)
                            .header("X-USER-ID", userId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isCreated());
        }
    }

    @Nested
    @DisplayName("DELETE /tasks/{taskId}/tags/{tagId} (태그 해제)")
    class RemoveTagFromTaskTests {

        @Test
        @DisplayName("성공 (204 No Content)")
        void removeTagFromTask_Success() throws Exception {
            Long tagId = 1L;
            willDoNothing().given(taskService).removeTagFromTask(userId, taskId, tagId);

            mockMvc.perform(delete("/tasks/{taskId}/tags/{tagId}", taskId, tagId)
                            .header("X-USER-ID", userId))
                    .andExpect(status().isNoContent());
        }
    }
}
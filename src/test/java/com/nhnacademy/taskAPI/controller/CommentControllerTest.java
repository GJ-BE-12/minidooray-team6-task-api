package com.nhnacademy.taskAPI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.taskAPI.dto.response.CommentResponseDto;
import com.nhnacademy.taskAPI.exception.CommentNotFoundException;
import com.nhnacademy.taskAPI.exception.GlobalExceptionHandler;
import com.nhnacademy.taskAPI.exception.MemberAccessDeniedException;
import com.nhnacademy.taskAPI.service.CommentService;

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

@WebMvcTest(controllers = {CommentController.class, GlobalExceptionHandler.class})
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CommentService commentService;

    private final Long accountId = 101L;
    private final Long projectId = 1L;
    private final Long taskId = 1L;
    private final Long commentId = 1L;
    private CommentResponseDto commentResponseDto;
    
    @BeforeEach
    void setUp() {
        commentResponseDto = new CommentResponseDto(commentId, accountId, taskId, "Test Comment");
    }

    @Nested
    @DisplayName("POST /projects/{projectId}/tasks/{taskId}/comments (댓글 생성)")
    class CreateCommentTests {
        
        @Test
        @DisplayName("성공 (201 Created)")
        void createComment_Success() throws Exception {
            Map<String, String> requestMap = Map.of("content", "Test Comment");
            
            given(commentService.createComment(eq(accountId), eq(taskId), any()))
                    .willReturn(commentResponseDto);

            mockMvc.perform(post("/projects/{projectId}/tasks/{taskId}/comments", projectId, taskId)
                            .header("X-USER-ID", accountId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestMap)))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", "/comments/" + commentId))
                    .andExpect(jsonPath("$.content").value("Test Comment"));
        }

        @Test
        @DisplayName("실패 - 유효성 검사 (400 Bad Request)")
        void createComment_ValidationFailed() throws Exception {
            Map<String, String> badRequestMap = Map.of("content", ""); 

            mockMvc.perform(post("/projects/{projectId}/tasks/{taskId}/comments", projectId, taskId)
                            .header("X-USER-ID", accountId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(badRequestMap)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("GET /projects/{projectId}/tasks/{taskId}/comments (댓글 목록 조회)")
    class GetCommentsForTaskTests {
        
        @Test
        @DisplayName("성공 (200 OK)")
        void getCommentsForTask_Success() throws Exception {
            given(commentService.findCommentsByTaskId(accountId, taskId)).willReturn(List.of(commentResponseDto));
            
            mockMvc.perform(get("/projects/{projectId}/tasks/{taskId}/comments", projectId, taskId)
                            .header("X-USER-ID", accountId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1));
        }

        @Test
        @DisplayName("실패 - 프로젝트 접근 권한 없음 (403 Forbidden)")
        void getCommentsForTask_AccessDenied() throws Exception {
            given(commentService.findCommentsByTaskId(999L, taskId))
                    .willThrow(new MemberAccessDeniedException("Access Denied"));
            
            mockMvc.perform(get("/projects/{projectId}/tasks/{taskId}/comments", projectId, taskId)
                            .header("X-USER-ID", 999L)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden());
        }
    }
    
    @Nested
    @DisplayName("PUT /projects/{projectId}/tasks/{taskId}/comments/{commentId} (댓글 수정)")
    class UpdateCommentTests {
        
        @Test
        @DisplayName("성공 (200 OK)")
        void updateComment_Success() throws Exception {
            Map<String, String> requestMap = Map.of("content", "Updated");
            CommentResponseDto responseDto = new CommentResponseDto(commentId, accountId, taskId, "Updated");
            
            given(commentService.updateComment(eq(accountId), eq(commentId), any()))
                    .willReturn(responseDto);

            mockMvc.perform(put("/projects/{projectId}/tasks/{taskId}/comments/{commentId}", projectId, taskId, commentId)
                            .header("X-USER-ID", accountId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestMap)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").value("Updated"));
        }

        @Test
        @DisplayName("실패 - 댓글 없음 (404 Not Found)")
        void updateComment_NotFound() throws Exception {
            Map<String, String> requestMap = Map.of("content", "Updated");
            
            given(commentService.updateComment(eq(accountId), eq(999L), any()))
                    .willThrow(new CommentNotFoundException("Not Found"));
            
            mockMvc.perform(put("/projects/{projectId}/tasks/{taskId}/comments/{commentId}", projectId, taskId, 999L)
                            .header("X-USER-ID", accountId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON) // 404 JSON 응답을 받기 위해 필수
                            .content(objectMapper.writeValueAsString(requestMap)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("실패 - 작성자 아님 (403 Forbidden)")
        void updateComment_AccessDenied() throws Exception {
            Map<String, String> requestMap = Map.of("content", "Updated");
            Long otherUserId = 999L;

            given(commentService.updateComment(eq(otherUserId), eq(commentId), any()))
                    .willThrow(new MemberAccessDeniedException("Writer only"));

            mockMvc.perform(put("/projects/{projectId}/tasks/{taskId}/comments/{commentId}", projectId, taskId, commentId)
                            .header("X-USER-ID", otherUserId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestMap)))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("DELETE /projects/{projectId}/tasks/{taskId}/comments/{commentId} (댓글 삭제)")
    class DeleteCommentTests {
        
        @Test
        @DisplayName("성공 (204 No Content)")
        void deleteComment_Success() throws Exception {
            willDoNothing().given(commentService).deleteComment(accountId, commentId);
            
            mockMvc.perform(delete("/projects/{projectId}/tasks/{taskId}/comments/{commentId}", projectId, taskId, commentId)
                            .header("X-USER-ID", accountId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }
    }
}
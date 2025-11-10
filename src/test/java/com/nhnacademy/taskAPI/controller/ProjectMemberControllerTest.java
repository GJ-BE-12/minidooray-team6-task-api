package com.nhnacademy.taskAPI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.taskAPI.exception.GlobalExceptionHandler;
import com.nhnacademy.taskAPI.exception.MemberAccessDeniedException;
import com.nhnacademy.taskAPI.service.ProjectMemberService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {ProjectMemberController.class, GlobalExceptionHandler.class})
class ProjectMemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProjectMemberService projectMemberService;

    private final Long adminId = 1L;
    private final Long memberId = 101L;
    private final Long projectId = 1L;
    
    @Nested
    @DisplayName("POST /projects/{projectId}/members (멤버 추가)")
    class AddMembersTests {

        @Test
        @DisplayName("성공 (201 Created)")
        void addMembers_Success() throws Exception {
            List<Map<String, Long>> requestList = List.of(Map.of("accountId", memberId));
            willDoNothing().given(projectMemberService).addMembersToProject(eq(adminId), eq(projectId), any(List.class));

            mockMvc.perform(post("/projects/{projectId}/members", projectId)
                            .header("X-USER-ID", adminId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestList)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("실패 - 관리자 아님 (403 Forbidden)")
        void addMembers_AccessDenied() throws Exception {
            List<Map<String, Long>> requestList = List.of(Map.of("accountId", memberId));
            willThrow(new MemberAccessDeniedException("Admin only"))
                    .given(projectMemberService).addMembersToProject(eq(memberId), eq(projectId), any(List.class));

            mockMvc.perform(post("/projects/{projectId}/members", projectId)
                            .header("X-USER-ID", memberId) // 멤버가 멤버 추가 시도
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestList)))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("GET /projects/{projectId}/members (멤버 조회)")
    class GetMembersTests {

        @Test
        @DisplayName("성공 (200 OK) - Long 리스트 반환")
        void getMembers_Success() throws Exception {
            given(projectMemberService.getMemberAccountIdsForProject(memberId, projectId)).willReturn(List.of(adminId, memberId));

            mockMvc.perform(get("/projects/{projectId}/members", projectId)
                            .header("X-USER-ID", memberId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0]").value(adminId));
        }
    }

    @Nested
    @DisplayName("DELETE /projects/{projectId}/members/{userId} (멤버 삭제)")
    class RemoveMemberTests {

        @Test
        @DisplayName("성공 (204 No Content)")
        void removeMember_Success() throws Exception {
            willDoNothing().given(projectMemberService).removeMemberFromProject(adminId, projectId, memberId);
            
            mockMvc.perform(delete("/projects/{projectId}/members/{userId}", projectId, memberId)
                            .header("X-USER-ID", adminId))
                    .andExpect(status().isNoContent());
        }
    }
}
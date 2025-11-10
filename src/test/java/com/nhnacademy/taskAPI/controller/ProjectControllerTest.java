//package com.nhnacademy.taskAPI.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.nhnacademy.taskAPI.dto.request.ProjectCreateRequestDto;
//import com.nhnacademy.taskAPI.dto.request.ProjectStatusUpdateDto;
//import com.nhnacademy.taskAPI.dto.request.ProjectUpdateDto;
//import com.nhnacademy.taskAPI.dto.response.ProjectDetailsDto;
//import com.nhnacademy.taskAPI.dto.response.ProjectResponseDto;
//import com.nhnacademy.taskAPI.entity.ProjectStatus;
//import com.nhnacademy.taskAPI.exception.GlobalExceptionHandler;
//import com.nhnacademy.taskAPI.exception.MemberAccessDeniedException;
//import com.nhnacademy.taskAPI.exception.ProjectNotFoundException;
//import com.nhnacademy.taskAPI.service.ProjectService;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.BDDMockito.willDoNothing;
//import static org.mockito.BDDMockito.willThrow;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//// 1. GlobalExceptionHandler를 함께 로드해야 4xx, 5xx 응답 테스트가 가능합니다.
//@WebMvcTest(controllers = {ProjectController.class, GlobalExceptionHandler.class})
//class ProjectControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockitoBean // Spring Boot 3.1+ 에서는 @MockBean 대신 사용 가능
//    private ProjectService projectService;
//
//    private Long adminId;
//    private Long memberId;
//    private Long projectId;
//    private ProjectResponseDto projectResponseDto;
//
//    @BeforeEach
//    void setUp() {
//        adminId = 100L;
//        memberId = 102L;
//        projectId = 1L;
//
//        projectResponseDto = new ProjectResponseDto(projectId, "Test Project", ProjectStatus.ACTIVE, adminId);
//    }
//
//    // @Nested를 사용하면 테스트를 메소드별로 그룹화할 수 있습니다.
//
//    @Nested
//    @DisplayName("POST /projects (프로젝트 생성)")
//    class CreateProjectTests {
//
//        @Test
//        @DisplayName("성공 (201 Created)")
//        void createProject_Success() throws Exception {
//            // Given
//            ProjectCreateRequestDto requestDto = new ProjectCreateRequestDto("Test Project");
//            // adminId (100L)과 requestDto가 주어졌을 때
//            given(projectService.createProject(eq(adminId), any(ProjectCreateRequestDto.class)))
//                    .willReturn(projectResponseDto); // projectResponseDto를 반환
//
//            // When & Then
//            mockMvc.perform(post("/projects")
//                            .header("X-USER-ID", adminId)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(requestDto)))
//                    .andExpect(status().isCreated()) // 201 Created
//                    .andExpect(header().string("Location", "/projects/" + projectId))
//                    .andExpect(jsonPath("$.id").value(projectId))
//                    .andExpect(jsonPath("$.name").value("Test Project"));
//        }
//
//        @Test
//        @DisplayName("실패 - Validation 에러 (400 Bad Request)")
//        void createProject_ValidationFailed() throws Exception {
//            // Given
//            ProjectCreateRequestDto badRequestDto = new ProjectCreateRequestDto(null); // @Valid에 걸림
//
//            // When & Then
//            mockMvc.perform(post("/projects")
//                            .header("X-USER-ID", adminId)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(badRequestDto)))
//                    .andExpect(status().isBadRequest()); // 400 Bad Request
//        }
//    }
//
//    @Nested
//    @DisplayName("GET /projects (내 프로젝트 목록 조회)")
//    class GetMyProjectsTests {
//
//        @Test
//        @DisplayName("성공 (200 OK)")
//        void getMyProjects_Success() throws Exception {
//            // Given
//            List<ProjectResponseDto> projects = List.of(projectResponseDto);
//            given(projectService.getMyProjects(memberId)).willReturn(projects);
//
//            // When & Then
//            mockMvc.perform(get("/projects")
//                            .header("X-USER-ID", memberId)
//                            .accept(MediaType.APPLICATION_JSON)) // JSON 응답 기대
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.length()").value(1))
//                    .andExpect(jsonPath("$[0].id").value(projectId));
//        }
//
//        @Test
//        @DisplayName("성공 - 프로젝트 없음 (200 OK, 빈 리스트)")
//        void getMyProjects_Empty() throws Exception {
//            // Given
//            given(projectService.getMyProjects(memberId)).willReturn(List.of()); // 빈 리스트 반환
//
//            // When & Then
//            mockMvc.perform(get("/projects")
//                            .header("X-USER-ID", memberId)
//                            .accept(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.length()").value(0)); // 빈 배열([]) 검증
//        }
//    }
//
//    @Nested
//    @DisplayName("GET /projects/{projectId} (프로젝트 상세 조회)")
//    class GetProjectDetailsTests {
//
//        @Test
//        @DisplayName("성공 (200 OK)")
//        void getProjectDetails_Success() throws Exception {
//            // Given
//            ProjectDetailsDto detailsDto = new ProjectDetailsDto(projectResponseDto, List.of(), List.of(), List.of(), List.of(memberId));
//            given(projectService.getProject(memberId, projectId)).willReturn(detailsDto);
//
//            // When & Then
//            mockMvc.perform(get("/projects/{projectId}", projectId)
//                            .header("X-USER-ID", memberId)
//                            .accept(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.project.id").value(projectId));
//        }
//
//        @Test
//        @DisplayName("실패 - 프로젝트 없음 (404 Not Found)")
//        void getProjectDetails_NotFound() throws Exception {
//            // Given
//            given(projectService.getProject(memberId, 999L))
//                    .willThrow(new ProjectNotFoundException("프로젝트를 찾을 수 없습니다."));
//
//            // When & Then
//            mockMvc.perform(get("/projects/{projectId}", 999L)
//                            .header("X-USER-ID", memberId)
//                            .accept(MediaType.APPLICATION_JSON)) // <-- Accept 헤더 필수
//                    .andExpect(status().isNotFound());
//        }
//
//        @Test
//        @DisplayName("실패 - 멤버 권한 없음 (403 Forbidden)")
//        void getProjectDetails_AccessDenied() throws Exception {
//            // Given
//            Long otherUserId = 999L; // 이 프로젝트의 멤버가 아닌 유저
//            given(projectService.getProject(otherUserId, projectId))
//                    .willThrow(new MemberAccessDeniedException("접근 권한이 없습니다."));
//
//            // When & Then
//            mockMvc.perform(get("/projects/{projectId}", projectId)
//                            .header("X-USER-ID", otherUserId)
//                            .accept(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isForbidden()); // 403 Forbidden
//        }
//    }
//
//    @Nested
//    @DisplayName("PUT /projects/{projectId} (프로젝트 수정)")
//    class UpdateProjectTests {
//
//        @Test
//        @DisplayName("성공 (200 OK)")
//        void updateProject_Success() throws Exception {
//            // Given
//            ProjectUpdateDto updateDto = new ProjectUpdateDto("Updated Name", ProjectStatus.HIBERNATE);
//            ProjectResponseDto updatedResponse = new ProjectResponseDto(projectId, "Updated Name", ProjectStatus.HIBERNATE, adminId);
//
//            given(projectService.updateProject(adminId, projectId, any(ProjectUpdateDto.class)))
//                    .willReturn(updatedResponse);
//
//            // When & Then
//            mockMvc.perform(put("/projects/{projectId}", projectId)
//                            .header("X-USER-ID", adminId)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(updateDto)))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.name").value("Updated Name"))
//                    .andExpect(jsonPath("$.status").value("HIBERNATE"));
//        }
//
//        @Test
//        @DisplayName("실패 - 관리자 권한 없음 (403 Forbidden)")
//        void updateProject_AccessDenied() throws Exception {
//            // Given
//            ProjectUpdateDto updateDto = new ProjectUpdateDto("Updated Name", ProjectStatus.HIBERNATE);
//            // memberId(102L)가 수정을 시도
//            given(projectService.updateProject(memberId, projectId, any(ProjectUpdateDto.class)))
//                    .willThrow(new MemberAccessDeniedException("관리자만 수정 가능합니다."));
//
//            // When & Then
//            mockMvc.perform(put("/projects/{projectId}", projectId)
//                            .header("X-USER-ID", memberId) // 관리자가 아닌 멤버 ID
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(updateDto)))
//                    .andExpect(status().isForbidden());
//        }
//    }
//
//    @Nested
//    @DisplayName("DELETE /projects/{projectId} (프로젝트 삭제)")
//    class DeleteProjectTests {
//
//        @Test
//        @DisplayName("성공 (204 No Content)")
//        void deleteProject_Success() throws Exception {
//            // Given
//            // projectService.deleteProject(adminId, projectId)는 아무것도 반환하지 않음
//            willDoNothing().given(projectService).deleteProject(adminId, projectId);
//
//            // When & Then
//            mockMvc.perform(delete("/projects/{projectId}", projectId)
//                            .header("X-USER-ID", adminId))
//                    .andExpect(status().isNoContent()); // 204 No Content
//        }
//
//        @Test
//        @DisplayName("실패 - 관리자 권한 없음 (403 Forbidden)")
//        void deleteProject_AccessDenied() throws Exception {
//            // Given
//            // memberId(102L)가 삭제를 시도
//            willThrow(new MemberAccessDeniedException("관리자만 삭제 가능합니다."))
//                    .given(projectService).deleteProject(memberId, projectId);
//
//            // When & Then
//            mockMvc.perform(delete("/projects/{projectId}", projectId)
//                            .header("X-USER-ID", memberId)) // 관리자가 아닌 멤버 ID
//                    .andExpect(status().isForbidden());
//        }
//    }
//
//    @Nested
//    @DisplayName("PATCH /projects/{projectId}/status (상태 변경)")
//    class UpdateProjectStatusTests {
//
//        @Test
//        @DisplayName("성공 (200 OK)")
//        void updateProjectStatus_Success() throws Exception {
//            // Given
//            ProjectStatusUpdateDto statusDto = new ProjectStatusUpdateDto(ProjectStatus.CLOSED);
//            ProjectResponseDto updatedResponse = new ProjectResponseDto(projectId, "Test Project", ProjectStatus.CLOSED, adminId);
//
//            given(projectService.updateProjectStatus(adminId, projectId, any(ProjectStatusUpdateDto.class)))
//                    .willReturn(updatedResponse);
//
//            // When & Then
//            mockMvc.perform(patch("/projects/{projectId}/status", projectId)
//                            .header("X-USER-ID", adminId)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(statusDto)))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.status").value("CLOSED"));
//        }
//    }
//}
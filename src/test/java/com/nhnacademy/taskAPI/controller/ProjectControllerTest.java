package com.nhnacademy.taskAPI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.taskAPI.dto.request.ProjectCreateRequestDto;
import com.nhnacademy.taskAPI.dto.request.ProjectStatusUpdateDto;
import com.nhnacademy.taskAPI.dto.request.ProjectUpdateDto;
import com.nhnacademy.taskAPI.dto.response.ProjectDetailsDto;
import com.nhnacademy.taskAPI.dto.response.ProjectResponseDto;
import com.nhnacademy.taskAPI.entity.ProjectStatus;
import com.nhnacademy.taskAPI.exception.GlobalExceptionHandler;
import com.nhnacademy.taskAPI.exception.MemberAccessDeniedException;
import com.nhnacademy.taskAPI.exception.ProjectNotFoundException;
import com.nhnacademy.taskAPI.service.ProjectService;
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

@WebMvcTest(controllers = {ProjectController.class, GlobalExceptionHandler.class})
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProjectService projectService;

    private final Long adminId = 1L;
    private final Long memberId = 101L;
    private final Long projectId = 1L;
    private ProjectResponseDto projectResponseDto;

    @BeforeEach
    void setUp() {
        projectResponseDto = new ProjectResponseDto(projectId, "Test Project", ProjectStatus.ACTIVE, adminId);
    }

    @Nested
    @DisplayName("POST /projects")
    class CreateProjectTests {

        @Test
        @DisplayName("성공")
        void createProject_Success() throws Exception {
            ProjectCreateRequestDto requestDto = new ProjectCreateRequestDto("New Project");
            ProjectResponseDto responseDto = new ProjectResponseDto(projectId, "New Project", ProjectStatus.ACTIVE, adminId);

            given(projectService.createProject(eq(adminId), any(ProjectCreateRequestDto.class))).willReturn(responseDto);

            mockMvc.perform(post("/projects")
                            .header("X-USER-ID", adminId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", "/projects/" + projectId))
                    .andExpect(jsonPath("$.name").value("New Project"));
        }

        @Test
        @DisplayName("실패 - Validation")
        void createProject_ValidationFailed() throws Exception {
            ProjectCreateRequestDto badDto = new ProjectCreateRequestDto(null);

            mockMvc.perform(post("/projects")
                            .header("X-USER-ID", adminId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(badDto)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("GET /projects")
    class GetMyProjectsTests {

        @Test
        @DisplayName("성공")
        void getMyProjects_Success() throws Exception {
            given(projectService.getMyProjects(memberId)).willReturn(List.of(projectResponseDto));

            mockMvc.perform(get("/projects")
                            .header("X-USER-ID", memberId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(jsonPath("$[0].name").value("Test Project"));
        }
    }

    @Nested
    @DisplayName("GET /projects/{projectId}")
    class GetProjectDetailsTests {

        @Test
        @DisplayName("성공")
        void getProjectDetails_Success() throws Exception {
            ProjectDetailsDto detailsDto = new ProjectDetailsDto(projectResponseDto, List.of(), List.of(), List.of(), List.of(memberId));

            given(projectService.getProject(memberId, projectId)).willReturn(detailsDto);

            mockMvc.perform(get("/projects/{projectId}", projectId)
                            .header("X-USER-ID", memberId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.project.id").value(projectId));
        }

        @Test
        @DisplayName("실패 - Not Found")
        void getProjectDetails_NotFound() throws Exception {
            given(projectService.getProject(memberId, 999L)).willThrow(new ProjectNotFoundException("Not Found"));

            mockMvc.perform(get("/projects/{projectId}", 999L)
                            .header("X-USER-ID", memberId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("실패 - Access Denied")
        void getProjectDetails_AccessDenied() throws Exception {
            given(projectService.getProject(999L, projectId)).willThrow(new MemberAccessDeniedException("Access Denied"));

            mockMvc.perform(get("/projects/{projectId}", projectId)
                            .header("X-USER-ID", 999L)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("PUT /projects/{projectId}")
    class UpdateProjectTests {

        @Test
        @DisplayName("성공")
        void updateProject_Success() throws Exception {
            ProjectUpdateDto requestDto = new ProjectUpdateDto("Updated", ProjectStatus.HIBERNATE);
            ProjectResponseDto responseDto = new ProjectResponseDto(projectId, "Updated", ProjectStatus.HIBERNATE, adminId);

            given(projectService.updateProject(eq(adminId), eq(projectId), any(ProjectUpdateDto.class))).willReturn(responseDto);

            mockMvc.perform(put("/projects/{projectId}", projectId)
                            .header("X-USER-ID", adminId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Updated"));
        }

        @Test
        @DisplayName("실패 - Validation")
        void updateProject_ValidationFailed() throws Exception {
            ProjectUpdateDto badDto = new ProjectUpdateDto("", ProjectStatus.ACTIVE);

            mockMvc.perform(put("/projects/{projectId}", projectId)
                            .header("X-USER-ID", adminId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(badDto)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("DELETE /projects/{projectId}")
    class DeleteProjectTests {

        @Test
        @DisplayName("성공")
        void deleteProject_Success() throws Exception {
            willDoNothing().given(projectService).deleteProject(adminId, projectId);

            mockMvc.perform(delete("/projects/{projectId}", projectId)
                            .header("X-USER-ID", adminId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }
    }

    @Nested
    @DisplayName("PATCH /projects/{projectId}/status")
    class UpdateProjectStatusTests {

        @Test
        @DisplayName("성공")
        void updateProjectStatus_Success() throws Exception {
            Map<String, String> requestMap = Map.of("status", "CLOSED");
            ProjectResponseDto responseDto = new ProjectResponseDto(projectId, "Test Project", ProjectStatus.CLOSED, adminId);

            given(projectService.updateProjectStatus(eq(adminId), eq(projectId), any(ProjectStatusUpdateDto.class)))
                    .willReturn(responseDto);

            mockMvc.perform(patch("/projects/{projectId}/status", projectId)
                            .header("X-USER-ID", adminId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestMap)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("CLOSED"));
        }
    }
}
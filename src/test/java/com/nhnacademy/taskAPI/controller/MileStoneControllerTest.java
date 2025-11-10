package com.nhnacademy.taskAPI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.taskAPI.dto.response.MileStoneResponseDto;
import com.nhnacademy.taskAPI.exception.GlobalExceptionHandler;
import com.nhnacademy.taskAPI.exception.MemberAccessDeniedException;
import com.nhnacademy.taskAPI.exception.MilestoneNotFoundException;
import com.nhnacademy.taskAPI.service.MileStoneService;

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

@WebMvcTest(controllers = {MileStoneController.class, GlobalExceptionHandler.class})
class MileStoneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MileStoneService mileStoneService;

    private final Long adminId = 1L;
    private final Long memberId = 101L;
    private final Long projectId = 1L;
    private final Long milestoneId = 1L;
    
    @Nested
    @DisplayName("POST /projects/{projectId}/milestones")
    class CreateMilestoneTests {

        @Test
        @DisplayName("성공")
        void createMilestone_Success() throws Exception {
            Map<String, String> requestMap = Map.of("name", "New Milestone");
            MileStoneResponseDto responseDto = new MileStoneResponseDto(milestoneId, "New Milestone");
            
            given(mileStoneService.createMilestone(eq(adminId), eq(projectId), any()))
                    .willReturn(responseDto);

            mockMvc.perform(post("/projects/{projectId}/milestones", projectId)
                            .header("X-USER-ID", adminId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestMap)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("New Milestone"));
        }
    }

    @Nested
    @DisplayName("GET /projects/{projectId}/milestones")
    class GetMilestonesTests {

        @Test
        @DisplayName("성공")
        void getMilestonesForProject_Success() throws Exception {
            MileStoneResponseDto responseDto = new MileStoneResponseDto(milestoneId, "Milestone");
            given(mileStoneService.findMilestonesByProjectId(memberId, projectId)).willReturn(List.of(responseDto));
            
            mockMvc.perform(get("/projects/{projectId}/milestones", projectId)
                            .header("X-USER-ID", memberId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1));
        }
    }

    @Nested
    @DisplayName("PUT /projects/{projectId}/milestones/{milestoneId}")
    class UpdateMilestoneTests {

        @Test
        @DisplayName("성공")
        void updateMilestone_Success() throws Exception {
            Map<String, String> requestMap = Map.of("name", "Updated");
            MileStoneResponseDto responseDto = new MileStoneResponseDto(milestoneId, "Updated");
            
            given(mileStoneService.updateMilestone(eq(adminId), eq(milestoneId), any()))
                    .willReturn(responseDto);

            mockMvc.perform(put("/projects/{projectId}/milestones/{milestoneId}", projectId, milestoneId)
                            .header("X-USER-ID", adminId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestMap)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Updated"));
        }

        @Test
        @DisplayName("실패 - Not Found")
        void updateMilestone_NotFound() throws Exception {
            Map<String, String> requestMap = Map.of("name", "Updated");
            
            given(mileStoneService.updateMilestone(eq(adminId), eq(999L), any()))
                    .willThrow(new MilestoneNotFoundException("Not Found"));
            
            mockMvc.perform(put("/projects/{projectId}/milestones/{milestoneId}", projectId, 999L)
                            .header("X-USER-ID", adminId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestMap)))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("DELETE /projects/{projectId}/milestones/{milestoneId}")
    class DeleteMilestoneTests {
        
        @Test
        @DisplayName("성공")
        void deleteMilestone_Success() throws Exception {
            willDoNothing().given(mileStoneService).deleteMilestone(adminId, milestoneId);

            mockMvc.perform(delete("/projects/{projectId}/milestones/{milestoneId}", projectId, milestoneId)
                            .header("X-USER-ID", adminId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }
    }
}
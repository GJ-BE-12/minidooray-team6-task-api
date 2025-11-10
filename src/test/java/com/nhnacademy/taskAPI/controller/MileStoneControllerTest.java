package com.nhnacademy.taskAPI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.taskAPI.dto.response.MileStoneResponseDto;
import com.nhnacademy.taskAPI.exception.GlobalExceptionHandler;
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
    @DisplayName("POST /projects/{projectId}/milestones (마일스톤 생성)")
    class CreateMilestoneTests {

        @Test
        @DisplayName("성공 (200 OK)")
        void createMilestone_Success() throws Exception {
            Map<String, String> requestMap = Map.of("name", "New Milestone");
            MileStoneResponseDto responseDto = new MileStoneResponseDto(milestoneId, "New Milestone");
            
            given(mileStoneService.createMilestone(eq(adminId), eq(projectId), any()))
                    .willReturn(responseDto);

            mockMvc.perform(post("/projects/{projectId}/milestones", projectId)
                            .header("X-USER-ID", adminId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestMap)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("New Milestone"));
        }
    }

    @Nested
    @DisplayName("GET /projects/{projectId}/milestones (마일스톤 목록 조회)")
    class GetMilestonesTests {

        @Test
        @DisplayName("성공 (200 OK)")
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
    @DisplayName("PUT /milestones/{milestoneId} (마일스톤 수정)")
    class UpdateMilestoneTests {

        @Test
        @DisplayName("성공 (200 OK)")
        void updateMilestone_Success() throws Exception {
            Map<String, String> requestMap = Map.of("name", "Updated");
            MileStoneResponseDto responseDto = new MileStoneResponseDto(milestoneId, "Updated");
            
            given(mileStoneService.updateMilestone(eq(adminId), eq(milestoneId), any()))
                    .willReturn(responseDto);

            mockMvc.perform(put("/milestones/{milestoneId}", milestoneId)
                            .header("X-USER-ID", adminId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestMap)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Updated"));
        }
    }

    @Nested
    @DisplayName("DELETE /milestones/{milestoneId} (마일스톤 삭제)")
    class DeleteMilestoneTests {
        
        @Test
        @DisplayName("성공 (204 No Content)")
        void deleteMilestone_Success() throws Exception {
            willDoNothing().given(mileStoneService).deleteMilestone(adminId, milestoneId);

            mockMvc.perform(delete("/milestones/{milestoneId}", milestoneId)
                            .header("X-USER-ID", adminId))
                    .andExpect(status().isNoContent());
        }
    }
}
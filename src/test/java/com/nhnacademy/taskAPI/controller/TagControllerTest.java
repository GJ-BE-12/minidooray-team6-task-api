package com.nhnacademy.taskAPI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.taskAPI.dto.request.TagCreateRequestDto;
import com.nhnacademy.taskAPI.dto.request.TagUpdateRequestDto;
import com.nhnacademy.taskAPI.dto.response.TagResponseDto;
import com.nhnacademy.taskAPI.exception.GlobalExceptionHandler;
import com.nhnacademy.taskAPI.exception.TagNotFoundException;
import com.nhnacademy.taskAPI.service.TagService;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {TagController.class, GlobalExceptionHandler.class})
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TagService tagService;

    private final Long userId = 101L;
    private final Long projectId = 1L;
    private final Long tagId = 1L;

    @Nested
    @DisplayName("POST /projects/{projectId}/tags")
    class CreateTagTests {
        
        @Test
        @DisplayName("성공")
        void createTag_Success() throws Exception {
            TagCreateRequestDto requestDto = new TagCreateRequestDto("New Tag");
            TagResponseDto responseDto = new TagResponseDto(tagId, "New Tag");
            
            given(tagService.createTag(eq(userId), eq(projectId), any(TagCreateRequestDto.class)))
                    .willReturn(responseDto);

            mockMvc.perform(post("/projects/{projectId}/tags", projectId)
                            .header("X-USER-ID", userId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("New Tag"));
        }
    }

    @Nested
    @DisplayName("GET /projects/{projectId}/tags/{tagId}")
    class GetTagByProjectIdTests {
        
        @Test
        @DisplayName("성공")
        void getTagByProjectId_Success() throws Exception {
            TagResponseDto responseDto = new TagResponseDto(tagId, "Tag");
            given(tagService.getTagbyProject(userId, projectId)).willReturn(List.of(responseDto));
            
            mockMvc.perform(get("/projects/{projectId}/tags/{tagId}", projectId, tagId)
                            .header("X-USER-ID", userId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1));
        }
    }

    @Nested
    @DisplayName("PUT /projects/{projectId}/tags/{tagId}")
    class UpdateTagTests {
        
        @Test
        @DisplayName("성공")
        void updateTag_Success() throws Exception {
            TagUpdateRequestDto requestDto = new TagUpdateRequestDto("Updated");
            TagResponseDto responseDto = new TagResponseDto(tagId, "Updated");
            
            given(tagService.updateTag(eq(userId), eq(tagId), any(TagUpdateRequestDto.class)))
                    .willReturn(responseDto);

            mockMvc.perform(put("/projects/{projectId}/tags/{tagId}", projectId, tagId)
                            .header("X-USER-ID", userId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Updated"));
        }

        @Test
        @DisplayName("실패 - Not Found")
        void updateTag_NotFound() throws Exception {
            TagUpdateRequestDto requestDto = new TagUpdateRequestDto("Updated");
            
            given(tagService.updateTag(eq(userId), eq(999L), any(TagUpdateRequestDto.class)))
                    .willThrow(new TagNotFoundException("Not Found"));
            
            mockMvc.perform(put("/projects/{projectId}/tags/{tagId}", projectId, 999L)
                            .header("X-USER-ID", userId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("DELETE /projects/{projectId}/tags/{tagId}")
    class DeleteTagTests {
        
        @Test
        @DisplayName("성공")
        void deleteTag_Success() throws Exception {
            willDoNothing().given(tagService).deleteTag(userId, tagId);

            mockMvc.perform(delete("/projects/{projectId}/tags/{tagId}", projectId, tagId)
                            .header("X-USER-ID", userId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }
    }
}
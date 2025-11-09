package com.nhnacademy.taskAPI.controller;

import com.nhnacademy.taskAPI.dto.request.TagCreateRequestDto;
import com.nhnacademy.taskAPI.dto.request.TagUpdateRequestDto;
import com.nhnacademy.taskAPI.dto.response.TagResponseDto;
import com.nhnacademy.taskAPI.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class TagController {
    private final TagService tagService;

    @PostMapping("/projects/{projectId}/tags")
    public ResponseEntity<TagResponseDto> createTag(@PathVariable Long projectId,
                                                    @RequestBody TagCreateRequestDto requestDto,
                                                    @RequestHeader("X-USER-ID") Long userId) {

        TagResponseDto responseDto = tagService.createTag(userId, projectId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/tags/{tagId}")
    public ResponseEntity<TagResponseDto> updateTag(@PathVariable Long tagId,
                                                    @RequestBody TagUpdateRequestDto requestDto,
                                                    @RequestHeader("X-USER-ID") Long userId) {

        TagResponseDto responseDto = tagService.updateTag(userId, tagId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/tags/{tagId}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long tagId,
                                          @RequestHeader("X-USER-ID") Long userId) {
        tagService.deleteTag(userId, tagId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tags/{projectId}")
    public List<TagResponseDto> getTagByProjectId(@PathVariable Long projectId,
                                                  @RequestHeader("X-USER-ID") Long userId) {

        return tagService.getTagbyProject(userId, projectId);
    }


}

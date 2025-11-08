package com.nhnacademy.taskAPI.service;

import com.nhnacademy.taskAPI.dto.request.TagCreateRequest;
import com.nhnacademy.taskAPI.dto.request.TagUpdateRequest;
import com.nhnacademy.taskAPI.dto.response.TagResponseDto;

public interface TagService {
    TagResponseDto createTag(Long userId, Long projectId, TagCreateRequest requestDto);


    void deleteTag(Long userId, Long tagId);

    TagResponseDto updateTag(Long userId, Long tagId, TagUpdateRequest requestDto);
}

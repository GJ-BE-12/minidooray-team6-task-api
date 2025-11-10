package com.nhnacademy.taskAPI.service;

import com.nhnacademy.taskAPI.dto.request.TagCreateRequestDto;
import com.nhnacademy.taskAPI.dto.request.TagUpdateRequestDto;
import com.nhnacademy.taskAPI.dto.response.TagResponseDto;

import java.util.List;

public interface TagService {
    TagResponseDto createTag(Long userId, Long projectId, TagCreateRequestDto requestDto);

    void deleteTag(Long userId, Long tagId);

    TagResponseDto updateTag(Long userId, Long tagId, TagUpdateRequestDto requestDto);

    List<TagResponseDto> getTagbyProject(Long userId, Long projectId);
}

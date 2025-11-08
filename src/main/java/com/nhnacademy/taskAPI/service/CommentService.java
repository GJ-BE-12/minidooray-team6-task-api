package com.nhnacademy.taskAPI.service;

import com.nhnacademy.taskAPI.dto.request.CommentCreateRequestDto;
import com.nhnacademy.taskAPI.dto.request.CommentUpdateDto;
import com.nhnacademy.taskAPI.dto.response.CommentResponseDto;

import java.util.List;

public interface CommentService {

    CommentResponseDto createComment(Long accountId, Long taskId, CommentCreateRequestDto requestDto);
    List<CommentResponseDto> findCommentsByTaskId(Long accountId, Long taskId);
    CommentResponseDto updateComment(Long accountId, Long commentId, CommentUpdateDto requestDto);
    void deleteComment(Long accountId, Long commentId);
}
package com.nhnacademy.taskAPI.controller;

import com.nhnacademy.taskAPI.dto.request.CommentCreateRequestDto;
import com.nhnacademy.taskAPI.dto.request.CommentUpdateDto;
import com.nhnacademy.taskAPI.dto.response.CommentResponseDto;
import com.nhnacademy.taskAPI.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/projects/{projectId}/tasks/{taskId}/comments")
    public ResponseEntity<CommentResponseDto> createComment(
            @RequestHeader("X-USER-ID") Long accountId,
            @PathVariable Long taskId,
            @Valid @RequestBody CommentCreateRequestDto requestDto) {

        CommentResponseDto createdComment = commentService.createComment(accountId, taskId, requestDto);

        URI location = URI.create(String.format("/comments/%d", createdComment.getId()));
        return ResponseEntity.created(location).body(createdComment);
    }

    @GetMapping("/projects/{projectId}/tasks/{taskId}/comments")
    public ResponseEntity<List<CommentResponseDto>> getCommentsForTask(
            @RequestHeader("X-USER-ID") Long accountId,
            @PathVariable Long taskId) {

        List<CommentResponseDto> comments = commentService.findCommentsByTaskId(accountId, taskId);
        return ResponseEntity.ok(comments);
    }

    @PutMapping("/projects/{projectId}/tasks/{taskId}/comments/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @RequestHeader("X-USER-ID") Long accountId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateDto requestDto) {

        CommentResponseDto updatedComment = commentService.updateComment(accountId, commentId, requestDto);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/projects/{projectId}/tasks/{taskId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @RequestHeader("X-USER-ID") Long accountId,
            @PathVariable Long commentId) {

        commentService.deleteComment(accountId, commentId);
        return ResponseEntity.noContent().build();
    }
}
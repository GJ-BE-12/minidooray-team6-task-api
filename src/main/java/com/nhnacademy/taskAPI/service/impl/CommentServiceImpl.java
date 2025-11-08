package com.nhnacademy.taskAPI.service.impl;

import com.nhnacademy.taskAPI.dto.request.CommentCreateRequestDto;
import com.nhnacademy.taskAPI.dto.request.CommentUpdateDto;
import com.nhnacademy.taskAPI.dto.response.CommentResponseDto;
import com.nhnacademy.taskAPI.entity.Comment;
import com.nhnacademy.taskAPI.entity.Task;
import com.nhnacademy.taskAPI.repository.CommentRepository;
import com.nhnacademy.taskAPI.repository.ProjectMemberRepository;
import com.nhnacademy.taskAPI.repository.TaskRepository;
import com.nhnacademy.taskAPI.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Override
    public CommentResponseDto createComment(Long accountId, Long taskId, CommentCreateRequestDto requestDto) {
        Task task = checkAccessAndGetTask(accountId, taskId);

        Comment comment = new Comment(task, accountId, requestDto.getContent());
        Comment saveComment = commentRepository.save(comment);

        return CommentResponseDto.fromEntity(saveComment);
    }

    @Override
    public List<CommentResponseDto> findCommentsByTaskId(Long accountId, Long taskId) {
        checkAccessAndGetTask(accountId, taskId);

        List<Comment> comments = commentRepository.findByTaskId(taskId);

        return comments.stream()
                .map(CommentResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CommentResponseDto updateComment(Long accountId, Long commentId, CommentUpdateDto requestDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("없는 댓글입니다."));
        if(!comment.getWriterId().equals(accountId)){
            throw new RuntimeException("댓글은 본인만 수정할 수 있어요");
        }
        comment.updateComment(requestDto.getContent());

        return CommentResponseDto.fromEntity(comment);
    }

    @Override
    public void deleteComment(Long accountId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

        Long adminId = comment.getTask().getProject().getAdminId();
        boolean isAdmin = adminId.equals(accountId);
        boolean isWriter = comment.getWriterId().equals(accountId);

        if (!isAdmin && !isWriter) {
            throw new RuntimeException("댓글 작성자 또는 프로젝트 관리자만 삭제할 수 있습니다.");
        }
        commentRepository.delete(comment);
    }

    private Task checkAccessAndGetTask(Long accountId, Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("테스크를 찾을 수 없습니다."));

        // 태스크가 속한 프로젝트의 멤버인지 확인
        if (!projectMemberRepository.existsByProjectIdAndAccountId(task.getProject().getId(), accountId)) {
            throw new RuntimeException("테스크의 댓글에 접근할 권한이 없습니다.");
        }
        return task;
    }
}

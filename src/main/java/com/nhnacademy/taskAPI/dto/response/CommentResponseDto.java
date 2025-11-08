package com.nhnacademy.taskAPI.dto.response;

import com.nhnacademy.taskAPI.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {
    private Long id;
    private Long writerId;
    private Long taskId;
    private String content;

    public static CommentResponseDto fromEntity(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getTask().getId(),
                comment.getWriterId(),
                comment.getContent()
        );
    }
}

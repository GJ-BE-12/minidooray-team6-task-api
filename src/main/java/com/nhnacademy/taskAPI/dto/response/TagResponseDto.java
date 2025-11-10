package com.nhnacademy.taskAPI.dto.response;

import com.nhnacademy.taskAPI.entity.Tag;
import com.nhnacademy.taskAPI.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * tag의 정보를 담고 ProjectDetailsDto 내의 Tage 목록에 사용
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagResponseDto {
    private Long id;
    private String name;

    public static TagResponseDto fromEntity(Tag tag) {
        return new TagResponseDto(
                tag.getId(),
                tag.getName()
        );
    }
}

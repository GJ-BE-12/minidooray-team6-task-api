package com.nhnacademy.taskAPI.dto.response;

import com.nhnacademy.taskAPI.entity.Milestone;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MileStoneResponseDto {
    private Long id;
    private String name;


    public static MileStoneResponseDto fromEntity(Milestone milestone) {
        return new MileStoneResponseDto(
                milestone.getId(),
                milestone.getName()
        );
    }
}

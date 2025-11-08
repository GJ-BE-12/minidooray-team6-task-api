package com.nhnacademy.taskAPI.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "task_tag", uniqueConstraints = {
        // UNIQUE KEY(uk_task_tag) 설정
        @UniqueConstraint(
                name = "uk_task_tag",
                columnNames = {"task_id", "tag_id"}
        )
})
public class TaskTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    /**
     * Service에서 태그-태스크 매핑을 위한 생성자
     */
    public TaskTag(Task task, Tag tag) {
        this.task = task;
        this.tag = tag;
    }
}
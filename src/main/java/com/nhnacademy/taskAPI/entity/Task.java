package com.nhnacademy.taskAPI.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener; // 1. Auditing 리스너 import

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "task")
@EntityListeners(AuditingEntityListener.class)
public class Task extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "milestone_id")
    private Milestone milestone;

    @NotNull
    @Column(name = "creator_account_id")
    private Long creatorAccountId;

    @NotNull
    @Column(name = "title")
    private String title;

    @Lob
    @Column(name = "content")
    private String content;

    @OneToMany(mappedBy = "task", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<TaskTag> taskTags = new ArrayList<>();

    @OneToMany(mappedBy = "task", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();


    public Task(Project project, Long creatorAccountId, String title, String content, Milestone milestone) {
        this.project = project;
        this.creatorAccountId = creatorAccountId;
        this.title = title;
        this.content = content;
        this.milestone = milestone;
    }
}

package com.nhnacademy.taskAPI.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "comment")
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @NotNull
    @Column(name = "creator_account_id")
    private Long creatorAccountId;

    @NotNull
    @Lob
    @Column(name = "content")
    private String content;

}
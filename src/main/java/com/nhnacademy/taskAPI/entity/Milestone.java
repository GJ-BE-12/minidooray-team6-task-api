package com.nhnacademy.taskAPI.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "milestone")

public class Milestone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "milestone")
    private List<Task> tasks = new ArrayList<>();

    public Milestone(Project project, String name) {
        this.project = project;
        this.name = name;
    }

    public void updateMilestone(String name){
        this.name = name;
    }

    @PreRemove
    public void preRemove() {
        for (Task task : tasks) {
            task.setMilestone(null);
        }
    }
}
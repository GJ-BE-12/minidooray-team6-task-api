package com.nhnacademy.taskAPI.service.impl;

import com.nhnacademy.taskAPI.dto.request.TagCreateRequestDto;
import com.nhnacademy.taskAPI.dto.request.TagUpdateRequestDto;
import com.nhnacademy.taskAPI.dto.response.TagResponseDto;
import com.nhnacademy.taskAPI.entity.Project;
import com.nhnacademy.taskAPI.entity.Tag;
import com.nhnacademy.taskAPI.repository.ProjectMemberRepository;
import com.nhnacademy.taskAPI.repository.ProjectRepository;
import com.nhnacademy.taskAPI.repository.TagRepository;
import com.nhnacademy.taskAPI.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;
    private final TagRepository tagRepository;
    private final ProjectAuthService projectAuthService;

    @Override
    public TagResponseDto createTag(Long userId, Long projectId, TagCreateRequestDto requestDto) {
        projectAuthService.existUserId(userId,projectId);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("프로젝트를 찾을 수 없습니다: " + projectId));

        Tag newTag = new Tag(project, requestDto.getName());
        Tag savedTag = tagRepository.save(newTag);

        return new TagResponseDto(savedTag.getId(), savedTag.getName());
    }

    @Override
    public void deleteTag(Long userId, Long tagId) {
        Tag tag = tagRepository.findById(tagId).orElseThrow(RuntimeException::new);
        long projectId = tag.getProject().getId();
        projectAuthService.existUserId(userId,projectId);

        tagRepository.deleteById(tagId);
    }

    @Transactional
    @Override
    public TagResponseDto updateTag(Long userId, Long tagId, TagUpdateRequestDto requestDto) {
        Tag tag = tagRepository.findById(tagId).orElseThrow(RuntimeException::new);
        long projectId = tag.getProject().getId();
        projectAuthService.existUserId(userId, projectId);

        tag.setName(requestDto.getName());
        return new TagResponseDto(tag.getId(), tag.getName());
    }

    public List<TagResponseDto> getTagbyProject(Long userId, Long projectId) {
        projectAuthService.existUserId(userId,projectId);

        List<Tag> tags = tagRepository.findByProjectId(projectId);
        List<TagResponseDto> responseDtos = new ArrayList<>();

        for (Tag t : tags) {
            responseDtos.add(new TagResponseDto(t.getId(), t.getName()));
        }

        return responseDtos;
    }
}

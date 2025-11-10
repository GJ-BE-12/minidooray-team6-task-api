package com.nhnacademy.taskAPI.service.impl;

import com.nhnacademy.taskAPI.dto.response.*;
import com.nhnacademy.taskAPI.entity.*;
import com.nhnacademy.taskAPI.dto.request.ProjectCreateRequestDto;
import com.nhnacademy.taskAPI.dto.request.ProjectStatusUpdateDto;
import com.nhnacademy.taskAPI.dto.request.ProjectUpdateDto;
import com.nhnacademy.taskAPI.exception.MemberAccessDeniedException;
import com.nhnacademy.taskAPI.exception.ProjectNotFoundException;
import com.nhnacademy.taskAPI.repository.*;
import com.nhnacademy.taskAPI.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final MilestoneRepository milestoneRepository;
    private final TaskRepository taskRepository;
    private final TagRepository tagRepository;


    @Override
    @Transactional
    public ProjectResponseDto createProject(Long adminId, ProjectCreateRequestDto requestDto) {

        Project project = new Project(requestDto.getName(), adminId);
        Project savedProject = projectRepository.save(project);

        ProjectMember adminAsMember = new ProjectMember(savedProject, adminId);
        projectMemberRepository.save(adminAsMember);

        return ProjectResponseDto.fromEntity(savedProject);
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectDetailsDto getProject(Long accountId, Long projectId) { // 1. 반환 타입 변경

        // 2. 기존 권한 검사 (유지)
        if (!projectMemberRepository.existsByProjectIdAndAccountId(projectId, accountId)) {
            throw new MemberAccessDeniedException("이 프로젝트에 접근할 권한이 없습니다.");
        }

        // 3. 기본 프로젝트 정보 조회 (유지)
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("프로젝트를 찾을 수 없습니다."));

        // --- 4. ProjectDetailsDto 구성을 위한 추가 정보 조회 (새 로직) ---

        // 4-1. 프로젝트 기본 정보 DTO
        ProjectResponseDto projectDto = ProjectResponseDto.fromEntity(project);

        // 4-2. 마일스톤 조회 (프로젝트 ID로 조회)
        //    (프로젝트와 마일스톤 관계가 1:1 또는 1:N 중 첫번째라고 가정)
        List<Milestone> milestones = milestoneRepository.findByProjectId(projectId); // 예시 메소드
        List<MileStoneResponseDto> milestonesDtos = milestones.stream()
                .map(MileStoneResponseDto::fromEntity) // 각 Milestone 객체를 MileStoneResponseDto로 변환
                .collect(Collectors.toList());



        // 4-3. 태스크 목록 조회 (프로젝트 ID로 조회)
        List<Task> tasks = taskRepository.findByProjectId(projectId); // 예시 메소드
        List<TaskResponseDto> taskDtos = tasks.stream()
                .map(TaskResponseDto::fromEntity)
                .collect(Collectors.toList());

        // 4-4. 태그 목록 조회 (프로젝트 ID로 조회)
        // (Tag가 Task에만 붙어있다면, tasks에서 tag를 추출해야 할 수도 있습니다)
        List<Tag> tags = tagRepository.findByProjectId(projectId); // 예시 메소드
        List<TagResponseDto> tagDtos = tags.stream()
                .map(TagResponseDto::fromEntity)
                .collect(Collectors.toList());

        // 4-5. 멤버 목록 조회 (프로젝트 ID로 조회)
        List<ProjectMember> members = projectMemberRepository.findByProjectId(projectId);
        List<Long> memberDtos = members.stream()
                .map(ProjectMember::getAccountId) // ProjectMember가 Account 엔티티를 가지고 있다고 가정
//                .map(AccountRDto::fromEntity)
                .collect(Collectors.toList());
        // --- 추가 로직 끝 ---


        // 5. 최종 DTO 조립 및 반환
        return new ProjectDetailsDto(
                projectDto,
                milestonesDtos,
                taskDtos,
                tagDtos,
                memberDtos
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponseDto> getMyProjects(Long accountId) {

        List<ProjectMember> memberships = projectMemberRepository.findByAccountId(accountId);

        return memberships.stream()
                .map(ProjectMember::getProject)
                .map(ProjectResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProjectResponseDto updateProject(Long adminId, Long projectId, ProjectUpdateDto requestDto) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("프로젝트를 찾을 수 없습니다."));

        if (!project.getAdminId().equals(adminId)) {
            throw new MemberAccessDeniedException("프로젝트 관리자만 수정할 수 있습니다.");
        }

        project.updateProject(requestDto.getName(), requestDto.getStatus());

        return ProjectResponseDto.fromEntity(project);
    }

    @Override
    @Transactional
    public void deleteProject(Long adminId, Long projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("프로젝트를 찾을 수 없습니다."));

        if (!project.getAdminId().equals(adminId)) {
            throw new MemberAccessDeniedException("프로젝트 관리자만 삭제할 수 있습니다.");
        }

        projectRepository.delete(project);
    }

    @Override
    @Transactional
    public ProjectResponseDto updateProjectStatus(Long adminId, Long projectId, ProjectStatusUpdateDto requestDto) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("프로젝트를 찾을 수 없습니다."));

        if (!project.getAdminId().equals(adminId)) {
            throw new MemberAccessDeniedException("프로젝트 관리자만 상태를 수정할 수 있습니다.");
        }

        project.updateStatus(requestDto.getStatus());

        return ProjectResponseDto.fromEntity(project);
    }
}
package com.nhnacademy.taskAPI.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class AccountApiClient {

    private final RestTemplate restTemplate;

    @Value("${api.account.url}")
    private String accountApiUrl;

    public boolean isProjectMember(Long projectId, Long accountId) {
        String url = String.format("%s/projects/%d/members/%d", accountApiUrl, projectId, accountId);

        try {
            restTemplate.getForEntity(url, Void.class);
            return true;
        } catch (HttpClientErrorException e) {
//            에러 수정
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public List<Long> getProjectIdsByAccountId(Long accountId) {
        String url = String.format("%s/accounts/%d/projects", accountApiUrl, accountId);

        try {
            ResponseEntity<List<Long>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Long>>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public void addMemberToProject(Long projectId, Long accountIdToAdd) {
        String url = String.format("%s/projects/%d/members", accountApiUrl, projectId);
        
        Map<String, Long> requestBody = Collections.singletonMap("accountId", accountIdToAdd);

        try {
            restTemplate.postForObject(url, requestBody, Void.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                throw new RuntimeException("이미 해당 프로젝트의 멤버입니다.");
//                exception 상황에 맞게 추가
            }
            throw new RuntimeException("AccountApi 멤버 추가 호출 실패: " + e.getMessage());
        }
    }
}
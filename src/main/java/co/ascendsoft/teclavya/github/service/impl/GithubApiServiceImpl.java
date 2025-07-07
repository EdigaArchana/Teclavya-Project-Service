package co.ascendsoft.teclavya.github.service.impl;

import co.ascendsoft.teclavya.exceptions.models.BadRequestException;
import co.ascendsoft.teclavya.github.dto.GitHubUserAccessTokenResponse;
import co.ascendsoft.teclavya.github.entities.GitHubUserAccessToken;
import co.ascendsoft.teclavya.github.repo.GitHubUserAccessTokenRepository;
import co.ascendsoft.teclavya.github.service.GithubApiService;
import co.ascendsoft.teclavya.user.entities.User;
import co.ascendsoft.teclavya.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class GithubApiServiceImpl implements GithubApiService {

    @Value("${app.github.client-id}")
    private String CLIENT_ID;
    @Value("${app.github.client-secret}")
    private String CLIENT_SECRET;

    private final RestTemplate restTemplate;
    private final GitHubUserAccessTokenRepository githubUserAccessTokenRepository;
    private final UserRepository userRepository;

    public void createGitHubRepo(String githubToken, String repoName) {
        String apiUrl = "https://api.github.com/user/repos";

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.valueOf("application/vnd.github+json")));
        headers.setBearerAuth(githubToken);
        headers.set("X-GitHub-Api-Version", "2022-11-28");

        // Request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", repoName);
        requestBody.put("private", true);

        // Prepare request
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Send POST request
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, requestEntity, String.class);
            System.out.println("Response status: " + response.getStatusCode());
            System.out.println("Response body: " + response.getBody());
        } catch (Exception e) {
            System.err.println("Failed to create repo: " + e.getMessage());
        }
    }

    public void refreshAccessToken(String refreshToken, String userId) {
        String url = "https://github.com/login/oauth/access_token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", CLIENT_ID);
        body.add("client_secret", CLIENT_SECRET);
        body.add("grant_type", "refresh_token");
        body.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<GitHubUserAccessTokenResponse> response = restTemplate.postForEntity(
                url,
                requestEntity,
                GitHubUserAccessTokenResponse.class
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            GitHubUserAccessTokenResponse tokenResponse = response.getBody();

            User user=userRepository.findByUserId(userId).get();

            GitHubUserAccessToken userAccessToken = githubUserAccessTokenRepository.findByUser(user)
                    .orElseThrow(() -> new BadRequestException("Access token for user not found"));

            userAccessToken.setAccessToken(tokenResponse.access_token());
            userAccessToken.setExpiresAt(tokenResponse.expires_in() != null
                    ? LocalDateTime.now().plusSeconds(tokenResponse.expires_in())
                    : null);
            userAccessToken.setRefreshToken(tokenResponse.refresh_token());
            userAccessToken.setRefreshTokenExpiresAt(tokenResponse.refresh_token_expires_in() != null
                    ? LocalDateTime.now().plusSeconds(tokenResponse.refresh_token_expires_in())
                    : null);

            githubUserAccessTokenRepository.save(userAccessToken);
        } else {
            log.error("GitHub token refresh failed. Status: {}, Response: {}",
                    response.getStatusCode(), response.getBody());
        }
    }
}

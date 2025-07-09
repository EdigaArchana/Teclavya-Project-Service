package co.ascendsoft.teclavya.github.service.impl;

import co.ascendsoft.teclavya.github.dto.GitHubUserAccessTokenResponse;
import co.ascendsoft.teclavya.github.entities.GitHubUserAccessToken;
import co.ascendsoft.teclavya.github.repo.GitHubUserAccessTokenRepository;
import co.ascendsoft.teclavya.github.service.OauthService;
import co.ascendsoft.teclavya.user.entities.User;
import co.ascendsoft.teclavya.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OauthServiceImpl implements OauthService {

    @Value("${app.github.client-id}")
    private String CLIENT_ID;
    @Value("${app.github.client-secret}")
    private String CLIENT_SECRET;

    private static final String TOKEN_URL = "https://github.com/login/oauth/access_token";

    private final RestTemplate restTemplate;
    private final GitHubUserAccessTokenRepository githubUserAccessTokenRepository;
    private final UserService userService;


    @Override
    public String getAuthorizationUrl() {
        return "https://github.com/login/oauth/authorize?client_id="+CLIENT_ID;
    }

    @Override
    public void handleCallback(String code, String userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("client_id", CLIENT_ID);
        form.add("client_secret", CLIENT_SECRET);
        form.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);

        ResponseEntity<GitHubUserAccessTokenResponse> response = restTemplate.postForEntity(
                TOKEN_URL,
                request,
                GitHubUserAccessTokenResponse.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            GitHubUserAccessTokenResponse tokenResponse = response.getBody();
            User user = userService.findOrCreateUser(userId);

            GitHubUserAccessToken userAccessToken = GitHubUserAccessToken.builder()
                    .accessToken(tokenResponse.access_token())
                    .expiresAt(tokenResponse.expires_in() != null
                            ? LocalDateTime.now().plusSeconds(tokenResponse.expires_in())
                            : null)
                    .refreshToken(tokenResponse.refresh_token())
                    .refreshTokenExpiresAt(tokenResponse.refresh_token_expires_in() != null
                            ? LocalDateTime.now().plusSeconds(tokenResponse.refresh_token_expires_in())
                            : null)
                    .user(user)
                    .build();

            githubUserAccessTokenRepository.save(userAccessToken);
        }
    }
}

package co.ascendsoft.teclavya.github.controller;

import co.ascendsoft.teclavya.github.service.OauthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
@Validated
@Slf4j
public class OauthController {

    private final OauthService oauthService;

    @GetMapping("/github")
    public ResponseEntity<Void> redirectToGithub(
            @RequestParam("userId") String userId,
            HttpServletResponse response
    ) {
        String authorizationUrl = oauthService.getAuthorizationUrl();

        Cookie cookie = new Cookie("userId", userId);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(300);
        response.addCookie(cookie);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(authorizationUrl))
                .build();
    }

    @GetMapping("/github/callback")
    public ResponseEntity<Void> githubCallback(
            @RequestParam("code") String code,
            @CookieValue(value = "userId", required = false) String userId
    ) {
        oauthService.handleCallback(code, userId);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("https://www.google.com"))
                .build();
    }

}

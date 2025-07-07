package co.ascendsoft.teclavya.github.dto;

public record GitHubUserAccessTokenResponse(
        String access_token,
        Integer expires_in,
        String refresh_token,
        Integer refresh_token_expires_in
) {
}

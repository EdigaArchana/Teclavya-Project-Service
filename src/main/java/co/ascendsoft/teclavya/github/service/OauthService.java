package co.ascendsoft.teclavya.github.service;

public interface OauthService {
    String getAuthorizationUrl();

    void handleCallback(String code, String userId);
}

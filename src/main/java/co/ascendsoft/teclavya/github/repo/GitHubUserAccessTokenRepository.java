package co.ascendsoft.teclavya.github.repo;

import co.ascendsoft.teclavya.github.entities.GitHubUserAccessToken;
import co.ascendsoft.teclavya.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GitHubUserAccessTokenRepository extends JpaRepository<GitHubUserAccessToken, Long> {
    Optional<GitHubUserAccessToken> findByUser(User user);
}
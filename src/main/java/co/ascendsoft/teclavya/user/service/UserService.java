package co.ascendsoft.teclavya.user.service;

import co.ascendsoft.teclavya.user.entities.User;
import co.ascendsoft.teclavya.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public User findOrCreateUser(String userId) {
        return userRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .userId(userId)
                            .build();
                    return userRepository.save(newUser);
                });
    }
}

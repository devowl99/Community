package community.community.Service;

import community.community.Domain.User;
import community.community.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(String username, String password) {
        // 사용자 이름 중복 확인
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }

        // 사용자 저장
        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // 이후 Spring Security의 BCryptPasswordEncoder를 통해 암호화 가능
        return userRepository.save(user);
    }

    public User login(String username, String password) {
        // 사용자 인증
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty() || !userOptional.get().getPassword().equals(password)) {
            throw new IllegalArgumentException("로그인 정보가 잘못되었습니다.");
        }

        return userOptional.get();
    }
}

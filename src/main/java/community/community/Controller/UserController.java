package community.community.Controller;

import community.community.DTO.UserResponse;
import community.community.Domain.User;
import community.community.Service.UserService;
import jakarta.servlet.http.HttpSession; // 세션 활용
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 회원가입
     * POST api/user/register
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        userService.register(user.getUsername(), user.getPassword());
        return ResponseEntity.ok("회원가입 성공");
    }

    /**
     * 로그인
     * POST api/user/login
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody User user,
            HttpSession session
    ) {
        User loggedInUser = userService.login(user.getUsername(), user.getPassword());
        session.setAttribute("user", loggedInUser); // 세션에 사용자 저장
        return ResponseEntity.ok("로그인 성공");
    }

    /**
     * 로그아웃
     * POST api/user/logout
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return ResponseEntity.ok("로그아웃 성공");
    }

    /**
     * 마이페이지 조회
     * GET /api/user/mypage
     */
    @GetMapping("/mypage")
    public ResponseEntity<?> getMyPage(HttpSession session) {
        var user = session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }

        var loggedInUser = (User) user;
        var userResponse = new UserResponse(loggedInUser.getId(), loggedInUser.getUsername());
        return ResponseEntity.ok(userResponse);
    }
}

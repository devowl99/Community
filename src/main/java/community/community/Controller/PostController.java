package community.community.Controller;

import community.community.DTO.DetailedPostResponse;
import community.community.DTO.PostRequest;
import community.community.DTO.PostResponse;
import community.community.Domain.Post;
import community.community.Domain.User;
import community.community.Service.PostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/post")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * 게시글 생성
     * POST /api/post
     * @param postRequest
     * @param session
     * @return
     */
    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostRequest postRequest, HttpSession session) {
        var user = session.getAttribute("user"); // var : Java 10에서 도입된 지역 변수 타입 추론 키워드 (명시적 타입 지정 X)
        if (user == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }
        var loggedInUser = (community.community.Domain.User) user;
        postService.createPost(postRequest.getTitle(), postRequest.getContent(), loggedInUser.getId());
        return ResponseEntity.ok("게시글이 성공적으로 생성되었습니다.");
    }

    /**
     * 전체 게시글 조회
     * GET /api/post
     * @return
     */
    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<PostResponse> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    /**
     * 특정 게시글 조회
     * GET /api/post/{id}
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<DetailedPostResponse> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    /**
     * 내 게시글 조회
     * GET /api/post/my
     * @param session
     * @return
     */
    @GetMapping("/my")
    public ResponseEntity<List<PostResponse>> getMyPosts(HttpSession session) {
        var user = session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body(null); // 로그인 필요
        }
        var loggedInUser = (User) user;
        List<PostResponse> myPosts = postService.getPostsByUserId(loggedInUser.getId());
        return ResponseEntity.ok(myPosts);
    }

    /**
     * 게시글 수정
     * PUT /api/post/{id}
     * @param id
     * @param postRequest
     * @param session
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(
            @PathVariable Long id,
            @RequestBody PostRequest postRequest,
            HttpSession session) {
        var user = session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }
        var loggedInUser = (User) user;
        postService.updatePost(id, postRequest.getTitle(), postRequest.getContent(), loggedInUser.getId());
        return ResponseEntity.ok("게시글이 성공적으로 수정되었습니다.");
    }

    /**
     * 게시글 삭제
     * DELETE /api/post/{id}
     * @param id
     * @param session
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id, HttpSession session) {
        var user = session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }
        var loggedInUser = (User) user;
        postService.deletePost(id, loggedInUser.getId());
        return ResponseEntity.ok("게시글이 성공적으로 삭제되었습니다.");
    }

}

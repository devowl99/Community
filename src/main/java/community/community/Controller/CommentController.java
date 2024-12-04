package community.community.Controller;

import community.community.DTO.CommentRequest;
import community.community.DTO.CommentResponse;
import community.community.Domain.User;
import community.community.Service.CommentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/comment")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * 댓글 작성
     * POST /api/comment/post/{postId}
     */
    @PostMapping("/post/{postId}")
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable Long postId,
            @RequestBody CommentRequest request,
            HttpSession session) {
        var user = session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body(null); // 로그인 필요
        }

        var loggedInUser = (User) user;
        CommentResponse response = commentService.createComment(postId, request.getContent(), loggedInUser.getId());
        return ResponseEntity.ok(response);
    }

    /**
     * 내 댓글 조회
     * GET /api/comment/my
     */
    @GetMapping("/my")
    public ResponseEntity<List<CommentResponse>> getMyComments(HttpSession session) {
        var user = session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body(null); // 로그인 필요
        }
        var loggedInUser = (User) user;
        List<CommentResponse> myComments = commentService.getMyComments(loggedInUser.getId());
        return ResponseEntity.ok(myComments);
    }

    /**
     * 댓글 수정
     * PUT /api/comment/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateComment(
            @PathVariable Long id,
            @RequestBody CommentRequest request,
            HttpSession session) {
        var user = session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }

        var loggedInUser = (User) user;
        commentService.updateComment(id, request.getContent(), loggedInUser.getId());
        return ResponseEntity.ok("댓글이 성공적으로 수정되었습니다.");
    }

    /**
     * 댓글 삭제
     * DELETE /api/comment/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(
            @PathVariable Long id,
            HttpSession session) {
        var user = session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }

        var loggedInUser = (User) user;
        commentService.deleteComment(id, loggedInUser.getId());
        return ResponseEntity.ok("댓글이 성공적으로 삭제되었습니다.");
    }
}

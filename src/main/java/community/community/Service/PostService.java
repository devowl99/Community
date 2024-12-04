package community.community.Service;

import community.community.Domain.Post;
import community.community.Domain.Comment;
import community.community.Domain.User;

import community.community.DTO.PostResponse;
import community.community.DTO.CommentResponse;
import community.community.DTO.DetailedPostResponse;

import community.community.Repository.PostRepository;
import community.community.Repository.UserRepository;
import community.community.Repository.CommentRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    // 게시글 생성
    public Post createPost(String title, String content, Long userId) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setAuthor(author);
        return postRepository.save(post);
    }

    // 모든 게시글 조회
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll().stream()
                .map(post -> new PostResponse(
                        post.getId(),
                        post.getTitle(),
                        post.getContent(),
                        post.getAuthor().getUsername() // Lazy Loading 문제 해결
                ))
                .collect(Collectors.toList());
    }

    // 특정 게시글 조회
    public DetailedPostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        List<CommentResponse> comments = commentRepository.findByPostId(id).stream()
                .map(comment -> new CommentResponse(
                        comment.getId(),
                        comment.getContent(),
                        comment.getAuthor().getUsername(),
                        comment.getPost().getId()
                ))
                .collect(Collectors.toList());

        return new DetailedPostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor().getUsername(),
                comments
        );
    }


    // 내 게시글 조회
    public List<PostResponse> getPostsByUserId(Long userId) {
        List<Post> posts = postRepository.findByAuthorId(userId);
        return posts.stream()
                .map(post -> new PostResponse(
                        post.getId(),
                        post.getTitle(),
                        post.getContent(),
                        post.getAuthor().getUsername()
                ))
                .collect(Collectors.toList());
    }

    // 게시글 수정
    public void updatePost(Long postId, String title, String content, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        if (!post.getAuthor().getId().equals(userId)) {
            throw new IllegalArgumentException("게시글 수정 권한이 없습니다.");
        }
        post.setTitle(title);
        post.setContent(content);
        postRepository.save(post); // 수정된 데이터 저장
    }

    // 게시글 삭제
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        if (!post.getAuthor().getId().equals(userId)) {
            throw new IllegalArgumentException("게시글을 삭제할 권한이 없습니다.");
        }
        postRepository.delete(post);
    }
}

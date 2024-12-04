package community.community.DTO;

public class CommentResponse {
    private Long id;
    private String content;
    private String authorUsername;
    private Long postId;

    public CommentResponse(Long id, String content, String authorUsername, Long postId) {
        this.id = id;
        this.content = content;
        this.authorUsername = authorUsername;
        this.postId = postId;
    }
    // Getters
    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public Long getPostId() {
        return postId;
    }
}

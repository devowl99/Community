package community.community.DTO;

import java.util.List;

public class DetailedPostResponse {
    private Long id;
    private String title;
    private String content;
    private String authorUsername;
    private List<CommentResponse> comments;

    public DetailedPostResponse(Long id, String title, String content, String authorUsername, List<CommentResponse> comments) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorUsername = authorUsername;
        this.comments = comments;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public List<CommentResponse> getComments() {
        return comments;
    }
}

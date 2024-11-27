package community.community.DTO;

public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String authorUsername;

    public PostResponse(Long id, String title, String content, String authorUsername) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorUsername = authorUsername;
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
}

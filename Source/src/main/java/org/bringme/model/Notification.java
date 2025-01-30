package org.bringme.model;

/**
 * Model of how hte notification entity represented in the database
 */
public class Notification {
    private Long id;
    /**
     * Owner of the notification
     */
    private int userId ;
    private String content;
    /**
     * Read \ Unread
     */
    private int marked;

    /**
     * On which request notification should be sent
     */
    private int requestId;

    public Notification(){}
    public Notification(Long id, int userId, String content, int marked, int requestId) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.marked = marked;
        this.requestId = requestId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int isMarked() {
        return marked;
    }

    public void setMarked(int marked) {
        this.marked = marked;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", userId=" + userId +
                ", content='" + content + '\'' +
                ", read=" + marked +
                ", requestId=" + requestId +
                '}';
    }
}

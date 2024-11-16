package org.bringme.model;

public class Rate {
    private Long id;
    private String comments;
    private int value;
    private int userId ;
    private int requestId;

    public Rate(){}
    public Rate(Long id, String comments, int value, int userId, int requestId) {
        this.id = id;
        this.comments = comments;
        this.value = value;
        this.userId = userId;
        this.requestId = requestId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "Rate{" +
                "id=" + id +
                ", comments='" + comments + '\'' +
                ", value=" + value +
                ", userId=" + userId +
                ", requestId=" + requestId +
                '}';
    }
}

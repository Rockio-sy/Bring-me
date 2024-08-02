package org.bringme.model;

public class Request {
    private Long id;
    private Integer requesterUserId;
    private Integer requestedUserId;
    private Integer itemId;

    public Request(){}

    public Request(Long id, Integer requesterUserId, Integer requestedUserId, Integer itemId) {
        this.id = id;
        this.requesterUserId = requesterUserId;
        this.requestedUserId = requestedUserId;
        this.itemId = itemId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRequesterUserId() {
        return requesterUserId;
    }

    public void setRequesterUserId(Integer requesterUserId) {
        this.requesterUserId = requesterUserId;
    }

    public Integer getRequestedUserId() {
        return requestedUserId;
    }

    public void setRequestedUserId(Integer requestedUserId) {
        this.requestedUserId = requestedUserId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", requesterUserId=" + requesterUserId +
                ", requestedUserId=" + requestedUserId +
                ", itemId=" + itemId +
                '}';
    }
}

package org.bringme.model;

/**
 * Model of how the request entity represented in the database
 */
public class Request {
    private Long id;
    /**
     * User who asks for request
     */
    private Integer requesterUserId;

    /**
     * User who received the request
     */
    private Integer requestedUserId;
    /**
     * On which item
     */
    private Integer itemId;
    /**
     * On which trip
     */
    private Integer tripId;

    /**
     * PLace to meet, where item is located
     */
    private Integer origin;

    /**
     * Place to delegate, destination of trip and item
     */
    private Integer destination;
    private String comments;

    /**
     * True - approved <p>
     * False - Not approved
     */
    private boolean approvement;
    private float price;

    public Request(){}

    public Request(Long id, Integer requesterUserId, Integer requestedUserId, Integer itemId, Integer tripId, Integer origin, Integer destination, String comments, boolean approvement, float price) {
        this.id = id;
        this.requesterUserId = requesterUserId;
        this.requestedUserId = requestedUserId;
        this.itemId = itemId;
        this.tripId = tripId;
        this.origin = origin;
        this.destination = destination;
        this.comments = comments;
        this.approvement = approvement;
        this.price = price;
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

    public Integer getTripId() {
        return tripId;
    }

    public void setTripId(Integer tripId) {
        this.tripId = tripId;
    }

    public Integer getOrigin() {
        return origin;
    }

    public void setOrigin(Integer origin) {
        this.origin = origin;
    }

    public Integer getDestination() {
        return destination;
    }

    public void setDestination(Integer destination) {
        this.destination = destination;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean isApprovement() {
        return approvement;
    }

    public void setApprovement(boolean approvement) {
        this.approvement = approvement;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", requesterUserId=" + requesterUserId +
                ", requestedUserId=" + requestedUserId +
                ", itemId=" + itemId +
                ", tripId=" + tripId +
                ", origin=" + origin +
                ", destination=" + destination +
                ", comments='" + comments + '\'' +
                ", approvement=" + approvement +
                ", price=" + price +
                '}';
    }
}

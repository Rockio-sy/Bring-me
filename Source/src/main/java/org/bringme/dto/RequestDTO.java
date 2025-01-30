package org.bringme.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RequestDTO {

    private Long id;

    /**
     * User who applied the request
     */
    private Long requesterUserId;

    /**
     * User who received the request
     */
    private Long requestedUserId;
    @NotNull
    private Integer itemId;
    @NotNull
    private Integer tripId;

    /**
     * Place where to meet or take the item
     */
    private Integer origin;

    /**
     * Place to where delegate
     */
    private Integer destination;
    @NotBlank
    private String comments;
    /**
     * Approvement (done by the requested)
     */
    private boolean approvement;
    @NotNull
    private float price;

    public RequestDTO() {
    }

    // Response CreateNewRequest constructor
    public RequestDTO(Long id, Long requesterUserId, Long requestedUserId, Integer itemId, Integer tripId, Integer origin, Integer destination, String comments, boolean approvement, float price) {
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

    // JSON Request CreateNewRequest constructor
    // TODO: Add currency to the entity to use it with price
    public RequestDTO( Integer itemId, Integer tripId, String comments, float price) {
        this.itemId = itemId;
        this.tripId = tripId;
        this.comments = comments;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public  Long getRequesterUserId() {
        return requesterUserId;
    }

    public void setRequesterUserId(Long requesterUserId) {
        this.requesterUserId = requesterUserId;
    }

    public Long getRequestedUserId() {
        return requestedUserId;
    }

    public void setRequestedUserId(Long requestedUserId) {
        this.requestedUserId = requestedUserId;
    }

    public @NotNull Integer getItemId() {
        return itemId;
    }

    public void setItemId(@NotNull Integer itemId) {
        this.itemId = itemId;
    }

    public @NotNull Integer getTripId() {
        return tripId;
    }

    public void setTripId(@NotNull Integer tripId) {
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

    public @NotBlank String getComments() {
        return comments;
    }

    public void setComments(@NotBlank String comments) {
        this.comments = comments;
    }


    public boolean isApprovement() {
        return approvement;
    }

    public void setApprovement(boolean approvement) {
        this.approvement = approvement;
    }

    @NotNull
    public float getPrice() {
        return price;
    }

    public void setPrice(@NotNull float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "RequestDTO{" +
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

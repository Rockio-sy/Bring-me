package org.bringme.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RequestDTO {

    private Long id;
    @NotNull
    private Integer requesterUserId;
    @NotNull
    private Integer requestedUserId;
    @NotNull
    private Integer itemId;
    @NotNull
    private Integer tripId;
    @NotNull
    private Integer origin;
    @NotNull
    private Integer destination;
    @NotBlank
    private String comments;
    @NotNull
    private boolean approvement;
    @NotNull
    private float price;

    public RequestDTO(){}

    // Response CreateNewRequest constructor
    public RequestDTO(Long id, Integer requesterUserId, Integer requestedUserId, Integer itemId, Integer tripId, Integer origin, Integer destination, String comments, boolean approvement, float price) {
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
    public RequestDTO(Integer requesterUserId, Integer requestedUserId, Integer itemId, Integer tripId, Integer origin, Integer destination, String comments, boolean approvement, float price) {
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

    public @NotNull Integer getRequesterUserId() {
        return requesterUserId;
    }

    public void setRequesterUserId(@NotNull Integer requesterUserId) {
        this.requesterUserId = requesterUserId;
    }

    public @NotNull Integer getRequestedUserId() {
        return requestedUserId;
    }

    public void setRequestedUserId(@NotNull Integer requestedUserId) {
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

    public @NotNull Integer getOrigin() {
        return origin;
    }

    public void setOrigin(@NotNull Integer origin) {
        this.origin = origin;
    }

    public @NotNull Integer getDestination() {
        return destination;
    }

    public void setDestination(@NotNull Integer destination) {
        this.destination = destination;
    }

    public @NotBlank String getComments() {
        return comments;
    }

    public void setComments(@NotBlank String comments) {
        this.comments = comments;
    }

    @NotNull
    public boolean isApprovement() {
        return approvement;
    }

    public void setApprovement(@NotNull boolean approvement) {
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

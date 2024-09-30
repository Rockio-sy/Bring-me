package org.bringme.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ItemDTO {

    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private int origin;
    @NotNull
    private int destination;
    @NotNull
    private float weight;
    @NotNull
    private float height;
    @NotNull
    private float length;
    @NotBlank
    private String comments;
    @NotBlank
    private String detailedOriginAddress;
    @NotBlank
    private String photo;
    @NotNull
    private Long user_id;

    // Self constructor
    public ItemDTO(){}

    // Response CreateNewItem constructor
    public ItemDTO(Long id, String name, int origin, int destination, float weight, float height, float length, String comments, String detailedOriginAddress, String photo, Long user_id) {
        this.id = id;
        this.name = name;
        this.origin = origin;
        this.destination = destination;
        this.weight = weight;
        this.height = height;
        this.length = length;
        this.comments = comments;
        this.detailedOriginAddress = detailedOriginAddress;
        this.photo = photo;
        this.user_id = user_id;
    }

    // Request CreateNewUser constructor
    public ItemDTO(String name, int origin, int destination, float weight, float height, float length, String comments, String detailedOriginAddress, String photo, Long user_id) {
        this.name = name;
        this.origin = origin;
        this.destination = destination;
        this.weight = weight;
        this.height = height;
        this.length = length;
        this.comments = comments;
        this.detailedOriginAddress = detailedOriginAddress;
        this.photo = photo;
        this.user_id = user_id;
    }


    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId( Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrigin() {
        return origin;
    }

    public void setOrigin(int origin) {
        this.origin = origin;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDetailedOriginAddress() {
        return detailedOriginAddress;
    }

    public void setDetailedOriginAddress(String detailedOriginAddress) {
        this.detailedOriginAddress = detailedOriginAddress;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }
}

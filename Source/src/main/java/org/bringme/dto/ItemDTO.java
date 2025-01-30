package org.bringme.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Item DTO class to handle requests and response of item operations
 */
public class ItemDTO {

    /**
     * Unique ID Primary key
     */
    private Long id;
    @NotBlank(message = "Name cannot be empty")
    private String name;
    /**
     * The place where item is located
     */
    @NotNull(message = "Country not found")
    private int origin;
    /**
     * The place where item should be delegated
     */
    @NotNull(message = "Country not found")
    private int destination;
    @NotNull(message = "Invalid weight")
    private float weight;
    @NotNull(message = "Invalid height")
    private float height;
    @NotNull(message = "Invalid length")
    private float length;
    @NotBlank(message = "Write comments")
    private String comments;
    /**
     * Details of the address to where item should be delegated
     */
    @NotBlank(message = "Address should be inserted")
    private String detailedOriginAddress;
    @NotBlank(message = "No photo found")
    private String photo;

    /**
     * Owner id
     */
    private Long user_id;

    // Self constructor
    public ItemDTO() {
    }

    /**
     * Response CreateNewItem constructor
     */

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

    /**
     * Request CreateNewUser constructor
     */
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

    public void setId(Long id) {
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

    @Override
    public String toString() {
        return "ItemDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", origin=" + origin +
                ", destination=" + destination +
                ", weight=" + weight +
                ", height=" + height +
                ", length=" + length +
                ", comments='" + comments + '\'' +
                ", detailedOriginAddress='" + detailedOriginAddress + '\'' +
                ", photo='" + photo + '\'' +
                ", user_id=" + user_id +
                '}';
    }
}

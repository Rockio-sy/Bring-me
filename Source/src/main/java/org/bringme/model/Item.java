package org.bringme.model;

public class Item {
    private Long id;
    private String name;
    private int origin;
    private int destination;
    private float weight;
    private float height;
    private float length;
    private String comments;
    private String detailedOriginAddress;
    private String photo;
    private Long user_id;

    public Item() {}

    public Item(Long id, String name, int origin, int destination, float weight, float height,
                float length, String comments, String detailedOriginAddress, String photo, Long user_id) {
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

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
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

    @Override
    public String toString() {
        return "Item{" +
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

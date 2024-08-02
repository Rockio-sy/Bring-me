package org.bringme.model;

public class Item {
    private Long id;
    private String name;
    private String origin;
    private String destination;

    public Item() {
    }

    public Item(Long id, String name, String origin, String destination) {
        this.id = id;
        this.name = name;
        this.origin = origin;
        this.destination = destination;
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

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }


}

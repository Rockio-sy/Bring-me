package org.bringme.model;

import java.time.LocalDateTime;

public class Trip {
    private Long id;
    private int origin;
    private int destination;
    private String destinationAirport;
    private float emptyWeight;
    private LocalDateTime arrivalTime;
    private LocalDateTime departureTime;
    private boolean transit;
    private String comments;
    private Long passengerId;

    public Trip(){}
    public Trip(Long id, int origin, int destination, String destinationAirport, float emptyWeight, LocalDateTime arrivalTime, LocalDateTime departureTime, boolean transit, String comments, Long passengerId) {
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.destinationAirport = destinationAirport;
        this.emptyWeight = emptyWeight;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.transit = transit;
        this.comments = comments;
        this.passengerId = passengerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDestinationAirport() {
        return destinationAirport;
    }

    public void setDestinationAirport(String destinationAirport) {
        this.destinationAirport = destinationAirport;
    }

    public float getEmptyWeight() {
        return emptyWeight;
    }

    public void setEmptyWeight(float emptyWeight) {
        this.emptyWeight = emptyWeight;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public boolean isTransit() {
        return transit;
    }

    public void setTransit(boolean transit) {
        this.transit = transit;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Long getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Long passengerId) {
        this.passengerId = passengerId;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "id=" + id +
                ", origin=" + origin +
                ", destination=" + destination +
                ", destinationAirport='" + destinationAirport + '\'' +
                ", emptyWeight=" + emptyWeight +
                ", arrivalTime=" + arrivalTime +
                ", departureTime=" + departureTime +
                ", transit=" + transit +
                ", comments='" + comments + '\'' +
                ", passengerId=" + passengerId +
                '}';
    }
}

package org.bringme.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class TripDTO {
    private Long id;
    @NotNull
    private int origin;
    @NotNull
    private int destination;
    @NotBlank
    private String destinationAirport;
    @NotNull
    private float emptyWeight;
    @NotNull
    private LocalDateTime arrivalTime;
    @NotNull
    private LocalDateTime departureTime;
    @NotNull
    private boolean transit;
    @NotBlank
    private String comments;
    @NotNull
    private int passengerId;

    public TripDTO() {
    }

    // CreateNewTrip response
    public TripDTO(Long id, int origin, int destination, String destinationAirport, float emptyWeight, LocalDateTime arrivalTime, LocalDateTime departureTime, boolean transit, String comments, int passengerId) {
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

    // CreateNewTrip request
    public TripDTO(int origin, int destination, String destinationAirport, float emptyWeight, LocalDateTime arrivalTime, LocalDateTime departureTime, boolean transit, String comments, int passengerId) {
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

    @NotNull
    public int getOrigin() {
        return origin;
    }

    public void setOrigin(@NotNull int origin) {
        this.origin = origin;
    }

    @NotNull
    public int getDestination() {
        return destination;
    }

    public void setDestination(@NotNull int destination) {
        this.destination = destination;
    }

    public @NotBlank String getDestinationAirport() {
        return destinationAirport;
    }

    public void setDestinationAirport(@NotBlank String destinationAirport) {
        this.destinationAirport = destinationAirport;
    }

    @NotNull
    public float getEmptyWeight() {
        return emptyWeight;
    }

    public void setEmptyWeight(@NotNull float emptyWeight) {
        this.emptyWeight = emptyWeight;
    }

    public @NotNull LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(@NotNull LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public @NotNull LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(@NotNull LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    @NotNull
    public boolean isTransit() {
        return transit;
    }

    public void setTransit(@NotNull boolean transit) {
        this.transit = transit;
    }

    public @NotBlank String getComments() {
        return comments;
    }

    public void setComments(@NotBlank String comments) {
        this.comments = comments;
    }

    @NotNull
    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(@NotNull int passengerId) {
        this.passengerId = passengerId;
    }

    @Override
    public String toString() {
        return "TripDTO{" +
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

package org.bringme.utils;

import org.bringme.dto.ItemDTO;
import org.bringme.dto.RequestDTO;
import org.bringme.dto.TripDTO;
import org.bringme.model.Item;
import org.bringme.model.Request;
import org.bringme.model.Trip;
import org.springframework.stereotype.Component;

@Component
public class Converter {
    // ItemDTO class to Item class
    public Item DTOtoItem(ItemDTO itemDTO){
        return new Item(
                null,
                itemDTO.getName(),
                itemDTO.getOrigin(),
                itemDTO.getDestination(),
                itemDTO.getWeight(),
                itemDTO.getHeight(),
                itemDTO.getLength(),
                itemDTO.getComments(),
                itemDTO.getDetailedOriginAddress(),
                itemDTO.getPhoto(),
                itemDTO.getUser_id()
        );
    }

    // Item class to ItemDTO
    public ItemDTO itemToDTO(Item item){
        return new ItemDTO(
                item.getId(),
                item.getName(),
                item.getOrigin(),
                item.getDestination(),
                item.getWeight(),
                item.getHeight(),
                item.getLength(),
                item.getComments(),
                item.getDetailedOriginAddress(),
                item.getPhoto(),
                item.getUser_id());
    }

    // TripDTO class to Trip class
    public Trip DTOtoTrip(TripDTO tripDTO){
        Trip trip= new Trip();
        trip.setId(null);
        trip.setOrigin(tripDTO.getOrigin());
        trip.setDestination(tripDTO.getDestination());
        trip.setDestinationAirport(tripDTO.getDestinationAirport());
        trip.setEmptyWeight(tripDTO.getEmptyWeight());
        trip.setArrivalTime(tripDTO.getArrivalTime());
        trip.setDepartureTime(tripDTO.getDepartureTime());
        trip.setTransit(tripDTO.isTransit());
        trip.setComments(tripDTO.getComments());
        trip.setPassengerId(tripDTO.getPassengerId());
        return trip;
    }

    // Trip class to TripDTO class
    public TripDTO tripToDTO(Trip trip){
        return new TripDTO(
                trip.getId(),
                trip.getOrigin(),
                trip.getDestination(),
                trip.getDestinationAirport(),
                trip.getEmptyWeight(),
                trip.getArrivalTime(),
                trip.getDepartureTime(),
                trip.isTransit(),
                trip.getComments(),
                trip.getPassengerId()
        );
    }

    // Request class to RequestDTO class
    public RequestDTO requestToDTO(Request request){
        RequestDTO newRequestDTO = new RequestDTO();
        newRequestDTO.setId(request.getId());
        newRequestDTO.setRequesterUserId(request.getRequesterUserId());
        newRequestDTO.setRequestedUserId(request.getRequestedUserId());
        newRequestDTO.setItemId(request.getItemId());
        newRequestDTO.setTripId(request.getTripId());
        newRequestDTO.setOrigin(request.getOrigin());
        newRequestDTO.setDestination(request.getDestination());
        newRequestDTO.setComments(request.getComments());
        newRequestDTO.setApprovement(request.isApprovement());
        newRequestDTO.setPrice(request.getPrice());
        return newRequestDTO;
    }

    public Request DTOtoRequest(RequestDTO requestDTO){
        Request newRequest = new Request();
        newRequest.setId(null);
        newRequest.setRequesterUserId(requestDTO.getRequesterUserId());
        newRequest.setRequestedUserId(requestDTO.getRequestedUserId());
        newRequest.setItemId(requestDTO.getItemId());
        newRequest.setTripId(requestDTO.getTripId());
        newRequest.setOrigin(requestDTO.getOrigin());
        newRequest.setDestination(requestDTO.getDestination());
        newRequest.setComments(requestDTO.getComments());
        newRequest.setApprovement(requestDTO.isApprovement());
        newRequest.setPrice(requestDTO.getPrice());
        return newRequest;
    }
}

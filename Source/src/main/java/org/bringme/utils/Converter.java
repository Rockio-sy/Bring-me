package org.bringme.utils;

import org.bringme.dto.ItemDTO;
import org.bringme.dto.TripDTO;
import org.bringme.model.Item;
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
}

package org.bringme.service.impl;

import org.bringme.dto.TripDTO;
import org.bringme.model.Trip;
import org.bringme.repository.TripRepository;
import org.bringme.service.TripService;
import org.bringme.utils.Converter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final Converter converter;

    public TripServiceImpl(TripRepository tripRepository, Converter converter){
        this.tripRepository = tripRepository;
        this.converter = converter;
    }

    @Override
    public TripDTO saveTrip(TripDTO requestTrip){
        // Convert to Trip model
       Trip newTrip = converter.DTOtoTrip(requestTrip);

        Long generatedId = tripRepository.saveTrip(newTrip);
        if(generatedId == null){
            return null;
        }

        // Convert to TripDTO then return
        TripDTO responseTrip = converter.tripToDTO(newTrip);
        responseTrip.setId(generatedId);
        return responseTrip;
    }

    @Override
    public TripDTO getById(Long id) {
        // Get trip from database
        Optional<Trip> savedTrip = tripRepository.getById(id);

        // Convert to DTO and return
        return savedTrip.map(converter::tripToDTO).orElse(null);
    }

    @Override
    public List<TripDTO> getAllTrips(){
        // Get list from database
        List<Trip> savedList = tripRepository.getAll();

        //Check if list is empty
        if(savedList.isEmpty()){
            return null;
        }

        // Convert every trip to DTO class and save it in new list
        List<TripDTO> responseList = new ArrayList<>();
        for(Trip savedTrip : savedList){
            TripDTO convertedTrip = converter.tripToDTO(savedTrip);
            responseList.add(convertedTrip);
        }

        return responseList;
    }
}

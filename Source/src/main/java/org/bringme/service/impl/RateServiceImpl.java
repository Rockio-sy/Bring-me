package org.bringme.service.impl;

import org.bringme.dto.RateDTO;
import org.bringme.model.Rate;
import org.bringme.repository.PersonRepository;
import org.bringme.repository.RateRepository;
import org.bringme.repository.RequestRepository;
import org.bringme.service.PersonService;
import org.bringme.service.RateService;
import org.bringme.service.exceptions.CustomException;
import org.bringme.utils.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RateServiceImpl implements RateService {
    private final RateRepository rateRepository;
    private final Converter converter;
    private final PersonRepository personRepository;
    private final RequestRepository requestRepository;

    public RateServiceImpl(RateRepository rateRepository, Converter converter, PersonRepository personRepository, RequestRepository requestRepository) {
        this.rateRepository = rateRepository;
        this.converter = converter;
        this.personRepository = personRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    public List<RateDTO> getAllRates(int userId) {
        if (personRepository.getById(Integer.toUnsignedLong(userId)).isEmpty()) {
            throw new CustomException("User not found", HttpStatus.BAD_REQUEST);
        }
        List<Rate> data = rateRepository.getAll(userId);
        if (data.isEmpty()) {
            throw new CustomException("No data", HttpStatus.NO_CONTENT);
        }
        List<RateDTO> response = new ArrayList<>();
        for (Rate r : data) {
            RateDTO dto = converter.rateToDTO(r);
            response.add(dto);
        }
        return response;
    }

    @Override
    public void checkRatingAvailability(Long userId, int ratedUserId) {
        if(!requestRepository.isThereCommonRequest(userId, ratedUserId)){
            System.out.println("HERE");
            System.out.println("Current: "+userId +"\nRated: "+ ratedUserId);
            throw new CustomException("No common requests", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public RateDTO createNewRate(RateDTO rate) {
        Rate model = converter.DTOtoRate(rate);
        Long id = rateRepository.save(model);
        if(id == null){
            throw new CustomException("Cannot create the rate", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return converter.rateToDTO(model);
    }
}

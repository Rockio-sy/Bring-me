package org.bringme.service.impl;

import org.bringme.dto.RateDTO;
import org.bringme.exceptions.CannotGetIdOfInsertDataException;
import org.bringme.exceptions.NoCommonRequestException;
import org.bringme.exceptions.NotFoundException;
import org.bringme.model.Rate;
import org.bringme.repository.PersonRepository;
import org.bringme.repository.RateRepository;
import org.bringme.repository.RequestRepository;
import org.bringme.service.RateService;
import org.bringme.exceptions.CustomException;
import org.bringme.utils.Converter;
import org.springframework.dao.EmptyResultDataAccessException;
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
    /**
     * Retrieves all rates for a given user.
     *
     * @param userId The ID of the user for whom to retrieve rates.
     * @return A list of RateDTO objects representing the user's rates.
     * @throws CustomException If the user is not found, or if no rates are found, throws a BAD_REQUEST or NO_CONTENT exception.
     */
    @Override
    public List<RateDTO> getAllRates(int userId) {
        if (personRepository.getById(Integer.toUnsignedLong(userId)).isEmpty()) {
            throw new NotFoundException("User not found", "RateServiceImpl::getAllRates");
        }
        List<Rate> data = rateRepository.getAll(userId);
        if (data.isEmpty()) {
            throw new NotFoundException("No data found", "RateServiceImpl::getAllRates");
        }
        List<RateDTO> response = new ArrayList<>();
        for (Rate r : data) {
            RateDTO dto = converter.rateToDTO(r);
            response.add(dto);
        }
        return response;
    }

    /**
     * Checks whether a rating can be given between two users based on their common requests.
     *
     * @param userId The ID of the user who wants to rate.
     * @param ratedUserId The ID of the user being rated.
     * @throws CustomException If no common requests are found between the users, throws a BAD_REQUEST exception.
     */
    @Override
    public void checkRatingAvailability(Long userId, int ratedUserId) {
        if(!requestRepository.isThereCommonRequest(userId, ratedUserId)){
            throw new NoCommonRequestException(userId, ratedUserId);
        }
    }
    /**
     * Creates a new rating for a user and saves it to the database.
     *
     * @param rate A RateDTO object containing the rating details.
     * @return A RateDTO object representing the saved rating.
     * @throws CustomException If there is an error creating the rate, throws an INTERNAL_SERVER_ERROR exception.
     */
    @Override
    public RateDTO createNewRate(RateDTO rate) {
        Rate model = converter.DTOtoRate(rate);
        try{
            Long id = rateRepository.save(model);
            model.setId(id);
            return converter.rateToDTO(model);
        }catch (EmptyResultDataAccessException e){
            throw new CannotGetIdOfInsertDataException("CreateNewUser", e);
        }


    }
}

package org.bringme.service;

import org.bringme.dto.RateDTO;

import java.util.List;

public interface RateService {
    List<RateDTO> getAllRates(int userId);

    void checkRatingAvailability(Long userId, int ratedUserId);

    RateDTO createNewRate(RateDTO rate);
}

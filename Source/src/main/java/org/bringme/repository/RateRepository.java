package org.bringme.repository;

import org.bringme.model.Rate;

import java.util.List;

public interface RateRepository {
    List<Rate> getAll(int userId);

    Long save(Rate model);
}

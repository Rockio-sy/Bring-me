package org.bringme.repository;

import org.bringme.dto.RequestDTO;
import org.bringme.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository {
    List<Request> getAll();
    Long saveRequest(Request request);
    Optional<Request> getRequestById(Long id);
}

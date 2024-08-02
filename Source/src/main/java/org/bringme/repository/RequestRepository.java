package org.bringme.repository;

import org.bringme.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository {
    List<Request> getAll();
    int saveRequest(Request request);
    Optional<Request> getRequestById(Long id);
    // Search for requests
}

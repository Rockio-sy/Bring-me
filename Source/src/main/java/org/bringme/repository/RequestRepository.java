package org.bringme.repository;

import org.bringme.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository {
    List<Request> getAll(Long  userId);
    Long saveRequest(Request request);
    Optional<Request> getRequestById(Long id);

    Long isExists(Integer itemId, Integer tripId);

    List<Request> getSentRequests(Long userId);

    List<Request> getByDirections(Long userId, int origin, int destination);

    List<Request> getReceivedRequests(Long userId);
}

package org.bringme.service;

import org.bringme.dto.RequestDTO;
import org.bringme.model.Request;

import java.util.List;

public interface RequestService {
    List<RequestDTO> getAll(Long userId);
    Request getRequestById(Long id);
    RequestDTO saveRequest(RequestDTO request);

    Long isExists(Integer id, Integer tripId);

    List<RequestDTO> getSentRequests(Long userId);

    List<RequestDTO> filterByDirections(Long userId, int origin, int destination);

    List<RequestDTO> getReceivedRequests(Long userId);

    boolean approveRequest(Long userId, Long requestId);

    List<RequestDTO> filterByApprovement(Long userId);

    List<RequestDTO> filterByWait(Long userId);

    boolean isThereCommonRequest(Long guestId, int hostId);
}

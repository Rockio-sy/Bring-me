package org.bringme.service;

import org.bringme.dto.RequestDTO;
import org.bringme.model.Request;

import java.util.List;

public interface RequestService {
    List<RequestDTO> getAll(Long userId);
    Request getRequestById(Long id);
    RequestDTO saveRequest(RequestDTO request);

    Long isExists(Integer id, Integer tripId);
}

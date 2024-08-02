package org.bringme.service.impl;

import org.bringme.model.Request;
import org.bringme.repository.RequestRepository;
import org.bringme.service.RequestService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    public RequestServiceImpl(RequestRepository requestRepository){
        this.requestRepository = requestRepository;
    }

    @Override
    public List<Request> getAll() {
        return requestRepository.getAll();
    }

    @Override
    public Request getRequestById(Long id) {
        Optional<Request> newRequest = requestRepository.getRequestById(id);
        return newRequest.orElse(null);
    }

    @Override
    public Request saveRequest(Request request) {
        int rowAffected = requestRepository.saveRequest(request);
        return (rowAffected > 0)?request:null;
    }
}

package org.bringme.service.impl;

import org.bringme.dto.RequestDTO;
import org.bringme.model.Request;
import org.bringme.repository.RequestRepository;
import org.bringme.service.RequestService;
import org.bringme.utils.Converter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final Converter converter;
    public RequestServiceImpl(RequestRepository requestRepository, Converter converter){
        this.converter = converter;
        this.requestRepository = requestRepository;
    }

    @Override
    public List<RequestDTO> getAll() {
        List<Request> requestList = requestRepository.getAll();
        if (requestList.isEmpty()){
            return null;
        }

        // Convert to DTO
        List<RequestDTO> resposneList = new ArrayList<>();
        for(Request old : requestList){
            RequestDTO newRequest = converter.requestToDTO(old);
            resposneList.add(newRequest);
        }

        return resposneList;

    }

    @Override
    public RequestDTO saveRequest(RequestDTO request) {

        // Convert to Model class
        Request requestToSave = converter.DTOtoRequest(request);

        // Get generated id
        Long generatedId = requestRepository.saveRequest(requestToSave);

        // Convert to DTO
        RequestDTO responseRequest = converter.requestToDTO(requestToSave);
        // Set the generated id
        responseRequest.setId(generatedId);

        return responseRequest;
    }

    @Override
    public Request getRequestById(Long id) {
        Optional<Request> newRequest = requestRepository.getRequestById(id);
        return newRequest.orElse(null);
    }
}

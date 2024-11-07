package org.bringme.service.impl;

import org.bringme.dto.RequestDTO;
import org.bringme.model.Item;
import org.bringme.model.Notification;
import org.bringme.model.Request;
import org.bringme.model.Trip;
import org.bringme.repository.ItemRepository;
import org.bringme.repository.RequestRepository;
import org.bringme.repository.TripRepository;
import org.bringme.service.EmailService;
import org.bringme.service.NotificationService;
import org.bringme.service.RequestService;
import org.bringme.utils.Converter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final ItemRepository itemRepository;
    private final TripRepository tripRepository;
    private final Converter converter;
    private final EmailService emailService;
    private final NotificationService notificationService;

    public RequestServiceImpl(RequestRepository requestRepository, Converter converter, ItemRepository itemRepository, TripRepository tripRepository, EmailService emailService, NotificationService notificationService) {
        this.converter = converter;
        this.requestRepository = requestRepository;
        this.itemRepository = itemRepository;
        this.tripRepository = tripRepository;
        this.emailService = emailService;
        this.notificationService = notificationService;
    }

    @Override
    public List<RequestDTO> getAll(Long userId) {
        List<Request> requestList = requestRepository.getAll(userId);
        if (requestList.isEmpty()) {
            return null;
        }

        // Convert to DTO
        List<RequestDTO> resposneList = new ArrayList<>();
        for (Request old : requestList) {
            RequestDTO newRequest = converter.requestToDTO(old);
            resposneList.add(newRequest);
        }

        return resposneList;
    }

    @Override
    public RequestDTO saveRequest(RequestDTO request) {

        // get item, trip and requested user id
        Optional<Item> item = itemRepository.getById(request.getItemId().longValue());
        Optional<Trip> trip = tripRepository.getById(request.getTripId().longValue());
        if (item.isEmpty() || trip.isEmpty()) {
            return null;
        }

        if (item.get().getOrigin() != item.get().getOrigin() || item.get().getDestination() != item.get().getDestination()) {
            return null;
        }

        // Set values of requested user id, trip and item to save in the database
        if (Objects.equals(item.get().getUser_id(), request.getRequesterUserId())) {
            request.setRequestedUserId(trip.get().getPassengerId());
        } else if (Objects.equals(trip.get().getPassengerId(), request.getRequesterUserId())) {
            request.setRequestedUserId(item.get().getUser_id());
        }

        request.setOrigin(item.get().getOrigin());
        request.setDestination(item.get().getDestination());

        // Convert to Model class
        Request modelRequest = converter.DTOtoRequest(request);

        // Get generated id
        Long generatedId = requestRepository.saveRequest(modelRequest);

        // Convert to DTO
        RequestDTO responseRequest = converter.requestToDTO(modelRequest);

        emailService.sendEmail("Your request has been created", "New Request", (request.getRequesterUserId()));
        emailService.sendEmail("Someone is requesting you, check the website for more info", "New Request", request.getRequestedUserId());
        notificationService.saveNotification((request.getRequesterUserId().intValue()), "Your request has been created", request.getId().intValue());
        notificationService.saveNotification((request.getRequestedUserId().intValue()), "Someone is requesting you, check the website for more info", request.getId().intValue());
        // Set the generated id
        responseRequest.setId(generatedId);

        return responseRequest;
    }

    @Override
    public Long isExists(Integer itemId, Integer tripId) {
        return requestRepository.isExists(itemId, tripId);
    }

    @Override
    public List<RequestDTO> getSentRequests(Long userId) {
        // Get data from database
        List<Request> data = requestRepository.getSentRequests(userId);

        if (data.isEmpty()) {
            return List.of();
        }

        List<RequestDTO> response = new ArrayList<>();
        // Convert to DTO
        for (Request re : data) {
            RequestDTO dto = converter.requestToDTO(re);
            response.add(dto);
        }

        return response;
    }

    @Override
    public List<RequestDTO> filterByDirections(Long userId, int origin, int destination) {
        // Get data from database
        List<Request> data = requestRepository.getByDirections(userId, origin, destination);

        if (data.isEmpty()) {
            return List.of();
        }

        List<RequestDTO> response = new ArrayList<>();
        // Convert to DTO
        for (Request re : data) {
            RequestDTO dto = converter.requestToDTO(re);
            response.add(dto);
        }
        return response;
    }

    @Override
    public List<RequestDTO> getReceivedRequests(Long userId) {
        // Get data from database
        List<Request> data = requestRepository.getReceivedRequests(userId);

        if (data.isEmpty()) {
            return List.of();
        }

        List<RequestDTO> response = new ArrayList<>();
        // Convert to DTO
        for (Request re : data) {
            RequestDTO dto = converter.requestToDTO(re);
            response.add(dto);
        }

        return response;
    }

    @Override
    public boolean approveRequest(Long userId, Long requestId) {
        Optional<Request> checkRequest = requestRepository.getRequestById(requestId);
        if (checkRequest.isEmpty()) {
            return false;
        }
        // Check if the user is the requested
        if (!checkRequest.get().getRequestedUserId().equals(userId.intValue())) {
            return false;
        }
        int rowAffected = requestRepository.approveRequest(requestId);
        if (rowAffected <= 0) {
            return false;
        }
        emailService.sendEmail("Your request has been approved by the requested, request will be closed after 30 days\n"
                ,"Approvement"
                 ,(checkRequest.get().getRequesterUserId()).longValue());
        emailService.sendEmail("You have approved request.\nRequest will be closed after 30 days" +
                        "\nFrom:" + checkRequest.get().getOrigin() + "\nTo:" + checkRequest.get().getDestination()
                        , "Approvement"
                        , (checkRequest.get().getRequestedUserId().longValue()));

        notificationService.saveNotification((checkRequest.get().getRequesterUserId()), "Your request has been approved by the requested" ,requestId.intValue());
        notificationService.saveNotification((checkRequest.get().getRequestedUserId()), "You have approved request" ,requestId.intValue());

        return true;
    }

    @Override
    public List<RequestDTO> filterByApprovement(Long userId) {
        // Get data from database
        List<Request> data = requestRepository.filterByApprovement(userId);

        if (data.isEmpty()) {
            return List.of();
        }

        List<RequestDTO> response = new ArrayList<>();
        // Convert to DTO
        for (Request re : data) {
            RequestDTO dto = converter.requestToDTO(re);
            response.add(dto);
        }

        return response;
    }

    @Override
    public List<RequestDTO> filterByWait(Long userId) {
        // Get data from database
        List<Request> data = requestRepository.filterByWait(userId);

        if (data.isEmpty()) {
            return List.of();
        }

        List<RequestDTO> response = new ArrayList<>();
        // Convert to DTO
        for (Request re : data) {
            RequestDTO dto = converter.requestToDTO(re);
            response.add(dto);
        }

        return response;
    }

    @Override
    public boolean isThereCommonRequest(Long guestId, int hostId) {
        return requestRepository.isThereCommonRequest(guestId, hostId);
    }

    @Override
    public Request getRequestById(Long id) {
        Optional<Request> newRequest = requestRepository.getRequestById(id);
        return newRequest.orElse(null);
    }
}

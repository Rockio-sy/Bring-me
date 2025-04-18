package org.bringme.service.impl;

import org.bringme.dto.RequestDTO;
import org.bringme.exceptions.*;
import org.bringme.model.Item;
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


    /**
     * Retrieves all requests associated with the specified user.
     *
     * @param userId The ID of the user whose requests are to be retrieved.
     * @return A list of RequestDTO objects representing the user's requests.
     * @throws CustomException If no requests are found, throws a NO_CONTENT exception.
     */
    @Override
    public List<RequestDTO> getAll(Long userId) {
        List<Request> requestList = requestRepository.getAll(userId);
        if (requestList.isEmpty()) {
            throw new NotFoundException("No content", "RequestServiceImp::getAll");
        }

        // Convert to DTO
        List<RequestDTO> resposneList = new ArrayList<>();
        for (Request old : requestList) {
            RequestDTO newRequest = converter.requestToDTO(old);
            resposneList.add(newRequest);
        }

        return resposneList;
    }

    /**
     * Saves a new request after validating its associated item and trip.
     *
     * @param request The request DTO to be saved.
     * @return A RequestDTO object representing the saved request with the generated ID.
     * @throws CustomException If the item or trip is not found or if directions are incompatible.
     * @throws CustomException If the user is not authorized to make the request.
     */
    @Override
    public RequestDTO saveRequest(RequestDTO request) {

        // get item, trip and requested user id
        Optional<Item> item = itemRepository.getById(request.getItemId().longValue());
        Optional<Trip> trip = tripRepository.getById(request.getTripId().longValue());
        if (item.isEmpty() || trip.isEmpty()) {
            throw new NotFoundException("Item or trip are not found", "RequestServiceImp::saveRequest");
        }

        if (item.get().getOrigin() != trip.get().getOrigin() || item.get().getDestination() != trip.get().getDestination()) {
            throw new LogicDirectionsOrTimeException("Directions are incompatible");
        }

        // Set values of requested user id, trip and item to save in the database
        if (Objects.equals(item.get().getUser_id(), request.getRequesterUserId())) {
            request.setRequestedUserId(trip.get().getPassengerId());
        } else if (Objects.equals(trip.get().getPassengerId(), request.getRequesterUserId())) {
            request.setRequestedUserId(item.get().getUser_id());
        } else {
            throw new OperationDoesntBelongToUser("Trip or Item do not belong to the current user (who is requesting)", request.getRequesterUserId());

        }

        request.setOrigin(item.get().getOrigin());
        request.setDestination(item.get().getDestination());

        // Convert to Model class
        Request modelRequest = converter.DTOtoRequest(request);

        // Get generated id
        Long generatedId = requestRepository.saveRequest(modelRequest);

        // Convert to DTO
        RequestDTO responseRequest = converter.requestToDTO(modelRequest);

        // Set the generated id to the response request
        responseRequest.setId(generatedId);

        emailService.sendEmail("Your request has been created", "New Request", (request.getRequesterUserId()));
        emailService.sendEmail("Someone is requesting you, check the website for more info", "New Request", request.getRequestedUserId());

        notificationService.saveNotification((request.getRequesterUserId().intValue()), "Your request has been created", responseRequest.getId().intValue());
        notificationService.saveNotification((request.getRequestedUserId().intValue()), "Someone is requesting you, check the website for more info", responseRequest.getId().intValue());


        return responseRequest;
    }

    /**
     * Approves the specified request by its requestId.
     *
     * @param userId    The ID of the user approving the request.
     * @param requestId The ID of the request to be approved.
     * @throws CustomException If the request is not found or if the user is not the requested user.
     */
    @Override
    public void approveRequest(Long userId, Long requestId) {
        Optional<Request> checkRequest = requestRepository.getRequestById(requestId);
        if (checkRequest.isEmpty()) {
            throw new NotFoundException("Request not found", "RequestServiceImp::approveRequest");
        }
        // Check if the user is the requested
        if (!checkRequest.get().getRequestedUserId().equals(userId.intValue())) {
            throw new OperationDoesntBelongToUser("Current user is not the requested user", userId);
        }
        requestRepository.approveRequest(requestId);

        emailService.sendEmail("You have approved request.\nRequest will be closed after 30 days" +
                        "\nFrom:" + checkRequest.get().getOrigin() + "\nTo:" + checkRequest.get().getDestination()
                , "Approvement"
                , (checkRequest.get().getRequestedUserId().longValue()));

        emailService.sendEmail("Your request has been approved by the requested, request will be closed after 30 days\n"
                , "Approvement"
                , (checkRequest.get().getRequesterUserId()).longValue());


        notificationService.saveNotification((checkRequest.get().getRequesterUserId()), "Your request has been approved by the requested", requestId.intValue());
        notificationService.saveNotification((checkRequest.get().getRequestedUserId()), "You have approved request", requestId.intValue());
    }

    /**
     * Checks if a request already exists for the specified item and trip.
     *
     * @param itemId The ID of the item.
     * @param tripId The ID of the trip.
     * @throws CustomException If a request already exists for the specified item and trip.
     */
    @Override
    public void isExist(Integer itemId, Integer tripId) {
        Long id = requestRepository.isExists(itemId, tripId);
        if (id != null) {
            throw new AlreadyExistedException("Request already exist", "Request");
        }
    }

    /**
     * Retrieves all sent requests by the specified user.
     *
     * @param userId The ID of the user whose sent requests are to be retrieved.
     * @return A list of RequestDTO objects representing the sent requests.
     * @throws CustomException If no sent requests are found, throws a NO_CONTENT exception.
     */
    @Override
    public List<RequestDTO> getSentRequests(Long userId) {
        // Get data from database
        List<Request> data = requestRepository.getSentRequests(userId);

        if (data.isEmpty()) {
            throw new NotFoundException("No content", "RequestServiceImp::getSentRequests");
        }

        List<RequestDTO> response = new ArrayList<>();
        // Convert to DTO
        for (Request re : data) {
            RequestDTO dto = converter.requestToDTO(re);
            response.add(dto);
        }

        return response;
    }

    /**
     * Filters requests by the specified origin and destination.
     *
     * @param userId      The ID of the user whose requests are to be filtered.
     * @param origin      The origin location of the requests.
     * @param destination The destination location of the requests.
     * @return A list of RequestDTO objects matching the specified origin and destination.
     * @throws CustomException If no matching requests are found, throws a NO_CONTENT exception.
     */
    @Override
    public List<RequestDTO> filterByDirections(Long userId, int origin, int destination) {
        // Get data from database
        List<Request> data = requestRepository.getByDirections(userId, origin, destination);

        if (data.isEmpty()) {
            throw new NotFoundException("No content", "RequestServiceImp::filterByDirections");
        }

        List<RequestDTO> response = new ArrayList<>();
        // Convert to DTO
        for (Request re : data) {
            RequestDTO dto = converter.requestToDTO(re);
            response.add(dto);
        }
        return response;
    }

    /**
     * Retrieves all received requests for the specified user.
     *
     * @param userId The ID of the user whose received requests are to be retrieved.
     * @return A list of RequestDTO objects representing the received requests.
     * @throws CustomException If no received requests are found, throws a NO_CONTENT exception.
     */
    @Override
    public List<RequestDTO> getReceivedRequests(Long userId) {
        // Get data from database
        List<Request> data = requestRepository.getReceivedRequests(userId);

        if (data.isEmpty()) {
            throw new NotFoundException("No content", "RequestServiceImp::getReceivedRequests");
        }

        List<RequestDTO> response = new ArrayList<>();
        // Convert to DTO
        for (Request re : data) {
            RequestDTO dto = converter.requestToDTO(re);
            response.add(dto);
        }

        return response;
    }

    /**
     * Filters requests by their approval status.
     *
     * @param userId The ID of the user whose requests are to be filtered.
     * @return A list of RequestDTO objects that are filtered by their approval status.
     * @throws CustomException If no matching requests are found, throws a NO_CONTENT exception.
     */
    @Override
    public List<RequestDTO> filterByApprovement(Long userId) {
        // Get data from database
        List<Request> data = requestRepository.filterByApprovement(userId);

        if (data.isEmpty()) {
            throw new NotFoundException("No content", "RequestServiceImp::filterByApprovement");
        }

        List<RequestDTO> response = new ArrayList<>();
        // Convert to DTO
        for (Request re : data) {
            RequestDTO dto = converter.requestToDTO(re);
            response.add(dto);
        }

        return response;
    }

    /**
     * Filters requests that are currently waiting for approval.
     *
     * @param userId The ID of the user whose requests are to be filtered.
     * @return A list of RequestDTO objects representing requests that are waiting for approval.
     * @throws CustomException If no matching requests are found, throws a NO_CONTENT exception.
     */
    @Override
    public List<RequestDTO> filterByWait(Long userId) {
        // Get data from database
        List<Request> data = requestRepository.filterByWait(userId);

        if (data.isEmpty()) {
            throw new NotFoundException("No content", "RequestServiceImp::filterByWait");
        }

        List<RequestDTO> response = new ArrayList<>();
        // Convert to DTO
        for (Request re : data) {
            RequestDTO dto = converter.requestToDTO(re);
            response.add(dto);
        }

        return response;
    }

    /**
     * Checks if there is a common request between a guest and a host.
     *
     * @param guestId The ID of the guest.
     * @param hostId  The ID of the host.
     * @return true if a common request exists; otherwise, throws a CustomException with a BAD_REQUEST status.
     * @throws CustomException If no common request is found between the guest and host.
     */
    @Override
    public boolean isThereCommonRequest(Long guestId, int hostId) {
        if (requestRepository.isThereCommonRequest(guestId, hostId)) {
            throw new NoCommonRequestException((long)hostId, guestId.intValue());
        }
        return true;
    }

    /**
     * Retrieves a request by its unique requestId.
     *
     * @param id The ID of the request to retrieve.
     * @return A Request object representing the requested request.
     * @throws CustomException If the request is not found, throws a NOT_FOUND exception.
     */
    @Override
    public Request getRequestById(Long id) {
        Optional<Request> newRequest = requestRepository.getRequestById(id);
        if (newRequest.isEmpty()) {
            throw new NotFoundException("Not found", "RequestServiceImp::getRequestById");
        }
        return newRequest.get();
    }
}

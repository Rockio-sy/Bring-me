package org.bringme.utils;

import org.bringme.dto.*;
import org.bringme.model.*;
import org.springframework.stereotype.Component;

/**
 * Converters functions to convert from DTO to DAO and vice versa
 */
@Component
public class Converter {

    /**
     * @param itemDTO to be converted
     * @return {@link Item} object
     */
    public Item DTOtoItem(ItemDTO itemDTO){
        return new Item(
                null,
                itemDTO.getName(),
                itemDTO.getOrigin(),
                itemDTO.getDestination(),
                itemDTO.getWeight(),
                itemDTO.getHeight(),
                itemDTO.getLength(),
                itemDTO.getComments(),
                itemDTO.getDetailedOriginAddress(),
                itemDTO.getPhoto(),
                itemDTO.getUser_id()
        );
    }

    /**
     * @param item Entity to be converted
     * @return {@link ItemDTO} object
     */
    public ItemDTO itemToDTO(Item item){
        return new ItemDTO(
                item.getId(),
                item.getName(),
                item.getOrigin(),
                item.getDestination(),
                item.getWeight(),
                item.getHeight(),
                item.getLength(),
                item.getComments(),
                item.getDetailedOriginAddress(),
                item.getPhoto(),
                item.getUser_id());
    }


    /**
     *
     * @param tripDTO to be converted
     * @return {@link Trip} object
     */
    public Trip DTOtoTrip(TripDTO tripDTO){
        Trip trip= new Trip();
        trip.setId(null);
        trip.setOrigin(tripDTO.getOrigin());
        trip.setDestination(tripDTO.getDestination());
        trip.setDestinationAirport(tripDTO.getDestinationAirport());
        trip.setEmptyWeight(tripDTO.getEmptyWeight());
        trip.setArrivalTime(tripDTO.getArrivalTime());
        trip.setDepartureTime(tripDTO.getDepartureTime());
        trip.setTransit(tripDTO.isTransit());
        trip.setComments(tripDTO.getComments());
        trip.setPassengerId(tripDTO.getPassengerId());
        return trip;
    }

    /**
     *
     * @param trip to be converted
     * @return {@link TripDTO} object
     */
    public TripDTO tripToDTO(Trip trip){
        return new TripDTO(
                trip.getId(),
                trip.getOrigin(),
                trip.getDestination(),
                trip.getDestinationAirport(),
                trip.getEmptyWeight(),
                trip.getArrivalTime(),
                trip.getDepartureTime(),
                trip.isTransit(),
                trip.getComments(),
                trip.getPassengerId()
        );
    }

    /**
     *
     * @param request to be converted
     * @return {@link RequestDTO} object
     */
    public RequestDTO requestToDTO(Request request){
        RequestDTO dto = new RequestDTO();
        dto.setId(request.getId());
        dto.setRequesterUserId(request.getRequesterUserId().longValue());
        dto.setRequestedUserId(request.getRequestedUserId().longValue());
        dto.setItemId(request.getItemId());
        dto.setTripId(request.getTripId());
        dto.setOrigin(request.getOrigin());
        dto.setDestination(request.getDestination());
        dto.setComments(request.getComments());
        dto.setApprovement(request.isApprovement());
        dto.setPrice(request.getPrice());
        dto.setCurrency(request.getCurrency());
        return dto;
    }

    /**
     *
     * @param requestDTO to be converted
     * @return {@link Request} object
     */
    public Request DTOtoRequest(RequestDTO requestDTO){
        Request newRequest = new Request();
        newRequest.setId(null);
        newRequest.setRequesterUserId(requestDTO.getRequesterUserId().intValue());
        newRequest.setRequestedUserId(requestDTO.getRequestedUserId().intValue());
        newRequest.setItemId(requestDTO.getItemId());
        newRequest.setTripId(requestDTO.getTripId());
        newRequest.setOrigin(requestDTO.getOrigin());
        newRequest.setDestination(requestDTO.getDestination());
        newRequest.setComments(requestDTO.getComments());
        newRequest.setApprovement(requestDTO.isApprovement());
        newRequest.setPrice(requestDTO.getPrice());
        newRequest.setCurrency(requestDTO.getCurrency());
        return newRequest;
    }

    /**
     * @param personDTO  to be converted
     * @return {@link Person} object
     */
    public Person DTOtoPerson(PersonDTO personDTO){
        Person newPerson = new Person();
        newPerson.setId(null);
        newPerson.setFirstName(personDTO.getFirstName());
        newPerson.setLastName(personDTO.getLastName());
        newPerson.setAddress(personDTO.getAddress());
        newPerson.setPhone(personDTO.getPhone());
        newPerson.setEmail(personDTO.getEmail());
        newPerson.setPassword(personDTO.getPassword());
        newPerson.setAccountStatus(1);
        return newPerson;
    }

    /**
     *
     * @param person model to be converted
     * @return {@link PersonDTO} object
     */
    public PersonDTO personToDTO(Person person){
        PersonDTO dto = new PersonDTO();
        dto.setId(person.getId());
        dto.setFirstName(person.getFirstName());
        dto.setLastName(person.getLastName());
        dto.setAddress(person.getAddress());
        dto.setPhone(person.getPhone());
        dto.setEmail(person.getEmail());
        dto.setPassword(person.getPassword());
        return dto;
    }

    /**
     * Specified converter to {@link Person} model for {@link org.bringme.controller.RequestController#getUserDetails(java.lang.String, int) getUserDetailes endpoint}
     *
     * @param person Model representing the person.
     * @return {@link PersonDTO} with name, address, email, and phone only.
     */

    public PersonDTO personToDetails(Person person){
        PersonDTO dto = new PersonDTO();
        dto.setFirstName(person.getFirstName());
        dto.setLastName(person.getLastName());
        dto.setAddress(person.getAddress());
        dto.setEmail(person.getEmail());
        dto.setPhone(person.getPhone());
        return dto;
    }

    /**
     * @param model notification model to be converted
     * @return {@link NotificationDTO} object
     */
    public NotificationDTO notificationToDTO(Notification model){
        return new NotificationDTO(model.getId(), model.getUserId(), model.getContent(), model.isMarked(), model.getRequestId());
    }

    /**
     *
     * @param model {@link Report} model to be converted
     * @return {@link ReportDTO} object
     */
    public ReportDTO reportToDTO(Report model){
        ReportDTO dto = new ReportDTO();
        dto.setId(model.getId());
        dto.setReporterUserId(model.getReporterUserId());
        dto.setReportedUserId(model.getReportedUserId());
        dto.setRequestId(model.getRequestId());
        dto.setAnswer(model.getAnswer());
        dto.setAnsweredById(model.getAnsweredById());
        dto.setContent(model.getContent());
        return dto;
    }

    /**
     *
     * @param reportDTO to be converted
     * @return {@link Report} object
     */
    public Report DTOtoReport(ReportDTO reportDTO){
        Report model = new Report();
        model.setId(reportDTO.getId());
        model.setReporterUserId(reportDTO.getReporterUserId());
        model.setReportedUserId(reportDTO.getReportedUserId());
        model.setRequestId(reportDTO.getRequestId());
        model.setAnswer(reportDTO.getAnswer());
        model.setAnsweredById(reportDTO.getAnsweredById());
        model.setContent(reportDTO.getContent());
        return model;
    }

    /**
     *
     * @param r {@link Rate} to be converted
     * @return {@link RateDTO} object
     */
    public RateDTO rateToDTO(Rate r) {
        return new RateDTO(r.getId(), r.getUserId(), r.getComments(), r.getValue(), r.getRequestId());
    }

    /**
     *
     * @param r {@link RateDTO} to be converted
     * @return {@link Rate} object
     */
    public Rate DTOtoRate(RateDTO r){
        Rate model = new Rate();
        model.setValue(r.value());
        model.setComments(r.comment());
        model.setRequestId(r.requestId());
        model.setUserId(r.ratedUserId());
        return model;
    }
}

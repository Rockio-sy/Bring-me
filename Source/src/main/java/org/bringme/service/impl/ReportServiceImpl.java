package org.bringme.service.impl;

import org.bringme.dto.ReportDTO;
import org.bringme.exceptions.*;
import org.bringme.model.Report;
import org.bringme.model.Request;
import org.bringme.repository.ReportRepository;
import org.bringme.repository.RequestRepository;
import org.bringme.service.ReportService;
import org.bringme.utils.Converter;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final RequestRepository requestRepository;
    private final Converter converter;

    public ReportServiceImpl(ReportRepository reportRepository, RequestRepository requestRepository, Converter converter) {
        this.reportRepository = reportRepository;
        this.requestRepository = requestRepository;
        this.converter = converter;
    }

    /**
     * Validates the report form to ensure that the report can be created.
     *
     * @param form   The report form containing the report details.
     * @param userID The ID of the user submitting the report.
     * @return A boolean indicating whether the form is valid.
     * @throws CustomException If the request doesn't exist, is not approved, or the user does not belong to the request, throws BAD_REQUEST or FORBIDDEN exception.
     */
    @Override
    public boolean validateReportForm(ReportDTO form, Long userID) {
        Long requestId = Integer.toUnsignedLong(form.getRequestId());
        Optional<Request> requestModel = requestRepository.getRequestById(requestId);

        if (requestModel.isEmpty()) {
            throw new NotFoundException("Request doesn't exist", "ReportService::validateReportForm");
        }

        if (!requestModel.get().isApprovement()) {
            throw new RequestNotApprovedException("Request is not approved", userID);
        }

        if (!(userID.intValue() == (requestModel.get().getRequesterUserId())
                || userID.intValue() == (requestModel.get().getRequestedUserId()))) {
            throw new OperationDoesntBelongToUser("User doesn't belong to this request", userID);
        }
        return true;
    }

    /**
     * Creates a new report based on the provided report form and user ID.
     *
     * @param form   The report form containing the details of the report.
     * @param userID The ID of the user submitting the report.
     * @return A ReportDTO object representing the created report.
     * @throws CustomException If the request is not found or there is an error creating the report, throws BAD_REQUEST or INTERNAL_SERVER_ERROR exception.
     */
    @Override
    public ReportDTO createNewReport(ReportDTO form, Long userID) {
        Long requestId = Integer.toUnsignedLong(form.getRequestId());
        Optional<Request> requestModel = requestRepository.getRequestById(requestId);

        // Extract reporter and reported users
        if (requestModel.isEmpty()) {
            throw new NotFoundException("Request not found", "ReportService::createNewReport");
        }
        var request = requestModel.get();
        int reporter;
        int reported;
        if (request.getRequestedUserId() == userID.intValue()) {
            reported = userID.intValue();
            reporter = request.getRequesterUserId();
        } else {
            reporter = userID.intValue();
            reported = request.getRequestedUserId();
        }

        // Save report in database
        Report model = new Report(requestId.intValue(), reporter, reported, form.getContent());
        try {
            Long reportId = reportRepository.save(model);

            model.setId(reportId);
            return converter.reportToDTO(model);
        }catch (EmptyResultDataAccessException e){
            throw new CannotGetIdOfInsertDataException("ReportServiceImpl::CreateNewReport", e);
        }
    }

    /**
     * Retrieves all reports from the database.
     *
     * @return A list of ReportDTO objects representing all reports.
     * @throws CustomException If no reports are found, throws a NO_CONTENT exception.
     */
    @Override
    public List<ReportDTO> getAll() {
        List<Report> data = reportRepository.getAll();
        if (data.isEmpty()) {
            throw new NotFoundException("No data", "ReportService::getAll");
        }
        List<ReportDTO> response = new ArrayList<>();
        for (Report re : data) {
            ReportDTO dto = converter.reportToDTO(re);
            response.add(dto);
        }
        return response;
    }

    /**
     * Retrieves all reports that have not been answered.
     *
     * @return A list of ReportDTO objects representing reports that have not been answered.
     * @throws CustomException If no unanswered reports are found, throws a NO_CONTENT exception.
     */
    @Override
    public List<ReportDTO> getNotAnswered() {
        List<Report> data = reportRepository.getNotAnswered();
        if (data.isEmpty()) {
            throw new NotFoundException("No data", "ReportService::getNotAnswered");
        }
        List<ReportDTO> response = new ArrayList<>();
        for (Report re : data) {
            ReportDTO dto = converter.reportToDTO(re);
            response.add(dto);
        }
        return response;
    }

    /**
     * Retrieves a specific report by its ID.
     *
     * @param id The ID of the report to retrieve.
     * @return A ReportDTO object representing the specific report.
     * @throws CustomException If the report is not found, throws a NOT_FOUND exception.
     */
    @Override
    public ReportDTO getSpecific(Long id) {
        Optional<Report> data = reportRepository.getSpecific(id);
        if (data.isEmpty()) {
            throw new NotFoundException("Report doesn't exist", "ReportService::getSpecific");
        }
        return converter.reportToDTO(data.get());
    }

    /**
     * Allows an admin to answer a report.
     *
     * @param adminId  The ID of the admin answering the report.
     * @param reportId The ID of the report being answered.
     * @param answer   The answer to the report.
     * @throws CustomException If the report doesn't exist, is already answered, or if the admin attempts to answer an invalid report, throws NOT_FOUND or FORBIDDEN exception.
     */
    @Override
    public void answerReport(Long adminId, Long reportId, String answer) {
        Optional<Report> check = reportRepository.getById(reportId);
        if (check.isEmpty()) {
            throw new NotFoundException("Report doesn't exist", "ReportService::getSpecific");
        }
        if (!(check.get().getAnswer().isEmpty())) {
            throw new ReportAlreadyAnsweredException();
        }
        reportRepository.answerReport(reportId, adminId, answer);
    }
}
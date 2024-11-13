package org.bringme.service.impl;

import org.bringme.dto.ReportDTO;
import org.bringme.model.Report;
import org.bringme.model.Request;
import org.bringme.repository.ReportRepository;
import org.bringme.repository.RequestRepository;
import org.bringme.service.ReportService;
import org.bringme.service.exceptions.CustomException;
import org.bringme.utils.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;
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

    @Override
    public boolean validateReportForm(ReportDTO form, Long userID) {
        Long requestId = Integer.toUnsignedLong(form.getRequestId());
        Optional<Request> requestModel = requestRepository.getRequestById(requestId);

        if (requestModel.isEmpty()) {
            throw new CustomException("Request doesn't exist", HttpStatus.BAD_REQUEST);
        }

        if (!requestModel.get().isApprovement()) {
            throw new CustomException("Request is not approved", HttpStatus.BAD_REQUEST);
        }

        if (!(userID.intValue() == (requestModel.get().getRequesterUserId())
                || userID.intValue() == (requestModel.get().getRequestedUserId()))) {
            throw new CustomException("User doesn't belong to this request", HttpStatus.BAD_REQUEST);
        }
        return true;
    }

    @Override
    public ReportDTO createNewReport(ReportDTO form, Long userID) {
        Long requestId = Integer.toUnsignedLong(form.getRequestId());
        Optional<Request> requestModel = requestRepository.getRequestById(requestId);


        // Extract reporter and reported users
        var request = requestModel.get();
        int reporter;
        int reported;
        if(request.getRequestedUserId() == userID.intValue()){
            reported = userID.intValue();
            reporter = request.getRequesterUserId();
        }else{
            reporter = userID.intValue();
            reported = request.getRequestedUserId();
        }
        // Save report in database
        Report model = new Report(requestId.intValue(), reporter, reported, form.getContent());
        Long reportId = reportRepository.save(model);
        if(reportId == null){
            throw new CustomException("Error creating the report", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        model.setId(reportId);
        return converter.reportToDTO(model);
    }
}
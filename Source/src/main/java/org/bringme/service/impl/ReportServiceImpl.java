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
            throw new CustomException("User doesn't belong to this request", HttpStatus.FORBIDDEN);
        }
        return true;
    }

    @Override
    public ReportDTO createNewReport(ReportDTO form, Long userID) {
        Long requestId = Integer.toUnsignedLong(form.getRequestId());
        Optional<Request> requestModel = requestRepository.getRequestById(requestId);

        // Extract reporter and reported users
        if (requestModel.isEmpty()) {
            throw new CustomException("Request not found", HttpStatus.BAD_REQUEST);
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
        Long reportId = reportRepository.save(model);
        if (reportId == null) {
            throw new CustomException("Error creating the report", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        model.setId(reportId);
        return converter.reportToDTO(model);
    }

    @Override
    public List<ReportDTO> getAll() {
        List<Report> data = reportRepository.getAll();
        if (data.isEmpty()) {
            throw new CustomException("No data", HttpStatus.NO_CONTENT);
        }
        List<ReportDTO> response = new ArrayList<>();
        for (Report re : data) {
            ReportDTO dto = converter.reportToDTO(re);
            response.add(dto);
        }
        return response;
    }

    @Override
    public List<ReportDTO> getNotAnswered() {
        List<Report> data = reportRepository.getNotAnswered();
        if (data.isEmpty()) {
            throw new CustomException("No data", HttpStatus.NO_CONTENT);
        }
        List<ReportDTO> response = new ArrayList<>();
        for (Report re : data) {
            ReportDTO dto = converter.reportToDTO(re);
            response.add(dto);
        }
        return response;
    }

    @Override
    public ReportDTO getSpecific(Long id) {
        Optional<Report> data = reportRepository.getSpecific(id);
        if (data.isEmpty()) {
            throw new CustomException("Report doesn't exist", HttpStatus.NOT_FOUND);
        }
        return converter.reportToDTO(data.get());
    }

    @Override
    public void answerReport(Long adminId, Long reportId, String answer) {
        Optional<Report> check = reportRepository.getById(reportId);
        if (check.isEmpty()) {
            throw new CustomException("Report doesn't exist", HttpStatus.NOT_FOUND);
        }
        if (!(check.get().getAnswer().isEmpty())) {
            throw new CustomException("Report already answered", HttpStatus.FORBIDDEN);
        }
        reportRepository.answerReport(reportId, adminId, answer);
    }
}
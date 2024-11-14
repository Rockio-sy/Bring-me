package org.bringme.service;

import org.bringme.dto.ReportDTO;

import java.util.List;

public interface ReportService {
    boolean validateReportForm(ReportDTO form, Long id);
    ReportDTO createNewReport(ReportDTO form, Long id);

    List<ReportDTO> getAll();

    List<ReportDTO> getNotAnswered();

    ReportDTO getSpecific(Long id);

    void answerReport(Long adminId, Long reportId, String answer);
}

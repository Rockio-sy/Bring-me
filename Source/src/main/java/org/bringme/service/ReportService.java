package org.bringme.service;

import org.bringme.dto.ReportDTO;

public interface ReportService {
    boolean validateReportForm(ReportDTO form, Long id);
    ReportDTO createNewReport(ReportDTO form, Long id);
}

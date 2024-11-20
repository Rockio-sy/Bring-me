package org.bringme.repository;

import org.bringme.model.Report;

import java.util.List;
import java.util.Optional;

public interface ReportRepository {
    Long save(Report model);

    List<Report> getAll();

    List<Report> getNotAnswered();

    Optional<Report> getSpecific(Long id);

    Optional<Report> getById(Long reportId);

    void answerReport(Long reportId, Long adminId, String answer);

    int getReportedUserId(Long reportId);
}

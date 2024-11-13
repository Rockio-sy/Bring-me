package org.bringme.repository;

import org.bringme.model.Report;

import java.util.List;
import java.util.Optional;

public interface ReportRepository {
    Long save(Report model);

    List<Report> getAll();

    List<Report> getNotAnswered();

    Optional<Report> getSpecific(Long id);
}

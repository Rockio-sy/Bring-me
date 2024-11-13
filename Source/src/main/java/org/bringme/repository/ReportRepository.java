package org.bringme.repository;

import org.bringme.model.Report;

import java.util.List;

public interface ReportRepository {
    Long save(Report model);

    List<Report> getAll();
}

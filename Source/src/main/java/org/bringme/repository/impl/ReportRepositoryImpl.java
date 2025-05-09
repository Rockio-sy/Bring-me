package org.bringme.repository.impl;

import org.bringme.exceptions.CannotGetIdOfInsertDataException;
import org.bringme.model.Report;
import org.bringme.repository.ReportRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class ReportRepositoryImpl implements ReportRepository {
    private final JdbcTemplate jdbcTemplate;

    public ReportRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Saves a new report in the database.
     *
     * @param model the {@link Report} object to be saved
     * @return the generated ID of the inserted report or {@code null} if an error occurs
     */
    @Override
    public Long save(Report model) {
        String sql = "INSERT INTO reports (request_id, reporter_user_id, reported_user_id, content)" +
                "VALUES " +
                "(?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setInt(1, model.getRequestId());
                ps.setInt(2, model.getReporterUserId());
                ps.setInt(3, model.getReportedUserId());
                ps.setString(4, model.getContent());
                return ps;
            }, keyHolder);

            return Objects.requireNonNull(keyHolder.getKey()).longValue();
        } catch (DataAccessException e) {
            throw new CannotGetIdOfInsertDataException("SaveReport", e);
        }
    }

    /**
     * Retrieves all reports from the database.
     *
     * @return a list of all {@link Report} objects
     */
    @Override
    public List<Report> getAll() {
        String sql = "SELECT * FROM reports";
        return jdbcTemplate.query(sql, new ReportRowMapper());
    }

    /**
     * Retrieves all reports that have not been answered yet.
     *
     * @return a list of {@link Report} objects with no answer
     */
    @Override
    public List<Report> getNotAnswered() {
        String sql = "SELECT * FROM reports WHERE answered_at = null";
        return jdbcTemplate.query(sql, new ReportRowMapper());
    }

    /**
     * Retrieves a specific report by its ID.
     *
     * @param id the ID of the report
     * @return an {@link Optional} containing the {@link Report} if found, otherwise empty
     */
    @Override
    public Optional<Report> getSpecific(Long id) {
        String sql = "SELECT * FROM reports WHERE id = ?";
        return jdbcTemplate.query(sql, new ReportRowMapper(), id).stream().findFirst();
    }

    /**
     * Retrieves a report by its ID.
     *
     * @param reportId the ID of the report
     * @return an {@link Optional} containing the {@link Report} if found, otherwise empty
     */
    @Override
    public Optional<Report> getById(Long reportId) {
        String sql = "SELECT * FROM reports WHERE id = ?";
        return jdbcTemplate.query(sql, new ReportRowMapper(), reportId).stream().findFirst();
    }

    /**
     * Updates a report with an admin's response.
     *
     * @param reportId the ID of the report being answered
     * @param adminId  the ID of the admin responding to the report
     * @param answer   the response content
     */
    @Override
    public void answerReport(Long reportId, Long adminId, String answer) {
        String sql = "UPDATE reports SET answer = ?, answered_by_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, answer, adminId, reportId);
    }

    /**
     * Retrieves the ID of the user who was reported in a specific report.
     *
     * @param reportId the ID of the report
     * @return the reported user's ID, or -1 if not found
     */
    @Override
    public int getReportedUserId(Long reportId) {
        String sql = "SELECT reported_user_id FROM reports WHERE id = ?";
        Integer id = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getInt("reported_user_id"), reportId);
        // -1 means user doesn't exist
        if (id == null) {
            return -1;
        }
        return id;
    }

    public static final class ReportRowMapper implements RowMapper<Report> {
        @Override
        public Report mapRow(ResultSet rs, int rowNum) throws SQLException {
            Report newReport = new Report();
            newReport.setId(rs.getLong("id"));
            newReport.setReportedUserId(rs.getInt("reported_user_id"));
            newReport.setReporterUserId(rs.getInt("reporter_user_id"));
            newReport.setRequestId(rs.getInt("request_id"));
            newReport.setContent(rs.getString("content"));
            newReport.setAnswer(rs.getString("answer"));
            newReport.setAnsweredById(rs.getInt("answered_by_id"));
            return newReport;
        }
    }
}

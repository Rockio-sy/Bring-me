package org.bringme.repository.impl;

import org.bringme.model.Report;
import org.bringme.repository.ReportRepository;
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
    public ReportRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public Long save(Report model) {
        String sql = "INSERT INTO reports (request_id, reporter_user_id, reported_user_id, content)" +
                "VALUES " +
                "(?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try{
            jdbcTemplate.update(connection ->{
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setInt(1, model.getRequestId());
                ps.setInt(2, model.getReporterUserId());
                ps.setInt(3, model.getReportedUserId());
                ps.setString(4, model.getContent());
                 return ps;
            }, keyHolder);

            return Objects.requireNonNull(keyHolder.getKey()).longValue();
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Report> getAll() {
        String sql = "SELECT * FROM reports";
        return jdbcTemplate.query(sql, new ReportRowMapper());
    }

    @Override
    public List<Report> getNotAnswered() {
        String sql = "SELECT * FROM reports WHERE answered_at = null";
        return jdbcTemplate.query(sql, new ReportRowMapper());
    }

    @Override
    public Optional<Report> getSpecific(Long id) {
        String sql = "SELECT * FROM reports WHERE id = ?";
        return jdbcTemplate.query(sql, new ReportRowMapper(), id).stream().findFirst();
    }

    public static final class ReportRowMapper implements RowMapper<Report>{
        @Override
        public Report mapRow(ResultSet rs, int rowNum) throws SQLException{
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

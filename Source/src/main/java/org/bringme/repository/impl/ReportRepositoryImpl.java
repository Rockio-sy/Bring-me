package org.bringme.repository.impl;

import org.bringme.model.Report;
import org.bringme.repository.ReportRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.Objects;

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
}

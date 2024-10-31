package org.bringme.repository.impl;

import org.bringme.model.Person;
import org.bringme.repository.AuthRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

@Repository
public class AuthRepositoryImpl implements AuthRepository {
    private final JdbcTemplate jdbcTemplate;

    public AuthRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long savePerson(Person person) {
        String sql = "INSERT INTO persons (first_name, last_name, address, email, phone, password) VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setString(1, person.getFirstName());
                ps.setString(2, person.getLastName());
                ps.setString(3, person.getAddress());
                ps.setString(4, person.getEmail());
                ps.setString(5, person.getPhone());
                ps.setString(6, person.getPassword());
                return ps;
            }, keyHolder);
            return Objects.requireNonNull(keyHolder.getKey()).longValue();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

    @Override
    public Optional<Person> getByEmailOrPhone(String emailOrPhone) {
        String sql = "SELECT * FROM persons WHERE email = ? OR phone = ?";
        return jdbcTemplate.query(sql, new AuthRowMapper(), emailOrPhone, emailOrPhone)
                .stream()
                .findFirst();
    }

    @Override
    public Long getIdByEmailOrPhone(String emailOrPhone) {
        String sql = "SELECT id FROM persons WHERE email = ? OR phone = ?";

        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getLong("id"), emailOrPhone, emailOrPhone);
        } catch (EmptyResultDataAccessException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public boolean isVerified(String emailOrPhone) {
        String sql = "SELECT verification FROM persons WHERE email=?, OR phone = ?";
        try {
            int status = Objects.requireNonNull(jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getInt("verification"), emailOrPhone, emailOrPhone));
            if(status > 0){
                return true;
            }
        }catch (EmptyResultDataAccessException e){
            System.out.println(e.getMessage());
            return false;
        }
        return false;
    }


    private static final class AuthRowMapper implements RowMapper<Person> {
        @Override
        public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
            Person newPerson = new Person();
            newPerson.setId(rs.getLong("id"));
            newPerson.setFirstName(rs.getString("first_name"));
            newPerson.setLastName(rs.getString("last_name"));
            newPerson.setAddress(rs.getString("address"));
            newPerson.setEmail(rs.getString("email"));
            newPerson.setPhone(rs.getString("phone"));
            newPerson.setPassword(rs.getString("password"));
            return newPerson;
        }
    }
}

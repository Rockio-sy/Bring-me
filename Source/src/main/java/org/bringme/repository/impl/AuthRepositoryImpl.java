package org.bringme.repository.impl;

import org.bringme.model.Person;
import org.bringme.repository.AuthRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class AuthRepositoryImpl implements AuthRepository {
    private final JdbcTemplate jdbcTemplate;

    public AuthRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Person> getByEmailOrPhone(String emailOrPhone) {
        String sql = "SELECT * FROM persons WHERE email = ? OR phone = ?";
        return jdbcTemplate.query(sql, new PersonRowMapper(), emailOrPhone, emailOrPhone)
                .stream()
                .findFirst();
    }

    private static final class PersonRowMapper implements RowMapper<Person> {
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

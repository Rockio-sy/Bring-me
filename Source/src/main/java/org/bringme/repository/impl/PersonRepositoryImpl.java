package org.bringme.repository.impl;

import org.bringme.dto.PersonDTO;
import org.bringme.model.Person;
import org.bringme.repository.PersonRepository;
import org.springframework.dao.EmptyResultDataAccessException;
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
public class PersonRepositoryImpl implements PersonRepository {

    private final JdbcTemplate jdbcTemplate;

    public PersonRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Optional<Person> getById(Long id) {
        String sql = "SELECT * FROM persons WHERE id = ?";
        return jdbcTemplate.query(sql, new PersonRowMapper(), id)
                .stream()
                .findFirst();
    }


    @Override
    public Optional<Person> getByEmail(String email) {
        String sql = "SELECT * FROM persons WHERE email = ?";
        return jdbcTemplate.query(sql, new PersonRowMapper(), email).stream().findFirst();
    }


    @Override
    public Optional<Person> getByPhone(String phone) {
        String sql = "SELECT * FROM persons WHERE phone=?";
        return jdbcTemplate.query(sql, new PersonRowMapper(), phone).stream().findFirst();
    }

    @Override
    public int updatePassword(Long userId, String newPassword){
        String sql = "UPDATE persons SET password = ? WHERE id = ?";
        return jdbcTemplate.update(sql, newPassword, userId);
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

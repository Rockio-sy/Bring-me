package org.example.repository.impl;

import org.example.model.Person;
import org.example.repository.PersonRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class PersonRepositoryImpl implements PersonRepository {

    private final JdbcTemplate jdbcTemplate;

    public PersonRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Person> getAll() {
        String sql = "SELECT * FROM persons";
        return jdbcTemplate.query(sql, new PersonRowMapper());
    }

    @Override
    public Optional<Person> getById(Long id) {
        String sql = "SELECT * FROM persons WHERE id = ?";
        return jdbcTemplate.query(sql, new PersonRowMapper(), id)
                .stream()
                .findFirst();
    }

    @Override
    public int savePerson(Person person) {
        String sql = "INSERT INTO persons (first_name, last_name, address, email, phone) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                person.getFirstName(),
                person.getLastName(),
                person.getAddress(),
                person.getEmail(),
                person.getPhone());
    }

    @Override
    public int updatePerson(Person person) {
        String sql = "UPDATE persons SET first_name = ?, last_name = ?, address = ?, email = ?, phone = ? WHERE id = ?";
        return jdbcTemplate.update(sql, person.getFirstName(), person.getLastName(), person.getAddress()
        , person.getEmail(), person.getPhone(), person.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM persons WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Person getPersonByPhone(String phone){
        String sql = "SELECT * FROM persons WHERE phone = ?";

        try{
            return jdbcTemplate.queryForObject(sql, new PersonRowMapper(), phone);
        }catch (EmptyResultDataAccessException e){
            return null;
        }
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
            return newPerson;
        }
    }

}

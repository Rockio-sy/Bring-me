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

    public PersonRepositoryImpl(JdbcTemplate jdbcTemplate){
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
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }

    }

    @Override
    public Optional<Person> getByEmail(String email){
        String sql = "SELECT * FROM persons WHERE email = ?";
        return jdbcTemplate.query(sql ,new PersonRowMapper(), email).stream().findFirst();
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
//    @Override
//    public List<Person> getAll() {
//        String sql = "SELECT * FROM persons";
//        return jdbcTemplate.query(sql, new PersonRowMapper());
//    }

//    // Check SQL injections in Docs JDBCTemplate
//    @Override
//    public Optional<Person> getPersonByPhone(String phone){
//        String sql = "SELECT * FROM persons WHERE phone = ?";
//        return jdbcTemplate.query(sql, new PersonRowMapper(), phone)
//                .stream()
//                .findFirst();
//    }

//    @Override
//    public int updatePerson(Person person) {
//        String sql = "UPDATE persons SET first_name = ?, last_name = ?, address = ?, email = ?, phone = ? WHERE id = ?";
//        return jdbcTemplate.update(sql, person.getFirstName(), person.getLastName(), person.getAddress()
//                , person.getEmail(), person.getPhone(), person.getId());
//    }
//
//    @Override
//    public void deleteById(Long id) {
//        String sql = "DELETE FROM persons WHERE id = ?";
//        jdbcTemplate.update(sql, id);
//    }

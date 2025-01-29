package org.bringme.repository.impl;

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
    public int updatePassword(Long userId, String newPassword) {
        String sql = "UPDATE persons SET password = ? WHERE id = ?";
        return jdbcTemplate.update(sql, newPassword, userId);
    }

    @Override
    public void verifyAccount(Long id) {
        String sql = "UPDATE persons SET verification = 1 WHERE id = ?";
        jdbcTemplate.update(sql, id);
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
    public Long save(Person model) {
        String sql = "INSERT INTO persons (first_name, last_name, address, email, phone, password, role)" +
                "VALUES" +
                "(?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setString(1, model.getFirstName());
                ps.setString(2, model.getLastName());
                ps.setString(3, model.getAddress());
                ps.setString(4, model.getEmail());
                ps.setString(5, model.getPhone());
                ps.setString(6, model.getPassword());
                ps.setString(7, model.getRole());
                return ps;
            }, keyHolder);
            return Objects.requireNonNull(keyHolder.getKey()).longValue();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public void unBandUser(Long id){
        String sql = "UPDATE persons SET account_status = 1 WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
    @Override
    public void bandUser(int id) {
        String sql = "UPDATE persons SET account_status = 2 WHERE id = ?";
        jdbcTemplate.update(sql, Integer.toUnsignedLong(id));
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
            newPerson.setRole(rs.getString("role"));
            return newPerson;
        }
    }

}

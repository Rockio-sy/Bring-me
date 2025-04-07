package org.bringme.repository.impl;

import org.bringme.exceptions.CannotGetIdOfInsertDataException;
import org.bringme.model.Person;
import org.bringme.repository.PersonRepository;
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

/**
 * Implementation of {@link PersonRepository} using JDBC.
 * Provides CRUD operations and account management for Person entities.
 */
@Repository
public class PersonRepositoryImpl implements PersonRepository {

    private final JdbcTemplate jdbcTemplate;

    public PersonRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Retrieves a Person by their ID.
     *
     * @param id The ID of the person.
     * @return An {@link Optional} containing the Person if found, otherwise empty.
     */
    @Override
    public Optional<Person> getById(Long id) {
        String sql = "SELECT * FROM persons WHERE id = ?";
        return jdbcTemplate.query(sql, new PersonRowMapper(), id)
                .stream()
                .findFirst();
    }


    /**
     * Retrieves a Person by their email.
     *
     * @param email The email of the person.
     * @return An {@link Optional} containing the Person if found, otherwise empty.
     */
    @Override
    public Optional<Person> getByEmail(String email) {
        String sql = "SELECT * FROM persons WHERE email = ?";
        return jdbcTemplate.query(sql, new PersonRowMapper(), email).stream().findFirst();
    }

    /**
     * Retrieves a Person by their phone number.
     *
     * @param phone The phone number of the person.
     * @return An {@link Optional} containing the Person if found, otherwise empty.
     */
    @Override
    public Optional<Person> getByPhone(String phone) {
        String sql = "SELECT * FROM persons WHERE phone=?";
        return jdbcTemplate.query(sql, new PersonRowMapper(), phone).stream().findFirst();
    }

    /**
     * Updates the password of a given user.
     *
     * @param userId      The ID of the user.
     * @param newPassword The new password to be set.
     * @return Number of rows affected.
     */
    @Override
    public int updatePassword(Long userId, String newPassword) {
        String sql = "UPDATE persons SET password = ? WHERE id = ?";
        return jdbcTemplate.update(sql, newPassword, userId);
    }

    /**
     * Verifies a user's account by updating the verification status.
     *
     * @param id The ID of the user to verify.
     */
    @Override
    public void verifyAccount(Long id) {
        String sql = "UPDATE persons SET verification = 1 WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    /**
     * Retrieves a user's ID using either their email or phone number.
     *
     * @param emailOrPhone The email or phone number of the user.
     * @return The user ID if found, otherwise null.
     */
    @Override
    public Long getIdByEmailOrPhone(String emailOrPhone) {
        String sql = "SELECT id FROM persons WHERE email = ? OR phone = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getLong("id"), emailOrPhone, emailOrPhone);
    }


    /**
     * Saves a new Person to the database.
     *
     * @param model The Person object to save.
     * @return The generated ID of the saved person, or null if an error occurred.
     */
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
        } catch (Exception e) {
            throw new CannotGetIdOfInsertDataException("SavePerson", e);
        }
    }

    /**
     * Unbans a user by updating their account status.
     *
     * @param id The ID of the user to unban.
     */
    @Override
    public void unBandUser(Long id) {
        String sql = "UPDATE persons SET account_status = 1 WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    /**
     * Bans a user by updating their account status.
     *
     * @param id The ID of the user to ban.
     */
    @Override
    public void bandUser(int id) {
        String sql = "UPDATE persons SET account_status = 2 WHERE id = ?";
        jdbcTemplate.update(sql, Integer.toUnsignedLong(id));
    }

    /**
     * RowMapper implementation for mapping ResultSet rows to Person objects.
     */
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
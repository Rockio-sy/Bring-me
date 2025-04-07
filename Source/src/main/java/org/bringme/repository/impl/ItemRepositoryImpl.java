package org.bringme.repository.impl;

import org.bringme.exceptions.CannotGetIdOfInsertDataException;
import org.bringme.model.Item;
import org.bringme.repository.ItemRepository;
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
public class ItemRepositoryImpl implements ItemRepository {

    private final JdbcTemplate jdbcTemplate;

    public ItemRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * List all items saved in database
     * @return List of {@link Item}
     */
    @Override
    public List<Item> getAll() {
        String sql = "SELECT * FROM items";
        return jdbcTemplate.query(sql, new ItemRowMapper());
    }

    /**
     * Get item by id
     * @param id of {@link Item}
     * @return {@link Item} if exists
     */
    @Override
    public Optional<Item> getById(Long id) {
        String sql = "SELECT * FROM items WHERE id = ?";
        return jdbcTemplate.query(sql, new ItemRowMapper(), id)
                .stream()
                .findFirst();
    }

    /**
     * Save new item into the database
     * @param item to be saved
     * @return ID save {@link Item}
     */
    @Override
    public Long saveItem(Item item) {
//        System.out.println("DEBUG:\n In Repository:\n\t function : saveItem.");
        String sql = "INSERT INTO items" +
                " (name, origin, destination, user_id, weight, length, height, full_address, comments, photo)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // KeyHolder to capture the ID
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setString(1, item.getName());
                ps.setInt(2, item.getOrigin());
                ps.setInt(3, item.getDestination());
                ps.setInt(4, item.getUser_id().intValue());
                ps.setDouble(5, item.getWeight());
                ps.setDouble(6, item.getLength());
                ps.setDouble(7, item.getHeight());
                ps.setString(8, item.getDetailedOriginAddress());
                ps.setString(9, item.getComments());
                ps.setString(10, item.getPhoto());
                return ps;
            }, keyHolder);

            // Retrieve and return the generated ID
            return Objects.requireNonNull(keyHolder.getKey()).longValue();
        } catch (Exception e) {
            throw new CannotGetIdOfInsertDataException("SaveItem", e);
        }
    }

    /**
     * Get all countries from specific country to specific country
     * @param origin start country
     * @param destination finish country
     * @return List of {@link Item}
     */
    @Override
    public List<Item> filterByCountries(int origin, int destination) {
        String sql = "SELECT * FROM items WHERE origin = ? AND destination = ?";
        return jdbcTemplate.query(sql, new ItemRowMapper(), origin, destination);
    }


    // RowMapper
    public static final class ItemRowMapper implements RowMapper<Item> {
        @Override
        public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
            Item newItem = new Item();
            newItem.setId(rs.getLong("id"));
            newItem.setName(rs.getString("name"));
            newItem.setOrigin(rs.getInt("origin"));
            newItem.setDestination(rs.getInt("destination"));
            newItem.setWeight(rs.getFloat("weight"));
            newItem.setHeight(rs.getFloat("height"));
            newItem.setLength(rs.getFloat("length"));
            newItem.setDetailedOriginAddress(rs.getString("full_address"));
            newItem.setComments(rs.getString("comments"));
            newItem.setPhoto(rs.getString("photo"));
            newItem.setUser_id(Integer.toUnsignedLong(rs.getInt("user_id")));
            return newItem;
        }
    }
}
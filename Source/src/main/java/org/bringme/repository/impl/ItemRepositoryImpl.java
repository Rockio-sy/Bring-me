package org.bringme.repository.impl;

import org.bringme.model.Item;
import org.bringme.repository.ItemRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private final JdbcTemplate jdbcTemplate;

    public ItemRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Item> getAll() {
        String sql = "SELECT * FROM items";
        return jdbcTemplate.query(sql, new ItemRowMapper());
    }

    @Override
    public Optional<Item> getById(Long id) {
        String sql = "SELECT * FROM items WHERE id = ?";
        return jdbcTemplate.query(sql, new ItemRowMapper(), id)
                .stream()
                .findFirst();
    }

    @Override
    public int saveItem(Item item) {
        String sql = "INSERT INTO items (name, origin, destination) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql,
                item.getName(),
                item.getOrigin(),
                item.getDestination());
    }

    public static final class ItemRowMapper implements RowMapper<Item> {
        @Override
        public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
            Item newItem = new Item();
            newItem.setId(rs.getLong("id"));
            newItem.setName(rs.getString("name"));
            newItem.setOrigin(rs.getString("origin"));
            newItem.setDestination(rs.getString("destination"));
            return newItem;
        }
    }
}

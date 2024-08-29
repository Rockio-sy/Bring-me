CREATE TABLE requests(
    id SERIAL PRIMARY KEY,
    requester_user_id INTEGER NOT NULL,
    requested_user_id INTEGER NOT NULL,
    item_id INTEGER NOT NULL,
    FOREIGN KEY (requester_user_id) REFERENCES persons(id),
    FOREIGN KEY (requested_user_id) REFERENCES persons(id),
    FOREIGN KEY (item_id) REFERENCES items(id)
);
CREATE TABLE rates(
id SERIAL PRIMARY KEY,
rated_user_id INT NOT NULL,
request_id INT NOT NULL,
rate_value INT,
comments TEXT DEFAULT '',
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
CONSTRAINT fk_user_id FOREIGN KEY (rated_user_id) REFERENCES persons(id) ON DELETE CASCADE,
CONSTRAINT fk_request_id FOREIGN KEY (request_id) REFERENCES requests(id) ON DELETE CASCADE
);
CREATE TABLE reports (
id SERIAL PRIMARY KEY,
request_id INT,
reporter_user_id INT,
reported_user_id INT,
answered_by_id INT DEFAULT NULL,
content TEXT NOT NULL,
answer TEXT DEFAULT NULL,
answered_at TIMESTAMP DEFAULT NULL,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (reporter_user_id) REFERENCES persons(id),
FOREIGN KEY (reported_user_id) REFERENCES persons(id),
FOREIGN KEY (answered_by_id) REFERENCES persons(id),
FOREIGN KEY (request_id) REFERENCES requests(id)
);
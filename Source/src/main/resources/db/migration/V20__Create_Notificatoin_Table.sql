CREATE TABLE notification (
id SERIAL PRIMARY KEY,
user_id INTEGER NOT NULL,
content TEXT NOT NULL,
WA_sent_number VARCHAR(15),
request_id INTEGER
)
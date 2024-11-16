ALTER TABLE notifications
ADD CONSTRAINT fk_user_id
FOREIGN KEY (user_id) REFERENCES persons(id)
ON DELETE CASCADE;

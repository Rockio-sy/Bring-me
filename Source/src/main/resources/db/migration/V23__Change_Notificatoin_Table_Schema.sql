ALTER TABLE notification RENAME TO notifications;
ALTER TABLE notifications RENAME wa_sent_number TO marked;
ALTER TABLE notifications ALTER COLUMN marked TYPE INT USING marked:: INTEGER;
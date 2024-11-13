ALTER TABLE reports ALTER COLUMN reported_user_id SET NOT NULL;
ALTER TABLE reports ALTER COLUMN reporter_user_id SET NOT NULL;
ALTER TABLE reports ALTER COLUMN request_id SET NOT NULL;
ALTER TABLE reports ALTER COLUMN answer SET DEFAULT NULL;
ALTER TABLE reports ALTER COLUMN answered_by_id SET DEFAULT NULL;
ALTER TABLE reports ALTER COLUMN answered_at SET DEFAULT NULL;


CREATE OR REPLACE FUNCTION update_answered_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.answered_at := CURRENT_TIMESTAMP;
    RETURN NEW;
END
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_answered_at
BEFORE UPDATE OF answer, answered_by_id ON reports
FOR EACH ROW
EXECUTE FUNCTION update_answered_at_column();
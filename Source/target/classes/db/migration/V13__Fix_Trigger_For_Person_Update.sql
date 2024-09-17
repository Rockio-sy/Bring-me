DROP TRIGGER IF EXISTS update_person_updated_at ON trips;

CREATE TRIGGER update_persons_updated_at
BEFORE UPDATE ON persons
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();
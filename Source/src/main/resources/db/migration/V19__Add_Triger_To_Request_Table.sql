CREATE TRIGGER update_requests_updated_at 
BEFORE UPDATE ON requests 
FOR  EACH ROW EXECUTE FUNCTION update_updated_at_column()

ALTER TABLE items ALTER COLUMN origin TYPE INTEGER USING origin::integer;
ALTER TABLE trips ALTER COLUMN origin TYPE INTEGER USING origin::integer;
ALTER TABLE items ALTER COLUMN destination TYPE INTEGER USING destination::integer;
ALTER TABLE trips ALTER COLUMN destination TYPE INTEGER USING destination::integer;

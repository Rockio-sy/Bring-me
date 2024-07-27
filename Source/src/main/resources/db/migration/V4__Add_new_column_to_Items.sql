-- Add user_id column to table items
ALTER TABLE items ADD COLUMN user_id INT;

-- Define the foreign key constraint
ALTER TABLE items
ADD CONSTRAINT fk_user
FOREIGN KEY (user_id)
REFERENCES persons(id);

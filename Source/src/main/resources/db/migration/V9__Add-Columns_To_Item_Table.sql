ALTER TABLE items ADD created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE items ADD updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE items ADD removed_at TIMESTAMP NULL;
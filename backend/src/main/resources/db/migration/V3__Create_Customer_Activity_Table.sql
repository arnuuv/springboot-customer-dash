CREATE TABLE customer_activity (
  id BIGSERIAL PRIMARY KEY,
  customer_id INTEGER NOT NULL,
  activity_type VARCHAR(50) NOT NULL,
  description TEXT NOT NULL,
  details TEXT,
  ip_address VARCHAR(45),
  user_agent TEXT,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE CASCADE
);
CREATE INDEX idx_customer_activity_customer_id ON customer_activity(customer_id);
CREATE INDEX idx_customer_activity_created_at ON customer_activity(created_at);
CREATE INDEX idx_customer_activity_type ON customer_activity(activity_type);
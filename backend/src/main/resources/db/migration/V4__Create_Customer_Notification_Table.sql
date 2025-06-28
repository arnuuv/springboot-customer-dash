CREATE TABLE customer_notification (
  id BIGSERIAL PRIMARY KEY,
  customer_id INTEGER NOT NULL,
  notification_type VARCHAR(50) NOT NULL,
  title VARCHAR(255) NOT NULL,
  message TEXT NOT NULL,
  is_read BOOLEAN NOT NULL DEFAULT FALSE,
  is_sent BOOLEAN NOT NULL DEFAULT FALSE,
  priority VARCHAR(20) NOT NULL DEFAULT 'NORMAL',
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  read_at TIMESTAMP,
  FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE CASCADE
);
CREATE INDEX idx_customer_notification_customer_id ON customer_notification(customer_id);
CREATE INDEX idx_customer_notification_is_read ON customer_notification(is_read);
CREATE INDEX idx_customer_notification_created_at ON customer_notification(created_at);
CREATE INDEX idx_customer_notification_priority ON customer_notification(priority);
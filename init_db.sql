CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE,
    full_name VARCHAR(100),
    user_type VARCHAR(20) CHECK (user_type IN ('admin', 'staff', 'manager')),
    phone VARCHAR(20),
    is_active BOOLEAN DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT OR IGNORE INTO users (username, password_hash, email, full_name, user_type, phone) 
VALUES ('admin', 'hashed_-1424385949', 'admin@property.com', 'Admin User', 'admin', '555-0123');

INSERT OR IGNORE INTO users (username, password_hash, email, full_name, user_type, phone) 
VALUES ('user', 'hashed_3599307', 'user@property.com', 'Regular User', 'staff', '555-0124');
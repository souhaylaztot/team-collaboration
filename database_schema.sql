                                                                                                                                                            -- Property Management System Database Schema
                                                                                                                                                            -- PostgreSQL Database: property_manager

                                                                                                                                                            -- Users and Authentication
                                                                                                                                                            CREATE TABLE users (
                                                                                                                                                                id SERIAL PRIMARY KEY,
                                                                                                                                                                username VARCHAR(50) UNIQUE NOT NULL,
                                                                                                                                                                password_hash VARCHAR(255) NOT NULL,
                                                                                                                                                                email VARCHAR(100) UNIQUE NOT NULL,
                                                                                                                                                                full_name VARCHAR(100) NOT NULL,
                                                                                                                                                                user_type VARCHAR(20) NOT NULL CHECK (user_type IN ('admin', 'staff', 'manager')),
                                                                                                                                                                phone VARCHAR(20),
                                                                                                                                                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                                                                                                                                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                                                                                                                                is_active BOOLEAN DEFAULT true
                                                                                                                                                            );

                                                                                                                                                            -- Buildings Management
                                                                                                                                                            CREATE TABLE buildings (
                                                                                                                                                                id SERIAL PRIMARY KEY,
                                                                                                                                                                name VARCHAR(100) NOT NULL,
                                                                                                                                                                location VARCHAR(255) NOT NULL,
                                                                                                                                                                floors INTEGER NOT NULL,
                                                                                                                                                                total_apartments INTEGER NOT NULL,
                                                                                                                                                                occupied_apartments INTEGER DEFAULT 0,
                                                                                                                                                                status VARCHAR(20) DEFAULT 'active' CHECK (status IN ('active', 'under_construction', 'maintenance', 'inactive')),
                                                                                                                                                                construction_date DATE,
                                                                                                                                                                description TEXT,
                                                                                                                                                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                                                                                                                                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                                                                                                                                                            );

                                                                                                                                                            -- Apartments/Units
                                                                                                                                                            CREATE TABLE apartments (
                                                                                                                                                                id SERIAL PRIMARY KEY,
                                                                                                                                                                building_id INTEGER REFERENCES buildings(id) ON DELETE CASCADE,
                                                                                                                                                                apartment_number VARCHAR(20) NOT NULL,
                                                                                                                                                                floor INTEGER NOT NULL,
                                                                                                                                                                size VARCHAR(20), -- e.g., "120 m²"
                                                                                                                                                                bedrooms INTEGER,
                                                                                                                                                                bathrooms INTEGER,
                                                                                                                                                                price DECIMAL(15,2) NOT NULL,
                                                                                                                                                                status VARCHAR(20) DEFAULT 'available' CHECK (status IN ('available', 'sold', 'reserved', 'maintenance')),
                                                                                                                                                                description TEXT,
                                                                                                                                                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                                                                                                                                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                                                                                                                                UNIQUE(building_id, apartment_number)
                                                                                                                                                            );

                                                                                                                                                            -- Buyers/Customers
                                                                                                                                                            CREATE TABLE buyers (
                                                                                                                                                                id SERIAL PRIMARY KEY,
                                                                                                                                                                name VARCHAR(100) NOT NULL,
                                                                                                                                                                phone VARCHAR(20),
                                                                                                                                                                email VARCHAR(100),
                                                                                                                                                                address TEXT,
                                                                                                                                                                national_id VARCHAR(50),
                                                                                                                                                                purchase_date DATE,
                                                                                                                                                                purchase_amount DECIMAL(15,2),
                                                                                                                                                                paid_amount DECIMAL(15,2) DEFAULT 0,
                                                                                                                                                                remaining_amount DECIMAL(15,2),
                                                                                                                                                                payment_status VARCHAR(20) DEFAULT 'pending' CHECK (payment_status IN ('paid', 'partial', 'pending', 'overdue')),
                                                                                                                                                                last_payment_date DATE,
                                                                                                                                                                next_due_date DATE,
                                                                                                                                                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                                                                                                                                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                                                                                                                                                            );

                                                                                                                                                            -- Property Purchases (Buyer-Apartment relationship)
                                                                                                                                                            CREATE TABLE property_purchases (
                                                                                                                                                                id SERIAL PRIMARY KEY,
                                                                                                                                                                buyer_id INTEGER REFERENCES buyers(id) ON DELETE CASCADE,
                                                                                                                                                                apartment_id INTEGER REFERENCES apartments(id) ON DELETE CASCADE,
                                                                                                                                                                purchase_date DATE NOT NULL,
                                                                                                                                                                purchase_amount DECIMAL(15,2) NOT NULL,
                                                                                                                                                                paid_amount DECIMAL(15,2) DEFAULT 0,
                                                                                                                                                                payment_status VARCHAR(20) DEFAULT 'pending',
                                                                                                                                                                contract_number VARCHAR(50),
                                                                                                                                                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                                                                                                                                UNIQUE(apartment_id) -- One apartment can only be sold once
                                                                                                                                                            );

                                                                                                                                                            -- Payment History
                                                                                                                                                            CREATE TABLE payments (
                                                                                                                                                                id SERIAL PRIMARY KEY,
                                                                                                                                                                buyer_id INTEGER REFERENCES buyers(id) ON DELETE CASCADE,
                                                                                                                                                                purchase_id INTEGER REFERENCES property_purchases(id) ON DELETE CASCADE,
                                                                                                                                                                amount DECIMAL(15,2) NOT NULL,
                                                                                                                                                                payment_date DATE NOT NULL,
                                                                                                                                                                payment_method VARCHAR(50), -- cash, bank_transfer, check, etc.
                                                                                                                                                                reference_number VARCHAR(100),
                                                                                                                                                                notes TEXT,
                                                                                                                                                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                                                                                                                                                            );

                                                                                                                                                            -- Land Management
                                                                                                                                                            CREATE TABLE lands (
                                                                                                                                                                id SERIAL PRIMARY KEY,
                                                                                                                                                                name VARCHAR(100) NOT NULL,
                                                                                                                                                                location VARCHAR(255) NOT NULL,
                                                                                                                                                                area VARCHAR(50), -- e.g., "5000 m²"
                                                                                                                                                                owner VARCHAR(100),
                                                                                                                                                                estimated_value DECIMAL(15,2),
                                                                                                                                                                status VARCHAR(20) DEFAULT 'available' CHECK (status IN ('available', 'sold', 'reserved', 'development')),
                                                                                                                                                                description TEXT,
                                                                                                                                                                documents_count INTEGER DEFAULT 0,
                                                                                                                                                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                                                                                                                                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                                                                                                                                                            );

                                                                                                                                                            -- Construction Permits
                                                                                                                                                            CREATE TABLE permits (
                                                                                                                                                                id SERIAL PRIMARY KEY,
                                                                                                                                                                permit_number VARCHAR(50) UNIQUE NOT NULL,
                                                                                                                                                                land_id INTEGER REFERENCES lands(id) ON DELETE SET NULL,
                                                                                                                                                                permit_type VARCHAR(50) NOT NULL, -- residential, commercial, renovation, etc.
                                                                                                                                                                requested_by VARCHAR(100) NOT NULL,
                                                                                                                                                                request_date DATE NOT NULL,
                                                                                                                                                                status VARCHAR(20) DEFAULT 'pending' CHECK (status IN ('pending', 'approved', 'rejected', 'expired')),
                                                                                                                                                                estimated_cost DECIMAL(15,2),
                                                                                                                                                                description TEXT,
                                                                                                                                                                approved_by VARCHAR(100),
                                                                                                                                                                approval_date DATE,
                                                                                                                                                                expiry_date DATE,
                                                                                                                                                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                                                                                                                                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                                                                                                                                                            );

                                                                                                                                                            -- Maintenance Requests
                                                                                                                                                            CREATE TABLE maintenance_requests (
                                                                                                                                                                id SERIAL PRIMARY KEY,
                                                                                                                                                                request_number VARCHAR(50) UNIQUE NOT NULL,
                                                                                                                                                                building_id INTEGER REFERENCES buildings(id) ON DELETE CASCADE,
                                                                                                                                                                apartment_id INTEGER REFERENCES apartments(id) ON DELETE SET NULL,
                                                                                                                                                                requested_by VARCHAR(100) NOT NULL,
                                                                                                                                                                issue_title VARCHAR(255) NOT NULL,
                                                                                                                                                                description TEXT NOT NULL,
                                                                                                                                                                priority VARCHAR(10) DEFAULT 'medium' CHECK (priority IN ('low', 'medium', 'high', 'critical')),
                                                                                                                                                                status VARCHAR(20) DEFAULT 'pending' CHECK (status IN ('pending', 'in-progress', 'completed', 'cancelled')),
                                                                                                                                                                estimated_cost DECIMAL(10,2),
                                                                                                                                                                actual_cost DECIMAL(10,2),
                                                                                                                                                                assigned_to VARCHAR(100),
                                                                                                                                                                request_date DATE NOT NULL,
                                                                                                                                                                completion_date DATE,
                                                                                                                                                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                                                                                                                                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                                                                                                                                                            );

                                                                                                                                                            -- Customer Requests/Support Tickets
                                                                                                                                                            CREATE TABLE customer_requests (
                                                                                                                                                                id SERIAL PRIMARY KEY,
                                                                                                                                                                request_number VARCHAR(50) UNIQUE NOT NULL,
                                                                                                                                                                buyer_id INTEGER REFERENCES buyers(id) ON DELETE CASCADE,
                                                                                                                                                                building_id INTEGER REFERENCES buildings(id) ON DELETE SET NULL,
                                                                                                                                                                apartment_id INTEGER REFERENCES apartments(id) ON DELETE SET NULL,
                                                                                                                                                                request_type VARCHAR(50) NOT NULL, -- information, document, maintenance, complaint, other
                                                                                                                                                                subject VARCHAR(255) NOT NULL,
                                                                                                                                                                priority VARCHAR(10) DEFAULT 'medium' CHECK (priority IN ('low', 'medium', 'high')),
                                                                                                                                                                status VARCHAR(20) DEFAULT 'open' CHECK (status IN ('open', 'in-progress', 'responded', 'closed')),
                                                                                                                                                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                                                                                                                                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                                                                                                                                                            );

                                                                                                                                                            -- Request Messages/Conversation
                                                                                                                                                            CREATE TABLE request_messages (
                                                                                                                                                                id SERIAL PRIMARY KEY,
                                                                                                                                                                request_id INTEGER REFERENCES customer_requests(id) ON DELETE CASCADE,
                                                                                                                                                                sender_name VARCHAR(100) NOT NULL,
                                                                                                                                                                message_text TEXT NOT NULL,
                                                                                                                                                                is_staff_message BOOLEAN DEFAULT false,
                                                                                                                                                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                                                                                                                                                            );

                                                                                                                                                            -- Transportation - Vehicles
                                                                                                                                                            CREATE TABLE vehicles (
                                                                                                                                                                id SERIAL PRIMARY KEY,
                                                                                                                                                                vehicle_number VARCHAR(50) UNIQUE NOT NULL,
                                                                                                                                                                vehicle_type VARCHAR(50) NOT NULL, -- passenger, cargo-truck, flatbed, mixer, etc.
                                                                                                                                                                capacity VARCHAR(50), -- "20 passengers", "10 tons", etc.
                                                                                                                                                                status VARCHAR(20) DEFAULT 'available' CHECK (status IN ('available', 'in-use', 'maintenance', 'out-of-service')),
                                                                                                                                                                last_maintenance_date DATE,
                                                                                                                                                                next_maintenance_date DATE,
                                                                                                                                                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                                                                                                                                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                                                                                                                                                            );

                                                                                                                                                            -- Worker Transportation
                                                                                                                                                            CREATE TABLE worker_transports (
                                                                                                                                                                id SERIAL PRIMARY KEY,
                                                                                                                                                                vehicle_id INTEGER REFERENCES vehicles(id) ON DELETE CASCADE,
                                                                                                                                                                driver_name VARCHAR(100) NOT NULL,
                                                                                                                                                                worker_count INTEGER NOT NULL,
                                                                                                                                                                pickup_point VARCHAR(255) NOT NULL,
                                                                                                                                                                destination VARCHAR(255) NOT NULL,
                                                                                                                                                                departure_time TIME NOT NULL,
                                                                                                                                                                arrival_time TIME,
                                                                                                                                                                transport_date DATE NOT NULL,
                                                                                                                                                                status VARCHAR(20) DEFAULT 'scheduled' CHECK (status IN ('scheduled', 'in-transit', 'completed', 'cancelled')),
                                                                                                                                                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                                                                                                                                                            );

                                                                                                                                                            -- Material Transportation
                                                                                                                                                            CREATE TABLE material_transports (
                                                                                                                                                                id SERIAL PRIMARY KEY,
                                                                                                                                                                vehicle_id INTEGER REFERENCES vehicles(id) ON DELETE CASCADE,
                                                                                                                                                                driver_name VARCHAR(100) NOT NULL,
                                                                                                                                                                material_type VARCHAR(100) NOT NULL,
                                                                                                                                                                quantity VARCHAR(50) NOT NULL,
                                                                                                                                                                unit VARCHAR(20) NOT NULL, -- kg, tons, units, etc.
                                                                                                                                                                origin VARCHAR(255) NOT NULL,
                                                                                                                                                                destination VARCHAR(255) NOT NULL,
                                                                                                                                                                departure_time TIME NOT NULL,
                                                                                                                                                                estimated_arrival TIME,
                                                                                                                                                                transport_date DATE NOT NULL,
                                                                                                                                                                status VARCHAR(20) DEFAULT 'scheduled' CHECK (status IN ('scheduled', 'loading', 'in-transit', 'delivered', 'cancelled')),
                                                                                                                                                                urgency VARCHAR(20) DEFAULT 'normal' CHECK (urgency IN ('normal', 'urgent', 'critical')),
                                                                                                                                                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                                                                                                                                                            );

                                                                                                                                                            -- Notifications
                                                                                                                                                            CREATE TABLE notifications (
                                                                                                                                                                id SERIAL PRIMARY KEY,
                                                                                                                                                                notification_type VARCHAR(50) NOT NULL, -- alert, rent, permit, maintenance, tenant, system
                                                                                                                                                                title VARCHAR(255) NOT NULL,
                                                                                                                                                                message TEXT NOT NULL,
                                                                                                                                                                is_read BOOLEAN DEFAULT false,
                                                                                                                                                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                                                                                                                                read_at TIMESTAMP
                                                                                                                                                            );

                                                                                                                                                            -- System Settings
                                                                                                                                                            CREATE TABLE system_settings (
                                                                                                                                                                id SERIAL PRIMARY KEY,
                                                                                                                                                                setting_key VARCHAR(100) UNIQUE NOT NULL,
                                                                                                                                                                setting_value TEXT,
                                                                                                                                                                description TEXT,
                                                                                                                                                                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                                                                                                                                                            );

                                                                                                                                                            -- Audit Log
                                                                                                                                                            CREATE TABLE audit_log (
                                                                                                                                                                id SERIAL PRIMARY KEY,
                                                                                                                                                                user_id INTEGER REFERENCES users(id) ON DELETE SET NULL,
                                                                                                                                                                action VARCHAR(100) NOT NULL,
                                                                                                                                                                table_name VARCHAR(50),
                                                                                                                                                                record_id INTEGER,
                                                                                                                                                                old_values JSONB,
                                                                                                                                                                new_values JSONB,
                                                                                                                                                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                                                                                                                                                            );

                                                                                                                                                            -- Create indexes for better performance
                                                                                                                                                            CREATE INDEX idx_buildings_status ON buildings(status);
                                                                                                                                                            CREATE INDEX idx_apartments_building_id ON apartments(building_id);
                                                                                                                                                            CREATE INDEX idx_apartments_status ON apartments(status);
                                                                                                                                                            CREATE INDEX idx_buyers_payment_status ON buyers(payment_status);
                                                                                                                                                            CREATE INDEX idx_property_purchases_buyer_id ON property_purchases(buyer_id);
                                                                                                                                                            CREATE INDEX idx_payments_buyer_id ON payments(buyer_id);
                                                                                                                                                            CREATE INDEX idx_maintenance_requests_status ON maintenance_requests(status);
                                                                                                                                                            CREATE INDEX idx_maintenance_requests_building_id ON maintenance_requests(building_id);
                                                                                                                                                            CREATE INDEX idx_customer_requests_status ON customer_requests(status);
                                                                                                                                                            CREATE INDEX idx_notifications_is_read ON notifications(is_read);
                                                                                                                                                            CREATE INDEX idx_notifications_type ON notifications(notification_type);

                                                                                                                                                            -- Insert default admin user (password should be hashed in real application)
                                                                                                                                                            INSERT INTO users (username, password_hash, email, full_name, user_type) 
                                                                                                                                                            VALUES ('admin', '$2a$10$example_hash', 'admin@propertymanager.com', 'System Administrator', 'admin');

                                                                                                                                                            -- Insert default system settings
                                                                                                                                                            INSERT INTO system_settings (setting_key, setting_value, description) VALUES
                                                                                                                                                            ('app_name', 'Smart Property Manager Pro', 'Application name'),
                                                                                                                                                            ('theme', 'light', 'Default theme (light/dark)'),
                                                                                                                                                            ('currency', 'MAD', 'Default currency'),
                                                                                                                                                            ('date_format', 'YYYY-MM-DD', 'Default date format');
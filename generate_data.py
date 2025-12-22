import os
import psycopg2
from dotenv import load_dotenv
from faker import Faker
import random
from datetime import datetime, timedelta
import hashlib

# Load environment variables
load_dotenv('db/.env')

# Database configuration
DB_CONFIG = {
    'host': os.getenv('DB_HOST'),
    'database': os.getenv('DB_NAME'),
    'user': os.getenv('DB_USER'),
    'password': os.getenv('DB_PASSWORD'),
    'port': os.getenv('DB_PORT')
}

fake = Faker(['en_US', 'ar_SA'])

def connect_db():
    """Connect to database"""
    return psycopg2.connect(**DB_CONFIG)

def clear_existing_data():
    """Clear existing data (except system data)"""
    conn = connect_db()
    cursor = conn.cursor()
    
    # Clear in reverse dependency order
    tables = [
        'audit_log', 'request_messages', 'customer_requests', 
        'material_transports', 'worker_transports', 'payments',
        'property_purchases', 'maintenance_requests', 'permits',
        'apartments', 'buildings', 'buyers', 'lands', 'vehicles',
        'notifications'
    ]
    
    for table in tables:
        cursor.execute(f"DELETE FROM {table}")
        print(f"Cleared {table}")
    
    conn.commit()
    cursor.close()
    conn.close()

def generate_users():
    """Generate users data"""
    conn = connect_db()
    cursor = conn.cursor()
    
    users_data = [
        ('admin', hashlib.md5('admin123'.encode()).hexdigest(), 'admin@propertymanager.com', 'Ahmed Hassan', 'admin', '+212-6-12-34-56-78'),
        ('manager1', hashlib.md5('manager123'.encode()).hexdigest(), 'manager@propertymanager.com', 'Fatima Zahra', 'manager', '+212-6-87-65-43-21'),
        ('staff1', hashlib.md5('staff123'.encode()).hexdigest(), 'staff1@propertymanager.com', 'Mohammed Alami', 'staff', '+212-6-11-22-33-44'),
        ('staff2', hashlib.md5('staff123'.encode()).hexdigest(), 'staff2@propertymanager.com', 'Aicha Benali', 'staff', '+212-6-55-66-77-88')
    ]
    
    # Clear existing users except system admin
    cursor.execute("DELETE FROM users WHERE username != 'admin'")
    
    for user in users_data:
        cursor.execute("""
            INSERT INTO users (username, password_hash, email, full_name, user_type, phone)
            VALUES (%s, %s, %s, %s, %s, %s)
            ON CONFLICT (username) DO UPDATE SET
            password_hash = EXCLUDED.password_hash,
            email = EXCLUDED.email,
            full_name = EXCLUDED.full_name,
            phone = EXCLUDED.phone
        """, user)
    
    conn.commit()
    cursor.close()
    conn.close()
    print("Generated users data")

def generate_buildings_and_apartments():
    """Generate buildings and apartments data"""
    conn = connect_db()
    cursor = conn.cursor()
    
    # Buildings data matching your Java app
    buildings_data = [
        ("Skyline Tower", "123 Main Street, Downtown Casablanca", 12, 48, 45, "active", "2022-01-15", "Modern residential tower with city views"),
        ("Riverside Apartments", "456 River Road, Westside Rabat", 8, 32, 30, "active", "2021-06-10", "Luxury apartments by the river"),
        ("Garden View Complex", "789 Park Avenue, Eastside Marrakech", 15, 60, 55, "active", "2020-03-20", "Family-friendly complex with gardens"),
        ("Metro Heights", "321 Business District, Casablanca", 10, 40, 38, "active", "2023-02-28", "Modern business district apartments"),
        ("Ocean Breeze", "555 Coastal Road, Agadir", 6, 24, 20, "active", "2022-08-15", "Beachfront residential complex"),
        ("Atlas View", "777 Mountain Road, Fez", 9, 36, 32, "active", "2021-11-30", "Mountain view luxury apartments")
    ]
    
    building_ids = []
    for building in buildings_data:
        cursor.execute("""
            INSERT INTO buildings (name, location, floors, total_apartments, occupied_apartments, status, construction_date, description)
            VALUES (%s, %s, %s, %s, %s, %s, %s, %s) RETURNING id
        """, building)
        building_id = cursor.fetchone()[0]
        building_ids.append((building_id, building[2], building[3]))  # id, floors, total_apartments
    
    # Generate apartments for each building
    apartment_types = [
        ("Studio", 1, 1, 45, 1800000, 2200000),
        ("1BR", 1, 1, 65, 2400000, 2800000),
        ("2BR", 2, 2, 85, 3200000, 3800000),
        ("3BR", 3, 2, 120, 4500000, 5200000),
        ("Penthouse", 4, 3, 180, 7000000, 9000000)
    ]
    
    statuses = ["available", "sold", "reserved"]
    
    for building_id, floors, total_apartments in building_ids:
        apt_count = 0
        for floor in range(1, floors + 1):
            apts_per_floor = total_apartments // floors
            for apt_num in range(1, apts_per_floor + 1):
                if apt_count >= total_apartments:
                    break
                
                apt_number = f"{floor:02d}{apt_num:02d}"
                apt_type = random.choice(apartment_types)
                size = f"{apt_type[3]} m²"
                bedrooms = apt_type[1]
                bathrooms = apt_type[2]
                price = random.randint(apt_type[4], apt_type[5])
                status = random.choice(statuses)
                
                cursor.execute("""
                    INSERT INTO apartments (building_id, apartment_number, floor, size, bedrooms, bathrooms, price, status)
                    VALUES (%s, %s, %s, %s, %s, %s, %s, %s)
                """, (building_id, apt_number, floor, size, bedrooms, bathrooms, price, status))
                
                apt_count += 1
    
    conn.commit()
    cursor.close()
    conn.close()
    print("Generated buildings and apartments data")

def generate_buyers_and_purchases():
    """Generate buyers and property purchases"""
    conn = connect_db()
    cursor = conn.cursor()
    
    # Get sold apartments
    cursor.execute("SELECT id, building_id, price FROM apartments WHERE status = 'sold'")
    sold_apartments = cursor.fetchall()
    
    moroccan_names = [
        "Ahmed Ben Ali", "Fatima Zahra", "Mohammed Alami", "Aicha Benali", "Youssef Tazi",
        "Khadija Fassi", "Omar Benjelloun", "Nadia Chraibi", "Rachid Lahlou", "Samira Kettani",
        "Hassan Berrada", "Zineb Alaoui", "Karim Benkirane", "Leila Tounsi", "Abdellatif Filali",
        "Meryem Idrissi", "Tarik Benali", "Salma Ouazzani", "Driss Benomar", "Houda Lamrani"
    ]
    
    payment_statuses = ["paid", "partial", "pending", "overdue"]
    
    for i, (apt_id, building_id, price) in enumerate(sold_apartments):
        name = random.choice(moroccan_names)
        phone = f"+212-6-{random.randint(10,99)}-{random.randint(10,99)}-{random.randint(10,99)}-{random.randint(10,99)}"
        email = f"{name.lower().replace(' ', '.')}@email.com"
        address = fake.address()
        national_id = f"ID{random.randint(100000, 999999)}"
        
        purchase_date = fake.date_between(start_date='-2y', end_date='today')
        purchase_amount = int(price)
        paid_amount = random.randint(int(purchase_amount * 0.2), int(purchase_amount * 0.9))
        remaining_amount = purchase_amount - paid_amount
        payment_status = random.choice(payment_statuses)
        
        last_payment = fake.date_between(start_date=purchase_date, end_date='today') if paid_amount > 0 else None
        next_due = fake.date_between(start_date='today', end_date='+6m') if remaining_amount > 0 else None
        
        # Insert buyer
        cursor.execute("""
            INSERT INTO buyers (name, phone, email, address, national_id, purchase_date, 
                              purchase_amount, paid_amount, remaining_amount, payment_status, 
                              last_payment_date, next_due_date)
            VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s) RETURNING id
        """, (name, phone, email, address, national_id, purchase_date, purchase_amount, 
              paid_amount, remaining_amount, payment_status, last_payment, next_due))
        
        buyer_id = cursor.fetchone()[0]
        
        # Insert property purchase
        contract_number = f"CNT-{purchase_date.year}-{buyer_id:04d}"
        cursor.execute("""
            INSERT INTO property_purchases (buyer_id, apartment_id, purchase_date, purchase_amount, 
                                          paid_amount, payment_status, contract_number)
            VALUES (%s, %s, %s, %s, %s, %s, %s) RETURNING id
        """, (buyer_id, apt_id, purchase_date, purchase_amount, paid_amount, payment_status, contract_number))
        
        purchase_id = cursor.fetchone()[0]
        
        # Generate payment history
        if paid_amount > 0:
            payment_count = random.randint(1, 5)
            remaining_to_pay = paid_amount
            payment_date = purchase_date
            
            for _ in range(payment_count):
                if remaining_to_pay <= 0:
                    break
                    
                payment_amount = min(remaining_to_pay, random.randint(100000, 500000))
                payment_method = random.choice(["bank_transfer", "cash", "check", "wire_transfer"])
                reference_number = f"PAY-{payment_date.strftime('%Y%m%d')}-{random.randint(1000, 9999)}"
                
                cursor.execute("""
                    INSERT INTO payments (buyer_id, purchase_id, amount, payment_date, payment_method, reference_number)
                    VALUES (%s, %s, %s, %s, %s, %s)
                """, (buyer_id, purchase_id, payment_amount, payment_date, payment_method, reference_number))
                
                remaining_to_pay -= payment_amount
                payment_date += timedelta(days=random.randint(30, 90))
    
    conn.commit()
    cursor.close()
    conn.close()
    print("Generated buyers and purchases data")

def generate_lands():
    """Generate lands data"""
    conn = connect_db()
    cursor = conn.cursor()
    
    lands_data = [
        ("Al Andalus Plot", "Hay Riad, Rabat", "2500 m²", "Mohammed Benali", 15000000, "available", "Prime location for residential development", 3),
        ("Atlas View Land", "Agdal, Rabat", "3200 m²", "Fatima Zahra", 22000000, "development", "Mountain view land with development permit", 5),
        ("Coastal Plot", "Marina, Casablanca", "1800 m²", "Ahmed Tazi", 35000000, "reserved", "Beachfront commercial land", 4),
        ("Industrial Zone A", "Ain Sebaa, Casablanca", "5000 m²", "Rachid Industries", 18000000, "available", "Industrial development zone", 2),
        ("Green Valley", "Souissi, Rabat", "4200 m²", "Nadia Alaoui", 28000000, "available", "Residential land in green area", 6),
        ("Business District Plot", "Maarif, Casablanca", "1500 m²", "Omar Benjelloun", 45000000, "sold", "Commercial business district", 3)
    ]
    
    for land in lands_data:
        cursor.execute("""
            INSERT INTO lands (name, location, area, owner, estimated_value, status, description, documents_count)
            VALUES (%s, %s, %s, %s, %s, %s, %s, %s)
        """, land)
    
    conn.commit()
    cursor.close()
    conn.close()
    print("Generated lands data")

def generate_permits():
    """Generate permits data"""
    conn = connect_db()
    cursor = conn.cursor()
    
    # Get land IDs
    cursor.execute("SELECT id FROM lands LIMIT 6")
    land_ids = [row[0] for row in cursor.fetchall()]
    
    permit_types = ["residential", "commercial", "renovation", "industrial"]
    statuses = ["pending", "approved", "rejected", "expired"]
    
    for i in range(10):
        permit_number = f"PRM-{datetime.now().year}-{i+1:04d}"
        land_id = random.choice(land_ids) if land_ids else None
        permit_type = random.choice(permit_types)
        requested_by = fake.name()
        request_date = fake.date_between(start_date='-1y', end_date='today')
        status = random.choice(statuses)
        estimated_cost = random.randint(500000, 5000000)
        
        approved_by = fake.name() if status == 'approved' else None
        approval_date = request_date + timedelta(days=random.randint(30, 90)) if status == 'approved' else None
        expiry_date = approval_date + timedelta(days=365) if approval_date else None
        
        cursor.execute("""
            INSERT INTO permits (permit_number, land_id, permit_type, requested_by, request_date, 
                               status, estimated_cost, approved_by, approval_date, expiry_date)
            VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
        """, (permit_number, land_id, permit_type, requested_by, request_date, 
              status, estimated_cost, approved_by, approval_date, expiry_date))
    
    conn.commit()
    cursor.close()
    conn.close()
    print("Generated permits data")

def generate_maintenance_requests():
    """Generate maintenance requests"""
    conn = connect_db()
    cursor = conn.cursor()
    
    # Get building and apartment IDs
    cursor.execute("SELECT id FROM buildings")
    building_ids = [row[0] for row in cursor.fetchall()]
    
    cursor.execute("SELECT id FROM apartments LIMIT 20")
    apartment_ids = [row[0] for row in cursor.fetchall()]
    
    issues = [
        "Water leak in bathroom", "Electrical outlet not working", "Air conditioning repair",
        "Door lock replacement", "Window glass broken", "Plumbing issue in kitchen",
        "Heating system malfunction", "Elevator maintenance", "Balcony railing repair",
        "Paint touch-up needed", "Tile replacement", "Light fixture repair"
    ]
    
    priorities = ["low", "medium", "high", "critical"]
    statuses = ["pending", "in-progress", "completed", "cancelled"]
    
    for i in range(15):
        request_number = f"MNT-{datetime.now().year}-{i+1:04d}"
        building_id = random.choice(building_ids)
        apartment_id = random.choice(apartment_ids) if random.choice([True, False]) else None
        requested_by = fake.name()
        issue_title = random.choice(issues)
        description = f"Maintenance required: {issue_title}. {fake.text(max_nb_chars=100)}"
        priority = random.choice(priorities)
        status = random.choice(statuses)
        estimated_cost = random.randint(500, 5000)
        actual_cost = estimated_cost + random.randint(-200, 500) if status == 'completed' else None
        assigned_to = fake.name() if status in ['in-progress', 'completed'] else None
        request_date = fake.date_between(start_date='-6m', end_date='today')
        completion_date = request_date + timedelta(days=random.randint(1, 30)) if status == 'completed' else None
        
        cursor.execute("""
            INSERT INTO maintenance_requests (request_number, building_id, apartment_id, requested_by,
                                            issue_title, description, priority, status, estimated_cost,
                                            actual_cost, assigned_to, request_date, completion_date)
            VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
        """, (request_number, building_id, apartment_id, requested_by, issue_title, description,
              priority, status, estimated_cost, actual_cost, assigned_to, request_date, completion_date))
    
    conn.commit()
    cursor.close()
    conn.close()
    print("Generated maintenance requests data")

def generate_vehicles_and_transportation():
    """Generate vehicles and transportation data"""
    conn = connect_db()
    cursor = conn.cursor()
    
    # Vehicles data
    vehicles_data = [
        ("VH-001", "passenger", "20 passengers", "available"),
        ("VH-002", "cargo-truck", "10 tons", "available"),
        ("VH-003", "flatbed", "15 tons", "in-use"),
        ("VH-004", "mixer", "8 cubic meters", "maintenance"),
        ("VH-005", "passenger", "15 passengers", "available"),
        ("VH-006", "cargo-truck", "12 tons", "available")
    ]
    
    vehicle_ids = []
    for vehicle in vehicles_data:
        last_maintenance = fake.date_between(start_date='-6m', end_date='-1m')
        next_maintenance = last_maintenance + timedelta(days=90)
        
        cursor.execute("""
            INSERT INTO vehicles (vehicle_number, vehicle_type, capacity, status, last_maintenance_date, next_maintenance_date)
            VALUES (%s, %s, %s, %s, %s, %s) RETURNING id
        """, (*vehicle, last_maintenance, next_maintenance))
        
        vehicle_ids.append(cursor.fetchone()[0])
    
    # Worker transportation
    for i in range(8):
        vehicle_id = random.choice(vehicle_ids[:2] + vehicle_ids[4:5])  # Only passenger vehicles
        driver_name = fake.name()
        worker_count = random.randint(5, 20)
        pickup_point = fake.address()
        destination = fake.address()
        departure_hour = random.randint(6, 8)
        departure_minute = random.randint(0, 59)
        departure_time = f"{departure_hour:02d}:{departure_minute:02d}:00"
        arrival_hour = departure_hour + random.randint(1, 3)
        arrival_time = f"{arrival_hour:02d}:{departure_minute:02d}:00"
        transport_date = fake.date_between(start_date='-1m', end_date='+1m')
        status = random.choice(["scheduled", "in-transit", "completed", "cancelled"])
        
        cursor.execute("""
            INSERT INTO worker_transports (vehicle_id, driver_name, worker_count, pickup_point,
                                         destination, departure_time, arrival_time, transport_date, status)
            VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)
        """, (vehicle_id, driver_name, worker_count, pickup_point, destination,
              departure_time, arrival_time, transport_date, status))
    
    # Material transportation
    materials = ["Cement", "Steel bars", "Bricks", "Sand", "Gravel", "Tiles", "Paint", "Glass panels"]
    units = ["tons", "kg", "units", "cubic meters"]
    urgencies = ["normal", "urgent", "critical"]
    
    for i in range(12):
        vehicle_id = random.choice(vehicle_ids[1:4] + vehicle_ids[5:6])  # Only cargo vehicles
        driver_name = fake.name()
        material_type = random.choice(materials)
        quantity = str(random.randint(1, 50))
        unit = random.choice(units)
        origin = fake.address()
        destination = fake.address()
        departure_hour = random.randint(5, 8)
        departure_minute = random.randint(0, 59)
        departure_time = f"{departure_hour:02d}:{departure_minute:02d}:00"
        arrival_hour = departure_hour + random.randint(2, 6)
        estimated_arrival = f"{arrival_hour:02d}:{departure_minute:02d}:00"
        transport_date = fake.date_between(start_date='-1m', end_date='+1m')
        status = random.choice(["scheduled", "loading", "in-transit", "delivered", "cancelled"])
        urgency = random.choice(urgencies)
        
        cursor.execute("""
            INSERT INTO material_transports (vehicle_id, driver_name, material_type, quantity, unit,
                                           origin, destination, departure_time, estimated_arrival,
                                           transport_date, status, urgency)
            VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
        """, (vehicle_id, driver_name, material_type, quantity, unit, origin, destination,
              departure_time, estimated_arrival, transport_date, status, urgency))
    
    conn.commit()
    cursor.close()
    conn.close()
    print("Generated vehicles and transportation data")

def generate_customer_requests():
    """Generate customer requests and messages"""
    conn = connect_db()
    cursor = conn.cursor()
    
    # Get buyer IDs
    cursor.execute("SELECT id FROM buyers LIMIT 10")
    buyer_ids = [row[0] for row in cursor.fetchall()]
    
    # Get building and apartment IDs
    cursor.execute("SELECT id FROM buildings LIMIT 5")
    building_ids = [row[0] for row in cursor.fetchall()]
    
    cursor.execute("SELECT id FROM apartments LIMIT 10")
    apartment_ids = [row[0] for row in cursor.fetchall()]
    
    request_types = ["information", "document", "maintenance", "complaint", "other"]
    subjects = [
        "Payment confirmation needed", "Document request", "Maintenance issue",
        "Noise complaint", "Parking problem", "Contract clarification",
        "Invoice inquiry", "Service request", "General information"
    ]
    priorities = ["low", "medium", "high"]
    statuses = ["open", "in-progress", "responded", "closed"]
    
    for i in range(12):
        request_number = f"REQ-{datetime.now().year}-{i+1:04d}"
        buyer_id = random.choice(buyer_ids) if buyer_ids else None
        building_id = random.choice(building_ids) if building_ids else None
        apartment_id = random.choice(apartment_ids) if apartment_ids else None
        request_type = random.choice(request_types)
        subject = random.choice(subjects)
        priority = random.choice(priorities)
        status = random.choice(statuses)
        
        cursor.execute("""
            INSERT INTO customer_requests (request_number, buyer_id, building_id, apartment_id,
                                         request_type, subject, priority, status)
            VALUES (%s, %s, %s, %s, %s, %s, %s, %s) RETURNING id
        """, (request_number, buyer_id, building_id, apartment_id, request_type, subject, priority, status))
        
        request_id = cursor.fetchone()[0]
        
        # Generate messages for this request
        message_count = random.randint(1, 4)
        for j in range(message_count):
            sender_name = fake.name() if j % 2 == 0 else "Support Team"
            message_text = fake.text(max_nb_chars=200)
            is_staff_message = j % 2 == 1
            
            cursor.execute("""
                INSERT INTO request_messages (request_id, sender_name, message_text, is_staff_message)
                VALUES (%s, %s, %s, %s)
            """, (request_id, sender_name, message_text, is_staff_message))
    
    conn.commit()
    cursor.close()
    conn.close()
    print("Generated customer requests and messages")

def generate_notifications():
    """Generate notifications"""
    conn = connect_db()
    cursor = conn.cursor()
    
    notification_types = ["alert", "rent", "permit", "maintenance", "tenant", "system"]
    
    notifications_data = [
        ("alert", "Payment Overdue", "Payment for apartment 0101 is overdue by 15 days", False),
        ("maintenance", "Maintenance Completed", "Elevator maintenance in Skyline Tower completed", True),
        ("permit", "Permit Approved", "Construction permit PRM-2024-0001 has been approved", False),
        ("system", "System Update", "System will be updated tonight from 2:00 AM to 4:00 AM", True),
        ("tenant", "New Tenant", "New tenant registered for apartment 0205 in Riverside Apartments", False),
        ("alert", "Maintenance Required", "Urgent maintenance required for heating system in Metro Heights", False),
        ("rent", "Rent Collection", "Monthly rent collection completed for Garden View Complex", True),
        ("system", "Backup Complete", "Daily database backup completed successfully", True)
    ]
    
    for notification in notifications_data:
        read_at = fake.date_time_between(start_date='-1w', end_date='now') if notification[3] else None
        
        cursor.execute("""
            INSERT INTO notifications (notification_type, title, message, is_read, read_at)
            VALUES (%s, %s, %s, %s, %s)
        """, (*notification, read_at))
    
    conn.commit()
    cursor.close()
    conn.close()
    print("Generated notifications")

def fetch_and_display_data():
    """Fetch and display sample data from database"""
    conn = connect_db()
    cursor = conn.cursor()
    
    print("\nDATABASE SUMMARY:")
    print("=" * 50)
    
    # Count records in each table
    tables = [
        'users', 'buildings', 'apartments', 'buyers', 'property_purchases',
        'payments', 'lands', 'permits', 'maintenance_requests', 'vehicles',
        'worker_transports', 'material_transports', 'customer_requests',
        'request_messages', 'notifications'
    ]
    
    for table in tables:
        cursor.execute(f"SELECT COUNT(*) FROM {table}")
        count = cursor.fetchone()[0]
        print(f"{table.replace('_', ' ').title()}: {count} records")
    
    print("\nSAMPLE BUILDINGS:")
    cursor.execute("SELECT name, location, total_apartments, occupied_apartments FROM buildings LIMIT 3")
    for row in cursor.fetchall():
        print(f"- {row[0]} ({row[1]}) - {row[3]}/{row[2]} occupied")
    
    print("\nPAYMENT SUMMARY:")
    cursor.execute("""
        SELECT payment_status, COUNT(*), SUM(purchase_amount), SUM(paid_amount)
        FROM buyers GROUP BY payment_status
    """)
    for row in cursor.fetchall():
        print(f"- {row[0].title()}: {row[1]} buyers, Total: {row[2]:,.0f} MAD, Paid: {row[3]:,.0f} MAD")
    
    cursor.close()
    conn.close()

def main():
    """Main function to generate all data"""
    print("Starting Property Management Data Generation...")
    print("=" * 60)
    
    try:
        # Clear existing data
        clear_existing_data()
        
        # Generate all data
        generate_users()
        generate_buildings_and_apartments()
        generate_buyers_and_purchases()
        generate_lands()
        generate_permits()
        generate_maintenance_requests()
        generate_vehicles_and_transportation()
        generate_customer_requests()
        generate_notifications()
        
        # Display summary
        fetch_and_display_data()
        
        print("\nData generation completed successfully!")
        print("Your Property Management System is ready with sample data.")
        
    except Exception as e:
        print(f"Error during data generation: {e}")
        import traceback
        traceback.print_exc()

if __name__ == "__main__":
    main()
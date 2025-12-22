# database_setup.py
import pandas as pd
import numpy as np
from faker import Faker
import random
from datetime import datetime, timedelta
import psycopg2
from psycopg2.extras import execute_values
import os
from dotenv import load_dotenv
import sys

# Charger les variables d'environnement
load_dotenv()

class DatabaseManager:
    def __init__(self):
        # Configuration de la base de données
        self.db_config = {
            'host': os.getenv('DB_HOST', 'localhost'),
            'database': os.getenv('DB_NAME', 'lpynb'),
            'user': os.getenv('DB_USER', 'postgres'),
            'password': os.getenv('DB_PASSWORD', ''),
            'port': os.getenv('DB_PORT', '5432')
        }
        
        # Initialiser Faker
        self.fake = Faker()
        
        # Stockage des DataFrames
        self.dataframes = {}
        
    def connect(self):
        """Établir la connexion à la base de données"""
        try:
            self.conn = psycopg2.connect(**self.db_config)
            self.cur = self.conn.cursor()
            print("✅ Connexion à PostgreSQL établie")
            return True
        except Exception as e:
            print(f"❌ Erreur de connexion: {e}")
            return False
    
    def close(self):
        """Fermer la connexion"""
        if hasattr(self, 'cur'):
            self.cur.close()
        if hasattr(self, 'conn'):
            self.conn.close()
        print("🔌 Connexion fermée")
    
    def create_database(self):
        """Créer la base de données si elle n'existe pas"""
        try:
            # Se connecter à la base par défaut
            temp_config = self.db_config.copy()
            temp_config['database'] = 'postgres'
            conn_temp = psycopg2.connect(**temp_config)
            conn_temp.autocommit = True
            cur_temp = conn_temp.cursor()
            
            # Vérifier si la base existe
            cur_temp.execute(f"SELECT 1 FROM pg_database WHERE datname = '{self.db_config['database']}'")
            exists = cur_temp.fetchone()
            
            if not exists:
                cur_temp.execute(f"CREATE DATABASE {self.db_config['database']}")
                print(f"✅ Base de données '{self.db_config['database']}' créée")
            
            cur_temp.close()
            conn_temp.close()
            
        except Exception as e:
            print(f"⚠️  Erreur lors de la création de la base: {e}")
    
    def create_tables(self):
        """Créer toutes les tables"""
        tables_sql = [
            """
            CREATE TABLE IF NOT EXISTS users (
                id SERIAL PRIMARY KEY,
                username VARCHAR(50) UNIQUE NOT NULL,
                password_hash VARCHAR(255) NOT NULL,
                email VARCHAR(100) UNIQUE,
                full_name VARCHAR(100),
                user_type VARCHAR(20) CHECK (user_type IN ('admin', 'staff', 'manager')),
                phone VARCHAR(20),
                is_active BOOLEAN DEFAULT true,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS buildings (
                id SERIAL PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
                location TEXT,
                floors INTEGER,
                total_apartments INTEGER,
                occupied_apartments INTEGER DEFAULT 0,
                status VARCHAR(30),
                construction_date DATE,
                description TEXT,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS apartments (
                id SERIAL PRIMARY KEY,
                building_id INTEGER REFERENCES buildings(id) ON DELETE CASCADE,
                apartment_number VARCHAR(20),
                floor INTEGER,
                size VARCHAR(20),
                bedrooms INTEGER,
                bathrooms INTEGER,
                price DECIMAL(15,2),
                status VARCHAR(20),
                description TEXT,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS buyers (
                id SERIAL PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
                phone VARCHAR(20),
                email VARCHAR(100),
                address TEXT,
                national_id VARCHAR(50),
                purchase_date DATE,
                purchase_amount DECIMAL(15,2),
                paid_amount DECIMAL(15,2) DEFAULT 0,
                remaining_amount DECIMAL(15,2) DEFAULT 0,
                payment_status VARCHAR(20),
                last_payment_date DATE,
                next_due_date DATE,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS vehicles (
                id SERIAL PRIMARY KEY,
                vehicle_number VARCHAR(20) UNIQUE,
                vehicle_type VARCHAR(30),
                capacity VARCHAR(30),
                status VARCHAR(30),
                last_maintenance_date DATE,
                next_maintenance_date DATE,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS property_purchases (
                id SERIAL PRIMARY KEY,
                buyer_id INTEGER REFERENCES buyers(id) ON DELETE CASCADE,
                apartment_id INTEGER REFERENCES apartments(id) ON DELETE SET NULL,
                purchase_date DATE,
                purchase_amount DECIMAL(15,2),
                paid_amount DECIMAL(15,2) DEFAULT 0,
                payment_status VARCHAR(20),
                contract_number VARCHAR(50),
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS payments (
                id SERIAL PRIMARY KEY,
                buyer_id INTEGER REFERENCES buyers(id) ON DELETE CASCADE,
                purchase_id INTEGER REFERENCES property_purchases(id) ON DELETE CASCADE,
                amount DECIMAL(15,2),
                payment_date DATE,
                payment_method VARCHAR(30),
                reference_number VARCHAR(50),
                notes TEXT,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS maintenance_requests (
                id SERIAL PRIMARY KEY,
                request_number VARCHAR(30) UNIQUE,
                building_id INTEGER REFERENCES buildings(id) ON DELETE CASCADE,
                apartment_id INTEGER REFERENCES apartments(id) ON DELETE SET NULL,
                requested_by VARCHAR(100),
                issue_title VARCHAR(200),
                description TEXT,
                priority VARCHAR(20),
                status VARCHAR(20),
                estimated_cost DECIMAL(15,2),
                actual_cost DECIMAL(15,2),
                assigned_to VARCHAR(100),
                request_date DATE,
                completion_date DATE,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS customer_requests (
                id SERIAL PRIMARY KEY,
                request_number VARCHAR(30) UNIQUE,
                buyer_id INTEGER REFERENCES buyers(id) ON DELETE SET NULL,
                building_id INTEGER REFERENCES buildings(id) ON DELETE SET NULL,
                apartment_id INTEGER REFERENCES apartments(id) ON DELETE SET NULL,
                request_type VARCHAR(30),
                subject VARCHAR(200),
                priority VARCHAR(20),
                status VARCHAR(20),
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS request_messages (
                id SERIAL PRIMARY KEY,
                request_id INTEGER REFERENCES customer_requests(id) ON DELETE CASCADE,
                sender_name VARCHAR(100),
                message_text TEXT,
                is_staff_message BOOLEAN DEFAULT false,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS worker_transports (
                id SERIAL PRIMARY KEY,
                vehicle_id INTEGER REFERENCES vehicles(id) ON DELETE SET NULL,
                driver_name VARCHAR(100),
                worker_count INTEGER,
                pickup_point TEXT,
                destination TEXT,
                departure_time TIME,
                arrival_time TIME,
                transport_date DATE,
                status VARCHAR(30),
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS material_transports (
                id SERIAL PRIMARY KEY,
                vehicle_id INTEGER REFERENCES vehicles(id) ON DELETE SET NULL,
                driver_name VARCHAR(100),
                material_type VARCHAR(50),
                quantity VARCHAR(20),
                unit VARCHAR(20),
                origin TEXT,
                destination TEXT,
                departure_time TIME,
                estimated_arrival TIME,
                transport_date DATE,
                status VARCHAR(30),
                urgency VARCHAR(20),
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS notifications (
                id SERIAL PRIMARY KEY,
                notification_type VARCHAR(30),
                title VARCHAR(200),
                message TEXT,
                is_read BOOLEAN DEFAULT false,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                read_at TIMESTAMP
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS system_settings (
                id SERIAL PRIMARY KEY,
                setting_key VARCHAR(50) UNIQUE NOT NULL,
                setting_value TEXT,
                description TEXT
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS audit_log (
                id SERIAL PRIMARY KEY,
                user_id INTEGER REFERENCES users(id) ON DELETE SET NULL,
                action VARCHAR(20),
                table_name VARCHAR(50),
                record_id INTEGER,
                old_values JSON,
                new_values JSON,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """
        ]
        
        try:
            for i, sql in enumerate(tables_sql):
                self.cur.execute(sql)
                print(f"✅ Table {i+1}/15 créée")
            
            self.conn.commit()
            print("🎉 Toutes les tables ont été créées avec succès")
            
        except Exception as e:
            self.conn.rollback()
            print(f"❌ Erreur lors de la création des tables: {e}")
    
    def truncate_tables(self):
        """Vider toutes les tables"""
        truncate_sql = """
        TRUNCATE TABLE request_messages, customer_requests, material_transports, 
        worker_transports, vehicles, maintenance_requests, payments, 
        property_purchases, buyers, apartments, buildings, notifications, 
        system_settings, audit_log, users RESTART IDENTITY CASCADE;
        """
        
        try:
            self.cur.execute(truncate_sql)
            self.conn.commit()
            print("🧹 Toutes les tables ont été vidées")
        except Exception as e:
            self.conn.rollback()
            print(f"❌ Erreur lors du vidage des tables: {e}")
    
    def generate_data(self):
        """Générer toutes les données"""
        print("🚀 Génération des données...")
        
        # Fonctions de génération de données (les mêmes que dans votre script original)
        self.dataframes['users'] = self.generate_users(20)
        self.dataframes['buildings'] = self.generate_buildings(10)
        self.dataframes['apartments'] = self.generate_apartments(self.dataframes['buildings'], 200)
        self.dataframes['buyers'] = self.generate_buyers(50)
        self.dataframes['property_purchases'] = self.generate_property_purchases(
            self.dataframes['buyers'], self.dataframes['apartments']
        )
        self.dataframes['payments'] = self.generate_payments(
            self.dataframes['property_purchases'], self.dataframes['buyers']
        )
        self.dataframes['vehicles'] = self.generate_vehicles(15)
        self.dataframes['maintenance_requests'] = self.generate_maintenance_requests(
            self.dataframes['buildings'], self.dataframes['apartments'], 50
        )
        self.dataframes['customer_requests'] = self.generate_customer_requests(
            self.dataframes['buyers'], self.dataframes['buildings'], self.dataframes['apartments'], 40
        )
        self.dataframes['request_messages'] = self.generate_request_messages(
            self.dataframes['customer_requests'], 100
        )
        self.dataframes['worker_transports'] = self.generate_worker_transports(
            self.dataframes['vehicles'], 30
        )
        self.dataframes['material_transports'] = self.generate_material_transports(
            self.dataframes['vehicles'], 25
        )
        self.dataframes['notifications'] = self.generate_notifications(50)
        self.dataframes['system_settings'] = self.generate_system_settings()
        self.dataframes['audit_log'] = self.generate_audit_log(
            self.dataframes['users'], 100
        )
        
        print("✅ Données générées avec succès")
        
        # Afficher un résumé
        print("\n📊 Résumé des données générées:")
        for table_name, df in self.dataframes.items():
            print(f"   {table_name:25} : {len(df):4} enregistrements")
    
    def insert_data(self):
        """Insérer toutes les données dans la base"""
        print("\n📥 Insertion des données dans PostgreSQL...")
        
        for table_name, df in self.dataframes.items():
            try:
                if not df.empty:
                    # Convertir DataFrame en liste de tuples
                    tuples = [tuple(x) for x in df.to_numpy()]
                    
                    # Obtenir les noms des colonnes
                    cols = ','.join(list(df.columns))
                    
                    # Créer la requête d'insertion
                    query = f"INSERT INTO {table_name} ({cols}) VALUES %s"
                    
                    # Exécuter l'insertion
                    execute_values(self.cur, query, tuples)
                    
                    print(f"✅ {table_name:25} : {len(df)} lignes insérées")
                    
            except Exception as e:
                print(f"❌ Erreur lors de l'insertion dans {table_name}: {e}")
                self.conn.rollback()
                return False
        
        self.conn.commit()
        print("🎉 Toutes les données ont été insérées avec succès")
        return True
    
    def save_to_csv(self, output_dir='./csv_data'):
        """Sauvegarder les données en fichiers CSV"""
        if not os.path.exists(output_dir):
            os.makedirs(output_dir)
        
        print(f"\n💾 Sauvegarde des données en CSV dans {output_dir}...")
        
        for table_name, df in self.dataframes.items():
            filename = os.path.join(output_dir, f'{table_name}.csv')
            df.to_csv(filename, index=False, encoding='utf-8')
            print(f"✅ {filename}")
    
    # =========================================================================
    # FONCTIONS DE GÉNÉRATION DE DONNÉES (copiées de votre script original)
    # =========================================================================
    
    def generate_users(self, num_users=20):
        users = []
        user_types = ['admin', 'staff', 'manager']
        user_type_weights = [0.1, 0.6, 0.3]
        
        for _ in range(num_users):
            user_type = random.choices(user_types, weights=user_type_weights)[0]
            username = self.fake.user_name()
            
            users.append({
                'username': username,
                'password_hash': self.fake.sha256(),
                'email': self.fake.email(),
                'full_name': self.fake.name(),
                'user_type': user_type,
                'phone': self.fake.phone_number()[:20],
                'is_active': random.choices([True, False], weights=[0.9, 0.1])[0],
                'created_at': self.fake.date_time_this_year(),
                'updated_at': self.fake.date_time_this_year()
            })
        
        return pd.DataFrame(users)
    
    def generate_buildings(self, num_buildings=10):
        buildings = []
        status_options = ['active', 'under_construction', 'maintenance', 'inactive']
        status_weights = [0.7, 0.1, 0.1, 0.1]
        
        building_names = [
            'Skyline Tower', 'Royal Residences', 'Ocean View Apartments',
            'Central Plaza', 'Green Valley Complex', 'Sunset Heights',
            'Mountain View Villas', 'City Center Apartments', 'Lakeview Residences',
            'Palm Grove Towers'
        ]
        
        for i in range(num_buildings):
            total_apt = random.randint(20, 100)
            buildings.append({
                'name': building_names[i % len(building_names)] + f" {random.randint(1, 5)}",
                'location': self.fake.address(),
                'floors': random.randint(5, 25),
                'total_apartments': total_apt,
                'occupied_apartments': random.randint(0, total_apt),
                'status': random.choices(status_options, weights=status_weights)[0],
                'construction_date': self.fake.date_between(start_date='-20y', end_date='-1y'),
                'description': self.fake.text(max_nb_chars=200),
                'created_at': self.fake.date_time_this_year(),
                'updated_at': self.fake.date_time_this_year()
            })
        
        return pd.DataFrame(buildings)
    
    def generate_apartments(self, buildings_df, num_apartments=200):
        apartments = []
        status_options = ['available', 'sold', 'reserved', 'maintenance']
        status_weights = [0.4, 0.4, 0.1, 0.1]
        sizes = ['80 m²', '100 m²', '120 m²', '150 m²', '200 m²', '250 m²']
        
        apt_id = 1
        for _, building in buildings_df.iterrows():
            building_apts = building['total_apartments']
            for apt_num in range(1, min(building_apts + 1, random.randint(5, 25))):
                floor = random.randint(1, building['floors'])
                
                apartments.append({
                    'building_id': building['id'] if 'id' in building else building.name + 1,
                    'apartment_number': f"{floor:02d}{apt_num:03d}",
                    'floor': floor,
                    'size': random.choice(sizes),
                    'bedrooms': random.randint(1, 4),
                    'bathrooms': random.randint(1, 3),
                    'price': round(random.uniform(500000, 3000000), 2),
                    'status': random.choices(status_options, weights=status_weights)[0],
                    'description': self.fake.text(max_nb_chars=150),
                    'created_at': self.fake.date_time_this_year(),
                    'updated_at': self.fake.date_time_this_year()
                })
                apt_id += 1
        
        return pd.DataFrame(apartments[:num_apartments])
    
    def generate_buyers(self, num_buyers=50):
        buyers = []
        payment_statuses = ['paid', 'partial', 'pending', 'overdue']
        payment_weights = [0.3, 0.4, 0.2, 0.1]
        
        for i in range(num_buyers):
            purchase_amount = round(random.uniform(500000, 3000000), 2)
            paid_amount = round(random.uniform(0, purchase_amount), 2)
            remaining_amount = purchase_amount - paid_amount
            
            buyers.append({
                'name': self.fake.name(),
                'phone': self.fake.phone_number()[:20],
                'email': self.fake.email(),
                'address': self.fake.address(),
                'national_id': self.fake.ssn(),
                'purchase_date': self.fake.date_this_year() if random.random() > 0.3 else None,
                'purchase_amount': purchase_amount if random.random() > 0.3 else None,
                'paid_amount': paid_amount,
                'remaining_amount': remaining_amount,
                'payment_status': random.choices(payment_statuses, weights=payment_weights)[0],
                'last_payment_date': self.fake.date_this_year() if random.random() > 0.5 else None,
                'next_due_date': self.fake.date_this_year() if random.random() > 0.5 else None,
                'created_at': self.fake.date_time_this_year(),
                'updated_at': self.fake.date_time_this_year()
            })
        
        return pd.DataFrame(buyers)
    
    def generate_property_purchases(self, buyers_df, apartments_df):
        purchases = []
        
        # Get sold apartments
        sold_apartments = apartments_df[apartments_df['status'] == 'sold'].head(
            min(30, len(apartments_df[apartments_df['status'] == 'sold']))
        )
        
        purchase_id = 1
        for _, apartment in sold_apartments.iterrows():
            buyer = buyers_df.sample(1).iloc[0]
            purchase_amount = apartment['price']
            paid_amount = round(random.uniform(0, purchase_amount), 2)
            
            purchases.append({
                'buyer_id': buyer['id'] if 'id' in buyer else buyer.name + 1,
                'apartment_id': apartment['id'] if 'id' in apartment else apartment.name + 1,
                'purchase_date': self.fake.date_this_year(),
                'purchase_amount': purchase_amount,
                'paid_amount': paid_amount,
                'payment_status': 'paid' if paid_amount >= purchase_amount else 'partial' if paid_amount > 0 else 'pending',
                'contract_number': f"CONTRACT-{random.randint(1000, 9999)}-{random.randint(1000, 9999)}",
                'created_at': self.fake.date_time_this_year()
            })
            purchase_id += 1
        
        return pd.DataFrame(purchases)
    
    def generate_payments(self, property_purchases_df, buyers_df):
        payments = []
        payment_methods = ['cash', 'bank_transfer', 'check', 'credit_card', 'online']
        
        payment_id = 1
        for _, purchase in property_purchases_df.iterrows():
            num_payments = random.randint(1, 5)
            total_paid = 0
            
            for i in range(num_payments):
                if total_paid >= purchase['purchase_amount']:
                    break
                    
                max_payment = purchase['purchase_amount'] - total_paid
                amount = round(random.uniform(10000, max_payment), 2) if max_payment > 10000 else max_payment
                total_paid += amount
                
                payments.append({
                    'buyer_id': purchase['buyer_id'],
                    'purchase_id': purchase['id'] if 'id' in purchase else purchase.name + 1,
                    'amount': amount,
                    'payment_date': self.fake.date_between(start_date=purchase['purchase_date'], end_date='today'),
                    'payment_method': random.choice(payment_methods),
                    'reference_number': f"REF-{random.randint(100000, 999999)}",
                    'notes': self.fake.text(max_nb_chars=100),
                    'created_at': self.fake.date_time_this_year()
                })
                payment_id += 1
        
        return pd.DataFrame(payments)
    
    def generate_vehicles(self, num_vehicles=15):
        vehicles = []
        vehicle_types = ['passenger', 'cargo-truck', 'flatbed', 'mixer', 'excavator', 'crane']
        status_options = ['available', 'in-use', 'maintenance', 'out-of-service']
        status_weights = [0.6, 0.2, 0.1, 0.1]
        capacities = ['20 passengers', '10 tons', '15 tons', '5 tons', '8 m³', '50 tons']
        
        for i in range(num_vehicles):
            last_maintenance = self.fake.date_this_year()
            next_maintenance = last_maintenance + timedelta(days=random.randint(30, 180))
            
            vehicles.append({
                'vehicle_number': f"VH{random.randint(1000, 9999)}",
                'vehicle_type': random.choice(vehicle_types),
                'capacity': random.choice(capacities),
                'status': random.choices(status_options, weights=status_weights)[0],
                'last_maintenance_date': last_maintenance,
                'next_maintenance_date': next_maintenance,
                'created_at': self.fake.date_time_this_year(),
                'updated_at': self.fake.date_time_this_year()
            })
        
        return pd.DataFrame(vehicles)
    
    def generate_maintenance_requests(self, buildings_df, apartments_df, num_requests=50):
        requests = []
        priority_options = ['low', 'medium', 'high', 'critical']
        priority_weights = [0.2, 0.5, 0.2, 0.1]
        status_options = ['pending', 'in-progress', 'completed', 'cancelled']
        status_weights = [0.2, 0.3, 0.4, 0.1]
        
        issues = [
            'Water leakage', 'Electrical problem', 'HVAC not working',
            'Broken window', 'Plumbing issue', 'Elevator malfunction',
            'Roof repair', 'Paint work needed', 'Door lock broken',
            'Appliance repair', 'Security system issue', 'Common area cleaning'
        ]
        
        for i in range(num_requests):
            building = buildings_df.sample(1).iloc[0]
            apartment = apartments_df[apartments_df['building_id'] == building['id']].sample(1).iloc[0] if random.random() > 0.3 else None
            
            request_date = self.fake.date_this_year()
            completion_date = request_date + timedelta(days=random.randint(1, 30)) if random.random() > 0.5 else None
            
            requests.append({
                'request_number': f"MREQ-{random.randint(20230000, 20239999)}",
                'building_id': building['id'] if 'id' in building else building.name + 1,
                'apartment_id': apartment['id'] if apartment is not None and 'id' in apartment else None,
                'requested_by': self.fake.name(),
                'issue_title': random.choice(issues),
                'description': self.fake.text(max_nb_chars=200),
                'priority': random.choices(priority_options, weights=priority_weights)[0],
                'status': random.choices(status_options, weights=status_weights)[0],
                'estimated_cost': round(random.uniform(100, 5000), 2),
                'actual_cost': round(random.uniform(100, 5000), 2) if completion_date else None,
                'assigned_to': self.fake.name() if random.random() > 0.4 else None,
                'request_date': request_date,
                'completion_date': completion_date,
                'created_at': self.fake.date_time_this_year(),
                'updated_at': self.fake.date_time_this_year()
            })
        
        return pd.DataFrame(requests)
    
    def generate_customer_requests(self, buyers_df, buildings_df, apartments_df, num_requests=40):
        requests = []
        request_types = ['information', 'document', 'maintenance', 'complaint', 'other']
        priority_options = ['low', 'medium', 'high']
        priority_weights = [0.3, 0.5, 0.2]
        status_options = ['open', 'in-progress', 'responded', 'closed']
        status_weights = [0.2, 0.3, 0.3, 0.2]
        
        subjects = [
            'Request for ownership documents',
            'Complaint about neighbor',
            'Information about payment schedule',
            'Request for maintenance',
            'Question about amenities',
            'Complaint about construction noise',
            'Request for parking space',
            'Question about utility bills'
        ]
        
        for i in range(num_requests):
            buyer = buyers_df.sample(1).iloc[0] if random.random() > 0.3 else None
            building = buildings_df.sample(1).iloc[0] if random.random() > 0.5 else None
            apartment = apartments_df.sample(1).iloc[0] if random.random() > 0.5 and building is not None else None
            
            requests.append({
                'request_number': f"CREQ-{random.randint(20230000, 20239999)}",
                'buyer_id': buyer['id'] if buyer is not None and 'id' in buyer else None,
                'building_id': building['id'] if building is not None and 'id' in building else None,
                'apartment_id': apartment['id'] if apartment is not None and 'id' in apartment else None,
                'request_type': random.choice(request_types),
                'subject': random.choice(subjects),
                'priority': random.choices(priority_options, weights=priority_weights)[0],
                'status': random.choices(status_options, weights=status_weights)[0],
                'created_at': self.fake.date_time_this_year(),
                'updated_at': self.fake.date_time_this_year()
            })
        
        return pd.DataFrame(requests)
    
    def generate_request_messages(self, customer_requests_df, num_messages=100):
        messages = []
        
        for i in range(num_messages):
            request = customer_requests_df.sample(1).iloc[0]
            
            messages.append({
                'request_id': request['id'] if 'id' in request else request.name + 1,
                'sender_name': self.fake.name(),
                'message_text': self.fake.text(max_nb_chars=300),
                'is_staff_message': random.choice([True, False]),
                'created_at': self.fake.date_time_this_year()
            })
        
        return pd.DataFrame(messages)
    
    def generate_worker_transports(self, vehicles_df, num_transports=30):
        transports = []
        status_options = ['scheduled', 'in-transit', 'completed', 'cancelled']
        status_weights = [0.2, 0.1, 0.6, 0.1]
        
        for i in range(num_transports):
            vehicle = vehicles_df[vehicles_df['vehicle_type'] == 'passenger'].sample(1).iloc[0] if not vehicles_df[vehicles_df['vehicle_type'] == 'passenger'].empty else vehicles_df.sample(1).iloc[0]
            transport_date = self.fake.date_this_year()
            departure_time = self.fake.time_object()
            
            departure_datetime = datetime.combine(transport_date, departure_time)
            arrival_datetime = departure_datetime + timedelta(hours=random.randint(1, 4))
            
            transports.append({
                'vehicle_id': vehicle['id'] if 'id' in vehicle else vehicle.name + 1,
                'driver_name': self.fake.name(),
                'worker_count': random.randint(5, 25),
                'pickup_point': self.fake.street_address(),
                'destination': self.fake.address(),
                'departure_time': departure_time.strftime('%H:%M:%S'),
                'arrival_time': arrival_datetime.time().strftime('%H:%M:%S') if random.random() > 0.3 else None,
                'transport_date': transport_date,
                'status': random.choices(status_options, weights=status_weights)[0],
                'created_at': self.fake.date_time_this_year()
            })
        
        return pd.DataFrame(transports)
    
    def generate_material_transports(self, vehicles_df, num_transports=25):
        transports = []
        status_options = ['scheduled', 'loading', 'in-transit', 'delivered', 'cancelled']
        status_weights = [0.2, 0.1, 0.1, 0.5, 0.1]
        urgency_options = ['normal', 'urgent', 'critical']
        urgency_weights = [0.7, 0.2, 0.1]
        material_types = ['cement', 'steel', 'bricks', 'sand', 'gravel', 'wood', 'pipes', 'electrical']
        units = ['kg', 'tons', 'units', 'bags', 'cubic meters']
        
        for i in range(num_transports):
            vehicle = vehicles_df[vehicles_df['vehicle_type'].isin(['cargo-truck', 'flatbed', 'mixer'])].sample(1).iloc[0] if not vehicles_df[vehicles_df['vehicle_type'].isin(['cargo-truck', 'flatbed', 'mixer'])].empty else vehicles_df.sample(1).iloc[0]
            transport_date = self.fake.date_this_year()
            departure_time = self.fake.time_object()
            
            transports.append({
                'vehicle_id': vehicle['id'] if 'id' in vehicle else vehicle.name + 1,
                'driver_name': self.fake.name(),
                'material_type': random.choice(material_types),
                'quantity': str(random.randint(10, 1000)),
                'unit': random.choice(units),
                'origin': self.fake.company(),
                'destination': self.fake.address(),
                'departure_time': departure_time.strftime('%H:%M:%S'),
                'estimated_arrival': (datetime.combine(transport_date, departure_time) + timedelta(hours=random.randint(2, 8))).time().strftime('%H:%M:%S'),
                'transport_date': transport_date,
                'status': random.choices(status_options, weights=status_weights)[0],
                'urgency': random.choices(urgency_options, weights=urgency_weights)[0],
                'created_at': self.fake.date_time_this_year()
            })
        
        return pd.DataFrame(transports)
    
    def generate_notifications(self, num_notifications=50):
        notifications = []
        notification_types = ['alert', 'rent', 'permit', 'maintenance', 'tenant', 'system']
        
        for i in range(num_notifications):
            created_at = self.fake.date_time_this_year()
            
            notifications.append({
                'notification_type': random.choice(notification_types),
                'title': self.fake.sentence(nb_words=6),
                'message': self.fake.text(max_nb_chars=200),
                'is_read': random.choice([True, False]),
                'created_at': created_at,
                'read_at': created_at + timedelta(hours=random.randint(1, 72)) if random.random() > 0.5 else None
            })
        
        return pd.DataFrame(notifications)
    
    def generate_system_settings(self):
        settings = [
            {'setting_key': 'app_name', 'setting_value': 'Smart Property Manager Pro', 'description': 'Application name'},
            {'setting_key': 'theme', 'setting_value': 'light', 'description': 'Default theme (light/dark)'},
            {'setting_key': 'currency', 'setting_value': 'MAD', 'description': 'Default currency'},
            {'setting_key': 'date_format', 'setting_value': 'YYYY-MM-DD', 'description': 'Default date format'},
            {'setting_key': 'company_name', 'setting_value': 'Beyers Property Management', 'description': 'Company name'},
            {'setting_key': 'tax_rate', 'setting_value': '20', 'description': 'Tax rate percentage'},
            {'setting_key': 'late_fee_percentage', 'setting_value': '5', 'description': 'Late fee percentage'},
            {'setting_key': 'maintenance_contact', 'setting_value': '555-1234', 'description': 'Maintenance contact number'},
            {'setting_key': 'office_address', 'setting_value': '123 Business Street, City, Country', 'description': 'Office address'},
            {'setting_key': 'email_signature', 'setting_value': 'Best regards,\nBeyers Property Management Team', 'description': 'Email signature'}
        ]
        
        return pd.DataFrame(settings)
    
    def generate_audit_log(self, users_df, num_logs=100):
        logs = []
        actions = ['CREATE', 'UPDATE', 'DELETE', 'LOGIN', 'LOGOUT', 'EXPORT', 'IMPORT']
        tables = ['users', 'buildings', 'apartments', 'buyers', 'payments', 'lands', 'permits']
        
        for i in range(num_logs):
            user = users_df.sample(1).iloc[0] if random.random() > 0.3 else None
            
            logs.append({
                'user_id': user['id'] if user is not None and 'id' in user else None,
                'action': random.choice(actions),
                'table_name': random.choice(tables),
                'record_id': random.randint(1, 100),
                'old_values': '{"old_value": "example"}',
                'new_values': '{"new_value": "example"}',
                'created_at': self.fake.date_time_this_year()
            })
        
        return pd.DataFrame(logs)
    
    def run_full_setup(self, generate_new=True):
        """Exécuter la configuration complète"""
        print("=" * 60)
        print("🚀 DÉMARRAGE DE LA CONFIGURATION DE LA BASE DE DONNÉES")
        print("=" * 60)
        
        try:
            # Étape 1: Créer la base de données
            print("\n📦 ÉTAPE 1: Création de la base de données")
            self.create_database()
            
            # Étape 2: Se connecter
            print("\n🔌 ÉTAPE 2: Connexion à la base de données")
            if not self.connect():
                return False
            
            # Étape 3: Créer les tables
            print("\n🗂️  ÉTAPE 3: Création des tables")
            self.create_tables()
            
            # Étape 4: Vider les tables existantes
            print("\n🧹 ÉTAPE 4: Nettoyage des tables")
            self.truncate_tables()
            
            # Étape 5: Générer les données
            print("\n🔄 ÉTAPE 5: Génération des données")
            self.generate_data()
            
            # Étape 6: Insérer les données
            print("\n📥 ÉTAPE 6: Insertion dans la base de données")
            if not self.insert_data():
                return False
            
            # Étape 7: Sauvegarder en CSV
            print("\n💾 ÉTAPE 7: Sauvegarde en fichiers CSV")
            self.save_to_csv()
            
            print("\n" + "=" * 60)
            print("✅ CONFIGURATION TERMINÉE AVEC SUCCÈS!")
            print("=" * 60)
            
            return True
            
        except Exception as e:
            print(f"\n❌ ERREUR: {e}")
            return False
        
        finally:
            self.close()


# =============================================================================
# SCRIPT PRINCIPAL
# =============================================================================

if __name__ == "__main__":
    # Créer un fichier .env avec vos configurations
    env_content = """
DB_HOST=localhost
DB_NAME=lpynb
DB_USER=postgres
DB_PASSWORD=votre_mot_de_passe
DB_PORT=5432
"""
    
    # Vérifier si le fichier .env existe
    if not os.path.exists('.env'):
        print("📝 Création du fichier .env...")
        with open('.env', 'w') as f:
            f.write(env_content)
        print("⚠️  Veuillez modifier le fichier '.env' avec vos informations de connexion PostgreSQL")
        sys.exit(1)
    
    # Initialiser et exécuter
    db_manager = DatabaseManager()
    
    # Demander à l'utilisateur ce qu'il veut faire
    print("Options disponibles:")
    print("1. Configuration complète (créer tables + générer données)")
    print("2. Générer uniquement les données (sans base de données)")
    print("3. Insérer uniquement les données CSV existantes")
    
    choice = input("\nVotre choix (1-3): ").strip()
    
    if choice == "1":
        # Configuration complète
        success = db_manager.run_full_setup()
        if success:
            print("\n🎉 La base de données 'lpynb' est prête à l'utilisation!")
        else:
            print("\n❌ Une erreur s'est produite")
    
    elif choice == "2":
        # Générer uniquement les données et sauvegarder en CSV
        print("\n🚀 Génération des données...")
        db_manager.generate_data()
        db_manager.save_to_csv()
        print("\n✅ Les fichiers CSV ont été générés dans le dossier 'csv_data'")
    
    elif choice == "3":
        # Insérer uniquement les données CSV existantes
        print("\n📥 Insertion des données CSV...")
        
        if db_manager.connect():
            # Vider les tables
            db_manager.truncate_tables()
            
            # Lire et insérer chaque fichier CSV
            csv_dir = './csv_data'
            if not os.path.exists(csv_dir):
                print(f"❌ Le dossier '{csv_dir}' n'existe pas")
                sys.exit(1)
            
            for csv_file in os.listdir(csv_dir):
                if csv_file.endswith('.csv'):
                    table_name = csv_file.replace('.csv', '')
                    file_path = os.path.join(csv_dir, csv_file)
                    
                    try:
                        df = pd.read_csv(file_path)
                        if not df.empty:
                            tuples = [tuple(x) for x in df.to_numpy()]
                            cols = ','.join(list(df.columns))
                            query = f"INSERT INTO {table_name} ({cols}) VALUES %s"
                            execute_values(db_manager.cur, query, tuples)
                            db_manager.conn.commit()
                            print(f"✅ {table_name:25} : {len(df)} lignes insérées")
                    except Exception as e:
                        print(f"❌ Erreur avec {table_name}: {e}")
                        db_manager.conn.rollback()
            
            db_manager.close()
            print("\n✅ Données CSV insérées avec succès!")
    
    else:
        print("❌ Choix invalide")
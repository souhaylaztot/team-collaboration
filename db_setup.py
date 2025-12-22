import os
import psycopg2
from dotenv import load_dotenv

# Load environment variables
load_dotenv('db/.env')

# Database configuration from .env
DB_CONFIG = {
    'host': os.getenv('DB_HOST'),
    'database': os.getenv('DB_NAME'),
    'user': os.getenv('DB_USER'),
    'password': os.getenv('DB_PASSWORD'),
    'port': os.getenv('DB_PORT')
}

def create_database():
    """Create the database if it doesn't exist"""
    try:
        # Connect to default postgres database
        conn = psycopg2.connect(
            host=DB_CONFIG['host'],
            database='postgres',
            user=DB_CONFIG['user'],
            password=DB_CONFIG['password'],
            port=DB_CONFIG['port']
        )
        conn.autocommit = True
        cursor = conn.cursor()
        
        # Check if database exists
        cursor.execute(f"SELECT 1 FROM pg_database WHERE datname = '{DB_CONFIG['database']}'")
        exists = cursor.fetchone()
        
        if not exists:
            cursor.execute(f"CREATE DATABASE {DB_CONFIG['database']}")
            print(f"✅ Database '{DB_CONFIG['database']}' created successfully!")
        else:
            print(f"ℹ️ Database '{DB_CONFIG['database']}' already exists")
            
        cursor.close()
        conn.close()
        
    except Exception as e:
        print(f"❌ Error creating database: {e}")

def execute_schema():
    """Execute the database schema"""
    try:
        # Connect to the property_manager database
        conn = psycopg2.connect(**DB_CONFIG)
        cursor = conn.cursor()
        
        # Read and execute schema file
        with open('database_schema.sql', 'r', encoding='utf-8') as file:
            schema_sql = file.read()
            
        # Split statements and execute one by one to handle existing tables
        statements = [stmt.strip() for stmt in schema_sql.split(';') if stmt.strip()]
        
        created_tables = 0
        existing_tables = 0
        
        for statement in statements:
            if statement:
                try:
                    cursor.execute(statement)
                    conn.commit()
                    if 'CREATE TABLE' in statement.upper():
                        created_tables += 1
                except Exception as e:
                    conn.rollback()  # Rollback failed transaction
                    if 'already exists' in str(e):
                        existing_tables += 1
                    else:
                        print(f"⚠️ Warning: {e}")
        
        if created_tables > 0:
            print(f"✅ Created {created_tables} new tables")
        if existing_tables > 0:
            print(f"ℹ️ {existing_tables} tables already existed")
        
        print("✅ Database schema setup completed!")
        
        # Verify tables created
        cursor.execute("SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'")
        tables = cursor.fetchall()
        print(f"📊 Created {len(tables)} tables: {[table[0] for table in tables]}")
        
        cursor.close()
        conn.close()
        
    except Exception as e:
        print(f"❌ Error executing schema: {e}")
        try:
            conn.rollback()
        except:
            pass
        return False
    
    return True

def test_connection():
    """Test database connection"""
    try:
        conn = psycopg2.connect(**DB_CONFIG)
        cursor = conn.cursor()
        cursor.execute("SELECT version()")
        version = cursor.fetchone()
        print(f"✅ Connected to PostgreSQL: {version[0]}")
        cursor.close()
        conn.close()
        return True
    except Exception as e:
        print(f"❌ Connection failed: {e}")
        return False

if __name__ == "__main__":
    print("🚀 Setting up Property Manager Database...")
    print(f"📍 Host: {DB_CONFIG['host']}:{DB_CONFIG['port']}")
    print(f"🗄️ Database: {DB_CONFIG['database']}")
    print(f"👤 User: {DB_CONFIG['user']}")
    print("-" * 50)
    
    # Step 1: Test connection
    if test_connection():
        # Step 2: Create database
        create_database()
        
        # Step 3: Execute schema
        execute_schema()
        
        print("-" * 50)
        print("🎉 Database setup completed successfully!")
    else:
        print("❌ Setup failed - check your database connection")
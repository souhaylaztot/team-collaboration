import os
import pg8000
from dotenv import load_dotenv

# Load environment variables
load_dotenv('db/.env')

# Database configuration from .env
DB_CONFIG = {
    'host': os.getenv('DB_HOST'),
    'database': os.getenv('DB_NAME'),
    'user': os.getenv('DB_USER'),
    'password': os.getenv('DB_PASSWORD'),
    'port': int(os.getenv('DB_PORT'))
}

def test_connection():
    """Test database connection"""
    try:
        conn = pg8000.connect(**DB_CONFIG)
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

def execute_schema():
    """Execute the database schema"""
    try:
        conn = pg8000.connect(**DB_CONFIG)
        cursor = conn.cursor()
        
        # Read and execute schema file
        with open('database_schema.sql', 'r', encoding='utf-8') as file:
            schema_sql = file.read()
            
        # Split by semicolon and execute each statement
        statements = [stmt.strip() for stmt in schema_sql.split(';') if stmt.strip()]
        
        for statement in statements:
            if statement:
                cursor.execute(statement)
        
        conn.commit()
        print("✅ Database schema executed successfully!")
        
        # Verify tables created
        cursor.execute("SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'")
        tables = cursor.fetchall()
        print(f"📊 Created {len(tables)} tables")
        
        cursor.close()
        conn.close()
        
    except Exception as e:
        print(f"❌ Error executing schema: {e}")

if __name__ == "__main__":
    print("🚀 Setting up Property Manager Database...")
    print(f"📍 Host: {DB_CONFIG['host']}:{DB_CONFIG['port']}")
    print(f"🗄️ Database: {DB_CONFIG['database']}")
    print(f"👤 User: {DB_CONFIG['user']}")
    print("-" * 50)
    
    if test_connection():
        execute_schema()
        print("🎉 Database setup completed!")
    else:
        print("❌ Setup failed")
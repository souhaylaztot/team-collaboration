# Data Generation Guide - Property Manager

## Overview
This guide explains how to run the `generate_data.py` script to populate your Property Manager database with sample data.

## Prerequisites

### 1. Database Setup
- PostgreSQL server running on localhost:5432
- Database named `property_manager` created
- User `postgres` with password `4426` (as configured in `db/.env`)

### 2. Python Environment
- Python 3.7 or higher installed
- Required packages installed (see Installation section)

## Installation

### Step 1: Install Python Dependencies
```cmd
pip install -r requirements.txt
```

Or install individually:
```cmd
pip install psycopg2-binary python-dotenv faker
```

### Step 2: Verify Database Connection
Ensure your PostgreSQL database is running and accessible with the credentials in `db/.env`:
- Host: localhost
- Database: property_manager
- User: postgres
- Password: 4426
- Port: 5432

## Running the Data Generation Script

### Method 1: Direct Python Execution
```cmd
python generate_data.py
```

### Method 2: From Project Root
```cmd
cd c:\Users\Souhayl\Desktop\batticonnect\moham\team-collaboration
python generate_data.py
```

## What the Script Does

The script will:

1. **Clear Existing Data** - Removes all existing records (except system data)
2. **Generate Users** - Creates admin, manager, and staff accounts
3. **Generate Buildings & Apartments** - Creates 6 buildings with apartments
4. **Generate Buyers & Purchases** - Creates buyers and property purchase records
5. **Generate Lands** - Creates land records with various statuses
6. **Generate Permits** - Creates construction/development permits
7. **Generate Maintenance Requests** - Creates maintenance tickets
8. **Generate Vehicles & Transportation** - Creates vehicle and transport records
9. **Generate Customer Requests** - Creates customer service requests
10. **Generate Notifications** - Creates system notifications

## Generated Data Summary

After successful execution, you'll have:

- **Users**: 4 user accounts (admin, manager, 2 staff)
- **Buildings**: 6 buildings with realistic Moroccan locations
- **Apartments**: ~240 apartments across all buildings
- **Buyers**: Buyers for all sold apartments with Moroccan names
- **Payments**: Payment history for all purchases
- **Lands**: 6 land plots with different statuses
- **Permits**: 10 construction permits
- **Maintenance**: 15 maintenance requests
- **Vehicles**: 6 vehicles for transportation
- **Transportation**: Worker and material transport records
- **Customer Requests**: 12 customer service requests
- **Notifications**: 8 system notifications

## User Accounts Created

| Username | Password | Role | Full Name |
|----------|----------|------|-----------|
| admin | admin123 | admin | Ahmed Hassan |
| manager1 | manager123 | manager | Fatima Zahra |
| staff1 | staff123 | staff | Mohammed Alami |
| staff2 | staff123 | staff | Aicha Benali |

## Sample Buildings Generated

1. **Skyline Tower** - Downtown Casablanca (12 floors, 48 apartments)
2. **Riverside Apartments** - Westside Rabat (8 floors, 32 apartments)
3. **Garden View Complex** - Eastside Marrakech (15 floors, 60 apartments)
4. **Metro Heights** - Business District, Casablanca (10 floors, 40 apartments)
5. **Ocean Breeze** - Coastal Road, Agadir (6 floors, 24 apartments)
6. **Atlas View** - Mountain Road, Fez (9 floors, 36 apartments)

## Troubleshooting

### Common Issues

**1. Database Connection Error**
```
psycopg2.OperationalError: could not connect to server
```
**Solution**: Verify PostgreSQL is running and credentials in `db/.env` are correct.

**2. Module Not Found Error**
```
ModuleNotFoundError: No module named 'psycopg2'
```
**Solution**: Install required packages using `pip install -r requirements.txt`

**3. Permission Denied**
```
psycopg2.errors.InsufficientPrivilege
```
**Solution**: Ensure the database user has CREATE, INSERT, DELETE permissions.

**4. Database Does Not Exist**
```
psycopg2.OperationalError: database "property_manager" does not exist
```
**Solution**: Create the database first using your PostgreSQL client.

### Verification Steps

After running the script, verify data was generated:

1. **Check Console Output** - Look for success messages for each data type
2. **Database Summary** - The script displays a summary of generated records
3. **Login Test** - Try logging into the JavaFX application with generated users

## Re-running the Script

The script is safe to run multiple times:
- It clears existing data before generating new data
- User accounts are updated (not duplicated) if they already exist
- All other data is completely regenerated

## Next Steps

After successful data generation:

1. **Test JavaFX Application** - Run `run-javafx.bat` to test the application
2. **Login Testing** - Use the generated user accounts to test different roles
3. **Data Verification** - Check various modules (Buildings, Buyers, etc.) in the app

## Support

If you encounter issues:
1. Check the console output for specific error messages
2. Verify database connection and permissions
3. Ensure all Python dependencies are installed
4. Check that the database schema is properly set up

---

**Note**: This script generates realistic sample data for development and testing purposes. All names, addresses, and contact information are generated using the Faker library and are not real.
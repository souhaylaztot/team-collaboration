package com.propertymanager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BuildingDAO {
    
    public static List<Building> getAllBuildings() {
        List<Building> buildings = new ArrayList<>();
        String sql = "SELECT id, name, location, floors, total_apartments, occupied_apartments, status FROM buildings ORDER BY name";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Building building = new Building(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("location"),
                    rs.getInt("floors"),
                    rs.getInt("total_apartments"),
                    rs.getInt("occupied_apartments"),
                    rs.getString("status")
                );
                buildings.add(building);
            }
            
            System.out.println("📊 Loaded " + buildings.size() + " buildings from database");
            
        } catch (SQLException e) {
            System.err.println("❌ Error loading buildings: " + e.getMessage());
            e.printStackTrace();
        }
        
        return buildings;
    }
    
    public static Building getBuildingById(int id) {
        String sql = "SELECT id, name, location, floors, total_apartments, occupied_apartments, status FROM buildings WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Building(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("location"),
                    rs.getInt("floors"),
                    rs.getInt("total_apartments"),
                    rs.getInt("occupied_apartments"),
                    rs.getString("status")
                );
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error loading building: " + e.getMessage());
        }
        
        return null;
    }
    
    public static int getTotalBuildings() {
        String sql = "SELECT COUNT(*) FROM buildings";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error counting buildings: " + e.getMessage());
        }
        
        return 0;
    }
    
    public static int getTotalApartments() {
        String sql = "SELECT SUM(total_apartments) FROM buildings";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error counting apartments: " + e.getMessage());
        }
        
        return 0;
    }
    
    public static int getOccupiedApartments() {
        String sql = "SELECT SUM(occupied_apartments) FROM buildings";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error counting occupied apartments: " + e.getMessage());
        }
        
        return 0;
    }
}
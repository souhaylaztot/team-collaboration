package com.propertymanager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LandDAO {
    
    public static List<Land> getAllLands() {
        List<Land> lands = new ArrayList<>();
        String sql = "SELECT id, name, location, area, owner, estimated_value, status, description, documents_count FROM lands ORDER BY name";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Land land = new Land(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("location"),
                    rs.getString("area"),
                    rs.getString("owner"),
                    rs.getDouble("estimated_value"),
                    rs.getString("status"),
                    rs.getString("description"),
                    rs.getInt("documents_count")
                );
                lands.add(land);
            }
            
            System.out.println("🏞️ Loaded " + lands.size() + " lands from database");
            
        } catch (SQLException e) {
            System.err.println("❌ Error loading lands: " + e.getMessage());
            e.printStackTrace();
        }
        
        return lands;
    }
    
    public static int getTotalLands() {
        String sql = "SELECT COUNT(*) FROM lands";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error counting lands: " + e.getMessage());
        }
        
        return 0;
    }
    
    public static double getTotalLandValue() {
        String sql = "SELECT SUM(estimated_value) FROM lands";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getDouble(1);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error calculating land value: " + e.getMessage());
        }
        
        return 0.0;
    }
    
    public static List<Land> getLandsByStatus(String status) {
        List<Land> lands = new ArrayList<>();
        String sql = "SELECT id, name, location, area, owner, estimated_value, status, description, documents_count FROM lands WHERE status = ? ORDER BY name";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Land land = new Land(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("location"),
                    rs.getString("area"),
                    rs.getString("owner"),
                    rs.getDouble("estimated_value"),
                    rs.getString("status"),
                    rs.getString("description"),
                    rs.getInt("documents_count")
                );
                lands.add(land);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error loading lands by status: " + e.getMessage());
        }
        
        return lands;
    }
}
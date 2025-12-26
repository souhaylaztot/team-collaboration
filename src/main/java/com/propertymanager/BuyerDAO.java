package com.propertymanager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BuyerDAO {
    
    public static List<Buyer> getAllBuyers() {
        List<Buyer> buyers = new ArrayList<>();
        String sql = """
            SELECT b.id, b.name, b.phone, b.email, b.purchase_date, 
                   b.purchase_amount, b.paid_amount, b.remaining_amount, 
                   b.payment_status, b.last_payment_date, b.next_due_date,
                   COALESCE(bld.name || ' - Apt ' || a.apartment_number, 'N/A') as property_info
            FROM buyers b
            LEFT JOIN property_purchases pp ON b.id = pp.buyer_id
            LEFT JOIN apartments a ON pp.apartment_id = a.id
            LEFT JOIN buildings bld ON a.building_id = bld.id
            ORDER BY b.name
        """;
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Buyer buyer = new Buyer(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("property_info"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getDate("purchase_date") != null ? rs.getDate("purchase_date").toString() : "",
                    rs.getInt("purchase_amount"),
                    rs.getInt("paid_amount"),
                    rs.getInt("remaining_amount"),
                    rs.getString("payment_status"),
                    rs.getDate("last_payment_date") != null ? rs.getDate("last_payment_date").toString() : "",
                    rs.getDate("next_due_date") != null ? rs.getDate("next_due_date").toString() : ""
                );
                buyers.add(buyer);
            }
            
            System.out.println("👥 Loaded " + buyers.size() + " buyers from database");
            
        } catch (SQLException e) {
            System.err.println("❌ Error loading buyers: " + e.getMessage());
            e.printStackTrace();
        }
        
        return buyers;
    }
    
    public static int getTotalBuyers() {
        String sql = "SELECT COUNT(*) FROM buyers";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error counting buyers: " + e.getMessage());
        }
        
        return 0;
    }
    
    public static double getTotalRevenue() {
        String sql = "SELECT SUM(paid_amount) FROM buyers";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getDouble(1);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error calculating revenue: " + e.getMessage());
        }
        
        return 0.0;
    }
    
    public static double getPendingPayments() {
        String sql = "SELECT SUM(remaining_amount) FROM buyers WHERE remaining_amount > 0";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getDouble(1);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error calculating pending payments: " + e.getMessage());
        }
        
        return 0.0;
    }
    
    public static List<Buyer> getBuyersByPaymentStatus(String status) {
        List<Buyer> buyers = new ArrayList<>();
        String sql = """
            SELECT b.id, b.name, b.phone, b.email, b.purchase_date, 
                   b.purchase_amount, b.paid_amount, b.remaining_amount, 
                   b.payment_status, b.last_payment_date, b.next_due_date,
                   COALESCE(bld.name || ' - Apt ' || a.apartment_number, 'N/A') as property_info
            FROM buyers b
            LEFT JOIN property_purchases pp ON b.id = pp.buyer_id
            LEFT JOIN apartments a ON pp.apartment_id = a.id
            LEFT JOIN buildings bld ON a.building_id = bld.id
            WHERE b.payment_status = ?
            ORDER BY b.name
        """;
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Buyer buyer = new Buyer(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("property_info"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getDate("purchase_date") != null ? rs.getDate("purchase_date").toString() : "",
                    rs.getInt("purchase_amount"),
                    rs.getInt("paid_amount"),
                    rs.getInt("remaining_amount"),
                    rs.getString("payment_status"),
                    rs.getDate("last_payment_date") != null ? rs.getDate("last_payment_date").toString() : "",
                    rs.getDate("next_due_date") != null ? rs.getDate("next_due_date").toString() : ""
                );
                buyers.add(buyer);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error loading buyers by status: " + e.getMessage());
        }
        
        return buyers;
    }
}
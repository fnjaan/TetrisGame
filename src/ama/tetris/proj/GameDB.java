
package ama.tetris.proj;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.proteanit.sql.DbUtils;


public class GameDB {
    private Connection con = null;
    
//    GameDB() {
//        try {
//            Class.forName("org.sqlite.JDBC");
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(GameDB.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    public void openconnection() {
        String url = "jdbc:sqlite:C:\\Users\\menat\\Documents\\NetBeansProjects\\TetrisGame\\res\\score.db";
        try {
             con = DriverManager.getConnection(url);  
             //System.out.println("connection success...");
        } catch (SQLException e) {
            System.out.println(e.getMessage());  
        }
        
    }
    
    public void closeconnection() {
        try {
            con.close();
            //System.out.println("CLOSED!");
        } catch (SQLException ex) {
            Logger.getLogger(GameDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addPlayer(String name) {
        String sql = "INSERT INTO players(Name) VALUES(?)";  
        try {  
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.executeUpdate();  
        } catch (SQLException ex) {
            Logger.getLogger(GameDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addPlayerScore(int score, int id) {
        String sql = "UPDATE players SET score = ? WHERE id = ?";  
        try {  
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, score);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();  
        } catch (SQLException ex) {
            Logger.getLogger(GameDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updatePlayerScore(int score, int id) {
        String sql1 = "SELECT Score FROM players WHERE id = ?";
        int tempScore = 0;
        try {  
            PreparedStatement pstmt1 = con.prepareStatement(sql1);
            pstmt1.setInt(1, id);
            ResultSet rs1 = pstmt1.executeQuery();
            while(rs1.next()) {
                tempScore = rs1.getInt("Score");
            }
            pstmt1.close();
            rs1.close();
        } catch (SQLException ex) {
            Logger.getLogger(GameDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(score > tempScore) {
            String sql2 = "UPDATE players SET score = ? WHERE id = ?";  
            try {  
                PreparedStatement pstmt2 = con.prepareStatement(sql2);
                pstmt2.setInt(1, score);
                pstmt2.setInt(2, id);
                pstmt2.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(GameDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       
    }
    
    public void populateNames() {
        String sql = "SELECT Name FROM players ORDER BY id DESC";
        try {
            Statement pstmt = con.createStatement();
            ResultSet rs = pstmt.executeQuery(sql);
            
            while (rs.next()) {
                
                UserUI.cmbxUsername.addItem(rs.getString("Name"));
            }
            rs.close();
            pstmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(GameDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void populateScores() {
        String sql = "SELECT Name, Score FROM players ORDER BY Score DESC";
        ArrayList<String> Scores = new ArrayList<>();
        try {
            Statement pstmt = con.createStatement();
            ResultSet rs = pstmt.executeQuery(sql);

//            while (rs.next()) {
//                Scores.add(Integer.toString(rs.getInt("Score")));
//            }
            ScoreRank.ScoreTable.setModel(DbUtils.resultSetToTableModel(rs));
            rs.close();
//            System.out.println(Scores);
           
            pstmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(GameDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int getID(String name) {
        int id = 0;
        String sql = "SELECT id FROM players WHERE Name = ?";
        try {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
               id = rs.getInt("id");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(GameDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }
}

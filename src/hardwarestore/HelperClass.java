/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hardwarestore;

import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 *
 * @author thisa
 */
public class HelperClass {
    
    
    
    public static void Date(JLabel pDate){
        DateTimeFormatter dtr = DateTimeFormatter.ofPattern("YYYY-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        pDate.setText(dtr.format(now));
    }
    
    public static void ShowTime(JLabel pTime) {
        new Timer(0, (ActionEvent e) -> {
            Date d = new Date();
            SimpleDateFormat s = new SimpleDateFormat("hh:mm:ss a");
            
            pTime.setText(s.format(d));
        }).start();
    }
   
    public static Connection connect(){
        
        Connection connection = null;
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/randina_hardware", "root", "");
            
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error: " + e);
            
        }
        return connection;
    }
    
    
    
    
    
}

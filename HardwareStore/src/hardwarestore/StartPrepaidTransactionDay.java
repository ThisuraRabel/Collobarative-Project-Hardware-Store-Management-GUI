/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hardwarestore;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author THR
 */
public class StartPrepaidTransactionDay extends javax.swing.JFrame {
    
    private Connection con = null;
    private PreparedStatement pst;
    private ResultSet rs;
    
    private String transactionNo3;
    private String totalPrice3;
    
    
    //private String debtId;
    
    private String debtId2;
    private String transactionNo;
    private String totalPrice;
    private String debtAmount;
    private String customerName;
    private String date;
    private String time;
    
    private String debtId3;
    private String transactionNo2;
    private String totalPrice2;
    private String debtAmount2;
    private String customerName2;
    private String date2;
    private String time2;
    
    private Double dbett;
    
    /**
     * Creates new form StartPrepaidTransactionDay
     */
    public StartPrepaidTransactionDay() {
        initComponents();
        con = HelperClass.connect();
        
        HelperClass.ShowTime(timeLabel);
        HelperClass.Date(dateLabel);
        
        IncompleteTable();
        CompleteTable();
        
        DeleteButton.setEnabled(false);
        PaidButton.setEnabled(false);
        RollbackButton.setEnabled(false);
        insertButton.setEnabled(false);
        
    }
    
    public StartPrepaidTransactionDay(String transNo, String totPrice) {
        
    transactionNo3 = transNo;
    totalPrice3 = totPrice;
    
        
        initComponents();
        con = HelperClass.connect();
        
        HelperClass.ShowTime(timeLabel);
        HelperClass.Date(dateLabel);
       
        IncompleteTable();
        CompleteTable();
        
        DeleteButton.setEnabled(false);
        PaidButton.setEnabled(false);
        RollbackButton.setEnabled(false);
        
        
    }
    /*
    public void autoDebtID(){
        Statement s;
        try {
            s = con.createStatement();
            rs = s.executeQuery("select MAX(debt_id) from incomplete");
            rs.next();
            rs.getString("MAX(debt_id)");
            
            if (rs.getString("MAX(debt_id)") == null){
                debtId="DT000001";    
                
            }
            else{
                long id = Long.parseLong(rs.getString("MAX(debt_id)").substring(2,rs.getString("MAX(debt_id)").length()));
                id++;
                debtId="DT" + String.format("%06d", id);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GoodsListing.class.getName()).log(Level.SEVERE, null, ex);
        }
     
    }
    */
    
    public void IncompleteTable(){
        
        try {
            pst = con.prepareStatement("select *from incomplete");
            rs = pst.executeQuery();
            ResultSetMetaData rsm = rs.getMetaData();
            int c;
            c = rsm.getColumnCount();
            DefaultTableModel dt = (DefaultTableModel)incompleteTable.getModel();
            
            dt.setRowCount(0);
            
            while(rs.next()){
                
                Vector vl = new Vector();
                for (int i = 1; i < c; i++){
                    vl.add(rs.getString("debt_id"));
                    vl.add(rs.getString("date"));
                    vl.add(rs.getString("time"));
                    vl.add(rs.getString("transaction_no"));
                    vl.add(rs.getString("total_price"));
                    vl.add(rs.getString("debt_amount"));
                    vl.add(rs.getString("customer_name"));
                    
                }
                dt.addRow(vl);
            }
                   
        } catch (SQLException ex) {
            Logger.getLogger(GoodsListing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void CompleteTable(){
        
        try {
            pst = con.prepareStatement("select *from complete");
            rs = pst.executeQuery();
            ResultSetMetaData rsm = rs.getMetaData();
            int c;
            c = rsm.getColumnCount();
            DefaultTableModel dti = (DefaultTableModel)completeTable.getModel();
            
            dti.setRowCount(0);
            
            while(rs.next()){
                
                Vector vl = new Vector();
                for (int i = 1; i < c; i++){
                    vl.add(rs.getString("debt_id"));
                    vl.add(rs.getString("date"));
                    vl.add(rs.getString("time"));
                    vl.add(rs.getString("paid_date"));
                    vl.add(rs.getString("paid_time"));
                    vl.add(rs.getString("transaction_no"));
                    vl.add(rs.getString("total_price"));
                    vl.add(rs.getString("debt_amount"));
                    vl.add(rs.getString("customer_name"));
                    
                }
                dti.addRow(vl);
            }
                   
        } catch (SQLException ex) {
            Logger.getLogger(GoodsListing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        topic = new javax.swing.JLabel();
        insertButton = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        incompleteTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        NameTextField = new javax.swing.JTextField();
        DebtTextField = new javax.swing.JTextField();
        PaidButton = new javax.swing.JButton();
        BackButton3 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        completeTable = new javax.swing.JTable();
        DeleteButton = new javax.swing.JButton();
        RollbackButton = new javax.swing.JButton();
        timeLabel = new javax.swing.JLabel();
        dateLabel = new javax.swing.JLabel();
        timeLabel1 = new javax.swing.JLabel();
        dateLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 255, 255));

        topic.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        topic.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        topic.setText("Incomplete Transactions");
        topic.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        insertButton.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        insertButton.setText("Insert");
        insertButton.setPreferredSize(new java.awt.Dimension(160, 37));
        insertButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertButtonActionPerformed(evt);
            }
        });

        incompleteTable.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        incompleteTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Debt ID", "Date", "Time", "Trans No", "Total Price", "Debt Amount", "Customer Name"
            }
        ));
        incompleteTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                incompleteTableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(incompleteTable);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Customer Name:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Debt Amount:");

        NameTextField.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        DebtTextField.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        PaidButton.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        PaidButton.setText("Mark As Paid");
        PaidButton.setPreferredSize(new java.awt.Dimension(160, 37));
        PaidButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PaidButtonActionPerformed(evt);
            }
        });

        BackButton3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        BackButton3.setText("Back");
        BackButton3.setPreferredSize(new java.awt.Dimension(160, 37));
        BackButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BackButton3ActionPerformed(evt);
            }
        });

        completeTable.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        completeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Paid ID", "Date", "Time", "Paid Date", "Paid Time", "Trans No", "Total Price", "Debt Amount", "Customer Name"
            }
        ));
        completeTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                completeTableMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(completeTable);

        DeleteButton.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        DeleteButton.setText("Delete");
        DeleteButton.setPreferredSize(new java.awt.Dimension(160, 37));
        DeleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteButtonActionPerformed(evt);
            }
        });

        RollbackButton.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        RollbackButton.setText("Roll Back");
        RollbackButton.setPreferredSize(new java.awt.Dimension(160, 37));
        RollbackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RollbackButtonActionPerformed(evt);
            }
        });

        timeLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        timeLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        timeLabel1.setText("time:");

        dateLabel1.setText("Date:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addComponent(dateLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(dateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(93, 93, 93)
                .addComponent(topic, javax.swing.GroupLayout.PREFERRED_SIZE, 573, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(timeLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(timeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(36, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 1329, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1329, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(NameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(DebtTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(36, 36, 36)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(insertButton, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(DeleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(231, 231, 231)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(RollbackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(PaidButton, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(73, 73, 73)
                                .addComponent(BackButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(35, 35, 35))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(topic, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(timeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(timeLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dateLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)))
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(NameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(insertButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PaidButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BackButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(DebtTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(DeleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RollbackButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(88, 88, 88))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void insertButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertButtonActionPerformed
        
        
        
        try{
                
            dbett = Double.parseDouble(DebtTextField.getText()); 
            
            try{
                    pst = con.prepareStatement("update daily_goods_transactions set debt = ? where trans_no = ?");
                
                    pst.setString(1,"Yes");
                    
                    pst.setString(2,transactionNo3);
                
                    pst.executeUpdate();
                    
                    
                
                } catch (SQLException ex) {
                    Logger.getLogger(GoodsTransactions.class.getName()).log(Level.SEVERE, null, ex);
                } 
            
            try {
                pst = con.prepareStatement("insert into incomplete(date,time,transaction_no,total_price,debt_amount,customer_name) values(?,?,?,?,?,?)");
                
                //pst.setString(1, debtId);
                pst.setString(1, dateLabel.getText());
                pst.setString(2, timeLabel.getText());
                pst.setString(3, transactionNo3); 
                pst.setString(4, totalPrice3);
                pst.setString(5, String.valueOf(dbett));
                pst.setString(6, NameTextField.getText());
                
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Record Entered Successfully! ");
                
            } catch (SQLException ex) {
                Logger.getLogger(GoodsListing.class.getName()).log(Level.SEVERE, null, ex);
            }
                
        }catch(NumberFormatException e){
                
            JOptionPane.showMessageDialog(this, "debt is empty, or make sure it is a number! ");
            System.out.println("moda yakek");
       
        }
        
        
        
        
        IncompleteTable();
        insertButton.setEnabled(false);
        
    }//GEN-LAST:event_insertButtonActionPerformed

    private void PaidButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PaidButtonActionPerformed
        
        try {
                pst = con.prepareStatement("insert into complete(date,time,paid_date,paid_time,transaction_no,total_price,debt_amount,customer_name) values(?,?,?,?,?,?,?,?)");
                
                //pst.setString(1, debtId2);
                pst.setString(1, date);
                pst.setString(2, time);
                pst.setString(3, dateLabel.getText()); 
                pst.setString(4, timeLabel.getText());
                pst.setString(5, transactionNo); 
                pst.setString(6, totalPrice);
                pst.setString(7, debtAmount);
                pst.setString(8, customerName);
                
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Paid Successfully! ");
                } catch (SQLException ex) {
                 Logger.getLogger(GoodsListing.class.getName()).log(Level.SEVERE, null, ex);
                }
        
        try {
            pst = con.prepareStatement("delete from incomplete where debt_id = ?");
            pst.setString(1, debtId2);
            pst.executeUpdate();
            //JOptionPane.showMessageDialog(this, "Item Deleted Successfully! ");
          
            
        } catch (SQLException ex) {
            Logger.getLogger(GoodsListing.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        IncompleteTable();
        
        CompleteTable();
        
        Double updatedDebt = 0.0;
        Double updatedFinal = 0.0;
        
        
        if(date.equals(dateLabel.getText())){
        
            try{
                    pst = con.prepareStatement("update daily_goods_transactions set debt = ? where trans_no = ?");
                    pst.setString(1, "No");
                    pst.setString(2, transactionNo);
                    
                    pst.executeUpdate();
                    //JOptionPane.showMessageDialog(this,"Log has Saved before! Saved Successfully!");
                } catch (SQLException ex) {
                    Logger.getLogger(GoodsTransactions.class.getName()).log(Level.SEVERE, null, ex);
                }
        }else{
        
            try {
                pst = con.prepareStatement("select total_amount, debt_total from transaction_logs where saved_date = ?");
                pst.setString(1, date);
                rs = pst.executeQuery();
                while(rs.next()){
                    updatedDebt = Double.parseDouble(rs.getString("debt_total")) - Double.parseDouble(debtAmount);
                    updatedFinal = Double.parseDouble(rs.getString("total_amount")) - updatedDebt;
                    System.out.println("udated debt:"+ updatedDebt);
                    System.out.println("udated Final:"+ updatedFinal);
                    
                }
                
            try{
                    pst = con.prepareStatement("update transaction_logs set debt_total = ?, final_total = ? where saved_date = ?");
                    pst.setString(1, String.valueOf(updatedDebt));
                    pst.setString(2, String.valueOf(updatedFinal));
                    pst.setString(3, date);
                    
                    pst.executeUpdate();
                    //JOptionPane.showMessageDialog(this,"Log has Saved before! Saved Successfully!");
                } catch (SQLException ex) {
                    Logger.getLogger(GoodsTransactions.class.getName()).log(Level.SEVERE, null, ex);
                }    
                
               
                TransactionLogs ob = new TransactionLogs(date, transactionNo, 0);
                ob.dispose();
            
            } catch (SQLException ex) {
                Logger.getLogger(GoodsTransactions.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        }
        
        DeleteButton.setEnabled(false);
        PaidButton.setEnabled(false);
        RollbackButton.setEnabled(false);
        
    }//GEN-LAST:event_PaidButtonActionPerformed

    private void BackButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BackButton3ActionPerformed
        
        if(transactionNo3 != null){
           this.dispose();
           GoodsTransactions ob = new GoodsTransactions();
           ob.setVisible(true);
        }else{
            this.dispose();
            DailyTransactions ob = new DailyTransactions();
            ob.setVisible(true);
        }
        
        
    }//GEN-LAST:event_BackButton3ActionPerformed

    private void DeleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteButtonActionPerformed
        
        try{
            pst = con.prepareStatement("update daily_goods_transactions set debt = ? where trans_no = ?");
            pst.setString(1,"No");
            pst.setString(2,transactionNo3);
                
            pst.executeUpdate();
                    
        } catch (SQLException ex) {
            Logger.getLogger(GoodsTransactions.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            pst = con.prepareStatement("delete from incomplete where debt_id = ?");
            pst.setString(1, debtId2);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Item Deleted Successfully! ");
          
            
        } catch (SQLException ex) {
            Logger.getLogger(GoodsListing.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        IncompleteTable();
        
    }//GEN-LAST:event_DeleteButtonActionPerformed

    private void RollbackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RollbackButtonActionPerformed
        try {
                pst = con.prepareStatement("insert into incomplete(date,time,transaction_no,total_price,debt_amount,customer_name) values(?,?,?,?,?,?)");
                
                //pst.setString(1, debtId3);
                pst.setString(1, date2);
                pst.setString(2, time2);
                pst.setString(3, transactionNo2); 
                pst.setString(4, totalPrice2);
                pst.setString(5, debtAmount2);
                pst.setString(6, customerName2);
                
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Rollback Done! ");
                } catch (SQLException ex) {
                 Logger.getLogger(GoodsListing.class.getName()).log(Level.SEVERE, null, ex);
                }
        
        try {
            pst = con.prepareStatement("delete from complete where debt_id = ?");
            pst.setString(1, debtId3);
            pst.executeUpdate();
            //JOptionPane.showMessageDialog(this, "Item Deleted Successfully! ");
          
            
        } catch (SQLException ex) {
            Logger.getLogger(GoodsListing.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        IncompleteTable();
        
        CompleteTable();
        
        
        Double updatedDebt = 0.0;
        Double updatedFinal = 0.0;
        
         
        if(date2.equals(dateLabel.getText())){
        
            try{
                    pst = con.prepareStatement("update daily_goods_transactions set debt = ? where trans_no = ?");
                    pst.setString(1, "Yes");
                    pst.setString(2, transactionNo2);
                    
                    pst.executeUpdate();
                    //JOptionPane.showMessageDialog(this,"Log has Saved before! Saved Successfully!");
                } catch (SQLException ex) {
                    Logger.getLogger(GoodsTransactions.class.getName()).log(Level.SEVERE, null, ex);
                }
            
        }else{
        
            try {
                pst = con.prepareStatement("select total_amount, debt_total from transaction_logs where saved_date = ?");
                pst.setString(1, date2);
                rs = pst.executeQuery();
                while(rs.next()){
                    updatedDebt = Double.parseDouble(rs.getString("debt_total")) + Double.parseDouble(debtAmount2);
                    updatedFinal = Double.parseDouble(rs.getString("total_amount")) - updatedDebt;
                    System.out.println("udated debt:"+ updatedDebt);
                    System.out.println("udated Final:"+ updatedFinal);
                    
                }
                
            try{
                    pst = con.prepareStatement("update transaction_logs set debt_total = ?, final_total = ? where saved_date = ?");
                    pst.setString(1, String.valueOf(updatedDebt));
                    pst.setString(2, String.valueOf(updatedFinal));
                    pst.setString(3, date2);
                    
                    pst.executeUpdate();
                    //JOptionPane.showMessageDialog(this,"Log has Saved before! Saved Successfully!");
                } catch (SQLException ex) {
                    Logger.getLogger(GoodsTransactions.class.getName()).log(Level.SEVERE, null, ex);
                }    
                
               
                TransactionLogs ob = new TransactionLogs(date2, transactionNo2, 1);
                ob.dispose();
            
            } catch (SQLException ex) {
            Logger.getLogger(GoodsTransactions.class.getName()).log(Level.SEVERE, null, ex);
            
            }
        
        }
        
        DeleteButton.setEnabled(false);
        PaidButton.setEnabled(false);
        RollbackButton.setEnabled(false);
    
    }//GEN-LAST:event_RollbackButtonActionPerformed

    private void incompleteTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_incompleteTableMouseClicked
        
        DefaultTableModel d5 = (DefaultTableModel)incompleteTable.getModel();
        int SelectIndex = incompleteTable.getSelectedRow();
        
        debtId2 = d5.getValueAt(SelectIndex, 0).toString();
        date = d5.getValueAt(SelectIndex, 1).toString();
        time = d5.getValueAt(SelectIndex, 2).toString();
        transactionNo = d5.getValueAt(SelectIndex, 3).toString();
        totalPrice = d5.getValueAt(SelectIndex, 4).toString();
        debtAmount = d5.getValueAt(SelectIndex, 5).toString();
        customerName = d5.getValueAt(SelectIndex, 6).toString();
        
        if(transactionNo3 != null){
            DeleteButton.setEnabled(true);
        }
        
        PaidButton.setEnabled(true);
        
    }//GEN-LAST:event_incompleteTableMouseClicked

    private void completeTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_completeTableMouseClicked
        DefaultTableModel d6 = (DefaultTableModel)completeTable.getModel();
        int SelectIndex = completeTable.getSelectedRow();
        
        debtId3 = d6.getValueAt(SelectIndex, 0).toString();
        date2 = d6.getValueAt(SelectIndex, 1).toString();
        time2 = d6.getValueAt(SelectIndex, 2).toString();
        transactionNo2 = d6.getValueAt(SelectIndex, 5).toString();
        totalPrice2 = d6.getValueAt(SelectIndex, 6).toString();
        debtAmount2 = d6.getValueAt(SelectIndex, 7).toString();
        customerName2 = d6.getValueAt(SelectIndex, 8).toString();
        
        RollbackButton.setEnabled(true);
        
    }//GEN-LAST:event_completeTableMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(StartPrepaidTransactionDay.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StartPrepaidTransactionDay.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StartPrepaidTransactionDay.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StartPrepaidTransactionDay.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StartPrepaidTransactionDay().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BackButton3;
    private javax.swing.JTextField DebtTextField;
    private javax.swing.JButton DeleteButton;
    private javax.swing.JTextField NameTextField;
    private javax.swing.JButton PaidButton;
    private javax.swing.JButton RollbackButton;
    private javax.swing.JTable completeTable;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JLabel dateLabel1;
    private javax.swing.JTable incompleteTable;
    private javax.swing.JButton insertButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel timeLabel;
    private javax.swing.JLabel timeLabel1;
    private javax.swing.JLabel topic;
    // End of variables declaration//GEN-END:variables
}

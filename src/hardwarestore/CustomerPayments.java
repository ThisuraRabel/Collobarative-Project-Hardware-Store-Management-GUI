/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hardwarestore;

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
 * @author thisa
 */
public class CustomerPayments extends javax.swing.JFrame {

    /**
     * Creates new form Transactions  
     */
    private Connection con = null;
    private PreparedStatement pst;
    private ResultSet rs; 
    private String custName;
    private String custID;
    private int lastpaidIndicator = 0;
    private String lastPaid = "null";
    private String CustBalance;
    private String prevAmt; 
    private double difference = 0.0;
    
    public CustomerPayments() {
        initComponents();
    }
    
     public CustomerPayments(String pCustID, String pName) {
        initComponents();
        HelperClass.Date(jLabel2);
        HelperClass.ShowTime(jLabel4);  
        con = HelperClass.connect();
        
        this.custID = pCustID;
        this.custName = pName;
        
        DisplayCustomerDetails(); //after all
        CustomerPaymentTable();
        clearFields();
        autoID();
   
    }
     
    public void DisplayCustomerDetails(){
        
        try {
            pst = con.prepareStatement("select * from postpaid_customer_transactions where cust_id = ?");
            pst.setString(1, custID);
            rs = pst.executeQuery();

            ResultSetMetaData rsm = rs.getMetaData();
            int c;
            c = rsm.getColumnCount();
            
            while(rs.next()){
 
                for (int i = c; i >= 0; i--){
                   
                    String tmp = rs.getString("amount");
                    
                    if (tmp.equals("0.0") == false && lastpaidIndicator == 0){
                        lastPaid = rs.getString("date");
                        lastpaidIndicator++;
                    }
                    
                    if(i == c - 1){
                        CustBalance = rs.getString("balance");
                    }  
                }
              
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(CustomerPayments.class.getName()).log(Level.SEVERE, null, ex);
        }
 
        jLabel13.setText(custName);
        jLabel17.setText(CustBalance);
        jLabel16.setText(lastPaid);
          
    } 
    
     public void autoID(){
        Statement s;
        try {
            s = con.createStatement();
            rs = s.executeQuery("select MAX(cust_trans_id) from postpaid_customer_transactions");
            rs.next();
            rs.getString("MAX(cust_trans_id)");
            
            if (rs.getString("MAX(cust_trans_id)") == null){
                item_nojLabel.setText("CT000001");              
            }
            else{
                long id = Long.parseLong(rs.getString("MAX(cust_trans_id)").substring(2,rs.getString("MAX(cust_trans_id)").length()));               
                id++;
                item_nojLabel.setText("CT" + String.format("%06d", id));
            }
        } catch (SQLException ex) {
            Logger.getLogger(GoodsListing.class.getName()).log(Level.SEVERE, null, ex);
        }
     
    }
     
       public void CustomerPaymentTable(){
        
        try {
            pst = con.prepareStatement("select *from postpaid_customer_transactions where cust_id = ? and quantity = ?");
            pst.setString(1, custID.toString());
            pst.setString(2, "");
            rs = pst.executeQuery();
            
            ResultSetMetaData rsm = rs.getMetaData();
            int c;
            c = rsm.getColumnCount();
            DefaultTableModel dt = (DefaultTableModel)jTable1.getModel();
            
            dt.setRowCount(0);
            
            while(rs.next()){
                
                Vector vl = new Vector();
                for (int i = 1; i < c; i++){

                        vl.add(rs.getString("cust_trans_id"));
                        vl.add(rs.getString("date"));
                        vl.add(rs.getString("time"));
                        vl.add(rs.getString("description"));
                        vl.add(rs.getString("amount"));         
                        vl.add(rs.getString("balance"));

                }
                dt.addRow(vl);
            }
                   
        } catch (SQLException ex) {
            Logger.getLogger(GoodsListing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       
    private void clearFields(){
       
        jButton10.setEnabled(false);  
        jButton13.setEnabled(false);  
        jButton12.setEnabled(false);  
        
        jTextField6.setText("");       
        jTextField3.setText("");  
        jLabel18.setVisible(false);       
        jLabel19.setVisible(false);
        jTextField4.setVisible(false);       
        jTextField7.setVisible(false);   
        jTextField6.requestFocus();        
        jButton10.setEnabled(true);
       
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        item_nojLabel = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jButton10 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 255, 255));

        jLabel1.setFont(new java.awt.Font("Cambria Math", 1, 24)); // NOI18N
        jLabel1.setText("Date:");

        jLabel2.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        jLabel2.setText("DD/MM/YY");

        jLabel11.setFont(new java.awt.Font("Comic Sans MS", 1, 48)); // NOI18N
        jLabel11.setText("    Customer Payments");

        jLabel3.setFont(new java.awt.Font("Cambria Math", 1, 24)); // NOI18N
        jLabel3.setText("Time:");

        jLabel4.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        jLabel4.setText("Min/Sec");

        jLabel6.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        jLabel6.setText("Customer Transaction No:");

        item_nojLabel.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        jLabel7.setText("Description:");

        jTextField6.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        jLabel8.setText("Amount:   ");

        jTextField3.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jButton10.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        jButton10.setText("Insert");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton12.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        jButton12.setText("Re-new");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton13.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        jButton13.setText("Delete");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton9.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        jButton9.setText("Back");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cust_Trans_ID", "Date", "Time", "Description", "Amount(Rs.)", "Balance"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setMinWidth(120);
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(120);
            jTable1.getColumnModel().getColumn(0).setMaxWidth(120);
            jTable1.getColumnModel().getColumn(1).setMinWidth(100);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(100);
            jTable1.getColumnModel().getColumn(1).setMaxWidth(100);
            jTable1.getColumnModel().getColumn(2).setMinWidth(120);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(120);
            jTable1.getColumnModel().getColumn(2).setMaxWidth(120);
            jTable1.getColumnModel().getColumn(4).setMinWidth(100);
            jTable1.getColumnModel().getColumn(4).setPreferredWidth(100);
            jTable1.getColumnModel().getColumn(4).setMaxWidth(100);
            jTable1.getColumnModel().getColumn(5).setMinWidth(100);
            jTable1.getColumnModel().getColumn(5).setPreferredWidth(100);
            jTable1.getColumnModel().getColumn(5).setMaxWidth(100);
        }

        jLabel15.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        jLabel15.setText("Last Paid:");

        jLabel10.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        jLabel10.setText("Balance:");

        jLabel16.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        jLabel16.setText("last paid(sql select)");

        jLabel17.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        jLabel17.setText("balance");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(181, Short.MAX_VALUE)
                .addComponent(jLabel16)
                .addGap(167, 167, 167))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(59, 59, 59)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel10)
                            .addGap(33, 33, 33)
                            .addComponent(jLabel17))
                        .addComponent(jLabel15))
                    .addContainerGap(59, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(84, Short.MAX_VALUE)
                .addComponent(jLabel16)
                .addGap(31, 31, 31))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(28, 28, 28)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel10)
                        .addComponent(jLabel17))
                    .addGap(29, 29, 29)
                    .addComponent(jLabel15)
                    .addContainerGap(29, Short.MAX_VALUE)))
        );

        jLabel12.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        jLabel12.setText("Customer Name: ");

        jLabel13.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        jLabel13.setText("name");

        jLabel18.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        jLabel18.setText("Date:");

        jTextField7.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        jLabel19.setText("Time: ");

        jTextField4.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(40, 40, 40)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel19)
                                    .addComponent(jLabel18))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(59, 59, 59)
                                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTextField4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextField7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 119, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 876, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jLabel1)
                                .addGap(28, 28, 28)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel4))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addGap(18, 18, 18)
                                        .addComponent(item_nojLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addGap(36, 36, 36)
                                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel12)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel13))
                                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(87, 87, 87))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(296, 296, 296)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 719, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addGap(33, 33, 33)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13))
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(item_nojLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(83, 83, 83)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9))
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 76, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 477, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(33, 33, 33)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18))
                        .addGap(29, 29, 29)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(57, 57, 57)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(42, 42, 42))))
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
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed

        try {
            double d1;
            d1 = Double.parseDouble(CustBalance) - Double.parseDouble(jTextField3.getText());

            try {
                pst = con.prepareStatement("insert into postpaid_customer_transactions(cust_trans_id,date,time,description,quantity,sold_price,balance,amount,cust_id,name) values(?,?,?,?,?,?,?,?,?,?)");

                pst.setString(1, item_nojLabel.getText());
                pst.setString(2, jLabel2.getText());
                pst.setString(3, jLabel4.getText());
                pst.setString(4, jTextField6.getText());
                pst.setString(5, "");
                pst.setString(6, "");
                pst.setString(7, Double.toString(d1)); 
                pst.setString(8, jTextField3.getText());
                pst.setString(9, custID);
                pst.setString(10, custName);

                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Item Entered Successfully! ");
            } catch (SQLException ex) {
                     Logger.getLogger(GoodsListing.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch(Exception e){
             JOptionPane.showMessageDialog(this, "Please enter valid Amount");
        }
  
        CustomerPaymentTable();
        clearFields();
        autoID();
        DisplayCustomerDetails();
        
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        //need to be fixed
        try{
            
            //get the balance from prev transaction       
            try {
                pst = con.prepareStatement("select *from postpaid_customer_transactions where cust_trans_id = ?");
                pst.setString(1, item_nojLabel.getText());
                rs = pst.executeQuery();       
                while(rs.next()){
                    CustBalance = rs.getString("balance");
                }     
                 System.out.println(CustBalance);
            }catch (SQLException ex) {
                Logger.getLogger(GoodsTransactions.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(CustBalance);
            double d1; 
            d1 = Double.parseDouble(CustBalance) + Double.parseDouble(prevAmt) - Double.parseDouble(jTextField3.getText());
            System.out.println(CustBalance);
            difference = Double.parseDouble(prevAmt) - Double.parseDouble(jTextField3.getText());
            
                try {
                    pst = con.prepareStatement("update postpaid_customer_transactions set date = ?, time = ?, description = ?, quantity = ?, sold_price = ?, balance = ?, amount = ?, cust_id = ?, name = ? where cust_trans_id = ?");
                
                    pst.setString(1, jTextField7.getText());
                    pst.setString(2, jTextField4.getText());
                    pst.setString(3, jTextField6.getText());
                    pst.setString(4, "");
                    pst.setString(5, "");
                    pst.setString(6, Double.toString(d1));
                    pst.setString(7, jTextField3.getText());
                    pst.setString(8, custID);
                    pst.setString(9, custName);
                    pst.setString(10, item_nojLabel.getText());
                    pst.executeUpdate();
                    
                    JOptionPane.showMessageDialog(this, "Item Updated Successfully! ");
                } catch (SQLException ex) {
                         Logger.getLogger(GoodsListing.class.getName()).log(Level.SEVERE, null, ex);
                }
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Please enter valid Amount ");
        } 
        
        try {
            pst = con.prepareStatement("select *from postpaid_customer_transactions where cust_id = ?");
            pst.setString(1, custID);
            rs = pst.executeQuery();
            
            while(rs.next()){
                String compStr = rs.getString("cust_trans_id");
                String custTID= rs.getString("cust_trans_id");                
                String prevBal = rs.getString("balance");

                int compTrasactionNumPart = Integer.parseInt(compStr.substring(2,8));

                int TrasactionNumPart = Integer.parseInt(item_nojLabel.getText().substring(2,8));
                
                System.out.println("comp T:" + compTrasactionNumPart);//
                System.out.println("current T:" + TrasactionNumPart);//
         
                if (compTrasactionNumPart > TrasactionNumPart){
                                                       // System.out.println("prevBal T:" + prevBal); //
                                                       //  System.out.println("compStr T:" + custTID); //
                                                         //System.out.println("compStr T:" + newBal); //

                    pst = con.prepareStatement("update postpaid_customer_transactions set balance = ? where cust_trans_id = ?");
                    double newBal = Double.parseDouble(prevBal) + difference;
                    pst.setString(1, Double.toString(newBal));
                    pst.setString(2, custTID);
                    pst.executeUpdate();

                }
            }
                         
        } catch (SQLException ex) {
            Logger.getLogger(GoodsTransactions.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        CustomerPaymentTable();
        clearFields();
        autoID();
        DisplayCustomerDetails();
        
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // renew the balance
         String compCTID ="";
        String currentCTID = item_nojLabel.getText();    
        try {
                pst = con.prepareStatement("select *from postpaid_customer_transactions where cust_id = ?");
                pst.setString(1, custID);
                rs = pst.executeQuery();
                ResultSetMetaData rsm = rs.getMetaData();
                int c;
                c = rsm.getColumnCount();
               
                
                while(rs.next()){
  
                    for (int i = 1; i < c; i++){      

                        if(i == c - 1){
                            compCTID = rs.getString("cust_trans_id");
                        }
                    }
                }
                         
        }catch (SQLException ex) {
                Logger.getLogger(GoodsTransactions.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println("compCTID T:" + compCTID); 
        //System.out.println("currentCTID T:" + currentCTID);
      
        if(compCTID.equals(currentCTID)){
       
            try {
            pst = con.prepareStatement("delete from postpaid_customer_transactions where cust_trans_id = ?");
            pst.setString(1, item_nojLabel.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Item Deleted Successfully! ");
          
        } catch (SQLException ex) {
            Logger.getLogger(GoodsListing.class.getName()).log(Level.SEVERE, null, ex);
        }

            CustomerPaymentTable();
            clearFields();
            autoID();
            DisplayCustomerDetails();
       }
       else{
           JOptionPane.showMessageDialog(this, "You can delete only the last entry! ");
       }
 
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        this.dispose();
        SelectCustomer ob = new SelectCustomer();
        ob.setVisible(true);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked

        jButton13.setEnabled(true);  
        jButton12.setEnabled(true);  
        jButton10.setEnabled(false);  
        
        DefaultTableModel d1 = (DefaultTableModel)jTable1.getModel();
        int SelectIndex = jTable1.getSelectedRow();  
        
        item_nojLabel.setText(d1.getValueAt(SelectIndex, 0).toString());
        jTextField7.setText(d1.getValueAt(SelectIndex, 1).toString());
        jTextField4.setText(d1.getValueAt(SelectIndex, 2).toString());
        jTextField6.setText(d1.getValueAt(SelectIndex, 3).toString());
        jTextField3.setText(d1.getValueAt(SelectIndex, 4).toString());
        prevAmt = jTextField3.getText();
        
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

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
            java.util.logging.Logger.getLogger(CustomerPayments.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CustomerPayments.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CustomerPayments.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CustomerPayments.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CustomerPayments().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel item_nojLabel;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    // End of variables declaration//GEN-END:variables
}

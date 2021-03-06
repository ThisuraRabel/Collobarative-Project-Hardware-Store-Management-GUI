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
public class OtherTransactions extends javax.swing.JFrame {

    private Connection con = null;
    private PreparedStatement pst;
    private ResultSet rs;
    private String amount;
    private String InOrout;
    /**
     * Creates new form OtherTransactions
     */
    public OtherTransactions() {
        initComponents();
        HelperClass.Date(jLabel2);
        HelperClass.ShowTime(jLabel4);
        con = HelperClass.connect();
        
        autoID();
        OtherTransactionTable();
        jLabel14.setVisible(false);
        jTextField4.setVisible(false);
        jLabel15.setVisible(false);
        jTextField7.setVisible(false);
    }

     public void autoID(){
        Statement s;
        try {
            s = con.createStatement();
            rs = s.executeQuery("select MAX(trans_id) from other_transactions");
            rs.next();
            rs.getString("MAX(trans_id)");
            
            if (rs.getString("MAX(trans_id)") == null){
                transaction_nojLabel.setText("TR000001");              
            }
            else{
                long id = Long.parseLong(rs.getString("MAX(trans_id)").substring(2,rs.getString("MAX(trans_id)").length()));
                id++;
                transaction_nojLabel.setText("TR" + String.format("%06d", id));
            }
        } catch (SQLException ex) {
            Logger.getLogger(GoodsListing.class.getName()).log(Level.SEVERE, null, ex);
        }
     
    }
     
       public void OtherTransactionTable(){
        
        try {
            pst = con.prepareStatement("select *from other_transactions");
            rs = pst.executeQuery();
            ResultSetMetaData rsm = rs.getMetaData();
            int c;
            c = rsm.getColumnCount();
            DefaultTableModel dt = (DefaultTableModel)otherTranscationsjTable.getModel();
            
            dt.setRowCount(0);
            
            while(rs.next()){
                
                Vector vl = new Vector();
                for (int i = 1; i < c; i++){
                    vl.add(rs.getString("trans_id"));
                    vl.add(rs.getString("date"));                    
                    vl.add(rs.getString("time"));
                    vl.add(rs.getString("description"));
                    vl.add(rs.getString("in_or_out"));
                    vl.add(rs.getString("amount"));
                }
                dt.addRow(vl);
            }
                   
        } catch (SQLException ex) {
            Logger.getLogger(GoodsListing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       
       
    private void clearFields(){
        jTextField6.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField7.setText("");
        jCheckBox1.setSelected(false);       
        jTextField6.requestFocus(); 
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
        jScrollPane1 = new javax.swing.JScrollPane();
        otherTranscationsjTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        transaction_nojLabel = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton10 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 255, 255));

        otherTranscationsjTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Trans_ID", "Date", "Time", "Description", "In or Out", "Amount(Rs.)"
            }
        ));
        otherTranscationsjTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                otherTranscationsjTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(otherTranscationsjTable);
        if (otherTranscationsjTable.getColumnModel().getColumnCount() > 0) {
            otherTranscationsjTable.getColumnModel().getColumn(0).setMinWidth(100);
            otherTranscationsjTable.getColumnModel().getColumn(0).setPreferredWidth(100);
            otherTranscationsjTable.getColumnModel().getColumn(0).setMaxWidth(100);
            otherTranscationsjTable.getColumnModel().getColumn(1).setMinWidth(120);
            otherTranscationsjTable.getColumnModel().getColumn(1).setPreferredWidth(120);
            otherTranscationsjTable.getColumnModel().getColumn(1).setMaxWidth(120);
            otherTranscationsjTable.getColumnModel().getColumn(2).setMinWidth(120);
            otherTranscationsjTable.getColumnModel().getColumn(2).setPreferredWidth(120);
            otherTranscationsjTable.getColumnModel().getColumn(2).setMaxWidth(120);
            otherTranscationsjTable.getColumnModel().getColumn(4).setMinWidth(80);
            otherTranscationsjTable.getColumnModel().getColumn(4).setPreferredWidth(80);
            otherTranscationsjTable.getColumnModel().getColumn(4).setMaxWidth(80);
            otherTranscationsjTable.getColumnModel().getColumn(5).setMinWidth(120);
            otherTranscationsjTable.getColumnModel().getColumn(5).setPreferredWidth(120);
            otherTranscationsjTable.getColumnModel().getColumn(5).setMaxWidth(120);
        }

        jLabel1.setFont(new java.awt.Font("Cambria Math", 1, 24)); // NOI18N
        jLabel1.setText("Date:");

        jLabel2.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        jLabel2.setText("DD/MM/YY");

        jLabel3.setFont(new java.awt.Font("Cambria Math", 1, 24)); // NOI18N
        jLabel3.setText("Time:");

        jLabel4.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        jLabel4.setText("Min/Sec");

        jLabel11.setFont(new java.awt.Font("Comic Sans MS", 1, 48)); // NOI18N
        jLabel11.setText("           Other Transactions");

        jLabel6.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        jLabel6.setText("Transaction No:");

        transaction_nojLabel.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        transaction_nojLabel.setText("no");

        jLabel7.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        jLabel7.setText("Description:");

        jTextField6.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        jLabel8.setText("Amount:            Rs.");

        jTextField3.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jCheckBox1.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox1.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        jCheckBox1.setText("In ");
        jCheckBox1.setMargin(new java.awt.Insets(5, 5, 5, 5));

        jButton10.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        jButton10.setText("Insert");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton13.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        jButton13.setText("Delete");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton12.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        jButton12.setText("Re-new");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton9.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        jButton9.setText("Back");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        jLabel14.setText("Date:");

        jTextField4.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        jLabel15.setText("Time:");

        jTextField7.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(243, 243, 243)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 901, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(28, 28, 28)
                        .addComponent(jLabel2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 105, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addGap(108, 108, 108))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1007, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(163, 163, 163)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                        .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel8)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField3))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel7)
                            .addGap(55, 55, 55)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(transaction_nojLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(154, 154, 154)
                        .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(237, 237, 237))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 110, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(transaction_nojLabel)
                            .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(33, 33, 33)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(44, 44, 44))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextField4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(76, 76, 76)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(57, 57, 57)
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(56, 56, 56)
                        .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(62, 62, 62)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(99, 99, 99))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 540, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        try{
    
            amount = jTextField3.getText();
                    
            double d1;
            d1 = Double.parseDouble(amount);

            if(jCheckBox1.isSelected()){            
               InOrout = "IN";
            }
            else{
               InOrout = "OUT";
            }

            try {
            pst = con.prepareStatement("insert into other_transactions(trans_id,date,time,description,in_or_out,amount) values(?,?,?,?,?,?)");
            pst.setString(1, transaction_nojLabel.getText());
            pst.setString(2, jLabel2.getText());
            pst.setString(3, jLabel4.getText());            
            pst.setString(4, jTextField6.getText());            
            pst.setString(5, InOrout);
            pst.setString(6, Double.toString(d1));
         
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Item Entered Successfully! ");
            } catch (SQLException ex) {
                 Logger.getLogger(GoodsListing.class.getName()).log(Level.SEVERE, null, ex);
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Please make shure Amount is floating point numbers! ");
        }
      
        clearFields();
        OtherTransactionTable();
        autoID();
        jLabel14.setVisible(false);
        jTextField4.setVisible(false);
        jLabel15.setVisible(false);
        jTextField7.setVisible(false);
     
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        try {
            pst = con.prepareStatement("delete from other_transactions where trans_id = ?");
            pst.setString(1, transaction_nojLabel.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Item Deleted Successfully! ");
            clearFields();
            jButton10.setEnabled(true);
            
        } catch (SQLException ex) {
            Logger.getLogger(GoodsListing.class.getName()).log(Level.SEVERE, null, ex);
        }
        autoID();
        OtherTransactionTable();
        jButton10.setEnabled(true);   
        jLabel14.setVisible(false);
        jTextField4.setVisible(false);
        jLabel15.setVisible(false);
        jTextField7.setVisible(false);
       

    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed

        try{
    
            amount = jTextField3.getText();
                    
            double d1;
            d1 = Double.parseDouble(amount);

            if(jCheckBox1.isSelected()){            
               InOrout = "IN";
            }
            else{
               InOrout = "OUT";
            }
           
            try{
                pst = con.prepareStatement("update other_transactions set date = ?, time = ?, description = ?, in_or_out = ? , amount = ? where trans_id = ?");
                pst.setString(1,jTextField4.getText());
                pst.setString(2,jTextField7.getText());
                pst.setString(3,jTextField6.getText());
                pst.setString(4,InOrout);
                pst.setString(5,jTextField3.getText());               
                pst.setString(6,transaction_nojLabel.getText());
         
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Item Updated Successfully! ");
               
            } catch (SQLException ex) {
                 Logger.getLogger(GoodsListing.class.getName()).log(Level.SEVERE, null, ex);
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Please make shure Amount is floating point numbers! ");
        }
      
        clearFields();
        OtherTransactionTable();
        autoID();
        jButton10.setEnabled(true);   
        jLabel14.setVisible(false);
        jTextField4.setVisible(false);
        jLabel15.setVisible(false);
        jTextField7.setVisible(false);
      
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        this.dispose();
        DailyTransactions ob = new DailyTransactions();
        ob.setVisible(true);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void otherTranscationsjTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_otherTranscationsjTableMouseClicked
        
        DefaultTableModel d1 = (DefaultTableModel)otherTranscationsjTable.getModel();
        int SelectIndex = otherTranscationsjTable.getSelectedRow();
        
        transaction_nojLabel.setText(d1.getValueAt(SelectIndex, 0).toString());       
        jTextField4.setText(d1.getValueAt(SelectIndex, 1).toString());
        jTextField7.setText(d1.getValueAt(SelectIndex, 2).toString());
        jTextField6.setText(d1.getValueAt(SelectIndex, 3).toString());
        InOrout = d1.getValueAt(SelectIndex, 4).toString();
        
        if("IN".equals(InOrout)){   
            jCheckBox1.setSelected(true);
        }
        
        jTextField3.setText(d1.getValueAt(SelectIndex, 5).toString());
        
        jLabel14.setVisible(true);
        jTextField4.setVisible(true);
        jLabel15.setVisible(true);
        jTextField7.setVisible(true);
      
        jButton10.setEnabled(false);
    }//GEN-LAST:event_otherTranscationsjTableMouseClicked

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
        // TODO add your handling code here://incompletence,trans_no,date,time,item_no,description,quantity,sold_price
    }//GEN-LAST:event_jTextField7ActionPerformed

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
            java.util.logging.Logger.getLogger(OtherTransactions.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(OtherTransactions.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(OtherTransactions.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OtherTransactions.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new OtherTransactions().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTable otherTranscationsjTable;
    private javax.swing.JLabel transaction_nojLabel;
    // End of variables declaration//GEN-END:variables
}

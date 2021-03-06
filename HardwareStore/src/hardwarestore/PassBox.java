/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hardwarestore;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author thisa
 */
public class PassBox extends javax.swing.JFrame {
 
    /**
     * Creates new form PassBox
     */
    private Connection con = null;
    private PreparedStatement pst;
    private ResultSet rs;  
    private PreparedStatement pst1;
    private ResultSet rs1;    
    private JLabel date;
    private String incomp;
    private String soldPrice;
    private FileWriter writer;
    private String quantity; 
    private String profit;
    private String cost;
       
    public PassBox() {
        initComponents();   
    }
    
    public PassBox(JLabel date) {
        initComponents();   
        searchbox.setText("");
        searchbox.requestFocus();
        HelperClass.Date(date);
        this.date = date;
        con = HelperClass.connect();
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
        jLabel16 = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        searchbox = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));

        jLabel16.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        jLabel16.setText("Enter Authentication Password: ");

        jButton9.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        jButton9.setText("Back");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        jButton10.setText("Ok");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        searchbox.setText("jPasswordField1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(searchbox, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 365, Short.MAX_VALUE)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(113, 113, 113))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(175, 175, 175)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(searchbox, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 166, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(99, 99, 99))
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

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        this.dispose();
        TransactionLogs ob = new TransactionLogs();
        ob.setVisible(true);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
          
        String ps = "2021";
        
        if (ps.equals(searchbox.getText())){

            String logID = date.getText().substring(0,10);
            soldPrice = "0.0";
            try {
                pst = con.prepareStatement("select *from daily_goods_transactions where date = ?");
                pst.setString(1, date.getText());
                rs = pst.executeQuery();

                try {
                    writer = new FileWriter("C:\\Users\\thisa\\Desktop\\Do not Delete\\DailyTransactions\\" +"TransLog" + logID +".txt");
                    writer.write("\n" + "\n" +  "\n"+ "------GOODS TRANSACTIONS------" + "\n" +  "\n"); 

                    writer.write(" Incompletence:   " + "Transaction No        " + "Date         " + "Time                          " + "Item No              " +
                                 "Description                                                               " + "Quantity     " 
                                 + "Sold Price      " + "Profit " + "\n");
                } catch (Exception e) {
                    Logger.getLogger(PassBox.class.getName()).log(Level.SEVERE, null, e);              
                }
                
                double d1 = 0.0;
                double d2 = 0.0;
                int itemCount = 0; 
                
                while(rs.next()){
                  
                    incomp =  rs.getString("incompletence");
                    String transNo = rs.getString("trans_no");
                    String pDate = rs.getString("date");
                    String time =  rs.getString("time");
                    String itemNo = rs.getString("item_no");
                    String desc = rs.getString("description");
                    quantity =  rs.getString("quantity");
                    soldPrice = rs.getString("sold_price");
                    
                    //calculate profit for each
                    pst1 = con.prepareStatement("select *from goods_listing where item_no = ?");
                    pst1.setString(1, itemNo);
                    rs1 = pst1.executeQuery();
                    
                    while(rs1.next()){
                        cost = rs1.getString("cost");
                    }
                    
                    double d3 = Double.parseDouble(soldPrice) / Double.parseDouble(quantity);
                    d3 = d3 - Double.parseDouble(cost);
                    profit = Double.toString(d3);
                    
                    System.out.println("Pro:  " + profit);
                    //add rows to file
                    try {   
                        writer.write("             " + incomp + "                 " 
                                + transNo + "        " + pDate + "    " + time + "         " 
                                + itemNo + "            " + desc + "                        "
                                + quantity + "             " + soldPrice + "         " + profit + "\n" );
                       
                    } catch (IOException ex) {
                        Logger.getLogger(PassBox.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    //get total values
                    d1 = d1 + Double.parseDouble(soldPrice);
                    soldPrice = Double.toString(d1);
 
                    d2 = d2 + Double.parseDouble(profit);
                    profit = Double.toString(d2);
                    
                    itemCount++;

                }//end of main while 
                
                //Write Other Transactions

                writer.write( "\n"+ "\n" +"------OTHER TRANSACTIONS------" + "\n" +  "\n"); 
                writer.write(" Transaction ID:   " + "Date         " + "Time                      " +
                             "Description              " +
                             "In or Out         " + "Amount " + "\n");
                
                pst = con.prepareStatement("select *from other_transactions where date = ?");
                pst.setString(1, date.getText());
                rs = pst.executeQuery();

                double leftAmt = 0.0;
                
                while(rs.next()){
                    try {   
                         writer.write("        " + rs.getString("trans_id") + "           " 
                        + rs.getString("date")  + "        " + rs.getString("time")  + "    "      
                        + rs.getString("description") + "          " + rs.getString("in_or_out")
                        + rs.getString("amount") + "\n" );

                    } catch (IOException ex) {
                         Logger.getLogger(PassBox.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    if(rs.getString("in_or_out").equals("IN")){
                        leftAmt = leftAmt + Double.parseDouble(rs.getString("amount"));
                    }
                    else{
                        leftAmt = leftAmt - Double.parseDouble(rs.getString("amount"));
                    }
                    
                }
                writer.write("\n" + "\n" + "\n" + "\n");
                writer.write("=================================================================="
                           + "==================================================================");
                writer.write("\n" + "\n" + "-----SUMMARY----" + "\n");           
                System.out.println("total Normal transactions: " + soldPrice);
                System.out.println("total Profit: " + profit);
                System.out.println("Other transactions left Amount: " + leftAmt);
                System.out.println("No of Items Sold: " + itemCount);
                writer.write("\n" + "\n" + "              total Normal transactions: " + soldPrice + "\n" + "              total Profit: " + profit + "\n");
                writer.write("               Other transactions left Amount: " + Double.toString(leftAmt)+ "\n" +"               No of Items Sold: " + itemCount);
                writer.close();

            } catch (SQLException ex) {
                Logger.getLogger(GoodsListing.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(PassBox.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            this.dispose();
            TransactionLogs ob = new TransactionLogs(); //**i have changed the parameterized constructor
            ob.setVisible(true);
       
           
        }
        else{
            JOptionPane.showMessageDialog(this, "Password Incorrect! ");
            searchbox.setText("");
            searchbox.requestFocus();
        }

    }//GEN-LAST:event_jButton10ActionPerformed

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
            java.util.logging.Logger.getLogger(PassBox.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PassBox.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PassBox.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PassBox.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PassBox().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField searchbox;
    // End of variables declaration//GEN-END:variables
}

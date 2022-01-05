/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hardwarestore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
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
public class TransactionLogs extends javax.swing.JFrame {

    /**
     * Creates new form TransactionLogs
     */
    private Connection con = null;
    private PreparedStatement pst;
    private ResultSet rs; 
   
    private String logDate;
    
    
    private String transNo;
    private String insertedDate;
    private String insertedTime;
    private String itemNo;
    private String description;
    private String unitPrice;
    private String quantity;
    private String totalPrice;
    private String updatedDate;
    private String updatedTime;
    private String debt;
    
    private String transId;  //*********
    private int z1 = 1;
    
    private Double quantityField;
    
    private String quantityAvailable;
    private String quantityAvailable2;
    private String prevQuantity;
    
    private String itemNo2;
    private String description2;
    private String unitPrice2;
    
    private Vector originalTableModel;
    private Vector originalTableModel1;
    
    private String transactionNo3;
    private String itemNo3;
    
    
    
    
    public TransactionLogs() {
        initComponents();
        
        HelperClass.Date(dateLabel);
        HelperClass.ShowTime(timeLabel);
        
        con = HelperClass.connect();
           
        transLogsTable();
        StockTable();
        
        //SearchBox2.setText("");           
        //SearchBox2.requestFocus(); 
        
        StockTable.setVisible(false);
        insertButton.setEnabled(false);
        deleteButton.setEnabled(false);
        
        originalTableModel = (Vector) ((DefaultTableModel) TransLogtable.getModel()).getDataVector().clone();//backup of original values to check
        originalTableModel1 = (Vector) ((DefaultTableModel) StockTable.getModel()).getDataVector().clone();
    }
    
    public TransactionLogs(String dDate, String dTransId, int No) {
        initComponents();
        
        HelperClass.Date(dateLabel);
        HelperClass.ShowTime(timeLabel);
        
        con = HelperClass.connect();
        
        insertButton.setEnabled(false);
        deleteButton.setEnabled(false);
        jButton7.setEnabled(false);
        jButton6.setEnabled(false);
        
        try {
            pst = con.prepareStatement("delete from temp_daily_transactions");
            
            pst.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(GoodsListing.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        File f1 = new File("C:\\Users\\THR\\Documents\\NetBeansProjects\\HardwareSaves\\HFiles\\"+dDate+".txt");
        
        try {
            FileInputStream fstream = new FileInputStream(f1);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            
            try {
                while((strLine = br.readLine()) != null){
                    String[] tokens = strLine.split("   ");
                    transNo = tokens[0];
                    insertedDate = tokens[1];
                    insertedTime = tokens[2];
                    itemNo = tokens[3];
                    description = tokens[4];      
                    unitPrice = tokens[5];      
                    quantity = tokens[6];     
                    totalPrice = tokens[7];
                    updatedDate = tokens[8];      
                    updatedTime = tokens[9];
                    debt = tokens[10];      
                            
                    
                    try {
                        pst = con.prepareStatement("insert into temp_daily_transactions(trans_no,inserted_date,inserted_time,item_no,description,unit_price,quantity,total_price,updated_date,updated_time,debt) values(?,?,?,?,?,?,?,?,?,?,?)");

                        pst.setString(1, transNo);
                        pst.setString(2, insertedDate);
                        pst.setString(3, insertedTime);
                        pst.setString(4, itemNo); 
                        pst.setString(5, description);
                        pst.setString(6, unitPrice);
                        pst.setString(7, quantity);
                        pst.setString(8, totalPrice);
                        pst.setString(9, updatedDate);
                        pst.setString(10, updatedTime);
                        pst.setString(11, debt);
                        pst.executeUpdate();
                        //JOptionPane.showMessageDialog(this, "Item Entered Successfully! ");
                    } catch (SQLException ex) {
                         Logger.getLogger(GoodsListing.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                in.close();
                
            } catch (IOException ex) {
                Logger.getLogger(TransactionLogs.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StartPrepaidTransactionDay.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(No == 0){
            try {
                pst = con.prepareStatement("update temp_daily_transactions set debt = ? where trans_no = ?");
                pst.setString(1, "No");
                pst.setString(2, dTransId);
                pst.executeUpdate();
            } catch (SQLException ex) {
                    Logger.getLogger(GoodsTransactions.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if(No == 1){
            try {
                pst = con.prepareStatement("update temp_daily_transactions set debt = ? where trans_no = ?");
                pst.setString(1, "Yes");
                pst.setString(2, dTransId);
                pst.executeUpdate();
            } catch (SQLException ex) {
                    Logger.getLogger(GoodsTransactions.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
      
      
      TempDailyTransactionTable();
       
      //*******************saving to file
                    File f2 = new File("C:\\Users\\THR\\Documents\\NetBeansProjects\\HardwareSaves\\HFiles\\"+dDate+".txt");
        
                    try {
                        FileWriter fw = new FileWriter(f2);
                        BufferedWriter bf = new BufferedWriter(fw);

                        for(int i=0; i<DailyGoodsTransactionTable.getRowCount(); i++){   

                            for(int j=0; j<DailyGoodsTransactionTable.getColumnCount(); j++){

                                bf.write(DailyGoodsTransactionTable.getValueAt(i, j).toString()+"   ");

                            }

                            bf.newLine();

                        }

                        bf.close();
                        fw.close();

                    } catch (IOException ex) {
                        Logger.getLogger(GoodsTransactions.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //*******************
      
      
        
    }
    
    public void autoID(){
        Statement s;
        try {
            s = con.createStatement();
            rs = s.executeQuery("select MAX(trans_no) from temp_daily_transactions");
            rs.next();
            rs.getString("MAX(trans_no)");
            
            if (rs.getString("MAX(trans_no)") == null){
                transId = "TR000001";    
                
            }
            else{
                long id = Long.parseLong(rs.getString("MAX(trans_no)").substring(2,rs.getString("MAX(trans_no)").length()));
                id++;
                transId = "TR" + String.format("%06d", id);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GoodsListing.class.getName()).log(Level.SEVERE, null, ex);
        }
     
    }
    
    public void CalculateTotPrice(){
     
            try{
                
                quantityField = Double.parseDouble(quantityTextField.getText());
                
                double p1 = quantityField * Double.parseDouble(unitPrice2);
                
                totalPriceL1.setText(String.valueOf(p1));
                
            }catch(NumberFormatException e){
                
                JOptionPane.showMessageDialog(this, "Quantity is empty, or make sure it is a number! ");
                System.out.println("moda yakek");
                z1=0;
            }
   
    }
    
    // ----------------------------------load tables
    
    public void StockTable(){
        
        try {
            pst = con.prepareStatement("select *from stock");
            rs = pst.executeQuery();
            ResultSetMetaData rsm = rs.getMetaData();
            int c;
            c = rsm.getColumnCount();
            DefaultTableModel dt = (DefaultTableModel)StockTable.getModel();
            
            dt.setRowCount(0);
            
            while(rs.next()){
                
                Vector vl = new Vector();
                for (int i = 1; i < c; i++){
                    vl.add(rs.getString("item_no"));
                    vl.add(rs.getString("description"));
                    vl.add(rs.getString("quantity_available"));
                    vl.add(rs.getString("unit_price"));
                    
                }
                dt.addRow(vl);
            }
                   
        } catch (SQLException ex) {
            Logger.getLogger(GoodsListing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void TempDailyTransactionTable(){
        
        try {
            pst = con.prepareStatement("select *from temp_daily_transactions");
            rs = pst.executeQuery();
            ResultSetMetaData rsm = rs.getMetaData();
            int c;
            c = rsm.getColumnCount();
            DefaultTableModel dt = (DefaultTableModel)DailyGoodsTransactionTable.getModel();
            
            dt.setRowCount(0);
            
            while(rs.next()){
                
                Vector vl = new Vector();
                for (int i = 1; i < c; i++){
                    
                    vl.add(rs.getString("trans_no"));
                    vl.add(rs.getString("inserted_date"));
                    vl.add(rs.getString("inserted_time"));
                    vl.add(rs.getString("item_no"));
                    vl.add(rs.getString("description"));
                    vl.add(rs.getString("unit_price"));
                    vl.add(rs.getString("quantity"));
                    vl.add(rs.getString("total_price"));
                    vl.add(rs.getString("updated_date"));
                    vl.add(rs.getString("updated_time"));
                    vl.add(rs.getString("debt"));
                }
                dt.addRow(vl);
            }
                   
        } catch (SQLException ex) {
            Logger.getLogger(GoodsListing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void transLogsTable(){
            
        try {
            pst = con.prepareStatement("select *from transaction_logs");
            rs = pst.executeQuery();
            
            ResultSetMetaData rsm = rs.getMetaData();
            int c;
            c = rsm.getColumnCount();
            DefaultTableModel dt = (DefaultTableModel)TransLogtable.getModel();
            
            dt.setRowCount(0);
            
            while(rs.next()){
                
                Vector vl = new Vector();
                for (int i = 1; i < c; i++){

                        vl.add(rs.getString("log_id"));
                        vl.add(rs.getString("saved_time"));
                        vl.add(rs.getString("saved_date"));
                        vl.add(rs.getString("total_amount"));
                        vl.add(rs.getString("debt_total"));
                        vl.add(rs.getString("final_total"));
                }
                dt.addRow(vl);
            }
                   
        } catch (SQLException ex) {
            Logger.getLogger(GoodsListing.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
    /**
     * 
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        dateLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        timeLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TransLogtable = new javax.swing.JTable();
        jButton6 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        DailyGoodsTransactionTable = new javax.swing.JTable();
        logID = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        StockTable = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        SearchBox2 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        searchbox = new javax.swing.JTextField();
        logID1 = new javax.swing.JLabel();
        insertButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        quantityLabel = new javax.swing.JLabel();
        totalPriceLabel = new javax.swing.JLabel();
        totalPriceL1 = new javax.swing.JLabel();
        quantityTextField = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 255, 255));

        dateLabel.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        dateLabel.setText("DD/MM/YY");

        jLabel1.setFont(new java.awt.Font("Cambria Math", 1, 24)); // NOI18N
        jLabel1.setText("Date:");

        jLabel5.setFont(new java.awt.Font("Comic Sans MS", 1, 36)); // NOI18N
        jLabel5.setText("Prepaid Transaction Log");

        jLabel3.setFont(new java.awt.Font("Cambria Math", 1, 24)); // NOI18N
        jLabel3.setText("Time:");

        timeLabel.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N

        TransLogtable.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        TransLogtable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Log_ID", "Saved Time", "Saved Date", "Total Amount", "Total Debt", "Final Total"
            }
        ));
        TransLogtable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TransLogtableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TransLogtable);

        jButton6.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        jButton6.setText("Back");
        jButton6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        DailyGoodsTransactionTable.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        DailyGoodsTransactionTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Trans No", "Insert Date", "Insert Time", "Item No", "Description", "Unit Price", "Quantity", "Total Price", "update Date", "update Time", "Debt"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        DailyGoodsTransactionTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                DailyGoodsTransactionTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(DailyGoodsTransactionTable);
        if (DailyGoodsTransactionTable.getColumnModel().getColumnCount() > 0) {
            DailyGoodsTransactionTable.getColumnModel().getColumn(0).setMinWidth(85);
            DailyGoodsTransactionTable.getColumnModel().getColumn(0).setPreferredWidth(85);
            DailyGoodsTransactionTable.getColumnModel().getColumn(0).setMaxWidth(85);
            DailyGoodsTransactionTable.getColumnModel().getColumn(1).setMinWidth(95);
            DailyGoodsTransactionTable.getColumnModel().getColumn(1).setPreferredWidth(95);
            DailyGoodsTransactionTable.getColumnModel().getColumn(1).setMaxWidth(95);
            DailyGoodsTransactionTable.getColumnModel().getColumn(2).setMinWidth(105);
            DailyGoodsTransactionTable.getColumnModel().getColumn(2).setPreferredWidth(105);
            DailyGoodsTransactionTable.getColumnModel().getColumn(2).setMaxWidth(105);
            DailyGoodsTransactionTable.getColumnModel().getColumn(3).setMinWidth(67);
            DailyGoodsTransactionTable.getColumnModel().getColumn(3).setPreferredWidth(67);
            DailyGoodsTransactionTable.getColumnModel().getColumn(3).setMaxWidth(67);
            DailyGoodsTransactionTable.getColumnModel().getColumn(5).setMinWidth(65);
            DailyGoodsTransactionTable.getColumnModel().getColumn(5).setPreferredWidth(65);
            DailyGoodsTransactionTable.getColumnModel().getColumn(5).setMaxWidth(65);
            DailyGoodsTransactionTable.getColumnModel().getColumn(6).setMinWidth(60);
            DailyGoodsTransactionTable.getColumnModel().getColumn(6).setPreferredWidth(60);
            DailyGoodsTransactionTable.getColumnModel().getColumn(6).setMaxWidth(60);
            DailyGoodsTransactionTable.getColumnModel().getColumn(7).setMinWidth(75);
            DailyGoodsTransactionTable.getColumnModel().getColumn(7).setPreferredWidth(75);
            DailyGoodsTransactionTable.getColumnModel().getColumn(7).setMaxWidth(75);
            DailyGoodsTransactionTable.getColumnModel().getColumn(8).setMinWidth(95);
            DailyGoodsTransactionTable.getColumnModel().getColumn(8).setPreferredWidth(95);
            DailyGoodsTransactionTable.getColumnModel().getColumn(8).setMaxWidth(95);
            DailyGoodsTransactionTable.getColumnModel().getColumn(9).setMinWidth(105);
            DailyGoodsTransactionTable.getColumnModel().getColumn(9).setPreferredWidth(105);
            DailyGoodsTransactionTable.getColumnModel().getColumn(9).setMaxWidth(105);
            DailyGoodsTransactionTable.getColumnModel().getColumn(10).setMinWidth(50);
            DailyGoodsTransactionTable.getColumnModel().getColumn(10).setPreferredWidth(50);
            DailyGoodsTransactionTable.getColumnModel().getColumn(10).setMaxWidth(50);
        }

        logID.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        logID.setText("----------");

        StockTable.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        StockTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item No", "Description", "Quantity Available", "Unit  Price"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        StockTable.getTableHeader().setReorderingAllowed(false);
        StockTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                StockTableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(StockTable);

        jLabel7.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        jLabel7.setText("Search:");

        SearchBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchBox2ActionPerformed(evt);
            }
        });
        SearchBox2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                SearchBox2KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(SearchBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel7))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(SearchBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 12, Short.MAX_VALUE))
        );

        jLabel6.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        jLabel6.setText("Search:");

        searchbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchboxActionPerformed(evt);
            }
        });
        searchbox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchboxKeyReleased(evt);
            }
        });

        logID1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        logID1.setText("Log ID:");

        insertButton.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        insertButton.setText("Insert");
        insertButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertButtonActionPerformed(evt);
            }
        });

        deleteButton.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        deleteButton.setText("Delete");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        quantityLabel.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        quantityLabel.setText("Quantity:");

        totalPriceLabel.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        totalPriceLabel.setText("Total Price: ");
        totalPriceLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                totalPriceLabelMouseEntered(evt);
            }
        });

        totalPriceL1.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        totalPriceL1.setText("000000.00 ");
        totalPriceL1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                totalPriceL1MouseEntered(evt);
            }
        });

        quantityTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quantityTextFieldActionPerformed(evt);
            }
        });
        quantityTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                quantityTextFieldKeyReleased(evt);
            }
        });

        jButton7.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        jButton7.setText("Calculate");
        jButton7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(logID1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(searchbox, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(logID, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(279, 279, 279)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(142, 142, 142)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(42, 42, 42)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(timeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dateLabel)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1070, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(insertButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(deleteButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(quantityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(quantityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(totalPriceLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(totalPriceL1)))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 651, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 695, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(29, 29, 29))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(timeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateLabel)
                            .addComponent(jLabel1)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(logID, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(logID1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(searchbox, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 31, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(quantityLabel)
                            .addComponent(quantityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(totalPriceLabel)
                            .addComponent(totalPriceL1))
                        .addGap(29, 29, 29)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(insertButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(deleteButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton6)
                        .addGap(18, 18, 18))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 479, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
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

    private void searchboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchboxActionPerformed

    private void searchboxKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchboxKeyReleased
        DefaultTableModel currtableModel = (DefaultTableModel) TransLogtable.getModel();//To empty the table before search
        currtableModel.setRowCount(0); //To search for contents from original table content
        
        for (Object rows : originalTableModel) {
            Vector rowVector = (Vector) rows;
            for (Object column : rowVector) {
                if (column.toString().contains(searchbox.getText())) {
                    //content found so adding to table
                    currtableModel.addRow(rowVector);
                    break;
                }
            }

        } 
    }//GEN-LAST:event_searchboxKeyReleased

    private void TransLogtableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TransLogtableMouseClicked
        
        StockTable.setVisible(true);
        
        DefaultTableModel d1 = (DefaultTableModel)TransLogtable.getModel();
        int SelectIndex = TransLogtable.getSelectedRow();  
        
        logID.setText(d1.getValueAt(SelectIndex, 0).toString());
        logDate = d1.getValueAt(SelectIndex, 2).toString();
        
        try {
            pst = con.prepareStatement("delete from temp_daily_transactions");
            
            pst.executeUpdate();
            //JOptionPane.showMessageDialog(this, "Item Deleted Successfully! ");
          
            
        } catch (SQLException ex) {
            Logger.getLogger(GoodsListing.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        File f1 = new File("C:\\Users\\THR\\Documents\\NetBeansProjects\\HardwareSaves\\HFiles\\"+logDate+".txt");
        
        try {
            FileInputStream fstream = new FileInputStream(f1);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            
            try {
                while((strLine = br.readLine()) != null){
                    String[] tokens = strLine.split("   ");
                    transNo = tokens[0];
                    insertedDate = tokens[1];
                    insertedTime = tokens[2];
                    itemNo = tokens[3];
                    description = tokens[4];      
                    unitPrice = tokens[5];      
                    quantity = tokens[6];     
                    totalPrice = tokens[7];
                    updatedDate = tokens[8];      
                    updatedTime = tokens[9];
                    debt = tokens[10];      
                            
                    
                    try {
                        pst = con.prepareStatement("insert into temp_daily_transactions(trans_no,inserted_date,inserted_time,item_no,description,unit_price,quantity,total_price,updated_date,updated_time,debt) values(?,?,?,?,?,?,?,?,?,?,?)");

                        pst.setString(1, transNo);
                        pst.setString(2, insertedDate);
                        pst.setString(3, insertedTime);
                        pst.setString(4, itemNo); 
                        pst.setString(5, description);
                        pst.setString(6, unitPrice);
                        pst.setString(7, quantity);
                        pst.setString(8, totalPrice);
                        pst.setString(9, updatedDate);
                        pst.setString(10, updatedTime);
                        pst.setString(11, debt);
                        pst.executeUpdate();
                        //JOptionPane.showMessageDialog(this, "Item Entered Successfully! ");
                    } catch (SQLException ex) {
                         Logger.getLogger(GoodsListing.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                in.close();
                
            } catch (IOException ex) {
                Logger.getLogger(TransactionLogs.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StartPrepaidTransactionDay.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       TempDailyTransactionTable(); 
    }//GEN-LAST:event_TransLogtableMouseClicked

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        this.dispose();
        DailyTransactions ob = new DailyTransactions();
        ob.setVisible(true);
        
    }//GEN-LAST:event_jButton6ActionPerformed

    private void DailyGoodsTransactionTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_DailyGoodsTransactionTableMouseClicked
        
        deleteButton.setEnabled(true);
        insertButton.setEnabled(false);
        
        DefaultTableModel d1 = (DefaultTableModel)DailyGoodsTransactionTable.getModel();
        int SelectIndex = DailyGoodsTransactionTable.getSelectedRow();
        
        
        transactionNo3 = d1.getValueAt(SelectIndex, 0).toString();
        itemNo3 = d1.getValueAt(SelectIndex, 3).toString();
        
        System.out.println(itemNo3);
                  
        quantityTextField.setText(d1.getValueAt(SelectIndex, 6).toString());
        totalPriceL1.setText(d1.getValueAt(SelectIndex, 7).toString());
        
        prevQuantity = quantityTextField.getText();

    }//GEN-LAST:event_DailyGoodsTransactionTableMouseClicked

    private void StockTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_StockTableMouseClicked
        
        insertButton.setEnabled(true);
        deleteButton.setEnabled(false);
        
        DefaultTableModel d1 = (DefaultTableModel)StockTable.getModel();
        int SelectIndex = StockTable.getSelectedRow();  
        
        itemNo2 = d1.getValueAt(SelectIndex, 0).toString();
        description2 = d1.getValueAt(SelectIndex, 1).toString();
        quantityAvailable = d1.getValueAt(SelectIndex, 2).toString();
        unitPrice2 = d1.getValueAt(SelectIndex, 3).toString();
        
  
    }//GEN-LAST:event_StockTableMouseClicked

    private void SearchBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SearchBox2ActionPerformed

    private void SearchBox2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SearchBox2KeyReleased
        DefaultTableModel currtableModel = (DefaultTableModel) StockTable.getModel();//To empty the table before search
        currtableModel.setRowCount(0); //To search for contents from original table content

        for (Object rows : originalTableModel1) {
            Vector rowVector = (Vector) rows;
            for (Object column : rowVector) {
                if (column.toString().contains(SearchBox2.getText())) {
                    //content found so adding to table
                    currtableModel.addRow(rowVector);
                    break;
                }
            }

        }

    }//GEN-LAST:event_SearchBox2KeyReleased

    private void insertButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertButtonActionPerformed
        
        if(!dateLabel.getText().equals(logDate)){
        
            this.CalculateTotPrice();    

            if(z1!=0){
            
                double d2;
                d2 = Double.parseDouble(quantityAvailable) - Double.parseDouble(quantityTextField.getText());         

                if(d2>=0){
                        
                    try {

                        pst = con.prepareStatement("update stock set quantity_available = ? where item_no = ?");
                        pst.setString(1, Double.toString(d2));
                        pst.setString(2, itemNo2);
                        pst.executeUpdate();
                        
                    } catch (SQLException ex) {
                        Logger.getLogger(GoodsTransactions.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    StockTable();
                    autoID();
                    
                    try {
                        pst = con.prepareStatement("insert into temp_daily_transactions(trans_no,inserted_date,inserted_time,item_no,description,unit_price,quantity,total_price,updated_date,updated_time,debt) values(?,?,?,?,?,?,?,?,?,?,?)");

                        pst.setString(1, transId);
                        pst.setString(2, dateLabel.getText());
                        pst.setString(3, timeLabel.getText());
                        pst.setString(4, itemNo2); 
                        pst.setString(5, description2);
                        pst.setString(6, unitPrice2);
                        pst.setString(7, quantityTextField.getText());
                        pst.setString(8, totalPriceL1.getText());
                        pst.setString(9, dateLabel.getText());
                        pst.setString(10, timeLabel.getText());
                        pst.setString(11, "No");

                        pst.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Item Entered Successfully! ");
                        
                    } catch (SQLException ex) {
                     Logger.getLogger(GoodsListing.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    TempDailyTransactionTable();
                    
                    //*******************saving to file
                    File f1 = new File("C:\\Users\\THR\\Documents\\NetBeansProjects\\HardwareSaves\\HFiles\\"+logDate+".txt");
        
                    try {
                        FileWriter fw = new FileWriter(f1);
                        BufferedWriter bf = new BufferedWriter(fw);

                        for(int i=0; i<DailyGoodsTransactionTable.getRowCount(); i++){   

                            for(int j=0; j<DailyGoodsTransactionTable.getColumnCount(); j++){

                                bf.write(DailyGoodsTransactionTable.getValueAt(i, j).toString()+"   ");

                            }

                            bf.newLine();

                        }

                        bf.close();
                        fw.close();

                    } catch (IOException ex) {
                        Logger.getLogger(GoodsTransactions.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //*******************
                    
                    insertButton.setEnabled(false);

                }else{
                    JOptionPane.showMessageDialog(this, "quantity not available! ");   
                }
            
            }
        
        
        }else{ 
           JOptionPane.showMessageDialog(this, "use todays prepaid transaction table! ");
        }
            
    }//GEN-LAST:event_insertButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        
        if(!dateLabel.getText().equals(logDate)){
            
            try {
                pst = con.prepareStatement("select *from stock where item_no = ?");
                pst.setString(1, itemNo3);
                rs = pst.executeQuery();
                while(rs.next()){
                    quantityAvailable2 = rs.getString("quantity_available");
                }
            } catch (SQLException ex) {
            Logger.getLogger(GoodsTransactions.class.getName()).log(Level.SEVERE, null, ex);
            }
        
            double d1;
            d1 = Double.parseDouble(quantityAvailable2) + Double.parseDouble(prevQuantity);
        
        
            try {
                    pst = con.prepareStatement("update stock set quantity_available = ? where item_no = ?");
                    pst.setString(1, Double.toString(d1));
                    pst.setString(2, itemNo3);
                    pst.executeUpdate();
            } catch (SQLException ex) {
                    Logger.getLogger(GoodsTransactions.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                pst = con.prepareStatement("delete from temp_daily_transactions where trans_no = ?");
                pst.setString(1, transactionNo3);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Item Deleted Successfully! ");


            } catch (SQLException ex) {
                Logger.getLogger(GoodsListing.class.getName()).log(Level.SEVERE, null, ex);
            }

            TempDailyTransactionTable();
            StockTable();
        
            //*******saving to file
                    File f1 = new File("C:\\Users\\THR\\Documents\\NetBeansProjects\\HardwareSaves\\HFiles\\"+logDate+".txt");
        
                    try {
                        FileWriter fw = new FileWriter(f1);
                        BufferedWriter bf = new BufferedWriter(fw);

                        for(int i=0; i<DailyGoodsTransactionTable.getRowCount(); i++){   

                            for(int j=0; j<DailyGoodsTransactionTable.getColumnCount(); j++){

                                bf.write(DailyGoodsTransactionTable.getValueAt(i, j).toString()+"   ");

                            }

                            bf.newLine();

                        }

                        bf.close();
                        fw.close();

                    } catch (IOException ex) {
                        Logger.getLogger(GoodsTransactions.class.getName()).log(Level.SEVERE, null, ex);
                    }
            //**********************
        
            deleteButton.setEnabled(false);
        
        }else{
            JOptionPane.showMessageDialog(this, "use todays prepaid transaction table! ");
        }
        
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void totalPriceLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_totalPriceLabelMouseEntered
        
    }//GEN-LAST:event_totalPriceLabelMouseEntered

    private void totalPriceL1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_totalPriceL1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_totalPriceL1MouseEntered

    private void quantityTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quantityTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_quantityTextFieldActionPerformed

    private void quantityTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_quantityTextFieldKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_quantityTextFieldKeyReleased

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        this.CalculateTotPrice();
    }//GEN-LAST:event_jButton7ActionPerformed

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
            java.util.logging.Logger.getLogger(TransactionLogs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TransactionLogs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TransactionLogs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TransactionLogs.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TransactionLogs().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable DailyGoodsTransactionTable;
    private javax.swing.JTextField SearchBox2;
    private javax.swing.JTable StockTable;
    private javax.swing.JTable TransLogtable;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton insertButton;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel logID;
    private javax.swing.JLabel logID1;
    private javax.swing.JLabel quantityLabel;
    private javax.swing.JTextField quantityTextField;
    private javax.swing.JTextField searchbox;
    private javax.swing.JLabel timeLabel;
    private javax.swing.JLabel totalPriceL1;
    private javax.swing.JLabel totalPriceLabel;
    // End of variables declaration//GEN-END:variables
}

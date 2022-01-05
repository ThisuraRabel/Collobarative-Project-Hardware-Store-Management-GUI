/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hardwarestore;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author thisa
 */
public class GoodsTransactions extends javax.swing.JFrame {

    private Connection con = null;
    private PreparedStatement pst;
    private ResultSet rs; 
    
    private String itemNo;
    private String description;
    private String quantityAvailable;
    private String prevQuantity;
    
    private Vector originalTableModel;
    
    private String billHeader = "";
    private int commenceIndicator = 0;
    private double billTotal = 0.0;
    private String fileContent = "";
    private String totalPart = "";
    private String itemNameBill;
    
    private String d1;
    private int z1 = 1;
    
    private double quantityField = 0.0;
    private String totalPrice;
   
    private String logID;
    
    private double sum = 0.0;
    private double debtTotal = 0.0;
    private double finalTotal = 0.0;
    
    private String yesNo;
    
    public GoodsTransactions() {    //default constructor
        initComponents();
        
        //date and time
        HelperClass.Date(dateLabel);      //getting date from helper class date function and assigning to date label
        //DDate = dateLabel.getText();
        HelperClass.ShowTime(timeLabel);  //getting time from helper class time function and assigning to time label
        
        //connecting to DB
        con = HelperClass.connect();  //calling helper class connect function to connect to DB
        
        this.StockTable();  // display values from stock table
        this.DailyGoodsTransactionTable();   // display values from daily_goods_transaction table
        
        this.autoID();    // generates a new ID for transaction
        
        //statup fields ajustments
        clearFields();
        totalPriceTextField.setEditable(false);
        unitPriceTextField.setEditable(false);
        billPogressIndicator.setText("");
        
        
        addAsIncompleteButton.setEnabled(false);  
        
        originalTableModel = (Vector) ((DefaultTableModel) StockTable.getModel()).getDataVector().clone();//backup of original values to check
        
        //notice box
        jEditorPane1.setContentType("text/html");    
        jEditorPane1.setText("<p>Notice : At the end of the day you should save the transaction log.</p>"); 
        
        
   
    }

   
    public void autoID(){
        Statement s;
        try {
            s = con.createStatement();
            rs = s.executeQuery("select MAX(trans_no) from daily_goods_transactions");
            rs.next();
            rs.getString("MAX(trans_no)");
            
            if (rs.getString("MAX(trans_no)") == null){
                TransactionNoLabel.setText("TR000001");    
                
            }
            else{
                long id = Long.parseLong(rs.getString("MAX(trans_no)").substring(2,rs.getString("MAX(trans_no)").length()));
                id++;
                TransactionNoLabel.setText("TR" + String.format("%06d", id));
            }
        } catch (SQLException ex) {
            Logger.getLogger(GoodsListing.class.getName()).log(Level.SEVERE, null, ex);
        }
     
    }
    
    
   public void logAutoID(){
        Statement s;
        try {
            s = con.createStatement();
            rs = s.executeQuery("select MAX(log_id) from transaction_logs");
            rs.next();
            rs.getString("MAX(log_id)");
            
            if (rs.getString("MAX(log_id)") == null){
                logID = "LG000001";    
                
            }
            else{
                long id = Long.parseLong(rs.getString("MAX(log_id)").substring(2,rs.getString("MAX(log_id)").length()));
                id++;
                logID = "LG" + String.format("%06d", id);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GoodsListing.class.getName()).log(Level.SEVERE, null, ex);
        }
     
    }
    
    public void CalculateTotPrice(){
    
        
            
            try{
                
                quantityField = Double.parseDouble(quantityTextField.getText());
                
                double p1 = quantityField * Double.parseDouble(unitPriceTextField.getText());
                
                d1 = String.valueOf(p1);
            
                totalPriceTextField.setText(d1);
                
            }catch(NumberFormatException e){
                
                JOptionPane.showMessageDialog(this, "Quantity is empty, or make sure it is a number! ");
                System.out.println("moda yakek");
                z1=0;
            }
          
        
    
    
    }
    
    public void CalculateDayTotal(){
    
      sum = 0.0;
      
      for(int i=0; i<DailyGoodsTransactionTable.getRowCount(); i++){   
            
        sum = sum + Double.parseDouble(DailyGoodsTransactionTable.getValueAt(i, 7).toString());
                
      }     
            
    //System.out.println("total is:" + sum);      
        
    
    }
    
    public void CalculateDayDebt(){
    
        debtTotal = 0.0;
        
        try {
            pst = con.prepareStatement("select date,debt_amount from incomplete");
            rs = pst.executeQuery();
            
            while(rs.next()){
                
                String date = rs.getString("date");
                
                if(date.equals(dateLabel.getText())){
                    
                    debtTotal = debtTotal + Double.parseDouble(rs.getString("debt_amount"));
                
                }
                    
            }
          
            System.out.println("debtTotal: "+ debtTotal);
                   
        } catch (SQLException ex) {
            Logger.getLogger(GoodsListing.class.getName()).log(Level.SEVERE, null, ex);
        }
     
    
    }
    
    public void CalculateFinalTotal(){
    
        CalculateDayTotal();
        CalculateDayDebt();
        finalTotal = sum - debtTotal;
        
    }
      
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
      
      public void DailyGoodsTransactionTable(){
        
        try {
            pst = con.prepareStatement("select *from daily_goods_transactions");
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
      
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel10 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        StockTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        dateLabel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        timeLabel = new javax.swing.JLabel();
        backButton = new javax.swing.JButton();
        insertButton = new javax.swing.JButton();
        clearBillDataButton = new javax.swing.JButton();
        updateButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        DailyGoodsTransactionTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        SearchBox = new javax.swing.JTextField();
        TransactionNoLabel = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        totalPriceLabel = new javax.swing.JLabel();
        unitPriceLabel = new javax.swing.JLabel();
        totalPriceTextField = new javax.swing.JTextField();
        quantityTextField = new javax.swing.JTextField();
        addToBillButton = new javax.swing.JButton();
        billPreviewButton = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        itemNoLabel = new javax.swing.JLabel();
        billPogressIndicator = new javax.swing.JLabel();
        quantityLabel = new javax.swing.JLabel();
        unitPriceTextField = new javax.swing.JTextField();
        backToMenuButton = new javax.swing.JButton();
        SaveLogButton = new javax.swing.JButton();
        ViewTodaysTotalButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jScrollPane4 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jPanel3 = new javax.swing.JPanel();
        dayTotalLabel = new javax.swing.JLabel();
        dayTotalLabel1 = new javax.swing.JLabel();
        dayTotalLabel2 = new javax.swing.JLabel();
        dayTotalLabel3 = new javax.swing.JLabel();
        finalTotalLabel = new javax.swing.JLabel();
        debtLabel = new javax.swing.JLabel();
        addAsIncompleteButton = new javax.swing.JButton();

        jLabel10.setFont(new java.awt.Font("Comic Sans MS", 1, 69)); // NOI18N
        jLabel10.setText("    Daily Transactions");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 255, 255));

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
        jScrollPane1.setViewportView(StockTable);
        if (StockTable.getColumnModel().getColumnCount() > 0) {
            StockTable.getColumnModel().getColumn(0).setMinWidth(100);
            StockTable.getColumnModel().getColumn(0).setPreferredWidth(100);
            StockTable.getColumnModel().getColumn(0).setMaxWidth(100);
            StockTable.getColumnModel().getColumn(2).setMinWidth(120);
            StockTable.getColumnModel().getColumn(2).setPreferredWidth(120);
            StockTable.getColumnModel().getColumn(2).setMaxWidth(120);
            StockTable.getColumnModel().getColumn(3).setMinWidth(100);
            StockTable.getColumnModel().getColumn(3).setPreferredWidth(100);
            StockTable.getColumnModel().getColumn(3).setMaxWidth(100);
        }

        jLabel1.setFont(new java.awt.Font("Cambria Math", 1, 24)); // NOI18N
        jLabel1.setText("Date:");

        dateLabel.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        dateLabel.setText("DD/MM/YY");

        jLabel3.setFont(new java.awt.Font("Cambria Math", 1, 24)); // NOI18N
        jLabel3.setText("Time:");

        timeLabel.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        timeLabel.setText("Min/Sec");

        backButton.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        backButton.setText("Back");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        insertButton.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        insertButton.setText("Insert");
        insertButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertButtonActionPerformed(evt);
            }
        });

        clearBillDataButton.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        clearBillDataButton.setText("Clear Bill Data");
        clearBillDataButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearBillDataButtonActionPerformed(evt);
            }
        });

        updateButton.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        updateButton.setText("Update");
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateButtonActionPerformed(evt);
            }
        });

        deleteButton.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        deleteButton.setText("Delete");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
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

        jLabel5.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        jLabel5.setText("Search:");

        SearchBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchBoxActionPerformed(evt);
            }
        });
        SearchBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                SearchBoxKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(SearchBox, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel5))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(SearchBox, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 12, Short.MAX_VALUE))
        );

        TransactionNoLabel.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        TransactionNoLabel.setText("trans no");

        jLabel7.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        jLabel7.setText("Item No:");

        totalPriceLabel.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        totalPriceLabel.setText("Total Price: ");
        totalPriceLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                totalPriceLabelMouseEntered(evt);
            }
        });

        unitPriceLabel.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        unitPriceLabel.setText("Unit Price:");

        totalPriceTextField.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        totalPriceTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                totalPriceTextFieldMouseEntered(evt);
            }
        });
        totalPriceTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalPriceTextFieldActionPerformed(evt);
            }
        });

        quantityTextField.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        quantityTextField.setMaximumSize(new java.awt.Dimension(146, 36));
        quantityTextField.setMinimumSize(new java.awt.Dimension(146, 36));
        quantityTextField.setPreferredSize(new java.awt.Dimension(146, 36));
        quantityTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                quantityTextFieldMouseEntered(evt);
            }
        });
        quantityTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quantityTextFieldActionPerformed(evt);
            }
        });

        addToBillButton.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        addToBillButton.setText("Add To Bill");
        addToBillButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addToBillButtonActionPerformed(evt);
            }
        });

        billPreviewButton.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        billPreviewButton.setText("Bill Preview");
        billPreviewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                billPreviewButtonActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Comic Sans MS", 1, 36)); // NOI18N
        jLabel11.setText(" Daily Goods Transactions");

        jLabel12.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        jLabel12.setText("Trans. No: ");

        itemNoLabel.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N

        billPogressIndicator.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        billPogressIndicator.setForeground(new java.awt.Color(0, 0, 255));
        billPogressIndicator.setText("  On going bill progress..");

        quantityLabel.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        quantityLabel.setText("Quantity:");

        unitPriceTextField.setFont(new java.awt.Font("Cambria Math", 0, 24)); // NOI18N
        unitPriceTextField.setMaximumSize(new java.awt.Dimension(146, 36));
        unitPriceTextField.setMinimumSize(new java.awt.Dimension(146, 36));
        unitPriceTextField.setPreferredSize(new java.awt.Dimension(146, 36));
        unitPriceTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                unitPriceTextFieldMouseEntered(evt);
            }
        });
        unitPriceTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unitPriceTextFieldActionPerformed(evt);
            }
        });

        backToMenuButton.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        backToMenuButton.setText("Back To Menu");
        backToMenuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backToMenuButtonActionPerformed(evt);
            }
        });

        SaveLogButton.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        SaveLogButton.setText("Save Transaction log");
        SaveLogButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveLogButtonActionPerformed(evt);
            }
        });

        ViewTodaysTotalButton.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        ViewTodaysTotalButton.setText("View Todays Total >>");
        ViewTodaysTotalButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewTodaysTotalButtonActionPerformed(evt);
            }
        });

        jScrollPane4.setViewportView(jEditorPane1);

        dayTotalLabel.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        dayTotalLabel.setText("000000.00");

        dayTotalLabel1.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        dayTotalLabel1.setText("Today Total:");

        dayTotalLabel2.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        dayTotalLabel2.setText("Final Total:");

        dayTotalLabel3.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        dayTotalLabel3.setText("Debt Total:");

        finalTotalLabel.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        finalTotalLabel.setText("000000.00");

        debtLabel.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        debtLabel.setText("000000.00");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dayTotalLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                    .addComponent(dayTotalLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dayTotalLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dayTotalLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(debtLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(finalTotalLabel, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dayTotalLabel1)
                    .addComponent(dayTotalLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dayTotalLabel3)
                    .addComponent(debtLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dayTotalLabel2)
                    .addComponent(finalTotalLabel))
                .addGap(23, 23, 23))
        );

        addAsIncompleteButton.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        addAsIncompleteButton.setText("Add as Incomplete");
        addAsIncompleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAsIncompleteButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(billPogressIndicator, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(clearBillDataButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(backToMenuButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(SaveLogButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(addAsIncompleteButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(addToBillButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(billPreviewButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(19, 19, 19)
                                .addComponent(jScrollPane2))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(12, 12, 12)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel7)
                                                .addGap(18, 18, 18)
                                                .addComponent(itemNoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel12)
                                                .addGap(18, 18, 18)
                                                .addComponent(TransactionNoLabel))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(quantityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(quantityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(unitPriceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(unitPriceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(totalPriceLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(totalPriceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(ViewTodaysTotalButton, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(29, 29, 29)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(updateButton, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                                                .addComponent(insertButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addGap(18, 18, 18)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(deleteButton, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                                                .addComponent(backButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane1)))
                        .addGap(27, 27, 27))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel1)
                        .addGap(28, 28, 28)
                        .addComponent(dateLabel)
                        .addGap(179, 179, 179)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 494, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 233, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(timeLabel)
                        .addGap(120, 120, 120))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(dateLabel)
                    .addComponent(timeLabel)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(24, 24, 24)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(insertButton)
                                    .addComponent(deleteButton))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(updateButton)
                                    .addComponent(backButton))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel12)
                                    .addComponent(TransactionNoLabel))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel7)
                                    .addComponent(itemNoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(28, 28, 28)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(quantityLabel)
                                        .addGap(27, 27, 27))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(quantityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(23, 23, 23)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(unitPriceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(unitPriceLabel))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(totalPriceLabel)
                                    .addComponent(totalPriceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jSeparator3, javax.swing.GroupLayout.DEFAULT_SIZE, 9, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ViewTodaysTotalButton)
                                .addGap(19, 19, 19)))
                        .addGap(18, 18, 18))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(SaveLogButton)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(addAsIncompleteButton)
                        .addGap(4, 4, 4)
                        .addComponent(billPogressIndicator, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(addToBillButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(billPreviewButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(clearBillDataButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(backToMenuButton))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 516, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42))
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

    private void SearchBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SearchBoxActionPerformed

    private void totalPriceTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalPriceTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalPriceTextFieldActionPerformed

    private void quantityTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quantityTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_quantityTextFieldActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
       clearFields();
       autoID();
       DailyGoodsTransactionTable();
       StockTable();
       addAsIncompleteButton.setEnabled(false);
    }//GEN-LAST:event_backButtonActionPerformed

    private void billPreviewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_billPreviewButtonActionPerformed

        totalPart = "                                                                                         " +
                 "____________" + "\n" + "    Total Price" + "                                                                         " +
                 Double.toString(billTotal) + "\n" + "                                                                                         =========" +
                 "\n" + "\n";
         
            try {
                FileWriter writer = new FileWriter("C:\\Users\\THR\\Documents\\NetBeansProjects\\HardwareSaves\\TEMP\\temp.txt");
                writer.write(" " + "Date:" + dateLabel.getText() + "                                         " + "Time:" + timeLabel.getText() + "\n");
                writer.write(billHeader);
                writer.write(fileContent); 
                writer.write(totalPart);
                writer.close(); 

            } catch (IOException ex) {
                    Logger.getLogger(GoodsTransactions.class.getName()).log(Level.SEVERE, null, ex);
            }   
        // date, time, billHeader, fileContent, totalPart
        BillPrewiew ob = new BillPrewiew(dateLabel.getText(), timeLabel.getText(), billHeader, fileContent, totalPart);
        ob.setVisible(true);
        
    }//GEN-LAST:event_billPreviewButtonActionPerformed

    private void StockTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_StockTableMouseClicked

       
        DefaultTableModel d1 = (DefaultTableModel)StockTable.getModel();
        int SelectIndex = StockTable.getSelectedRow();  
        
        itemNo = d1.getValueAt(SelectIndex, 0).toString();
        description = d1.getValueAt(SelectIndex, 1).toString();
        quantityAvailable = d1.getValueAt(SelectIndex, 2).toString();
        unitPriceTextField.setText(d1.getValueAt(SelectIndex, 3).toString());
        itemNoLabel.setText(itemNo);
         
        
        insertButton.setEnabled(true);
        backButton.setEnabled(true);
        
    }//GEN-LAST:event_StockTableMouseClicked

    private void insertButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertButtonActionPerformed
    
            this.CalculateTotPrice();    

            if(z1!=0){
            
            double d2;
            d2 = Double.parseDouble(quantityAvailable) - quantityField;         
            
            if(d2>=0){
                
                try {
                
                pst = con.prepareStatement("update stock set quantity_available = ? where item_no = ?");
                pst.setString(1, Double.toString(d2));
                pst.setString(2, itemNo);
                pst.executeUpdate();
                
                } catch (SQLException ex) {
                    Logger.getLogger(GoodsTransactions.class.getName()).log(Level.SEVERE, null, ex);
                }
            
            
                try {
                pst = con.prepareStatement("insert into daily_goods_transactions(trans_no,inserted_date,inserted_time,item_no,description,unit_price,quantity,total_price,updated_date,updated_time,debt) values(?,?,?,?,?,?,?,?,?,?,?)");
                
                pst.setString(1, TransactionNoLabel.getText());
                pst.setString(2, dateLabel.getText());
                pst.setString(3, timeLabel.getText());
                pst.setString(4, itemNo); //--------------------------------------------------
                pst.setString(5, description);
                pst.setString(6, unitPriceTextField.getText());
                pst.setString(7, quantityTextField.getText());
                pst.setString(8, totalPriceTextField.getText());
                pst.setString(9, dateLabel.getText());
                pst.setString(10, timeLabel.getText());
                pst.setString(11, "No");
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Item Entered Successfully! ");
                } catch (SQLException ex) {
                 Logger.getLogger(GoodsListing.class.getName()).log(Level.SEVERE, null, ex);
                }
       
                DailyGoodsTransactionTable();
                autoID();
         
            }else{
                JOptionPane.showMessageDialog(this, "quantity not available! ");   
            }
            
            }
            
            clearFields();
            StockTable();
            
    }//GEN-LAST:event_insertButtonActionPerformed

    private void SearchBoxKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SearchBoxKeyReleased
        DefaultTableModel currtableModel = (DefaultTableModel) StockTable.getModel();//To empty the table before search
        currtableModel.setRowCount(0); //To search for contents from original table content
        
        for (Object rows : originalTableModel) {
            Vector rowVector = (Vector) rows;
            for (Object column : rowVector) {
                if (column.toString().contains(SearchBox.getText())) {
                    //content found so adding to table
                    currtableModel.addRow(rowVector);
                    break;
                }
            }

        }
       
    }//GEN-LAST:event_SearchBoxKeyReleased

    private void DailyGoodsTransactionTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_DailyGoodsTransactionTableMouseClicked

    
        DefaultTableModel d1 = (DefaultTableModel)DailyGoodsTransactionTable.getModel();
        int SelectIndex = DailyGoodsTransactionTable.getSelectedRow();
        
        
        TransactionNoLabel.setText(d1.getValueAt(SelectIndex, 0).toString());
        itemNoLabel.setText(d1.getValueAt(SelectIndex, 3).toString());
        itemNameBill = d1.getValueAt(SelectIndex, 4).toString();           //*
        unitPriceTextField.setText(d1.getValueAt(SelectIndex, 5).toString());
        quantityTextField.setText(d1.getValueAt(SelectIndex, 6).toString());
        totalPrice = d1.getValueAt(SelectIndex, 7).toString();
        
        prevQuantity = quantityTextField.getText();
         
        addAsIncompleteButton.setEnabled(true);
        deleteButton.setEnabled(true);
        updateButton.setEnabled(true);
        backButton.setEnabled(true);
        insertButton.setEnabled(false);
        StockTable.setVisible(false);
        addToBillButton.setEnabled(true);
   
    }//GEN-LAST:event_DailyGoodsTransactionTableMouseClicked

    private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateButtonActionPerformed
       
        this.CalculateTotPrice();
        
        try {
                pst = con.prepareStatement("select *from stock where item_no = ?");
                pst.setString(1, itemNoLabel.getText());
                rs = pst.executeQuery();
                while(rs.next()){
                    quantityAvailable = rs.getString("quantity_available");
                }
        } catch (SQLException ex) {
            Logger.getLogger(GoodsTransactions.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        if(z1!=0){
        
            double d2;
            d2 = Double.parseDouble(quantityAvailable) + Double.parseDouble(prevQuantity) - quantityField;
            
            if(d2>0){
            
                try{
                    pst = con.prepareStatement("update stock set quantity_available = ? where item_no = ?");
                    pst.setString(1, Double.toString(d2));
                    pst.setString(2, itemNoLabel.getText());
                    pst.executeUpdate();
                } catch (SQLException ex) {
                    Logger.getLogger(GoodsTransactions.class.getName()).log(Level.SEVERE, null, ex);
                }
       
           
            
                try{
                    pst = con.prepareStatement("update daily_goods_transactions set updated_date = ?, updated_time = ?, item_no = ?, quantity = ?, total_price = ? where trans_no = ?");
                
                    pst.setString(1,dateLabel.getText());
                    pst.setString(2,timeLabel.getText());
                    pst.setString(3,itemNoLabel.getText());
                    pst.setString(4,quantityTextField.getText());
                    pst.setString(5,totalPriceTextField.getText());
                    pst.setString(6,TransactionNoLabel.getText());
                
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Item Updated Successfully! ");
                
                } catch (SQLException ex) {
                    Logger.getLogger(GoodsTransactions.class.getName()).log(Level.SEVERE, null, ex);
                } 
                
            }else{
                
                JOptionPane.showMessageDialog(this, "Quantity not available! ");
            
            }
                
        
        }
        
       
        clearFields(); 
        DailyGoodsTransactionTable();
        StockTable();
        autoID();  
        addAsIncompleteButton.setEnabled(false);
             
         
    }//GEN-LAST:event_updateButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        
        try {
                pst = con.prepareStatement("select *from stock where item_no = ?");
                pst.setString(1, itemNoLabel.getText());
                rs = pst.executeQuery();
                while(rs.next()){
                    quantityAvailable = rs.getString("quantity_available");
                }
            } catch (SQLException ex) {
            Logger.getLogger(GoodsTransactions.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        double d1;
        d1 = Double.parseDouble(quantityAvailable) + Double.parseDouble(prevQuantity);
        
        try {
                pst = con.prepareStatement("update stock set quantity_available = ? where item_no = ?");
                pst.setString(1, Double.toString(d1));
                pst.setString(2, itemNoLabel.getText());
                pst.executeUpdate();
        } catch (SQLException ex) {
                Logger.getLogger(GoodsTransactions.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            pst = con.prepareStatement("delete from daily_goods_transactions where trans_no = ?");
            pst.setString(1, TransactionNoLabel.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Item Deleted Successfully! ");
          
            
        } catch (SQLException ex) {
            Logger.getLogger(GoodsListing.class.getName()).log(Level.SEVERE, null, ex);
        }
        clearFields();       
        DailyGoodsTransactionTable();
        autoID();
        StockTable();
        addAsIncompleteButton.setEnabled(false);
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void clearBillDataButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearBillDataButtonActionPerformed
       
        commenceIndicator = 0;
        billHeader = "";
        billTotal = 0.0;
        fileContent = "";
        totalPart = "";
        billPogressIndicator.setText("");
        
        JOptionPane.showMessageDialog(this, "Bill Cleared! ");
        
      
    }//GEN-LAST:event_clearBillDataButtonActionPerformed

    private void addToBillButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addToBillButtonActionPerformed
        this.CalculateTotPrice();
        
        billPogressIndicator.setText("      On going bill progress..");
        commenceIndicator++;
        
        billTotal = billTotal + Double.parseDouble(totalPriceTextField.getText());
                
        if (commenceIndicator == 1){
            File file = new File("C:\\Users\\THR\\Documents\\NetBeansProjects\\HardwareSaves\\BillHeader.txt");
            Scanner scan;
            try {

                scan = new Scanner(file);
                while (scan.hasNextLine()){
                     billHeader = billHeader.concat(scan.nextLine() + "\n");
                }
                   
            } catch (FileNotFoundException ex) {
                Logger.getLogger(GoodsTransactions.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            fileContent = fileContent.concat( " " + itemNameBill + "         " + quantityTextField.getText() + 
                                             "       " + totalPriceTextField.getText() + "\n");
        }
        else{
            
            fileContent = fileContent.concat( " " + itemNameBill + "         " + quantityTextField.getText() + 
                                             "       " + totalPriceTextField.getText() + "\n");
        
        }
 
    }//GEN-LAST:event_addToBillButtonActionPerformed

    private void quantityTextFieldMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_quantityTextFieldMouseEntered
        
    }//GEN-LAST:event_quantityTextFieldMouseEntered

    private void totalPriceTextFieldMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_totalPriceTextFieldMouseEntered
        this.CalculateTotPrice();
    }//GEN-LAST:event_totalPriceTextFieldMouseEntered

    private void totalPriceLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_totalPriceLabelMouseEntered
        this.CalculateTotPrice();
    }//GEN-LAST:event_totalPriceLabelMouseEntered

    private void unitPriceTextFieldMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_unitPriceTextFieldMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_unitPriceTextFieldMouseEntered

    private void unitPriceTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unitPriceTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_unitPriceTextFieldActionPerformed

    private void backToMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backToMenuButtonActionPerformed
        this.dispose();
        DailyTransactions ob = new DailyTransactions();
        ob.setVisible(true);
    }//GEN-LAST:event_backToMenuButtonActionPerformed

    private void SaveLogButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveLogButtonActionPerformed
        
        //----------------------------adding entry to transaction log table
        CalculateFinalTotal();
        
        logAutoID();
        
        try {
                pst = con.prepareStatement("insert into transaction_logs(log_id, saved_time, saved_date, total_amount, debt_total, final_total) values(?,?,?,?,?,?)");
                pst.setString(1, logID); 
                pst.setString(2, timeLabel.getText());
                pst.setString(3, dateLabel.getText());
                pst.setString(4, String.valueOf(sum));
                pst.setString(5, String.valueOf(debtTotal));
                pst.setString(6, String.valueOf(finalTotal));
              
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this,"Log Saved Successfully! "); 
            } catch (Exception e) {
                System.out.println(e);
               
                try{
                    pst = con.prepareStatement("update transaction_logs set saved_time = ?, total_amount = ?, debt_total = ?, final_total = ? where saved_date = ?");
                    pst.setString(1, timeLabel.getText());
                    pst.setString(2, String.valueOf(sum));
                    pst.setString(3, String.valueOf(debtTotal));
                    pst.setString(4, String.valueOf(finalTotal));
                    pst.setString(5, dateLabel.getText());
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(this,"Log has Saved before! Saved Successfully!");
                } catch (SQLException ex) {
                    Logger.getLogger(GoodsTransactions.class.getName()).log(Level.SEVERE, null, ex);
                }
               
            }
        
        
        //----------------------------Todays transaction details saving to a file
        
        File f1 = new File("C:\\Users\\THR\\Documents\\NetBeansProjects\\HardwareSaves\\HFiles\\"+dateLabel.getText()+".txt");
        
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
        
    }//GEN-LAST:event_SaveLogButtonActionPerformed

    private void ViewTodaysTotalButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewTodaysTotalButtonActionPerformed
        
        CalculateFinalTotal();
        
        dayTotalLabel.setText(String.valueOf(sum));
        debtLabel.setText(String.valueOf(debtTotal));
        finalTotalLabel.setText(String.valueOf(finalTotal));
        
    }//GEN-LAST:event_ViewTodaysTotalButtonActionPerformed

    private void addAsIncompleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAsIncompleteButtonActionPerformed
                
        try {
                pst = con.prepareStatement("select *from daily_goods_transactions where trans_no = ?");
                pst.setString(1, TransactionNoLabel.getText());
                rs = pst.executeQuery();
                while(rs.next()){
                    yesNo = rs.getString("debt");
                }
            } catch (SQLException ex) {
            Logger.getLogger(GoodsTransactions.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        if(yesNo.equals("No")){
            JOptionPane.showMessageDialog(this, "insert Name and debt Amount in next page! ");
            this.dispose();
            StartPrepaidTransactionDay ob = new StartPrepaidTransactionDay(TransactionNoLabel.getText(),totalPrice);
            ob.setVisible(true);
        }else{
        
            JOptionPane.showMessageDialog(this, "This transaction is already added as incomplete! ");
        }
        
    addAsIncompleteButton.setEnabled(false);    
    }//GEN-LAST:event_addAsIncompleteButtonActionPerformed

    private void clearFields(){
        
        itemNoLabel.setText("");
        quantityTextField.setText("");
        totalPriceTextField.setText("");
        unitPriceTextField.setText("");
        SearchBox.setText("");           //***
        SearchBox.requestFocus();        //***
        StockTable.setVisible(true);
        addToBillButton.setEnabled(false);
        insertButton.setEnabled(false);
        backButton.setEnabled(false);
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        
       
        
    }
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
            java.util.logging.Logger.getLogger(GoodsTransactions.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GoodsTransactions.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GoodsTransactions.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GoodsTransactions.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GoodsTransactions().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable DailyGoodsTransactionTable;
    private javax.swing.JButton SaveLogButton;
    private javax.swing.JTextField SearchBox;
    private javax.swing.JTable StockTable;
    private javax.swing.JLabel TransactionNoLabel;
    private javax.swing.JButton ViewTodaysTotalButton;
    private javax.swing.JButton addAsIncompleteButton;
    private javax.swing.JButton addToBillButton;
    private javax.swing.JButton backButton;
    private javax.swing.JButton backToMenuButton;
    private javax.swing.JLabel billPogressIndicator;
    private javax.swing.JButton billPreviewButton;
    private javax.swing.JButton clearBillDataButton;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JLabel dayTotalLabel;
    private javax.swing.JLabel dayTotalLabel1;
    private javax.swing.JLabel dayTotalLabel2;
    private javax.swing.JLabel dayTotalLabel3;
    private javax.swing.JLabel debtLabel;
    private javax.swing.JButton deleteButton;
    private javax.swing.JLabel finalTotalLabel;
    private javax.swing.JButton insertButton;
    private javax.swing.JLabel itemNoLabel;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel quantityLabel;
    private javax.swing.JTextField quantityTextField;
    private javax.swing.JLabel timeLabel;
    private javax.swing.JLabel totalPriceLabel;
    private javax.swing.JTextField totalPriceTextField;
    private javax.swing.JLabel unitPriceLabel;
    private javax.swing.JTextField unitPriceTextField;
    private javax.swing.JButton updateButton;
    // End of variables declaration//GEN-END:variables
}

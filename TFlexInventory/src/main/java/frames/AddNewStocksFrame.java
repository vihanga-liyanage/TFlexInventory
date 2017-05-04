/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package main.java.frames;

import main.java.classes.Item;
import main.java.classes.ResultArray;
import main.java.classes.Transaction;
import main.java.classes.TransactionItem;
import main.java.classes.Validation;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import static main.java.frames.AdminPannel.setUIFont;

/**
 *
 * @author Vihanga Liyanage
 */
public class AddNewStocksFrame extends javax.swing.JFrame {

    Item item = new Item();
    AdminPannel adminPannel;
    /**
     * Creates new form AddNewStocksFrame
     */
    public AddNewStocksFrame() {
        //Setting icon
        ImageIcon img = new ImageIcon("src\\img\\icon-1.png");
        this.setIconImage(img.getImage());
        
        try {
            setUIFont(new javax.swing.plaf.FontUIResource("Segoe UI", Font.PLAIN, 14));
        } catch (Exception e) {
            Logger.getLogger(AdminPannel.class.getName()).log(Level.SEVERE, null, e);
        }

        //Changing look and feel
        //for metal - javax.swing.plaf.metal.MetalLookAndFeel
        //for windows - com.sun.java.swing.plaf.windows.WindowsLookAndFeel
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | 
                UnsupportedLookAndFeelException ex) {
            Logger.getLogger(AdminPannel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        initComponents();
        
        //Changing blendlist table header font
        inventoryTable.getTableHeader().setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        
        //enabling sorting
        inventoryTable.setAutoCreateRowSorter(true);

        //set focus to blendCombo
        itemsCombo.requestFocus();
        
        //Setting date
        DateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy");
        Date today = new Date();
        dateLabel.setText(formatter.format(today));
        
        //Initialize blendCombo
        item.initItemCombo(itemsCombo);
        itemsCombo.setSelectedIndex(-1);
        
        //Validation on qty, when key released
        itemStockQtyTxt.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String stock = itemStockQtyTxt.getText();
                if (stock.length() > 0) {
                    if (!(new Validation().isInt(stock))) {
                        JOptionPane.showMessageDialog(itemStockQtyTxt, "New stock quantity must be a valid number.", 
                                "Invalid Item Stock Quantity", 2);
                        itemStockQtyTxt.setText(stock.substring(0, stock.length() - 1));
                    } else if (Integer.parseInt(stock) < 0) {
                        JOptionPane.showMessageDialog(itemStockQtyTxt, "New stock quantity cannot be less than 0.", 
                                "Invalid Item Stock Quantity", 2);
                        itemStockQtyTxt.setText(stock.substring(0, stock.length() - 1));
                    }
                }
            }

            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
            }
        });
        
        //setting focus to qty txt when item selected
        itemsCombo.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    itemStockQtyTxt.requestFocus();
                }
            }
        });
        
        //Prompt confirmation on window close
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirmed = JOptionPane.showConfirmDialog(null, 
                    "Are you sure you want to close this window?\nAll data you entered will be lost.", 
                    "Confirm window close",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirmed == JOptionPane.YES_OPTION) {
                    close();
                }
            }
        });
        
        //enable delete button on row select
        deleteBtn.setEnabled(false);
        final ListSelectionModel mod = inventoryTable.getSelectionModel();
        mod.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent lse) {
                if (!mod.isSelectionEmpty()) {
                    deleteBtn.setEnabled(true);
                }
            }
        });
        
    }

    private void close(){
        this.dispose();
    }
    
    //formatting numbers to add commas
    private String formatNum(String num){
        String decimal=num, point = null;
        if(num.contains(".")){
            String[] temp = num.split("\\.");
            decimal = temp[0];
            point = temp[1];
        }
        int i = decimal.length();
        while (i > 3) {
            String part1 = decimal.substring(0, i-3);
            String part2 = decimal.substring(i-3);
            decimal = part1 + "," + part2;
            i-=3;
        }
        if (point != null){
            decimal += "." + point;
        }
        return decimal;
    }
    private String formatNum(int num){
        return formatNum(String.valueOf(num));
    }
    private String formatNum(float num){
        return formatNum(Float.toString(num));
    }
    
    //overiding Integer.parseInt() to accept nums with commas
    private int parseInt(String num){
        try{
            return Integer.parseInt(num);
        } catch (NumberFormatException e){
            if (num.matches("[[0-9]{1,2}+,]*")) {
                num = num.replace(",", "");
                return Integer.parseInt(num);
            }
        }
        return 0;
    }
    
    //overiding Float.parseFloat() to accept nums with commas
    private float parseFloat(String num){
        try{
            return Float.parseFloat(num);
        } catch (NumberFormatException e){
            if (num.matches("[[0-9]{1,2}+,]*.[0-9]*")) {
                num = num.replace(",", "");
                return Float.parseFloat(num);
            }
        }
        return 0;
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
        HeaderLbl = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        itemsCombo = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        itemStockQtyTxt = new javax.swing.JTextField();
        itemStockCombo = new javax.swing.JComboBox();
        itemAddBtn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        dateLabel = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        inventoryTable = new javax.swing.JTable();
        confirmBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 255, 102), 3));

        HeaderLbl.setFont(new java.awt.Font("Segoe UI Semibold", 0, 16)); // NOI18N
        HeaderLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        HeaderLbl.setText("Add New Stocks");
        HeaderLbl.setToolTipText("");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel15.setText("Start typing item names to begin.");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setText("Item");

        itemsCombo.setEditable(true);
        itemsCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        itemsCombo.setSelectedIndex(-1);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("New Stock Quantity");

        itemStockQtyTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemStockQtyTxtActionPerformed(evt);
            }
        });

        itemStockCombo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        itemStockCombo.setMaximumRowCount(2);
        itemStockCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "g", "kg", " " }));
        itemStockCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemStockComboActionPerformed(evt);
            }
        });

        itemAddBtn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        itemAddBtn.setText("Add");
        itemAddBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemAddBtnActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        dateLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        dateLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        dateLabel.setText("Feb 18, 2016");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Date :");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(dateLabel)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5))
                .addContainerGap())
        );

        jScrollPane6.setBorder(null);

        inventoryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item Code", "Item Name", "Category", "Available Stock (g)", "New Stock (g)", "Final Stock (g)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        inventoryTable.setRowHeight(24);
        jScrollPane6.setViewportView(inventoryTable);

        confirmBtn.setText("Confirm");
        confirmBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmBtnActionPerformed(evt);
            }
        });

        deleteBtn.setText("Delete");
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
                    .addComponent(HeaderLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(deleteBtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(confirmBtn))
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(itemsCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(itemStockQtyTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(itemStockCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(itemAddBtn))))
                        .addComponent(jLabel15))
                    .addContainerGap(332, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(HeaderLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(confirmBtn)
                    .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(45, 45, 45)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, 0)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 0, 0)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(itemAddBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(itemStockQtyTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(itemStockCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(itemsCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(372, Short.MAX_VALUE)))
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

    private void itemStockQtyTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemStockQtyTxtActionPerformed
        itemAddBtn.requestFocus();
    }//GEN-LAST:event_itemStockQtyTxtActionPerformed

    private void itemStockComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemStockComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_itemStockComboActionPerformed

    private void itemAddBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemAddBtnActionPerformed
        //Validation checks
        if (itemsCombo.getSelectedIndex() == -1){
            JOptionPane.showMessageDialog(itemsCombo, "Please select an item to add.", "Empty Item Selection", 2);
            itemsCombo.requestFocus();
            itemsCombo.setSelectedIndex(-1);
        } else if (itemStockQtyTxt.getText().equals("")) {
            JOptionPane.showMessageDialog(itemStockQtyTxt, "Please enter item stock quantity to add.", 
                    "Empty Item Stock Quantity", 2);
            itemStockQtyTxt.requestFocus();
        } else if (parseInt(itemStockQtyTxt.getText()) <= 0) {
            JOptionPane.showMessageDialog(itemStockQtyTxt, "Item stock quantity cannot be 0!", 
                    "Invalid item stock Quantity", 2);
            itemStockQtyTxt.requestFocus();

            //If all are good...
        } else {
            String itemName = (String) itemsCombo.getSelectedItem();
            itemName = itemName.replace("'", "\\'");
            int itemStockQty = parseInt(itemStockQtyTxt.getText());
            if (itemStockCombo.getSelectedItem().equals("kg")) {
                itemStockQty *= 1000;
            }
            boolean isNew = true;
            
            //Search if the item is already added
            int rowCount = inventoryTable.getRowCount();
            for (int i = 0; i < rowCount; i++) {
                /*
                0 - item code
                1 - item name
                2 - category
                3 - available stock
                4 - new stock
                5 - final stock
                */
                if (itemName.equals(inventoryTable.getValueAt(i, 1))) {
                    //calculating qty new stock qty
                    itemStockQty += parseInt(inventoryTable.getValueAt(i, 4).toString());
                    inventoryTable.setValueAt(formatNum(itemStockQty), i, 4);
                    
                    int availableStock = parseInt(inventoryTable.getValueAt(i, 3).toString());
                    inventoryTable.setValueAt(formatNum(availableStock + itemStockQty), i, 5);
                    isNew = false;
                    break;
                }
            }
            if (isNew) {
                //get item record from DB -> code, category, currentstock
                ResultArray itemRecord = item.getItemByItemName(itemName);
                
                Vector newRow = new Vector();
                itemRecord.next();
                newRow.addElement(itemRecord.getString(0));
                newRow.addElement(itemName);
                newRow.addElement(itemRecord.getString(1));
                newRow.addElement(formatNum(itemRecord.getString(2)));
                newRow.addElement(itemStockQty);
                newRow.addElement(itemStockQty + parseFloat(itemRecord.getString(2)));
                DefaultTableModel model = (DefaultTableModel) inventoryTable.getModel();
                model.addRow(newRow);
            }

            //Reset combo and qty txt
            itemStockQtyTxt.setText("");
            itemsCombo.setSelectedIndex(-1);
            itemsCombo.requestFocus();
        }
    }//GEN-LAST:event_itemAddBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        int dialogResult = JOptionPane.showConfirmDialog(inventoryTable, 
                "Please confirm record deletion", "Confirm Deletion", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (dialogResult == JOptionPane.YES_OPTION){
            DefaultTableModel model = (DefaultTableModel)inventoryTable.getModel();
            model.removeRow(inventoryTable.getSelectedRow());
            deleteBtn.setEnabled(false);
        }
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void confirmBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmBtnActionPerformed
        int dialogResult = JOptionPane.showConfirmDialog(inventoryTable, 
                "Please confirm new stocks.", 
                "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (dialogResult == JOptionPane.YES_OPTION){
            //Add transaction record
            Transaction t = new Transaction();
            t.addNewTransaction();
            
            //get transaction id
            int tid = t.getLastTransactionID();
            if (tid == -1)
                tid = 1;
            
            TransactionItem ti = new TransactionItem();
            
            int rowCount = inventoryTable.getRowCount();
            for (int i = 0; i < rowCount; i++) {
                //update inventory
                item.updateItemStock(inventoryTable.getValueAt(i, 0).toString(), inventoryTable.getValueAt(i, 5).toString());
                
                //Add transactionitem record
                ti.addNewTransactionItem(tid, inventoryTable.getValueAt(i, 0).toString(), 0, inventoryTable.getValueAt(i, 4).toString());
            }
            
            //re-populate InventoryTable in admin pannel
            adminPannel.populateInventoryTable();
            this.close();
        }
    }//GEN-LAST:event_confirmBtnActionPerformed

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
            java.util.logging.Logger.getLogger(AddNewStocksFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddNewStocksFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddNewStocksFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddNewStocksFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddNewStocksFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel HeaderLbl;
    private javax.swing.JButton confirmBtn;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JTable inventoryTable;
    private javax.swing.JButton itemAddBtn;
    private javax.swing.JComboBox itemStockCombo;
    private javax.swing.JTextField itemStockQtyTxt;
    private javax.swing.JComboBox itemsCombo;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane6;
    // End of variables declaration//GEN-END:variables
}

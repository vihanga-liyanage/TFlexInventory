package main.java.frames;

import java.awt.Component;
import main.java.classes.Item;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.table.DefaultTableModel;
import main.java.classes.Category;
import main.java.classes.ResultArray;
import main.java.classes.Transaction;

/**
 *
 * @author Vihanga Liyanage
 */
public class AdminPannel extends javax.swing.JFrame {

    Item item = new Item();
    Transaction transaction = new Transaction();
    Category category = new Category();
    
    /**
     * Creates new form AdminPannel
     */
    public AdminPannel() {
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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(AdminPannel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        initComponents();
        
        //Changing table headers to bold
        inventoryTable.getTableHeader().setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        newStockArrivalsTbl.getTableHeader().setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        deliveryNotesTbl.getTableHeader().setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        editItemTbl.getTableHeader().setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));

        //enabling sorting for tables
        inventoryTable.setAutoCreateRowSorter(true);
        
        //init edit item combo
        item.initItemCombo(editItemCombo);
        editItemCombo.setSelectedIndex(-1);
               
        startClock();
        
        //Populate main inventory table
        populateInventoryTable();
        
        //Populate transaction tables
        populateTransactionTables();
        
        //populating edit item, table
        populateEditItemTable();
        
        //Populate item edit category combo 
        populateItemEditCombo();
      
        //Keep the window fullscreen
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        
        //setting focus to edit button when item selected in item edit
        editItemCombo.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    editItemBtn.requestFocus();
                }
            }
        });
    
        //method for combox value setting when table row select in order main table
        final ListSelectionModel editItemTblSelectionModel = editItemTbl.getSelectionModel();
        editItemTblSelectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent lsevt) {
                if (!editItemTblSelectionModel.isSelectionEmpty()) {
                    int row = editItemTblSelectionModel.getMinSelectionIndex();
                    DefaultTableModel model = (DefaultTableModel)editItemTbl.getModel();
                    Vector dataRow = (Vector) model.getDataVector().get(row);
                    initItemEditPanel(dataRow);
                    
                    editItemCombo.setSelectedItem(editItemTbl.getValueAt(row, 1));
                }
            }
        });
    }

    public void setGreetings(String greeting) {
        greetingsLbl.setText("Welcome Mr. Dushyantha");
    }
    
    //Seperated method to call from outside the constructor
    public void populateInventoryTable() {
        item.populateItemTable((DefaultTableModel) inventoryTable.getModel());
    }
    
    public void populateTransactionTables() {
        transaction.populateItemTable((DefaultTableModel) newStockArrivalsTbl.getModel(), 
                (DefaultTableModel) deliveryNotesTbl.getModel());
    }
    
    //Seperated method to call from outside the constructor
    public void populateEditItemTable() {
        item.populateEditItemTable((DefaultTableModel) editItemTbl.getModel());
    }
    
    //Inner classes to store key value pairs in a combo box -> Category combo in item edit
    class ItemRenderer extends BasicComboBoxRenderer {
        public Component getListCellRendererComponent(
            JList list, Object value, int index,
            boolean isSelected, boolean cellHasFocus)
        {
            super.getListCellRendererComponent(list, value, index,
                isSelected, cellHasFocus);
            if (value != null) {
                ComboItem item = (ComboItem)value;
                setText( item.getName().toUpperCase() );
            }
            if (index == -1) {
                ComboItem item = (ComboItem)value;
                setText( "" + item.getId() );
            }
            return this;
        }
    }
    class ComboItem {
        private int id;
        private String name;
        public ComboItem(int id, String description)
        {
            this.id = id;
            this.name = description;
        }
        public int getId() { return id; }
        public String getName() { return name; }
        public String toString() { return name; }
    }
    
    
    //Populate item edit category combo 
    private void populateItemEditCombo() {
        ResultArray res = category.getAllCategoryNames();
        itemCategoryCombo.addItem(new ComboItem(0, "- Select Category -"));
        while (res.next()) {
            itemCategoryCombo.addItem(new ComboItem(Integer.parseInt(res.getString(0)),res.getString(1)));
        }
    }
    
    //Intialize item details in edit panel when item is selected
    private void initItemEditPanel(Vector itemDetails) {
        itemCodeTxt.setText((String) itemDetails.get(0));
        itemNameTxt.setText((String) itemDetails.get(1));
        itemNameTxt.setEditable(true);
        //Setting category
        String category = (String) itemDetails.get(2);
        ComboBoxModel model = itemCategoryCombo.getModel();
        int size = model.getSize();
        for (int i=0; i<size; i++){
            if (((ComboItem)model.getElementAt(i)).getName().equals(category)) {
                itemCategoryCombo.setSelectedIndex(i);
                break;
            }
        }
        
        itemUnitPriceTxt.setText((String) itemDetails.get(3));
        itemUnitPriceTxt.setEditable(true);
        itemEditSaveBtn.setEnabled(true);
        itemEditCancelBtn.setEnabled(true);

    }
        
    //Setting default font
    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }
    
    //Function run the clock in UI
    private void startClock() {
        Timer timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLabel.setText(DateFormat.getDateTimeInstance().format(new Date()));
            }
        });
        timer.setRepeats(true);
        timer.setCoalesce(true);
        timer.setInitialDelay(0);
        timer.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        logoLabel = new javax.swing.JLabel();
        profileBtn = new javax.swing.JButton();
        logoutBtn = new javax.swing.JButton();
        greetingsLbl = new javax.swing.JLabel();
        timeLabel = new javax.swing.JLabel();
        HeaderLbl = new javax.swing.JLabel();
        addNewDeliveryNoteBtn = new javax.swing.JButton();
        addNewStocksBtn = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane6 = new javax.swing.JScrollPane();
        inventoryTable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        newStockArrivalsPanel = new javax.swing.JScrollPane();
        newStockArrivalsTbl = new javax.swing.JTable();
        deliveryNotesPanel = new javax.swing.JScrollPane();
        deliveryNotesTbl = new javax.swing.JTable();
        transactionDetailsPanel = new javax.swing.JScrollPane();
        transactionsDetailsTbl = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        systemTab = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        itemEditPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        editItemTbl = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        editItemCombo = new javax.swing.JComboBox();
        editItemBtn = new javax.swing.JButton();
        addItemBtn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        itemNameTxt = new javax.swing.JTextField();
        itemCategoryCombo = new javax.swing.JComboBox();
        itemUnitPriceTxt = new javax.swing.JTextField();
        itemEditSaveBtn = new javax.swing.JButton();
        itemEditCancelBtn = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        itemCodeTxt = new javax.swing.JTextField();
        usersPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        logoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/main/resources/img/logo-new (Custom)-old.png"))); // NOI18N

        profileBtn.setText("Profile");

        logoutBtn.setText("Log Out");

        greetingsLbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        greetingsLbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        timeLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        timeLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        HeaderLbl.setFont(new java.awt.Font("Segoe UI Semibold", 0, 16)); // NOI18N
        HeaderLbl.setText("Welcome to TFlex Inventory System");
        HeaderLbl.setToolTipText("");

        addNewDeliveryNoteBtn.setText("Add New Delivery Note");
        addNewDeliveryNoteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewDeliveryNoteBtnActionPerformed(evt);
            }
        });

        addNewStocksBtn.setText("Add New Stocks");
        addNewStocksBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewStocksBtnActionPerformed(evt);
            }
        });

        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane1MouseClicked(evt);
            }
        });

        jScrollPane6.setBorder(null);

        inventoryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item Code", "Item Name", "Cateogry", "Current Stock (g)", "Unit Price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        inventoryTable.setRowHeight(24);
        jScrollPane6.setViewportView(inventoryTable);
        if (inventoryTable.getColumnModel().getColumnCount() > 0) {
            inventoryTable.getColumnModel().getColumn(1).setResizable(false);
        }

        jTabbedPane1.addTab("    Current Stocks    ", jScrollPane6);

        newStockArrivalsPanel.setPreferredSize(new java.awt.Dimension(500, 402));

        newStockArrivalsTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Date", "User"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        newStockArrivalsTbl.setRowHeight(24);
        newStockArrivalsPanel.setViewportView(newStockArrivalsTbl);
        if (newStockArrivalsTbl.getColumnModel().getColumnCount() > 0) {
            newStockArrivalsTbl.getColumnModel().getColumn(0).setPreferredWidth(40);
        }

        deliveryNotesPanel.setPreferredSize(new java.awt.Dimension(500, 402));

        deliveryNotesTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Date", "User"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        deliveryNotesTbl.setRowHeight(24);
        deliveryNotesPanel.setViewportView(deliveryNotesTbl);
        if (deliveryNotesTbl.getColumnModel().getColumnCount() > 0) {
            deliveryNotesTbl.getColumnModel().getColumn(0).setPreferredWidth(40);
        }

        transactionsDetailsTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item Code", "Item Name", "Quantity"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        transactionsDetailsTbl.setRowHeight(24);
        transactionDetailsPanel.setViewportView(transactionsDetailsTbl);
        if (transactionsDetailsTbl.getColumnModel().getColumnCount() > 0) {
            transactionsDetailsTbl.getColumnModel().getColumn(0).setPreferredWidth(40);
        }

        jLabel1.setText("New Stock Arrivals");

        jLabel2.setText("Delivery Notes");

        jLabel3.setText("Transactions Details");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(newStockArrivalsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE)
                    .addComponent(deliveryNotesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(transactionDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 581, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(newStockArrivalsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deliveryNotesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addComponent(transactionDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("    Transactions    ", jPanel1);

        editItemTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item Code", "Item Name", "Category", "Unit Price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        editItemTbl.setRowHeight(24);
        jScrollPane1.setViewportView(editItemTbl);
        if (editItemTbl.getColumnModel().getColumnCount() > 0) {
            editItemTbl.getColumnModel().getColumn(0).setPreferredWidth(30);
        }

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Start typing item name to update");

        editItemCombo.setEditable(true);

        editItemBtn.setText("Edit");
        editItemBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editItemBtnActionPerformed(evt);
            }
        });

        addItemBtn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        addItemBtn.setText("Add New Item");
        addItemBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addItemBtnActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Select an item to edit");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Item Name");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Category");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("Unit Price");

        itemNameTxt.setEditable(false);

        itemUnitPriceTxt.setEditable(false);

        itemEditSaveBtn.setText("Save");
        itemEditSaveBtn.setEnabled(false);

        itemEditCancelBtn.setText("Cancel");
        itemEditCancelBtn.setEnabled(false);
        itemEditCancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemEditCancelBtnActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Item Code");

        itemCodeTxt.setEditable(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(itemEditCancelBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(itemEditSaveBtn))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10))
                        .addGap(53, 53, 53)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(itemNameTxt)
                            .addComponent(itemCategoryCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(itemUnitPriceTxt)
                            .addComponent(itemCodeTxt))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addGap(37, 37, 37)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(itemCodeTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(itemNameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(itemCategoryCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(itemUnitPriceTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(itemEditSaveBtn)
                    .addComponent(itemEditCancelBtn))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout itemEditPanelLayout = new javax.swing.GroupLayout(itemEditPanel);
        itemEditPanel.setLayout(itemEditPanelLayout);
        itemEditPanelLayout.setHorizontalGroup(
            itemEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(itemEditPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(itemEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(itemEditPanelLayout.createSequentialGroup()
                        .addGroup(itemEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addGroup(itemEditPanelLayout.createSequentialGroup()
                                .addComponent(editItemCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(editItemBtn)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 598, Short.MAX_VALUE)
                        .addComponent(addItemBtn))
                    .addGroup(itemEditPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        itemEditPanelLayout.setVerticalGroup(
            itemEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(itemEditPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(itemEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(itemEditPanelLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(itemEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(editItemCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(editItemBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(addItemBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(itemEditPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jTabbedPane2.addTab("    Items    ", itemEditPanel);

        javax.swing.GroupLayout usersPanelLayout = new javax.swing.GroupLayout(usersPanel);
        usersPanel.setLayout(usersPanelLayout);
        usersPanelLayout.setHorizontalGroup(
            usersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1108, Short.MAX_VALUE)
        );
        usersPanelLayout.setVerticalGroup(
            usersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 469, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("    Users    ", usersPanel);

        javax.swing.GroupLayout systemTabLayout = new javax.swing.GroupLayout(systemTab);
        systemTab.setLayout(systemTabLayout);
        systemTabLayout.setHorizontalGroup(
            systemTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(systemTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2)
                .addContainerGap())
        );
        systemTabLayout.setVerticalGroup(
            systemTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(systemTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2)
                .addContainerGap())
        );

        jTabbedPane1.addTab("    System Settings    ", systemTab);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(logoLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(greetingsLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(profileBtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(logoutBtn))
                            .addComponent(timeLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(HeaderLbl)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(addNewStocksBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addNewDeliveryNoteBtn)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(timeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(logoutBtn)
                                .addComponent(profileBtn))
                            .addComponent(greetingsLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(logoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(HeaderLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addNewDeliveryNoteBtn)
                    .addComponent(addNewStocksBtn))
                .addGap(10, 10, 10)
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addNewDeliveryNoteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewDeliveryNoteBtnActionPerformed
        AddNewDeliveryNoteFrame frame = new AddNewDeliveryNoteFrame();
        frame.adminPannel = this;
        frame.setVisible(true);
    }//GEN-LAST:event_addNewDeliveryNoteBtnActionPerformed

    private void addNewStocksBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewStocksBtnActionPerformed
        AddNewStocksFrame frame = new AddNewStocksFrame();
        frame.adminPannel = this;
        frame.setVisible(true);
    }//GEN-LAST:event_addNewStocksBtnActionPerformed

    private void editItemBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editItemBtnActionPerformed
        String itemName = (String) editItemCombo.getSelectedItem();
        if (itemName == null) {
            JOptionPane.showMessageDialog(this, "Please select an item to edit.", "Empty Item Selection", 2);
        } else {
            DefaultTableModel model = (DefaultTableModel) editItemTbl.getModel();
            for (Object row: model.getDataVector()) {
                Vector rowVector = (Vector) row;
                if (rowVector.get(1).equals(itemName)) {
                    initItemEditPanel(rowVector);
                    break;
                }
            }
        }
        editItemTbl.clearSelection();
    }//GEN-LAST:event_editItemBtnActionPerformed

    private void addItemBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addItemBtnActionPerformed
        //        AddIngredient addItem = new AddIngredient();
        //        addItem.setAdminPannel(this);
        //        addItem.setVisible(true);
        //        addItem.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }//GEN-LAST:event_addItemBtnActionPerformed

    private void jTabbedPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseClicked
        editItemCombo.requestFocus();
    }//GEN-LAST:event_jTabbedPane1MouseClicked

    private void itemEditCancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemEditCancelBtnActionPerformed
        int confirmed = JOptionPane.showConfirmDialog(null, 
                    "Any data you entered will be lost.", 
                    "Confirm Cancel",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirmed == JOptionPane.YES_OPTION) {
            itemCodeTxt.setText("");
            itemNameTxt.setText("");
            itemNameTxt.setEditable(false);
            itemCategoryCombo.setSelectedIndex(0);
            itemUnitPriceTxt.setText("");
            itemUnitPriceTxt.setEditable(false);
            itemEditSaveBtn.setEnabled(false);
            itemEditCancelBtn.setEnabled(false);
            editItemCombo.setSelectedItem(null);
            editItemTbl.clearSelection();
            editItemCombo.requestFocus();
        }
    }//GEN-LAST:event_itemEditCancelBtnActionPerformed

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
            java.util.logging.Logger.getLogger(AdminPannel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminPannel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminPannel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminPannel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminPannel().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel HeaderLbl;
    private javax.swing.JButton addItemBtn;
    private javax.swing.JButton addNewDeliveryNoteBtn;
    private javax.swing.JButton addNewStocksBtn;
    private javax.swing.JScrollPane deliveryNotesPanel;
    private javax.swing.JTable deliveryNotesTbl;
    private javax.swing.JButton editItemBtn;
    public javax.swing.JComboBox editItemCombo;
    private javax.swing.JTable editItemTbl;
    private javax.swing.JLabel greetingsLbl;
    private javax.swing.JTable inventoryTable;
    private javax.swing.JComboBox itemCategoryCombo;
    private javax.swing.JTextField itemCodeTxt;
    private javax.swing.JButton itemEditCancelBtn;
    private javax.swing.JPanel itemEditPanel;
    private javax.swing.JButton itemEditSaveBtn;
    private javax.swing.JTextField itemNameTxt;
    private javax.swing.JTextField itemUnitPriceTxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JLabel logoLabel;
    private javax.swing.JButton logoutBtn;
    private javax.swing.JScrollPane newStockArrivalsPanel;
    private javax.swing.JTable newStockArrivalsTbl;
    private javax.swing.JButton profileBtn;
    private javax.swing.JPanel systemTab;
    private javax.swing.JLabel timeLabel;
    private javax.swing.JScrollPane transactionDetailsPanel;
    private javax.swing.JTable transactionsDetailsTbl;
    private javax.swing.JPanel usersPanel;
    // End of variables declaration//GEN-END:variables
}

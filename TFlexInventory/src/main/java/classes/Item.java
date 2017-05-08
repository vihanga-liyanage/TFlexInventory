package main.java.classes;

import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Vihanga Liyanage
 */
public class Item {
    
    DBConnection dbConn = DBConnection.getInstance();
    
    //returns a table model with data for main inventory table
    public void populateItemTable(DefaultTableModel model) {
        ResultArray res = null;
        String query = "SELECT code, itemname, cname, currentstock, price FROM category, item where item.category = category.cid";
        res = dbConn.getResultArray(query);
        model.setRowCount(0);
        while (res.next()) {
            //DefaultTableModel model = (DefaultTableModel) adminPannel.settingsIngredientTable.getModel();
            model.addRow(new Object[]{res.getString(0), res.getString(1), res.getString(2), res.getString(3), res.getString(4)});
        }
    }
    
    //Load items for the auto complete in item combo box
    public void initItemCombo(JComboBox blendsCombo) {
        AutoSuggest autoSuggest = new AutoSuggest();
        String query = "SELECT itemname FROM item ORDER BY itemname";
        ResultArray res = dbConn.getResultArray(query);
        autoSuggest.setAutoSuggest(blendsCombo, res);
    }
    
    //Return item record by itemname
    public ResultArray getItemByItemName(String itemName) {
        itemName = itemName.replace("'", "''");
        String query = "SELECT i.code, c.cname, i.currentstock FROM item i, category c WHERE i.itemname = '" + itemName + "' AND  i.category = c.cid";
        return dbConn.getResultArray(query);
    }
    
    public int updateItemStock(String code, String newStock) {
        String query = "UPDATE item SET currentstock = '" + newStock + "' WHERE code = '" + code + "' ";
        int ret = dbConn.updateResult(query);
        return ret;
    }
}


package classes;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Vihanga Liyanage
 */
public class Item {
    
    DBConnection dbConn = DBConnection.getInstance();
    
    //returns data for main inventory table
    public void viewAllItems(DefaultTableModel model) {
        ResultArray res = null;
        String query = "SELECT cname, code, itemname, currentstock, price FROM category, item where item.category = category.cid";
        res = dbConn.getResultArray(query);
        model.setRowCount(0);
        while (res.next()) {
            //DefaultTableModel model = (DefaultTableModel) adminPannel.settingsIngredientTable.getModel();
            model.addRow(new Object[]{res.getString(0), res.getString(1), res.getString(2), res.getString(3), res.getString(4)});
        }
    }
}

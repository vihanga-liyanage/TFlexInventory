package main.java.classes;

import javax.swing.table.DefaultTableModel;
import static main.java.classes.User.LoggedUserID;

/**
 *
 * @author Vihanga Liyanage
 */
public class Transaction {
    
    DBConnection dbConn = DBConnection.getInstance();
    
    //Add new transaction
    public int addNewTransaction(int isout) {
        String query = "INSERT INTO 'transaction' ('isout', 'user') VALUES ('" + isout + "', '" + LoggedUserID + "') ";
        return dbConn.updateResult(query);
    }
    
    //Return last id of transaction table
    public int getLastTransactionID(){
        String query = "SELECT tid FROM 'transaction' ORDER BY tid DESC LIMIT 1";
        ResultArray rs = dbConn.getResultArray(query);
        while (rs.next()){
           return Integer.parseInt(rs.getString(0));
        }
        return -1;
    }
    
    //return table models with data for transaction tables
    public void populateItemTable(DefaultTableModel newArrivalsModel, DefaultTableModel deliveryNotesModel) {
        ResultArray res = null;
        String query = "SELECT tid, date, firstname, lastname, isout FROM 'transaction', "
                + "'user' WHERE 'transaction'.user=user.uid";
        res = dbConn.getResultArray(query);
        newArrivalsModel.setRowCount(0);
        deliveryNotesModel.setRowCount(0);
        while (res.next()) {
            if (res.getString(4).equals("0")) {
                newArrivalsModel.addRow(new Object[]{res.getString(0), res.getString(1), res.getString(2) + " " 
                        + res.getString(3)});
            } else {
                deliveryNotesModel.addRow(new Object[]{res.getString(0), res.getString(1), res.getString(2) + " " 
                        + res.getString(3)});
            }
        }
    }
}

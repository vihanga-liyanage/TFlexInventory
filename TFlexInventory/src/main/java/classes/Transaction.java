package main.java.classes;

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
}

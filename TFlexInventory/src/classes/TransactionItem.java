package classes;

/**
 *
 * @author Vihanga Liyanage
 */
public class TransactionItem {
    
    DBConnection dbConn = DBConnection.getInstance();
    
    //Add new transactionitem
    public int addNewTransaction(int tid, String code, int isout, String qty) {
        String query = "INSERT INTO 'transactionitem' VALUES ('" + tid + "', '" + code + "', '" + isout + "', '" + qty + "') ";
        return dbConn.updateResult(query);
    }
    
}

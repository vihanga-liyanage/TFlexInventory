package main.java.classes;

import java.sql.SQLException;
import java.util.logging.Level;

/**
 *
 * @author Vihanga Liyanage
 */
public class Category {
    
    DBConnection dbConn = DBConnection.getInstance();
    
    //Return all category names
    public ResultArray getAllCategoryNames() {
        String query = "SELECT cid, cname FROM category";
        return dbConn.getResultArray(query);
    }
    
}

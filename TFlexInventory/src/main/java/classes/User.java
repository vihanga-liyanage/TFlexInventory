package main.java.classes;

/**
 *
 * @author Vihanga Liyanage
 */

public class User {

    public static int LoggedUserID;

    DBConnection dbConn = DBConnection.getInstance();

    //Check username and password for login
    public int checkLogin(String userName, String password) {
        String query = "SELECT username,level FROM user where password = '" + password + "' and username = ('" + userName + "')";
        ResultArray rs = dbConn.getResultArray(query);
        while(rs.next()){
            if (rs.getString(1).equals("0")) {
                return 1;
            } else if (rs.getString(1).equals("1")) {
                return 2;
            } else {
                return 3;
            }   
        }
        return 4;
    }
    
    //Return first name of the user when username given
    public String getUserFirstName(String username){
        String query = "SELECT firstname FROM user WHERE username='" + username + "'";
        ResultArray rs = dbConn.getResultArray(query);
        String name = "";
        while (rs.next()){
            name = rs.getString(0);
        }
        return name;
    }
    
    //Return uid of the user when username given
    public int getUserID(String username){
        String query = "SELECT uid FROM user WHERE username='" + username + "'";
        ResultArray rs = dbConn.getResultArray(query);
        String uid = "";
        while (rs.next()){
            uid = rs.getString(0);
        }
        return Integer.parseInt(uid);
    }
}

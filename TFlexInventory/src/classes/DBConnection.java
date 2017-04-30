package classes;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.swing.JOptionPane;

public class DBConnection {

    private static DBConnection dbconn = null;
    
    private Connection connection = null;
    public PreparedStatement pstStatement;
    public ResultSet resultSet;
    public Statement statement;
    public static Logger logger;
    
    //Global properties
    public static String TFlexFolderPath;
    
    private DBConnection() {
        if (connection == null){
            setConnection();
        }
        
        loadProperties();
        
        logger = Logger.getLogger("MyLog");
        FileHandler fh;  

        try {  
            // This block configure the logger with handler and formatter
            String logFilePath = TFlexFolderPath + "logs\\dblog.txt";
            
            //creat parent folders and log file if not exists
            File logFile = new File(logFilePath);
            logFile.getParentFile().mkdirs();
            logFile.createNewFile();
            
            fh = new FileHandler(logFilePath, true);  
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();  
            fh.setFormatter(formatter);  

            // the following statement is used to log any messages  
            logger.log(Level.INFO, "======================== Application started ========================");  

        } catch (SecurityException | IOException e) {  
            e.printStackTrace();  
        }
    }

    public static synchronized DBConnection getInstance(){
        if (dbconn == null) {
            dbconn = new DBConnection();
        }
        
        return dbconn;
    }
    private void setConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:TFlexInventory.sqlite");
        } catch (ClassNotFoundException e) {
            logger.log(Level.WARNING, e.getMessage());
            JOptionPane.showMessageDialog(null, "There were some issues with the database. Please contact developers.\n\nError code : DBConnection 26", "Error", 0);      
            System.exit(0);
        } catch (SQLException ex) {
            logger.log(Level.WARNING, ex.getMessage());
            JOptionPane.showMessageDialog(null, "There were some issues with the database. Please contact developers.\n\nError code : DBConnection 29", "Error", 0);
            System.exit(0);
        }
    }

    public ResultArray getResultArray(String query) {
        setConnection();
        ResultArray res = null;
        try {
            pstStatement = connection.prepareStatement(query);
            resultSet = pstStatement.executeQuery();
            res = new ResultArray(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.log(Level.WARNING, e.toString());
            JOptionPane.showMessageDialog(null, "There were some issues with the database. Please contact developers.\n\nError code : DBConnection 56", "Error", 0);
            System.exit(0);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    logger.log(Level.WARNING, e.getMessage());
                    JOptionPane.showMessageDialog(null, "There were some issues with the database. Please contact developers.\n\nError code : DBConnection 63", "Error", 0);
                    System.exit(0);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.log(Level.WARNING, e.getMessage());
                    JOptionPane.showMessageDialog(null, "There were some issues with the database. Please contact developers.\n\nError code : DBConnection 71", "Error", 0);
                    System.exit(0);
                }
            }
        }
        return res;
    }

    public int updateResult(String query) {
        setConnection();
        try {
            pstStatement = connection.prepareStatement(query);
            pstStatement.executeUpdate();
            return 1;
        } catch (SQLException e) {
            if ("java.sql.SQLException: [SQLITE_CONSTRAINT]  Abort due to constraint violation (UNIQUE constraint failed: ingredient.ingName)".equals(e + "")) {
                return 2;
            }
            logger.log(Level.WARNING, e.getMessage());
            JOptionPane.showMessageDialog(null, "There were some issues with the database. Please contact developers.\n\nError code : DBConnection 89", "Error", 0);
            System.exit(0);
            return 0;
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    logger.log(Level.WARNING, e.getMessage());
                    JOptionPane.showMessageDialog(null, "There were some issues with the database. Please contact developers.\n\nError code : DBConnection 97", "Error", 0);
                    System.exit(0);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.log(Level.WARNING, e.getMessage());
                    JOptionPane.showMessageDialog(null, "There were some issues with the database. Please contact developers.\n\nError code : DBConnection 105", "Error", 0);
                    System.exit(0);
                }
            }
        }
    }
    
    //Loading properties
    public final void loadProperties() {
        Properties prop = new Properties();
	InputStream input = null;

	try {
            input = new FileInputStream("src\\resources\\config.properties");
            prop.load(input);

            TFlexFolderPath = prop.getProperty("TFlexFolderPath");

	} catch (IOException ex) {
            logger.log(Level.WARNING, ex.getMessage());
            JOptionPane.showMessageDialog(null, "Could not read system properties. Please contact developers.\n\nError code : PDF 60", "Error", 0);
            System.exit(0);
	} finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    logger.log(Level.WARNING, e.getMessage());
                    JOptionPane.showMessageDialog(null, "Error reading system properties. Please contact developers.\n\nError code : PDF 68", "Error", 0);
                    System.exit(0);
                }
            }
	}
    }
}

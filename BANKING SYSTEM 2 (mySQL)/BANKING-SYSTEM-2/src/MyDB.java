
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dzhon
 */
public class MyDB {
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    String query = "";
    
    void createConnection() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/banking_system", "root", "");
            st = (Statement) con.createStatement();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MyDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void closeConnection() throws SQLException {
        rs.close();
        st.close();
        con.close();
    }
    
    public void addAccount(int accNo, int bal, String name, String address, String bDay, String contactNo){
        query = "INSERT INTO ACCOUNTS(ACCOUNT_NO, BALANCE, NAME, ADDRESS, BIRTHDAY, CONTACT_NO) VALUES("+ accNo +", "+ bal +", "+ name +", "+ address +", "+ bDay +", "+ contactNo +")";
    }

    public void updateBalance(int accNo, int newBalance){
        query = "UPDATE ACCOUNTS SET BALANCE="+ newBalance +" WHERE ACCOUNT_NO="+ accNo +";";
    }

    public void updateStatus(int accNo, String newStatus){
        query = "UPDATE ACCOUNTS SET BALANCE=0, STATUS="+ newStatus +" WHERE ACCOUNT_NO="+ accNo +";";
    }
}

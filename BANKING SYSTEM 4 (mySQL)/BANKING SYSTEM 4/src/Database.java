
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
public class Database {
    // VARIABLES FOR DATABASE CONNECTION
    Connection con = null;
    Statement stmnt = null;
    ResultSet result = null;
    PreparedStatement pstmnt = null;
    String query = "";
    
    // VARIABLES FOR DATA ACCESS OBJECT
    List<Account> accounts = new ArrayList<>();
    Account account = null;
    
    private void createDBConnection() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/BankingSystem", "root", "");
            stmnt = (Statement) con.createStatement();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void closeDBConnection() throws SQLException {
        result.close();
        stmnt.close();
        con.close();
    }
    
    private Account convertResultRowToList(ResultSet res) throws SQLException {
        int ID = res.getInt("ID");
        int AccountNumber = res.getInt("AccountNumber");
        int Balance = res.getInt("Balance");
        String FirstName = res.getString("FirstName");
        String LastName = res.getString("LastName");
        String ContactNumber = res.getString("ContactNumber");
        String Birthday = res.getString("Birthday");
        String Address = res.getString("Address");
        int Status = res.getInt("Status");
        
        account = new Account(AccountNumber, Balance, Status, FirstName, LastName, Address, Birthday, ContactNumber);
        return account;
    }
    
    public List<Account> getAllAccounts() {
            query = "SELECT * FROM Accounts;";
        try {
            createDBConnection();
            result = stmnt.executeQuery(query);
            
            while(result.next()){
                convertResultRowToList(result);
                accounts.add(account);
            }
            
            closeDBConnection();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return accounts;
    }
    
    public Account isExistingAccount(int AccountNumber){
        accounts = getAllAccounts();
        for(Account currAccount : accounts)
            if((currAccount.getAccountNumber() == AccountNumber))
                return currAccount;
        
        return null;
    }
    
    public Account validateActiveAccount(int AccountNumber){
        account = isExistingAccount(AccountNumber);
        if(!account.equals(null))
            if(account.getStatus() == 1)
                return account;
        
        return null;
    }
    
    public void insertAccount(Account account) throws SQLException {
        query = "INSERT INTO Accounts(AccountNumber, Balance, FirstName, LastName, ContactNumber, Birthday, Address, Status) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        createDBConnection();
        pstmnt = (PreparedStatement) con.prepareStatement(query); 
        pstmnt.setInt(1, account.getAccountNumber());
        pstmnt.setInt(2, account.getBalance());
        pstmnt.setString(3, account.getFirstName());
        pstmnt.setString(4, account.getLastName());
        pstmnt.setString(5, account.getContactNumber());
        pstmnt.setString(6, account.getBirthday());
        pstmnt.setString(7, account.getAddress());
        pstmnt.setInt(8, account.getStatus());
        pstmnt.executeUpdate();
        closeDBConnection();
    }
    
    public void updateAccountStatus(Account account, int NewStatus) throws SQLException{
        query = "UPDATE Accounts SET Status=?, Balance=0 WHERE AccountNumber=?";
        createDBConnection();
        pstmnt = (PreparedStatement) con.prepareStatement(query);
        pstmnt.setInt(1, NewStatus);
        pstmnt.setInt(2, account.getAccountNumber());
        pstmnt.executeUpdate();
        closeDBConnection();
    }
    
    public void updateAccountBalance(Account account, int NewBalance) throws SQLException{
        query = "UPDATE Accounts SET Balance=? WHERE AccountNumber=?";
        createDBConnection();
        pstmnt = (PreparedStatement) con.prepareStatement(query);
        pstmnt.setInt(1, NewBalance);
        pstmnt.setInt(2, account.getAccountNumber());
        pstmnt.executeUpdate();
        closeDBConnection();
    }
}

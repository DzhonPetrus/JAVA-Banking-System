
import com.mysql.jdbc.PreparedStatement;
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
public class Client_DAO {
    MyDB sql = new MyDB();
    List<Client> clients = new ArrayList<>();
    Client tempClient = null;
    
    private void ClearData(){
        tempClient = null;
        clients.clear();
    }
    
    private Client ConvertRowToClient(ResultSet rs) throws SQLException {
        int ID = rs.getInt("ID");
        int AccountNo = rs.getInt("AccountNo");
        int Balance = rs.getInt("Balance");
        String FName = rs.getString("FName");
        String LName = rs.getString("LName");
        String ContactNo = rs.getString("ContactNo");
        String Birthday = rs.getString("Birthday");
        String Address = rs.getString("Address");
        String Status = rs.getString("Status");
        
        tempClient = new Client(ID, AccountNo, Balance, FName, LName, ContactNo, Birthday, Address, Status);
        return tempClient;
    }
 
    public List<Client> GetAllClients(){
        ClearData();
        
        try {
            sql.createConnection();
            sql.rs = sql.st.executeQuery("Select * from client");
            
            // GET THE RESULT OF QUERY THEN ADD IT TO LIST
            while (sql.rs.next()){
                tempClient = ConvertRowToClient(sql.rs);
                clients.add(tempClient);
            }
            
            sql.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(Client_DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return clients;
    }
    
    public Client GetClientByAccNo(int AccNo) throws SQLException{
        ClearData();
        clients = GetAllClients();
        
        for(Client tempClient : clients)
            if(tempClient.getAccountNo() == AccNo) 
                return tempClient;
            
        return null;
    }
    
    public void AddClient(Client theClient) throws SQLException {
        sql.createConnection();
        sql.pst = (PreparedStatement) sql.con.prepareStatement("Insert into client(AccountNo, Balance, FName, LName, ContactNo, Birthday, Address, Status) values(?, ?, ?, ?, ?, ?, ?, ?)");
        sql.pst.setInt(1, theClient.getAccountNo());
        sql.pst.setInt(2, theClient.getBalance());
        sql.pst.setString(3, theClient.getFName());
        sql.pst.setString(4, theClient.getLName());
        sql.pst.setString(5, theClient.getContactNo());
        sql.pst.setString(6, theClient.getBirthday());
        sql.pst.setString(7, theClient.getAddress());
        sql.pst.setString(8, theClient.getStatus());
        sql.pst.executeUpdate();
        sql.closeConnection();
    }
    
    public void UpdateClientBalance(Client theClient, int newBalance) throws SQLException {
        sql.createConnection();
        sql.pst = (PreparedStatement) sql.con.prepareStatement("Update client set Balance=? where AccountNo=?");
        sql.pst.setInt(1, newBalance);
        sql.pst.setInt(2, theClient.getAccountNo());
        sql.pst.executeUpdate();
        sql.closeConnection();
    }
    
    public void UpdateClientStatus(Client theClient, String newStatus) throws SQLException{
        sql.createConnection();
        sql.pst = (PreparedStatement) sql.con.prepareStatement("Update client set Status=? where AccountNo=?");
        sql.pst.setString(1, newStatus);
        sql.pst.setInt(2, theClient.getAccountNo());
        sql.pst.executeUpdate();
        sql.closeConnection();
    }
    
    
}

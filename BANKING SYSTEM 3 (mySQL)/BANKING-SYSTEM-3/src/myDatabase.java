
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
public class myDatabase {
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    String query = "";
    
    private void createConnection() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/banking_system", "root", "");
            st = (Statement) con.createStatement();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(myDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void closeConnection() throws SQLException {
        rs.close();
        st.close();
        con.close();
    }
    
    
    List<Customer> customers = new ArrayList<>();
    Customer customer = null;
    
    private Customer sqlResultRowToCustomer(ResultSet rs) throws SQLException {
        int ID = rs.getInt("ID");
        int accountNo = rs.getInt("accountNo");
        int balance = rs.getInt("balance");
        String fName = rs.getString("fName");
        String lName = rs.getString("lName");
        String contactNo = rs.getString("contactNo");
        String birthday = rs.getString("birthday");
        String address = rs.getString("address");
        String status = rs.getString("status");
        
        customer = new Customer(ID, accountNo, balance, fName, lName, contactNo, birthday, address, status);
        return customer;
    }
    
    public List<Customer> getAllCustomers(){
        
        try {
            createConnection();
            rs = st.executeQuery("SELECT * FROM CUSTOMER;");
            
            while(rs.next()){
                sqlResultRowToCustomer(rs);
                customers.add(customer);
            }
            
            closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(myDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return customers;
    }
    
    public Customer getCustomerByAccountNumber(int accountNo){
        customers = getAllCustomers();
        for(Customer tempCustomer : customers)
            if(tempCustomer.getAccountNo() == accountNo)
                return tempCustomer;
        
        return null;
    }
    
    public void postCustomerData(Customer customer) throws SQLException {
        createConnection();
        pst = (PreparedStatement) con.prepareStatement("INSERT INTO CUSTOMER(accountNo, balance, fName, lName, contactNo, birthday, address, status) values(?, ?, ?, ?, ?, ?, ?, ?)");
        pst.setInt(1, customer.getAccountNo());
        pst.setInt(2, customer.getBalance());
        pst.setString(3, customer.getfName());
        pst.setString(4, customer.getlName());
        pst.setString(5, customer.getContactNo());
        pst.setString(6, customer.getBirthday());
        pst.setString(7, customer.getAddress());
        pst.setString(8, customer.getStatus());
        pst.executeUpdate();
        closeConnection();
    }
    
    public void putCustomerNewBalance(Customer customer, int newBalance) throws SQLException {
        createConnection();
        pst = (PreparedStatement) con.prepareStatement("UPDATE CUSTOMER SET balance=? WHERE accountNo=?");
        pst.setInt(1, newBalance);
        pst.setInt(2, customer.getAccountNo());
        pst.executeUpdate();
        closeConnection();
    }
    
    public void putCustomerNewStatus(Customer customer, String newStatus) throws SQLException {
        createConnection();
        pst = (PreparedStatement) con.prepareStatement("UPDATE CUSTOMER SET status=? WHERE accountNo=?");
        pst.setString(1, newStatus);
        pst.setInt(2, customer.getAccountNo());
        pst.executeUpdate();
        closeConnection();
    }
}

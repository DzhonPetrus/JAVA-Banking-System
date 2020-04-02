
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dzhon
 */
public class FileHandling {
    private File file;
    private List<Customer> customers = null;
    private Customer customer = null;

    public FileHandling(String fileName) {
        this.file = new File(fileName);
    }
    
    public FileHandling(File file) {
        this.file = file;
    }
    
    public List<Customer> loadCustomerDataFromFile() {
        customers = new ArrayList<>();
        
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String field = null;
            while((field = reader.readLine()) != null) {
                customer = new Customer( Integer.parseInt(field), Integer.parseInt(reader.readLine()), Integer.parseInt(reader.readLine()), reader.readLine(), reader.readLine(), reader.readLine(), reader.readLine(), reader.readLine(), reader.readLine());
                customers.add(customer);
                reader.readLine();
            }
        }
        catch ( IOException e) {
            System.out.println(e);
        }

        return customers;
    }
    
     public void saveCustomerDataToFile(Customer customer) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(customer.getID() + "\r\n" + customer.getAccountNo() + "\r\n" + customer.getBalance() + "\r\n" + customer.getfName()+ "\r\n" + customer.getlName()+ "\r\n" + customer.getAddress() + "\r\n" + customer.getBirthday()+ "\r\n" + customer.getContactNo() + "\r\n" + customer.getStatus() + "\r\n\r\n");
        } catch(IOException e) {
            System.out.println(e);
        }
    }
    
    public Customer searchCustomerDataByAccountNo(int accountNo){
        customers = loadCustomerDataFromFile();
        for(Customer customer : customers)
            if(customer.getAccountNo() == accountNo)
                return customer;
        
        return null;
    }
     
    public void updateCustomerData(Customer customer) {
        FileHandling tempDataHandler = new FileHandling("tempDataHandler.txt");
        
        customers = loadCustomerDataFromFile();
        for(Customer tempCustomer : customers){
            if(tempCustomer.getAccountNo() == customer.getAccountNo())
                    tempCustomer = customer;
            
            tempDataHandler.saveCustomerDataToFile(tempCustomer);
        }
        
        file.delete();
        tempDataHandler.renameFile("CustomersData.txt");
        
    }
    
    public void renameFile(String fileName){
        file.renameTo(new File(fileName));
    }
}

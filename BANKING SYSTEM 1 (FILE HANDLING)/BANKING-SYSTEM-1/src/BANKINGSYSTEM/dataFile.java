/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BANKINGSYSTEM;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dzhon
 */
public class dataFile {
    private File file;

    public dataFile(String fileName) {
        this.file = new File(fileName);
    }

    public dataFile(File file) {
        this.file = file;
    }

    public void fileRename(String name){
        file.renameTo(new File(name));
    }
    
    public void saveAccount(Account account) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(account.getId() + "\r\n" + account.getAccountNo() + "\r\n" + account.getBalance() + "\r\n" + account.getFname() + "\r\n" + account.getLname() + "\r\n" + account.getAddress() + "\r\n" + account.getbDay() + "\r\n" + account.getContactNo() + "\r\n" + account.getStatus() + "\r\n\r\n");
        } catch(IOException e) {
            System.out.println(e);
        }
    }

    /**
     *
     * @return
     * @throws IOException
     */
    public List<Account> loadAllAccounts() throws IOException {
        List<Account> accounts = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String field = null;
            while((field = reader.readLine()) != null) {
                Account account = new Account( Integer.parseInt(field), Integer.parseInt(reader.readLine()), Integer.parseInt(reader.readLine()), reader.readLine(), reader.readLine(), reader.readLine(), reader.readLine(), Long.parseLong(reader.readLine()), reader.readLine());
                accounts.add(account);
                reader.readLine();
            }
        }
        catch ( IOException e) {
            System.out.println(e);
        }

        return accounts;
    }
    
    public Account findByAccNo(Account tempAccount, int accNo) throws IOException {
        List<Account> accounts = new ArrayList<>();
        accounts = loadAllAccounts();
        for(Account account : accounts) {       
            if(account.getAccountNo() == accNo) {
                return account;
            }
        }
        return null;
    }
    
    public void updateAccount(Account oldAccount) throws IOException{
        dataFile tempFile = new dataFile("tempAccounts.txt");
        
        List<Account> accounts = new ArrayList<>();
        int ID = oldAccount.getId();
        accounts = loadAllAccounts();
        for(Account account : accounts) {       
            if(account.getId()== ID) 
                account = oldAccount;
            
            tempFile.saveAccount(account);
        }
        
        file.delete();
        tempFile.fileRename("Accounts.txt");
    }
}

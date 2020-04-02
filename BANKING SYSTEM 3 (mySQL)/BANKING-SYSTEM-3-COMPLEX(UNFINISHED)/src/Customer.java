/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dzhon
 */
public class Customer {
    int ID, accountNo, balance;
    String fName, lName, contactNo, birthday, address, status;

    public Customer(int ID, int accountNo, int balance, String fName, String lName, String contactNo, String birthday, String address, String status) {
        this.ID = ID;
        this.accountNo = accountNo;
        this.balance = balance;
        this.fName = fName;
        this.lName = lName;
        this.contactNo = contactNo;
        this.birthday = birthday;
        this.address = address;
        this.status = status;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(int accountNo) {
        this.accountNo = accountNo;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Customer{" + "ID=" + ID + ", accountNo=" + accountNo + ", balance=" + balance + ", fName=" + fName + ", lName=" + lName + ", contactNo=" + contactNo + ", birthday=" + birthday + ", address=" + address + ", status=" + status + '}';
    }
    
    
}

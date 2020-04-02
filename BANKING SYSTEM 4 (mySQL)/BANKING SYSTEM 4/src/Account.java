/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dzhon
 */
public class Account {
    int AccountNumber, Balance, Status;
    String FirstName, LastName, Address, Birthday, ContactNumber;

    public Account(int AccountNumber, int Balance, int Status, String FirstName, String LastName, String Address, String Birthday, String ContactNumber) {
        this.AccountNumber = AccountNumber;
        this.Balance = Balance;
        this.Status = Status;
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.Address = Address;
        this.Birthday = Birthday;
        this.ContactNumber = ContactNumber;
    }

    public int getAccountNumber() {
        return AccountNumber;
    }

    public void setAccountNumber(int AccountNumber) {
        this.AccountNumber = AccountNumber;
    }

    public int getBalance() {
        return Balance;
    }

    public void setBalance(int Balance) {
        this.Balance = Balance;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String LastName) {
        this.LastName = LastName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String Birthday) {
        this.Birthday = Birthday;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String ContactNumber) {
        this.ContactNumber = ContactNumber;
    }

    @Override
    public String toString() {
        return "Account{" + "AccountNumber=" + AccountNumber + ", Balance=" + Balance + ", Status=" + Status + ", FirstName=" + FirstName + ", LastName=" + LastName + ", Address=" + Address + ", Birthday=" + Birthday + ", ContactNumber=" + ContactNumber + '}';
    }
    
    
}

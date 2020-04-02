/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dzhon
 */
public class Client {
    int ID, AccountNo, Balance;
    String FName, LName, Birthday, Address, ContactNo, Status;

    public Client(int ID, int AccountNo, int Balance, String FName, String LName, String ContactNo, String Birthday, String Address,  String Status) {
        this.ID = ID;
        this.AccountNo = AccountNo;
        this.Balance = Balance;
        this.FName = FName;
        this.LName = LName;
        this.ContactNo = ContactNo;
        this.Birthday = Birthday;
        this.Address = Address;
        this.Status = Status;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getAccountNo() {
        return AccountNo;
    }

    public void setAccountNo(int AccountNo) {
        this.AccountNo = AccountNo;
    }

    public int getBalance() {
        return Balance;
    }

    public void setBalance(int Balance) {
        this.Balance = Balance;
    }

    public String getFName() {
        return FName;
    }

    public void setFName(String FName) {
        this.FName = FName;
    }

    public String getLName() {
        return LName;
    }

    public void setLName(String LName) {
        this.LName = LName;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String Birthday) {
        this.Birthday = Birthday;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getContactNo() {
        return ContactNo;
    }

    public void setContactNo(String ContactNo) {
        this.ContactNo = ContactNo;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    @Override
    public String toString() {
        return "Client{" + "ID=" + ID + ", AccountNo=" + AccountNo + ", Balance=" + Balance + ", FName=" + FName + ", LName=" + LName + ", ContactNo=" + ContactNo + ", Birthday=" + Birthday + ", Address=" + Address + ", Status=" + Status + '}';
    }
    
    
    
}

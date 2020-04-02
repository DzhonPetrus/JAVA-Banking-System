/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BANKINGSYSTEM;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author dzhon
 */
public class Account {
    int id, balance, accountNo;
    long contactNo;
    String fname, lname, address, bDay, Status;

    public Account(int id, int accountNo, int balance, String fname, String lname, String address, String bDay, long contactNo, String Status) {
        this.id = id;
        this.accountNo = accountNo;
        this.balance = balance;
        this.fname = fname;
        this.lname = lname;
        this.address = address;
        this.bDay = bDay;
        this.contactNo = contactNo;
        this.Status = Status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getbDay() {
        return bDay;
    }

    public void setbDay(String bDay) {
        this.bDay = bDay;
    }

    public long getContactNo() {
        return contactNo;
    }

    public void setContactNo(long contactNo) {
        this.contactNo = contactNo;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    @Override
    public String toString() {
        return "Account{" + "id=" + id + ", accountNo=" + accountNo + ", balance=" + balance + ", fname=" + fname + ", lname=" + lname + ", address=" + address + ", bDay=" + bDay + ", contactNo=" + contactNo + ", Status=" + Status + '}';
    }
    
}

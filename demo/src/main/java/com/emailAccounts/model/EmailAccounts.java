package com.emailAccounts.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Abhishek
 */
public class EmailAccounts 
{
    private int uniqueid;
    private String emailid;
    private String username;
    private String password;
    private String contactno;
    private int isdelete;
    private int active;

    // Method to get the current date and time as a formatted string
    public static String getCurrentDateTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return currentDateTime.format(formatter);
    }
    
    
    public int getUniqueid() {
        return uniqueid;
    }

    public void setUniqueid(int uniqueid) {
        this.uniqueid = uniqueid;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContactno() {
        return contactno;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
    }

    public int getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(int isdelete) {
        this.isdelete = isdelete;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public EmailAccounts(int uniqueid, String emailid, String username, String password, String contactno, int isdelete, int active) {
        this.uniqueid = uniqueid;
        this.emailid = emailid;
        this.username = username;
        this.password = password;
        this.contactno = contactno;
        this.isdelete = isdelete;
        this.active = active;
    }

    public EmailAccounts() {
    }

    @Override
    public String toString() {
        return "EmailAccounts{" + "uniqueid=" + uniqueid + ", emailid=" + emailid + ", username=" + username + ", password=" + password + ", contactno=" + contactno + ", isdelete=" + isdelete + ", active=" + active + '}';
    }

}

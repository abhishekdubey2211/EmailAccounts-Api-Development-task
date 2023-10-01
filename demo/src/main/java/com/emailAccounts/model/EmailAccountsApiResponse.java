/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.emailAccounts.model;

import java.util.List;

/**
 *
 * @author Abhishek
 */
public class EmailAccountsApiResponse {

    private String resdatetime;
    private int revisionno;
    private int status;
    private String statusdesc;
    private List<EmailAccounts> emailAccount;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusdesc() {
        return statusdesc;
    }

    public void setStatusdesc(String statusdesc) {
        this.statusdesc = statusdesc;
    }

    public int getRevisionno() {
        return revisionno;
    }

    public void setRevisionno(int revisionno) {
        this.revisionno = revisionno;
    }

    public String getResdatetime() {
        return resdatetime;
    }

    public void setResdatetime(String resdatetime) {
        this.resdatetime = resdatetime;
    }

    public List<EmailAccounts> getEmailAccount() {
        return emailAccount;
    }

    public void setEmailAccount(List<EmailAccounts> emailAccount) {
        this.emailAccount = emailAccount;
    }

    public EmailAccountsApiResponse(int status, String statusdesc, int revisionno, String resdatetime, List<EmailAccounts> emailAccount) {
        this.status = status;
        this.statusdesc = statusdesc;
        this.revisionno = revisionno;
        this.resdatetime = resdatetime;
        this.emailAccount = emailAccount;
    }

    public EmailAccountsApiResponse() {
    }

    @Override
    public String toString() {
        return "EmailAccountsApiResponse{" + "status=" + status + ", statusdesc=" + statusdesc + ", revisionno=" + revisionno + ", resdatetime=" + resdatetime + ", emailAccount=" + emailAccount + '}';
    }

}

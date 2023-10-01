///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.emailAccounts.controller;
//
//import com.emailAccounts.actions.EmailAccountsAction;
//import org.springframework.stereotype.Controller;
//import com.emailAccounts.actions.EmailAccountsAction;
//import com.emailAccounts.model.EmailAccounts;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
///**
// *
// * @author Abhishek
// */
//@RestController
//@RequestMapping("/email-accounts")
//public class EmailAccountsController {
//    
//    EmailAccountsAction emailAccountsAction = new EmailAccountsAction();
//
//    @PostMapping("/insert")
//    public String insertEmailAccount(@RequestBody EmailAccounts emailAccount) {
//        String result = emailAccountsAction.insertEmailaccounts(emailAccount);
//        return result;
//    }
//}
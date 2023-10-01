package com.emailAccounts.actions;

//import static com.emailAccounts.actions.PasswordEncryption.encrypt;
import com.emailAccounts.model.EmailAccounts;
import com.emailAccounts.model.EmailAccountsApiResponse;
import static com.service.Validator.constants.ConstantData.EMAIL_PATTERN;
import com.service.Validator.validator.Validations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/emailAccounts")
public class EmailAccountsAction {

    public static final String url = "jdbc:mysql://localhost:3306/acs_report_tenant_1";
    public static final String user = "root";
    public static final String pass = "ABHI";

//*****************************************************************    
    @GetMapping("/version")
    public String version(){
        return "EmailAccounts_Api_v0.0.1  ||  01 October 2023";
    }

    @PostMapping("/emailaccount")
    public EmailAccountsApiResponse insertEmailaccounts(@RequestBody EmailAccounts pushEmailAccounts) {
        String emailid = null, username = null, password = null, contactno = null;
        int active = 0;
        EmailAccountsApiResponse emailResponseApi = new EmailAccountsApiResponse();

        try {
            Connection con = DriverManager.getConnection(url, user, pass);
            emailResponseApi.setResdatetime(EmailAccounts.getCurrentDateTime());
            emailResponseApi.setRevisionno(1);

            emailid = pushEmailAccounts.getEmailid();
            if (emailValidate(emailid) != true) {
                emailResponseApi.setStatus(3);
                emailResponseApi.setStatusdesc("Email formate Invalid");

                return emailResponseApi;
            }

            username = pushEmailAccounts.getUsername();

            if (username == null || username.isBlank() || username.trim().length() < 1) {

                emailResponseApi.setStatus(4);
                emailResponseApi.setStatusdesc("Username should not be blank");

                return emailResponseApi;

            }

            if (checkMaxStringLength(username, 100) != true) {

                emailResponseApi.setStatus(5);
                emailResponseApi.setStatusdesc("username length should not greater than 100");

                return emailResponseApi;
            }

            if (checkMinStringLength(username, 3) != true) {

                emailResponseApi.setStatus(5);
                emailResponseApi.setStatusdesc("username length should not less than 3");

                return emailResponseApi;
            }

            password = pushEmailAccounts.getPassword();


            if (password == null || password.isBlank() || password.trim().length() < 1) {

                emailResponseApi.setStatus(5);
                emailResponseApi.setStatusdesc("password should not be blank");

                return emailResponseApi;

            }

            if (checkMaxStringLength(password, 20) != true) {

                emailResponseApi.setStatus(6);
                emailResponseApi.setStatusdesc("password length should not greater than 20");

                return emailResponseApi;
            }

            if (checkMinStringLength(password, 3) != true) {

                emailResponseApi.setStatus(7);
                emailResponseApi.setStatusdesc("password length should not less than 3");

                return emailResponseApi;
            }
            contactno = pushEmailAccounts.getContactno();

            if (contactno == null || contactno.isBlank() || contactno.trim().length() < 1) {

                emailResponseApi.setStatus(8);
                emailResponseApi.setStatusdesc("contactno should not be blank");

                return emailResponseApi;

            }
            if (!isValidContactNumber(contactno)) {
                emailResponseApi.setStatus(10);
                emailResponseApi.setStatusdesc("Invalid contact number format");
                return emailResponseApi; // Return the response immediately
            }

            active = pushEmailAccounts.getActive();

            if (active < 0 || active > 1) {
                emailResponseApi.setStatus(11);
                emailResponseApi.setStatusdesc("active should be 1 0r 0");

                return emailResponseApi;
            }
            String query = "CALL InsertAndGetEmailAccount(?, ?, ?, ?, ?)";
            CallableStatement cstmt = con.prepareCall(query);
            cstmt.setString(1, emailid);
            cstmt.setString(2, username);
            cstmt.setString(3, password);
            cstmt.setString(4, contactno);
            cstmt.setInt(5, active);
            ResultSet rs = cstmt.executeQuery();

            List<EmailAccounts> fetcheddata = getEmailAccount(rs);

            if (fetcheddata != null && !fetcheddata.isEmpty()) {
                emailResponseApi.setStatus(1);
                emailResponseApi.setStatusdesc("Success");
                emailResponseApi.setEmailAccount(fetcheddata);
            } else {
                emailResponseApi.setStatus(0);
                emailResponseApi.setStatusdesc("Fail");
            }

            rs.close();
            cstmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception properly, log it, or throw a custom exception.
        }

        return emailResponseApi;
    }

    @GetMapping("/{id}")
    public EmailAccountsApiResponse getEmailAccountsById(@PathVariable("id") int emailAccountId) throws SQLException {
        String emailid = null, username = null, password = null, contactno = null;
        int active = 0;
        EmailAccountsApiResponse emailResponseApi = new EmailAccountsApiResponse();

        try {
            Connection con = DriverManager.getConnection(url, user, pass);
            emailResponseApi.setResdatetime(EmailAccounts.getCurrentDateTime());
            emailResponseApi.setRevisionno(1);

            if (checkEmailAccountExists(emailAccountId) != true) {

                emailResponseApi.setStatus(2);
                emailResponseApi.setStatusdesc("Email Accounts Not Found");

                return emailResponseApi;
            }

            String query = "select * from email_accounts where uniqueid = ? ";
            CallableStatement cstmt = con.prepareCall(query);
            cstmt.setInt(1, emailAccountId);
            cstmt.executeQuery();

            ResultSet rs = cstmt.executeQuery();

            List<EmailAccounts> fetcheddata = getEmailAccount(rs);

            if (fetcheddata != null && !fetcheddata.isEmpty()) {
                emailResponseApi.setStatus(1);
                emailResponseApi.setStatusdesc("Success");
                emailResponseApi.setEmailAccount(fetcheddata);
            } else {
                emailResponseApi.setStatus(0);
                emailResponseApi.setStatusdesc("Fail");
            }

            rs.close();
            cstmt.close();
            con.close();
        } catch (Exception e) {

        }
        return emailResponseApi;
    }

    @DeleteMapping("/")
    public EmailAccountsApiResponse deleteEmailAccountsById(@RequestBody EmailAccounts deleteemail) throws SQLException {
        String emailid = null, username = null, password = null, contactno = null;
        int active = 0;
        EmailAccountsApiResponse emailResponseApi = new EmailAccountsApiResponse();
        int sucessid =0;
        try {
            Connection con = DriverManager.getConnection(url, user, pass);
            emailResponseApi.setResdatetime(EmailAccounts.getCurrentDateTime());
            emailResponseApi.setRevisionno(1);

            if (checkEmailAccountExists(deleteemail.getUniqueid()) != true) {

                emailResponseApi.setStatus(2);
                emailResponseApi.setStatusdesc("Email Accounts Not Found");

                return emailResponseApi;
            }

            String query = "update email_accounts set isdelete=1 where uniqueid = ? ";
            CallableStatement cstmt = con.prepareCall(query);
            cstmt.setInt(1, deleteemail.getUniqueid());
            boolean rs = cstmt.execute();

           
            
            if (rs != true) {

                emailResponseApi.setStatus(1);
                emailResponseApi.setStatusdesc("Sucess");

            } else {

                emailResponseApi.setStatus(0);
                emailResponseApi.setStatusdesc("Fail");

            }

//            rs.close();
            cstmt.close();
            con.close();
        } catch (Exception e) {

        }
        return emailResponseApi;
    }

    
    @PutMapping("/retrive")
    public EmailAccountsApiResponse unDeleteEmailAccountsById(@RequestBody EmailAccounts unDeleteemail) throws SQLException {
        String emailid = null, username = null, password = null, contactno = null;
        int active = 0;
        EmailAccountsApiResponse emailResponseApi = new EmailAccountsApiResponse();
        int sucessid =0;
        try {
            Connection con = DriverManager.getConnection(url, user, pass);
            emailResponseApi.setResdatetime(EmailAccounts.getCurrentDateTime());
            emailResponseApi.setRevisionno(1);

            if (checkEmailAccountExists(unDeleteemail.getUniqueid()) != false) {

                emailResponseApi.setStatus(15);
                emailResponseApi.setStatusdesc("Email Accounts Exists");

                return emailResponseApi;
            }

            String query = "update email_accounts set isdelete=0 where uniqueid = ? ";
            CallableStatement cstmt = con.prepareCall(query);
            cstmt.setInt(1, unDeleteemail.getUniqueid());
            cstmt.executeUpdate();

            System.out.println("Query :: " + cstmt.toString());

            String query2 = "select * from email_accounts where uniqueid = ?";
            CallableStatement cstmt2 = con.prepareCall(query2);
            cstmt2.setInt(1, unDeleteemail.getUniqueid());
            ResultSet rs = cstmt2.executeQuery();
            System.out.println("Query :: " + cstmt2.toString());
            System.out.println("Query :: " + rs.toString());

//            ResultSet rs2 = cstmt2.executeQuery();
            System.out.println("Resultset retrived :: "+rs.toString());

            List<EmailAccounts> fetcheddata = getEmailAccount(rs);


            if (fetcheddata != null && !fetcheddata.isEmpty()) {
                emailResponseApi.setStatus(1);
                emailResponseApi.setStatusdesc("Email Account with uniqueid "+unDeleteemail.getUniqueid()+" Retrived Success");
                emailResponseApi.setEmailAccount(fetcheddata);
            } else {
                emailResponseApi.setStatus(0);
                emailResponseApi.setStatusdesc("Fail");
            }

            rs.close();
            cstmt.close();
            con.close();
        } catch (Exception e) {

        }
        return emailResponseApi;
    }

    
    @GetMapping("/get_emailaccounts")
    public EmailAccountsApiResponse getAllEmailAccounts() throws SQLException {
        String emailid = null, username = null, password = null, contactno = null;
        int active = 0;
        EmailAccountsApiResponse emailResponseApi = new EmailAccountsApiResponse();

        try {
            Connection con = DriverManager.getConnection(url, user, pass);
            emailResponseApi.setResdatetime(EmailAccounts.getCurrentDateTime());
            emailResponseApi.setRevisionno(1);

            String query = "select * from email_accounts where isdelete =0";
            CallableStatement cstmt = con.prepareCall(query);
            ResultSet rs = cstmt.executeQuery();

            List<EmailAccounts> fetcheddata = getEmailAccount(rs);

            if (fetcheddata != null && !fetcheddata.isEmpty()) {
                emailResponseApi.setStatus(1);
                emailResponseApi.setStatusdesc("Success");
                emailResponseApi.setEmailAccount(fetcheddata);
            } else {
                emailResponseApi.setStatus(0);
                emailResponseApi.setStatusdesc("Fail");
            }

            rs.close();
            cstmt.close();
            con.close();
        } catch (Exception e) {

        }
        return emailResponseApi;
    }

    @PutMapping("/emailaccount")
    public EmailAccountsApiResponse editEmailaccounts(@RequestBody EmailAccounts putEmailAccounts) throws SQLException {
        String emailid = null, username = null, password = null, contactno = null;
        int active = 0, uniqueid = 0;
        EmailAccountsApiResponse emailResponseApi = new EmailAccountsApiResponse();

        try {
            Connection con = DriverManager.getConnection(url, user, pass);
            emailResponseApi.setResdatetime(EmailAccounts.getCurrentDateTime());
            emailResponseApi.setRevisionno(1);
//            emailid = putEmailAccounts.getEmailid();
//            username = putEmailAccounts.getUsername();
//            password = putEmailAccounts.getPassword();
//            contactno = putEmailAccounts.getContactno();
//            active = putEmailAccounts.getActive();

            emailid = putEmailAccounts.getEmailid();
            if (emailValidate(emailid) != true) {
                emailResponseApi.setStatus(3);
                emailResponseApi.setStatusdesc("Email formate Invalid");

                return emailResponseApi;
            }

            username = putEmailAccounts.getUsername();

            if (username == null || username.isBlank() || username.trim().length() < 1) {

                emailResponseApi.setStatus(4);
                emailResponseApi.setStatusdesc("Username should not be blank");

                return emailResponseApi;

            }

            if (checkMaxStringLength(username, 100) != true) {

                emailResponseApi.setStatus(5);
                emailResponseApi.setStatusdesc("username length should not greater than 100");

                return emailResponseApi;
            }

            if (checkMinStringLength(username, 3) != true) {

                emailResponseApi.setStatus(5);
                emailResponseApi.setStatusdesc("username length should not less than 3");

                return emailResponseApi;
            }

            password = putEmailAccounts.getPassword();

            if (password == null || password.isBlank() || password.trim().length() < 1) {

                emailResponseApi.setStatus(5);
                emailResponseApi.setStatusdesc("password should not be blank");

                return emailResponseApi;

            }

            if (checkMaxStringLength(password, 20) != true) {

                emailResponseApi.setStatus(6);
                emailResponseApi.setStatusdesc("password length should not greater than 20");

                return emailResponseApi;
            }

            if (checkMinStringLength(password, 3) != true) {

                emailResponseApi.setStatus(7);
                emailResponseApi.setStatusdesc("password length should not less than 3");

                return emailResponseApi;
            }
            contactno = putEmailAccounts.getContactno();

            if (contactno == null || contactno.isBlank() || contactno.trim().length() < 1) {

                emailResponseApi.setStatus(8);
                emailResponseApi.setStatusdesc("contactno should not be blank");

                return emailResponseApi;

            }
            if (!isValidContactNumber(contactno)) {
                emailResponseApi.setStatus(10);
                emailResponseApi.setStatusdesc("Invalid contact number format");
                return emailResponseApi; // Return the response immediately
            }
            active = putEmailAccounts.getActive();

            if (active < 0 || active > 1) {
                emailResponseApi.setStatus(11);
                emailResponseApi.setStatusdesc("active should be 1 0r 0");

                return emailResponseApi;
            }

            uniqueid = putEmailAccounts.getUniqueid();
            if (checkEmailAccountExists(uniqueid) != true) {

                emailResponseApi.setStatus(2);
                emailResponseApi.setStatusdesc("Email Accounts Not Found");
                return emailResponseApi;
            }

            String query = "Update email_accounts set emailid=? ,username =?,password =?,contactno=?,active =?, isdelete = 0 where uniqueid =?";
            CallableStatement cstmt = con.prepareCall(query);
            cstmt.setString(1, emailid);
            cstmt.setString(2, username);
            cstmt.setString(3, password);
            cstmt.setString(4, contactno);
            cstmt.setInt(5, active);
            cstmt.setInt(6, uniqueid);
            cstmt.executeUpdate();

            System.out.println("Query :: " + cstmt.toString());

            String query2 = "select * from email_accounts where uniqueid = ?";
            CallableStatement cstmt2 = con.prepareCall(query2);
            cstmt2.setInt(1, uniqueid);
            ResultSet rs = cstmt2.executeQuery();
            System.out.println("Query :: " + cstmt2.toString());
            System.out.println("Query :: " + rs.toString());

            List<EmailAccounts> fetcheddata = getEmailAccount(rs);

            if (fetcheddata != null && !fetcheddata.isEmpty()) {
                emailResponseApi.setStatus(1);
                emailResponseApi.setStatusdesc("Success");
                emailResponseApi.setEmailAccount(fetcheddata);
            } else {
                emailResponseApi.setStatus(0);
                emailResponseApi.setStatusdesc("Fail");
            }

            rs.close();
            cstmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception properly, log it, or throw a custom exception.
        }

        return emailResponseApi;
    }

    //*****************************************************************************************
    public List<EmailAccounts> getEmailAccount(ResultSet rs) throws SQLException {
        List<EmailAccounts> emailAccountList = new ArrayList<>();
        try{
        while (rs != null && rs.next()) {
            EmailAccounts data = new EmailAccounts();
            data.setUniqueid(rs.getInt("uniqueid"));
            data.setEmailid(rs.getString("emailid"));
            data.setUsername(rs.getString("username"));
            data.setPassword(rs.getString("password"));
            data.setContactno(rs.getString("contactno"));
            data.setActive(rs.getInt("active"));
            data.setIsdelete(rs.getInt("isdelete"));
            emailAccountList.add(data);
        }
        return emailAccountList;
        }catch(Exception e){
            
        }
        return null;
    }

    // Validations checkEmailAccountExists
    public static boolean checkEmailAccountExists(int uniqueid) throws SQLException {

//        Connection conn = null;
        // Connection conn = null;
        // CallableStatement
        CallableStatement cstmt = null;

        // ResultSet
        ResultSet rs = null;

        // query
        String query = null;

        boolean check = false;

        try {
            Connection conn = DriverManager.getConnection(url, user, pass);

            query = "select uniqueid from email_accounts where uniqueid = ? and isdelete =0";

            cstmt = conn.prepareCall(query);

            cstmt.setInt(1, uniqueid);
            System.out.println("checkEmailAccountExists :: Query >>>>  :" + cstmt.toString());
            rs = cstmt.executeQuery();
            System.out.println("checkEmailAccountExists :: Resultset >>>>  :" + rs.toString());

            if (rs != null) {
                if (rs.next()) {
                    return true;
                }
            }

        } catch (Exception e) {

            System.out.println("SOME EXCEPTION IN checkReportTfgrouopidExist >> " + e);

        } finally {

            try {

                if (rs != null) {

                    rs.close();
                }

                if (cstmt != null) {

                    cstmt.close();
                }

            } catch (Exception e) {

            }
            cstmt = null;
            query = null;
            rs = null;

        }
        return false;

    }

    // Validations emailValidate
    public static boolean emailValidate(String emailId) {

        Pattern pattern = null;
        Matcher matcher = null;

        try {

            pattern = Pattern.compile(EMAIL_PATTERN);
            matcher = pattern.matcher(emailId);

            if (matcher.matches()) {

                matcher.reset();
                return true;

            }

        } catch (Exception e) {

            System.out.println("SOME EXCEPTION IN emailValidate >> " + e);
            return false;

        } finally {

            emailId = null;
        }

        return false;

    }

    public static boolean checkMaxStringLength(String str, int maxlength) {

        int strLength = str.length();

        if (strLength > maxlength) {

            return false;

        }
        return true;

    }

    public static boolean checkMinStringLength(String str, int minlength) {

        int strLength = str.length();

        if (strLength < minlength) {

            return false;

        }
        return true;

    }

    public boolean isValidContactNumber(String contactno) {
        // Remove any non-numeric characters
        String cleanedContactno = contactno.replaceAll("[^0-9]", "");

        // Check if the cleaned contact number has exactly 10 digits
        return cleanedContactno.length() == 10;
    }

}

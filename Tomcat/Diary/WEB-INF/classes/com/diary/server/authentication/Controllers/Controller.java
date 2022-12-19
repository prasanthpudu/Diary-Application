package com.diary.server.authentication.Controllers;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.diary.server.database.DataBase;
import com.diary.encryption.EncryptDecrypt;
import com.diary.encryption.Hash;
import com.diary.models.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
public class Controller {
    static DataBase connection = DataBase.getDataBaseConnection();
    private static Controller object = null;

    private Controller() {

    }

    public synchronized static Controller getController() {
        if (object == null) {
            object = new Controller();
        }
        return object;
    }

    public String register(String email, String password, String userName, String dateOfBirth, String location,
            String gender, String phoneNo, String securityquestion) throws Exception {

        Random random = new Random();
        List<String> params = new ArrayList<String>();
        String encryptedPassword = Hash.toSHA1(password,"SHA-1");
        int status = 200;
        String tableName = "users";
        List<String> columns = new ArrayList<String>();
        Map<String, String> contrains = new LinkedHashMap<String, String>();
        List<String> conditions = new ArrayList<String>();
        List<String> booleans = new ArrayList<String>();
        System.out.println("changed");
        String userId = email.substring(0, email.indexOf("@")) + (random.nextInt(100000) + 100000);
        params.add(userId);
        while (true) {
            columns.add("*");
            contrains.put("userid", connection.setString(userId));
            contrains.put("email", connection.setString(email));
            conditions.add("=");
            conditions.add("=");
            booleans.add("AND");
            ResultSet set = connection.get(tableName, columns, contrains, conditions, booleans, null);
            try {
                if (!set.next()) {
                    break;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            userId = email.substring(0, email.indexOf("@")) + (random.nextInt(100000) + 100000);
            System.out.println("userid " + userId);

        }
        List<String> values = new ArrayList<String>();
        values.add(connection.setString(email));
        values.add(connection.setString(encryptedPassword));
        values.add(connection.setString(userId));
        status = connection.Insert(tableName, null, values);

        if (status == 200) {
            values.clear();
            tableName = "userdetails";
            values.add(connection.setString(userName));
            values.add(connection.setString(dateOfBirth));
            values.add(connection.setString(location));
            values.add(connection.setString(gender));
            values.add(connection.setString(phoneNo));
            values.add(connection.setString(userId));
            connection.Insert(tableName, null, values);
            assainKey(password, securityquestion,userId);
            return userId;
        }
        return null;

    }
    public void assainKey(String password,String securityQuestion,String userId) throws Exception{
        String key = Hash.toSHA1(password, "MD5");
        String recoverKey = Hash.toSHA1(securityQuestion, "MD5");
       
        String encrytionKey = EncryptDecrypt.generateKey();
        String encryptedEncryptionKey= EncryptDecrypt.encrypt(encrytionKey, key);
        String recoveryEncryptionKey= EncryptDecrypt.encrypt(encrytionKey, recoverKey);
        System.out.println("key: " + key + " recover key: " + recoverKey+"encrytionKey"+encrytionKey);
        String tableName = "encryption";
        List<String> values = new ArrayList<String>();
        values.add(connection.setString(userId));
        values.add(connection.setString(encryptedEncryptionKey));
        values.add(connection.setString(recoveryEncryptionKey));
        connection.Insert(tableName, null, values);
    }
    public int check(String userId) {
        String tableName = "users";
        List<String> columns = new ArrayList<String>();
        Map<String, String> contrains = new LinkedHashMap<String, String>();
        List<String> conditions = new ArrayList<String>();
        List<String> booleans = new ArrayList<String>();
        columns.add("*");
        contrains.put("userid", connection.setString(userId));
        contrains.put("email", connection.setString(userId));
        conditions.add("=");
        conditions.add("=");
        booleans.add("OR");

        ResultSet set = connection.get(tableName, columns, contrains, conditions, booleans, null);
        try {
            if (set.next()) {
                return 200;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 404;
    }

    public String login(String userId, String password) {
        String tableName = "users";
        List<String> columns = new ArrayList<String>();
        Map<String, String> contrains = new LinkedHashMap<String, String>();
        List<String> conditions = new ArrayList<String>();
        List<String> booleans = new ArrayList<String>();
        columns.add("*");
        contrains.put("userid", connection.setString(userId));
        contrains.put("email", connection.setString(userId));
        conditions.add("=");
        conditions.add("=");
        booleans.add("OR");
        String encryptedPassword = Hash.toSHA1(password,"SHA-1");
        System.out.println("password encrypted" + encryptedPassword);
        ResultSet set = connection.get(tableName, columns, contrains, conditions, booleans, null);
        password = null;
        try {
            if (set.next()) {
                userId = set.getString(3);
                password = set.getString(2);
                if (password.equals(encryptedPassword)) {
                    return userId;
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int UpdateBio(User user) throws IOException {

        user.print();
        Map<String, String> updateColumns = new LinkedHashMap<String, String>();
        Map<String, String> constrains = new LinkedHashMap<String, String>();
        List<String> conditions = new ArrayList<String>();
        String tableName = "userdetails";
        updateColumns.put("name", user.getUserName());
        updateColumns.put("dateofbirth", user.getDateOfBirth());
        updateColumns.put("gender", user.getGender());
        updateColumns.put("location", user.getLocation());
        updateColumns.put("phoneno", user.getPhoneno());
        for (int i = 0; i < 5; i++) {
            conditions.add("=");
        }
        int status = connection.update(tableName, updateColumns, constrains, conditions, null);
        return status;
    }

    public String getBio(String userId) {
        ObjectMapper mapper = new ObjectMapper();
        String tableName = "userdetails";
        List<String> columns = new ArrayList<String>();
        Map<String, String> contrains = new LinkedHashMap<String, String>();
        List<String> conditions = new ArrayList<String>();
        columns.add("*");
        contrains.put("userid", connection.setString(userId));
        conditions.add("=");
        ResultSet set = connection.get(tableName, columns, contrains, conditions, null, null);
        Map<String, String> map = new LinkedHashMap<>();
        try {
            if (set.next()) {
                map.put("name", set.getString(1));
                map.put("dateofbirth", set.getString(2));
                map.put("location", set.getString(3));
                map.put("gender", set.getString(4));
                map.put("phoneno", set.getString(5));
            }
            return mapper.writeValueAsString(map);
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
        }
        return "[]";
    }

}

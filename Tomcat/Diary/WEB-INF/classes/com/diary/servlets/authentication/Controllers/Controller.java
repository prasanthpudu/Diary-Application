package com.diary.servlets.authentication.Controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.diary.connection.DataBase;

import com.diary.encryption.Hash;
import com.diary.models.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class Controller {
    static DataBase connection = DataBase.getDataBaseConnection();
    private static  Controller object = null;
    private Controller(){

    }
    public synchronized static Controller getController(){
                if(object == null){
                    object = new Controller();  
                }
        return object;
    }
    public  int register(HttpServletRequest request,HttpServletResponse response){
       
        Random random = new Random();
        List<String> params = new ArrayList<String>();
        
        params.add(request.getParameter("email"));
        String encryptedPassword = Hash.toSHA1(request.getParameter("password"));
        params.add(encryptedPassword);
        
        String query = "INSERT INTO users VALUES(?,?,?)";
        int status=200;
        String userId =params.get(0).substring(0,params.get(0).indexOf("@"))+(random.nextInt(100000)+100000);
        params.add(userId);
        while(true){
            
            query = "SELECT * FROM  users WHERE userid ='" + userId+"' OR email = '" + userId+"'"; 
           ResultSet set  = connection.get(query);
           try {
                if(!set.next()){
                    params.set(2, userId);
                    break;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
           userId =params.get(0).substring(0,params.get(0).indexOf("@"))+(random.nextInt(100000)+100000);
          System.out.println("userid "+userId);
           
        }
         query = "INSERT INTO users VALUES(?,?,?)";
        status= connection.insertParams(params, query);
        params.clear();
        params.add(request.getParameter("username"));
        params.add(request.getParameter("dateofbirth"));
        params.add(request.getParameter("location"));
        params.add(request.getParameter("gender"));
        params.add(request.getParameter("phoneno"));
        params.add(userId);
       
        if(status==200){
            query = "INSERT INTO userdetails VALUES(?,?,?,?,?,?)";
            connection.insertParams(params,query);
            SessionController.getSessionController().replaceCookie(request, response, params.get(5));
        }
        return status;
    }
    public  int login(HttpServletRequest request, HttpServletResponse response,String userId,String password){
        String query = "SELECT * FROM  users WHERE userid ='" + userId+"' OR email = '" + userId+"'"; 
        String encryptedPassword = Hash.toSHA1(password);
        System.out.println("password encrypted"+ encryptedPassword);
        ResultSet set = connection.get(query);
        password =null;
        try {
            if(set.next()){
                userId=set.getString(3);
               password = set.getString(2); 
               if(password.equals(encryptedPassword)){
                    SessionController.getSessionController().replaceCookie(request, response, userId);
                    return 200;
                }
               return 400;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 404;
    }
    
    public  int UpdateBio(HttpServletRequest request) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String body = reader.readLine();
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(body, User.class);
        user.print();
        String query = "UPDATE userdetails SET name="+(user.getUserName()!=null?"'"+user.getUserName()+"'":"name")+","+
                                            "dateofbirth="+(user.getDateOfBirth()!=null?"'"+user.getDateOfBirth()+"'":"dateofbirth")+","+
                                            "gender="+(user.getGender()!=null?"'"+user.getGender()+"'":"gender")+","+
                                            "location="+(user.getLocation()!=null?"'"+user.getLocation()+"'":"location")+","+
                                            "phoneno="+(user.getPhoneno()!=null?"'"+user.getPhoneno()+"'":"phoneno") +" WHERE userid='"+user.getUserId()+"'";
        System.out.println("query"+query);
        int status =connection.update(query);
        return status;
    }
    public  String getBio(String userId){
        String query = "SELECT * FROM userdetails where userid='"+userId+"'";
        System.out.println("query"+query);
        ResultSet set =  connection.get(query);
        ObjectMapper mapper = new ObjectMapper();
        Map<String,String> map = new HashMap<>();
       try {
        if(set.next()){
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

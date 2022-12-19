package com.diary.server.authentication.Controllers;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.diary.encryption.Hash;
import com.diary.server.database.DataBase;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class SessionController {
static DataBase connection = DataBase.getDataBaseConnection();
private static  SessionController object = null;
    private SessionController(){

    }
    public synchronized static SessionController getSessionController(){
                if(object == null){
                    object = new SessionController();  
                }
        return object;
    }
    public  String assignCookie(Cookie cookies[]) throws IOException{

        if(cookies==null){
            return "[]";
        }
        for(Cookie c :cookies){
                if(c.getName().equals("diarysession")){
                    String sessionId=c.getValue();
                    // session setting //
                    String tableName= "sessions";
                    List<String> columns= new ArrayList<String>();
                    Map<String,String> contrains = new LinkedHashMap<String,String>();
                    List<String> conditions= new ArrayList<String>();
                    columns.add("*");
                    contrains.put("sessionid",connection.setString(sessionId));
                    conditions.add("=");
                    ResultSet set = connection.get(tableName,columns,contrains,conditions,null,null);
                    try {
                        if(set.next()){
                            String userId = set.getString(1);
                            String resp = "{\"userId\":\""+userId + "\"}";
                            System.out.println("session setted sucessfully");
                            c.setMaxAge(60*30);
                            return resp;         
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                
                }
            
        }
        return "[]";
    }
    public  int removeCookie(Cookie cookies[]){
        int status=400;
        if(cookies!=null){
            for(Cookie c : cookies){
                if(c.getName().equals("diarysession")){
                    String sessionId=c.getValue();
                    String tableName= "sessions";
                    Map<String,String> contrains = new LinkedHashMap<String,String>();
                    List<String> conditions= new ArrayList<String>();
                    conditions.add("=");
                    contrains.put("sessionid", connection.setString(sessionId));
                    status=connection.delete(tableName,contrains,conditions,null);
                    break;
                }
            }
        }
        return status;
    }
    public  Cookie[] addCookie(String sessionId,String userId,String password){
        Cookie cookies[] = new Cookie[2];
        cookies[0] = new Cookie("diarysession",sessionId);
        cookies[0].setMaxAge(60*30);
        String key = Hash.toSHA1(password, "MD5");
        cookies[1] = new Cookie("key",key);
        cookies[1].setMaxAge(60*30); 
        String tableName= "sessions";
        List<String> values= new ArrayList<String>();
        values.add(connection.setString(userId));
        values.add(connection.setString(sessionId));
        values.add("localtimestamp");
        connection.Insert(tableName, null, values);
        System.out.println("session sesting");
        return cookies;
    }
}

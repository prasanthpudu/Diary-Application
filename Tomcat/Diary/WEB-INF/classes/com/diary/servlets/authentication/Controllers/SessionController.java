package com.diary.servlets.authentication.Controllers;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.diary.connection.DataBase;

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
    public  void replaceCookie(HttpServletRequest request,HttpServletResponse response, String userId){
            removeCookie(request);
            addCookie(request,response,userId);
    }
    public  int assignCookie(HttpServletRequest request,HttpServletResponse response) throws IOException{

        Cookie cookies[]=request.getCookies();
        if(cookies==null){
            response.setStatus(404);
            return 404;
        }
        for(Cookie c :cookies){
                if(c.getName().equals("diarysession")){
                    String sessionId=c.getValue();
                    // session setting //
                    String query ="SELECT  * FROM sessions WHERE sessionId='"+sessionId+"'";
                    ResultSet set = connection.get(query);
                    try {
                        if(set.next()){
                            String userId = set.getString(1);
                            String resp = "{\"userId\":\""+userId + "\"}";
                            System.out.println("session setted sucessfully");
                            response.getWriter().write(resp);   
                            return 200;         
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                
                }
            
        }
        return 404;
    }
    public  int removeCookie(HttpServletRequest request){
        Cookie cookies[] = request.getCookies();
        int status=400;
        if(cookies!=null){
            for(Cookie c : cookies){
                if(c.getName().equals("diarysession")){
                    String sessionId=c.getValue();
                    String query = "DELETE FROM  sessions WHERE sessionid='" + sessionId+"'";
                    status=connection.delete(query);
                    break;
                }
            }
        }
        return status;
    }
    private  void addCookie(HttpServletRequest request, HttpServletResponse response,String userId){
        HttpSession session = request.getSession();
        if(true){
            Cookie cookie = new Cookie("diarysession",session.getId());
            cookie.setMaxAge(60*60*24*30);
            response.addCookie(cookie);
            String query = "INSERT INTO sessions values(?,?)";
            List<String> params = new ArrayList<String>();
            params.add(userId);
            params.add(session.getId());
            connection.insertParams(params,query);
            System.out.println("session sesting");
        }
    }
}

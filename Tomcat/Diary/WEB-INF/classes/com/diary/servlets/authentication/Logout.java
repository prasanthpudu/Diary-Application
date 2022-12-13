package com.diary.servlets.authentication;
import java.io.IOException;

import com.diary.AppVariables;
import com.diary.connection.DataBase;
import com.diary.servlets.authentication.Controllers.SessionController;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
public class Logout extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DataBase connection = DataBase.getDataBaseConnection();
        resp.addHeader("Access-Control-Allow-Origin", AppVariables.HOST);
        resp.addHeader("Access-Control-Allow-Credentials", "true");   
       
                if(SessionController.getSessionController().removeCookie(req)==200){
                    Cookie cookie = new Cookie("diarysession","deleted");
                    cookie.setMaxAge(0);
                    resp.addCookie(cookie);
                    resp.getWriter().write("success");
                }
                else{
                    resp.setStatus(500);
                }
            }
}

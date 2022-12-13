package com.diary.servlets.authentication;
import com.diary.AppVariables;
import com.diary.servlets.authentication.Controllers.Controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
public class Login extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
       System.out.println(req.getSession()+":   session");
       String userId = req.getParameter("userid");
       String password = req.getParameter("password");
       resp.addHeader("Access-Control-Allow-Origin",AppVariables.HOST);
       resp.addHeader("Access-Control-Allow-Credentials", "true");
       int status = Controller.getController().login(req, resp, userId, password);
       resp.setStatus(status);
       if(status ==200){
        resp.getWriter().println("success");
        return;
       }
       resp.getWriter().println("failure");
    }
}

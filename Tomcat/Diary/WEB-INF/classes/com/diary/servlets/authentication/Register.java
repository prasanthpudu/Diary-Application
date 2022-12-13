package com.diary.servlets.authentication;
import java.io.BufferedReader;
import java.io.IOException;
import com.diary.AppVariables;
import com.diary.servlets.authentication.Controllers.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
public class Register extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,IOException{
        resp.addHeader("Access-Control-Allow-Origin", AppVariables.HOST);
        resp.addHeader("Access-Control-Allow-Credentials", "true");  
        System.out.println("executing request"); 
        int status = Controller.getController().register(req,resp);
        resp.setStatus(status);
        } 
    }
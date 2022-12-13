package com.diary.servlets.authentication;
import com.diary.AppVariables;
import com.diary.servlets.authentication.Controllers.SessionController;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
public class SessionRequest extends HttpServlet{
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("requesing");
        resp.addHeader("Access-Control-Allow-Origin", AppVariables.HOST);
        resp.addHeader("Access-Control-Allow-Credentials", "true");
        int status = SessionController.getSessionController().assignCookie(req, resp);
        resp.setStatus(status);
          
    }
}

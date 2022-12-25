package com.diary.clients.authentication;
import java.io.BufferedReader;
import java.io.IOException;
import com.diary.AppVariables;
import com.diary.server.authentication.Controllers.Controller;
import com.diary.server.authentication.Controllers.SessionController;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
public class Register extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,IOException{
        System.out.println("executing request"); 
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String username=req.getParameter("username");
        String dateOfBirth=req.getParameter("dateofbirth");
        String location=req.getParameter("location");
        String gender=req.getParameter("gender");
        String phoneno=req.getParameter("phoneno");
        String securityQuestion = req.getParameter("securityquestion");
        String userId=null;
        try {
            userId = Controller.getController().register(email, password, username, dateOfBirth, location, gender, phoneno,securityQuestion);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(userId!=null){
            SessionController.getSessionController().removeCookie(req.getCookies());
            Cookie cookies[] = SessionController.getSessionController().addCookie(req.getSession().getId(), userId,password);
            for (Cookie cookie : cookies) {
                resp.addCookie(cookie);
            }
           
            resp.getWriter().println("{\"userId\":\""+userId + "\"}");
            return;
        }else{
            resp.getWriter().write("[]");
        }
        } 
    }
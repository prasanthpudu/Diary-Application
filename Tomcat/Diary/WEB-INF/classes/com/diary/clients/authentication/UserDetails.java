package com.diary.clients.authentication;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.diary.models.User;
import com.diary.server.authentication.Controllers.Controller;
import com.diary.server.authentication.Controllers.SessionController;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
public class UserDetails extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,IOException{
        String type=req.getParameter("type");
        System.out.println(type);
        if(type.equals("getbio")){
            String  userId= req.getParameter("userid");
            resp.getWriter().write(Controller.getController().getBio(userId));
        }
        if(type.equals("updatebio")){
            BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()));
            String body = reader.readLine();
            System.out.println(body);
            ObjectMapper mapper = new ObjectMapper();
            User user = mapper.readValue(body, User.class);
            resp.setStatus(Controller.getController().UpdateBio(user));
        }
        if(type.equals("changepassword")){
            String securitykey = req.getParameter("securityquestion");
            String userId = req.getParameter("userid");
            String newPassword = req.getParameter("newpassword");
            if(validPassword(newPassword)){
                try {
                    String status = Controller.getController().changePassword(userId,securitykey,newPassword);
                    resp.getWriter().write(status);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                resp.getWriter().write("failed");
            }
           

        }
    }
    public boolean validPassword(String password){
        if(password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$")){
            return true;
        }
        return false;
    } 
   
}
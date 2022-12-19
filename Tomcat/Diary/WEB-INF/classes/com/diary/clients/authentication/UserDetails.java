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
            System.out.println("execuitng");
            BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()));
            String body = reader.readLine();
            ObjectMapper mapper = new ObjectMapper();
            User user = mapper.readValue(body, User.class);
            resp.setStatus(Controller.getController().UpdateBio(user));
        }
       
    } 
}
package com.diary.servlets.authentication;
import java.io.IOException;

import com.diary.AppVariables;
import com.diary.connection.DataBase;
import com.diary.servlets.authentication.Controllers.Controller;
import com.diary.servlets.authentication.Controllers.SessionController;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
public class GetBio extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DataBase connection = DataBase.getDataBaseConnection();
        resp.addHeader("Access-Control-Allow-Origin", AppVariables.HOST);
        resp.addHeader("Access-Control-Allow-Credentials", "true");   
        String  userId= req.getParameter("userid");
        resp.getWriter().write(Controller.getController().getBio(userId));

        }
}
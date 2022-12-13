package com.diary.servlets.operation;

import java.io.IOException;

import com.diary.AppVariables;
import com.diary.connection.DataBase;
import com.diary.servlets.operation.Controllers.SearchController;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LastTwo extends HttpServlet{
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("execuitng");
        resp.addHeader("Access-Control-Allow-Origin", AppVariables.HOST);
        resp.addHeader("Access-Control-Allow-Credentials", "true");
        String userId = req.getParameter("userid");
        String yesterday = req.getParameter("yesterday");
        String lastTwoDates = SearchController.getSearchController().getLastTwo(userId, yesterday);

        resp.getWriter().write(lastTwoDates);
    }
}

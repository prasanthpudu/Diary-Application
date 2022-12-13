package com.diary.servlets.operation;

import java.io.IOException;

import com.diary.AppVariables;
import com.diary.servlets.operation.Controllers.OptionController;

import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DeleteNote extends HttpServlet{
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("execuitng");
        resp.addHeader("Access-Control-Allow-Origin", AppVariables.HOST);
        resp.addHeader("Access-Control-Allow-Credentials", "true");
        String userId= req.getParameter("userid");
        String noteId=req.getParameter("id");
        resp.setStatus(OptionController.getOptionController().deleteNote(userId, noteId));
    }
}

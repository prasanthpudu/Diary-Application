package com.diary.clients.operation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import com.diary.models.Text;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.diary.server.operation.Controller;
import com.diary.server.operation.OptionController;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Option extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("servlet execuitng");
        String type = req.getParameter("type");
        if (type.equals("save")) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()));
            String body = reader.readLine();
            ObjectMapper mapper = new ObjectMapper();
            Cookie cookies[] = req.getCookies();
            if(cookies==null){
                resp.getWriter().write("unautherized request");
                return;
            }
            String key = null;
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("key")){
                    key=cookie.getValue();
                }
            }
            if(key==null){
                resp.getWriter().write("unautherized request");
                return;
            }
            Text json = mapper.readValue(body, Text.class);
            int status=400;
            try {
                String response= Controller.getController().doSave(json,key);
                resp.getWriter().write("\"result\":\""+response+"\"");

            } catch (Exception e) {
                e.printStackTrace();
            }
           
        }
        if (type.equals("delete")) {
            String userId = req.getParameter("userid");
            String noteId = req.getParameter("id");
            resp.setStatus(OptionController.getOptionController().deleteNote(userId, noteId));
        }
        System.out.println("execuitng");
        String userId = req.getParameter("userid");
        String noteid = req.getParameter("id");
        resp.setStatus(OptionController.getOptionController().starNote(userId, noteid));
    }
}

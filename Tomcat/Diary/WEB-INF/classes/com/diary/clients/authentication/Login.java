package com.diary.clients.authentication;

import java.io.IOException;

import com.diary.server.authentication.Controllers.Controller;
import com.diary.server.authentication.Controllers.SessionController;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Login extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(req.getSession() + ":   session");
        String userId = req.getParameter("userid");
        String type = req.getParameter("type");

        if (type.equals("check")) {
            int status = Controller.getController().check(userId);
            if (status == 200) {
                resp.setStatus(status);
                resp.getWriter().write("success");
            } else {
                resp.getWriter().write("notfound");
            }

        } else {
            String password = req.getParameter("password");
            System.out.println(password);
            userId = Controller.getController().login(userId, password);
            if (userId !=null) {
                SessionController.getSessionController().removeCookie(req.getCookies());
                Cookie cookies[] = SessionController.getSessionController().addCookie(req.getSession().getId(), userId,password);
                for (Cookie cookie : cookies) {
                    resp.addCookie(cookie);
                }
               
                resp.getWriter().println("success");
                return;
            }
            resp.getWriter().println("failure");
        }

    }
}

package com.diary.clients.authentication;
import com.diary.server.authentication.Controllers.SessionController;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
public class SessionRequest extends HttpServlet{
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String type = req.getParameter("type");
        if(type.equals("getsession")){
            String response= SessionController.getSessionController().assignCookie(req.getCookies());
            resp.getWriter().write(response);
        }
        if(type.equals("logout")){
            if(SessionController.getSessionController().removeCookie(req.getCookies())==200){
                Cookie cookie = new Cookie("diarysession","deleted");
                Cookie key = new Cookie("key","deleted");
                key.setMaxAge(0);
                cookie.setMaxAge(0);
                resp.addCookie(cookie);
                resp.addCookie(key);
                resp.getWriter().write("success");
            }
            else{
                resp.getWriter().write("failure");
            }
        }
    }
          
}

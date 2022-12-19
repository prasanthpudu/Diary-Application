package com.diary.clients.operation;

import java.io.IOException;

import com.diary.server.operation.SearchController;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Search extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("execuitng search");
        String type = req.getParameter("type");
        String userId = req.getParameter("userid");
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
        if (type.equals("lasttwodates")) {

            String yesterday = req.getParameter("yesterday");
            String lastTwoDates = SearchController.getSearchController().getLastTwo(userId, yesterday);
            resp.getWriter().write(lastTwoDates);
        }
        if (type.equals("searchbetween")) {
            String startDate = req.getParameter("start");
            String endDate = req.getParameter("end");
            resp.getWriter().write(SearchController.getSearchController().searchBetween(userId, startDate, endDate));
        }
        if (type.equals("stared")) {
            resp.getWriter().write(SearchController.getSearchController().SearchStared(userId));
        }
        if (type.equals("getnotes")) {
            String date = req.getParameter("date");
            type = req.getParameter("actiontype");
            try {
                String respone  = SearchController.getSearchController().getNotes(userId, date, type,key);
                resp.getWriter().write(respone);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        
        }

    }
}

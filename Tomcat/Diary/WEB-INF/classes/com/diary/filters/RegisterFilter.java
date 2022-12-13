package com.diary.filters;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import com.diary.AppVariables;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RegisterFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)throws IOException, ServletException {
        System.out.println("executing one");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        List<String> params = new ArrayList<String>();
        params.add(req.getParameter("username"));
        params.add(req.getParameter("password"));
        params.add(req.getParameter("dateofbirth"));
        params.add(req.getParameter("email"));
        params.add(req.getParameter("gender"));
        params.add(req.getParameter("location"));
        params.add(req.getParameter("phoneno"));
        int status = FilterController.validateRegister(params);
        System.out.println(status+" status="+status);
        if(status==200){
            System.out.println("executing if");
            chain.doFilter(request, response);
            return;
        }
        else
        {
            System.out.println("executing else");
            resp.addHeader("Access-Control-Allow-Origin", AppVariables.HOST);
            resp.addHeader("Access-Control-Allow-Credentials", "true");   
            resp.setStatus(400);
        }
    }
}

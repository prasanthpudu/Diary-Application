package com.diary.filters;
import com.diary.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)throws IOException, ServletException {
            HttpServletRequest req = (HttpServletRequest)request;
            HttpServletResponse resp = (HttpServletResponse)response;
           
            String userId = req.getParameter("userid");
            String password = req.getParameter("password");
            int status = FilterController.validateLogin(userId,password);
            if(status==200){
                chain.doFilter(request,response);
                return;
            }
          
            else 
            {
                
            }
    }
    
}

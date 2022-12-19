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
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
public class FileAccess implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)throws IOException, ServletException {
            HttpServletRequest req = (HttpServletRequest)request;
            HttpServletResponse resp = (HttpServletResponse)response;
            String requestUrl = req.getRequestURL().toString();
            
            System.out.println("requestUrl=" + requestUrl);
            Cookie[] cookies = req.getCookies();
            for(Cookie cookie : cookies) {
                System.out.println(cookie.getName());
            }
            if(requestUrl.matches("^(https?):localhost:4200\\/writer\\/.*$")){
          
                chain.doFilter(request,response);
                return;
            }
          
            else 
            {
                resp.getWriter().write("unauthorised");
            }
    }
    
}

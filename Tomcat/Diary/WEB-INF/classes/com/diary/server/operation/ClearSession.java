package com.diary.server.operation;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.diary.server.database.DataBase;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener()
public class ClearSession implements ServletContextListener {

@Override
public void contextInitialized(ServletContextEvent event) {
    DataBase connection = DataBase.getDataBaseConnection();
    System.out.println("############################");
   System.out.println("listener exeutied(");
   System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$");
   Timer timer = new Timer();
   Date firstTime = new Date(System.currentTimeMillis()+5000);
    timer.schedule(new TimerTask() {

        @Override
        public void run() {
            System.out.println("one");
            String tableName= "sessions";
            Map<String,String> contrains = new LinkedHashMap<String,String>();
            List<String> conditions= new ArrayList<String>();
            contrains.put("localtimestamp- time", connection.setString("00:30:00"));
            conditions.add(">");
            connection.delete(tableName, contrains, conditions, null);
            
        }
        
    },firstTime, 1000*60*60);
   
}

@Override
public void contextDestroyed(ServletContextEvent event) {
    System.out.println("listener ended");
}
}
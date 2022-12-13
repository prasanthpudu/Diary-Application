package com.diary.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


public class SessionDaso  {
   
    public String get(String select){
        try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/diary","postgres", "0"))
        {
            
            Statement statement = connection.createStatement();
            ResultSet set= statement.executeQuery(select);
            if(set.next()){
                return set.getString(0);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    
}

package com.diary.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.diary.encryption.Hash;
public class UsersDaos {
   
    public int insertUserDetails(List<String> params,String insert){
        try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/diary","postgres", "0"))
        {
            
            PreparedStatement statement = connection.prepareStatement(insert);
            int i=1;
            for (String parameter : params) {
                statement.setString(i,parameter);
                i++;
            }
            statement.executeUpdate();
            return 200;
        }
        catch(SQLException e){
            e.printStackTrace();
            return 400;
        }
        catch(Exception e){
            return 500;
        }
    }
    public String getUserByUserIdOrEmail(String select){
        try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/diary","postgres", "0"))
        {
            Statement statement = connection.createStatement();
           ResultSet set =  statement.executeQuery(select);
           if(set.next()){
               return set.getString(2);
           }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

package com.diary.connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Statement;

import java.util.List;


public class DataBase {
    private static DataBase  object=null;
   private DataBase(){
    }
    public synchronized static DataBase getDataBaseConnection(){

        if(object==null){
                object = new DataBase();           
        }
        return object;
    }
    static 
    {
        try {
            Class.forName("org.postgresql.Driver");
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }


    /// operation of database ---

    // insert 
    public int update(String update){
        try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/diary","postgres", "0"))
        {
            Statement statement = connection.createStatement();
            statement.execute(update);
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
    //delete
    public int delete(String delete){
       
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/diary","postgres", "0"))
        {
            Statement statement = connection.createStatement();
            statement.executeUpdate(delete);
        } catch (SQLException e) {
          e.printStackTrace();
          return 400;
        }
        return 200;
    }
    //  insert with params
    public int insertParams(List<String> params,String insert){
        try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/diary","postgres", "0"))
        {
            
            PreparedStatement statement = connection.prepareStatement(insert);
            int i=1;
            for (String parameter : params) {
                System.out.println("params");
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
    // get
    public ResultSet get(String select){
        try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/diary","postgres", "0"))
        {
            Statement statement = connection.createStatement();
           ResultSet set =  statement.executeQuery(select);
           return set;
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
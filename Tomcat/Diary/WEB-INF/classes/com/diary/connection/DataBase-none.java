package com.diary.connection;
import com.diary.models.User;
import com.diary.encryption.Hash;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.sql.Date;

public class DataBasedsjk {
    private static DataBase  object=null;
   private DataBase(){
    }
    public static DataBase getDataBaseConnection(){

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
    public int registerUser(User user){
       
        String insert= "INSERT INTO users values(?,?,?,?,?)";
        String key=null;
        try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/diary","postgres", "0"))
        {
            String hash=Hash.toSHA1(user.getPassword());
            PreparedStatement statement = connection.prepareStatement(insert);
            statement.setString(1, user.getUserId());
            statement.setString(2, user.getUserName());
            statement.setString(3, hash);
            statement.setString(4, user.getDateOfBirth());
            statement.setString(5, user.getLocation());
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
    public int checkCredential(String userId,String password){
       
        try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/diary","postgres", "0"))
        {
            String encryptedPassword  = Hash.toSHA1(password);
            String select= "SELECT * FROM users WHERE userid='"+userId+"' AND password='"+encryptedPassword+"'";
            System.out.println(select);
            Statement statement = connection.createStatement();
           ResultSet set =  statement.executeQuery(select);
           if(set.next()){
                return 200;
           }
           return 404;
        }
        catch(SQLException e){
            e.printStackTrace();
            return 400;
        }
        catch (Exception e) {
            e.printStackTrace();
            return 500;
        }
        
    }
    public boolean newActivity(String userId,Timestamp time){
        String insert= "INSERT INTO activity values(?,?,?);";
        String id=null;
        try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/diary","postgres", "0"))
        {
            PreparedStatement statement=connection.prepareStatement(insert);
            id= userId+time.toString();
            id = Base64.getEncoder().encodeToString(id.getBytes());
            statement.setString(1, id);
            statement.setString(2, userId);
            statement.setTimestamp(3, time);
            statement.executeUpdate();
           return true;
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
       
    }
    public boolean updateText(String text,String userId){
        String update = "INSERT INTO texts values(?,?,?,?) ON CONFLICT (id) DO UPDATE SET text=?";
        Date today = new Date(System.currentTimeMillis());
        String id= userId+today.toString();
        id = Base64.getEncoder().encodeToString(id.getBytes());
        try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/diary","postgres", "0"))
        {
            PreparedStatement statement = connection.prepareStatement(update);
            statement.setString(1, userId);
            statement.setDate(2, today);
            statement.setString(3, text);
            statement.setString(4, id);
            statement.setString(5, text);
            System.out.println(statement.toString());
            statement.executeUpdate();
      
            Timestamp time = new Timestamp(System.currentTimeMillis());
            newActivity(userId, time);

        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public String getText(String userId,Date date){
        try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/diary","postgres", "0"))
        {
            String id= userId+date.toString();
            id = Base64.getEncoder().encodeToString(id.getBytes());
            String select = "SELECT * FROM texts WHERE id ='"+id+"'";
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery(select);
            if(set.next()){
                String result = set.getString(3);
            return result;
            }
          
        }
        catch(Exception e){
            e.printStackTrace();
           
        }
        return null;
    }
    public String searchTextBetween(String userId,Date start,Date end){
        String search="SELECT * FROM texts where userid = ?  AND date >= ? AND date <= ?";
        try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/diary","postgres", "0"))
        {
            PreparedStatement statement = connection.prepareStatement(search);
           
            statement.setString(1, userId);
            statement.setDate(2, start);
            statement.setDate(3, end);
            System.out.println(statement.toString());
            ResultSet set= statement.executeQuery();
            ObjectMapper mapper = new ObjectMapper();
            ArrayNode array = mapper.createArrayNode();
            while(set.next()){
               
                ObjectNode node = mapper.createObjectNode();
                node.put("date",set.getDate(2).toString());
                String data = set.getString(3).replaceAll("<[^>]*>", "");
                if(data.length()>300){
                    data= data.substring(0,300);
                }
                
                node.put("data",data);
                array.add(node);
                
            }
            String json = mapper.writeValueAsString(array);
            return json;
            
        }
        catch(SQLException e){
            e.printStackTrace();

        } 
        catch (JsonProcessingException e) {
           
            e.printStackTrace();
        }
        return "not-found";
       
    }
    public boolean addSession(String session,String userId){
        try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/diary","postgres", "0"))
        {
            String insert = "INSERT INTO sessions values(?,?)";
            PreparedStatement statement = connection.prepareStatement(insert);
            statement.setString(1, userId);
            statement.setString(2, session);
            System.out.println(statement.toString());
            statement.executeUpdate();
            return true;
        }
        catch(SQLException e){
           return false;
        } 
      
    }
    public String getSession(String session){
        try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/diary","postgres", "0"))
        {
            String select = "select * from sessions WHERE sessionid='"+session+"'";
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery(select);
            if(set.next()){
                return set.getString(1);
            }
            
        }
        catch(SQLException e){
          e.printStackTrace();

        } 
        return null;
    }
    public int check(String userId){
        String query = "SELECT * FROM users WHERE userId ='"+userId+"'"; 
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/diary","postgres", "0"))
        {
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery(query);
            if(set.next()){
               return 400;
            }
        } catch (SQLException e) {
          e.printStackTrace();
          return 400;
        }
        return 200;
    }
    public int deleteSession(String sessionId){
        String query = "DELETE FROM sessions WHERE sessionid='"+sessionId+"'"; 
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/diary","postgres", "0"))
        {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
          e.printStackTrace();
          return 400;
        }
        return 200;
    }
}
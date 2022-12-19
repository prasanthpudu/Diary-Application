package com.diary.server.operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.diary.encryption.EncryptDecrypt;
import com.diary.server.database.DataBase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.servlet.http.HttpServletRequest;

public class SearchController {
    Controller decrypt = Controller.getController();
    private static  SearchController object = null;
    private  DataBase connection = DataBase.getDataBaseConnection();
    ObjectMapper mapper = new ObjectMapper();
    private SearchController(){

    }
    public synchronized static SearchController getSearchController(){
                if(object == null){
                    object = new SearchController();  
                }
        return object;
    }
    public String getLastTwo(String userId,String yesterday) throws JsonProcessingException{
        String tableName= "texts";
        List<String> columns= new ArrayList<String>();
        String optional = "ORDER BY date DESC";
        Map<String,String> contrains = new LinkedHashMap<String,String>();
        List<String> conditions= new ArrayList<String>();
        List<String> booleans= new ArrayList<String>();
        columns.add("DISTINCT Date(time)");
        contrains.put("userid",connection.setString(userId));
        contrains.put("Date(time)", connection.setString(yesterday));
        conditions.add("=");
        conditions.add("<");
        booleans.add("AND");
        
        // String query= "SELECT DISTINCT Date(time) FROM texts WHERE userid='"+userId+"' AND  Date(time) <'"+yesterday+"'  ORDER BY date DESC";
       ResultSet set =  connection.get(tableName, columns, contrains, conditions, booleans, optional);
       List<String> result = new ArrayList<String>();
       try {
        while(set.next()){
            result.add(set.getString(1));
           }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return mapper.writeValueAsString(result);
    }
    public String searchBetween(String userId, String startDate, String endDate){
        
        String tableName= "texts";
        List<String> columns= new ArrayList<String>();
        String optional = "ORDER BY date DESC";
        Map<String,String> contrains = new LinkedHashMap<String,String>();
        List<String> conditions= new ArrayList<String>();
        List<String> booleans= new ArrayList<String>();
        columns.add("DISTINCT Date(time)");
        contrains.put("userid",connection.setString(userId));
        contrains.put("Date(time)", connection.setString(startDate));
        contrains.put("Date(time)", connection.setString(endDate));
        conditions.add("=");
        conditions.add(">");
        conditions.add("<");
        booleans.add("AND");
        booleans.add("AND");

        // String query = "SELECT DISTINCT ON (Date(time)) * FROM texts WHERE userid = '"+ userId+"' AND Date(time) >'"+startDate+"' AND Date(time) < '"+endDate+"'  ORDER BY Date(time) DESC";
       
        ResultSet set= connection.get(tableName, columns, contrains, conditions, booleans, optional);
        ArrayNode array  = mapper.createArrayNode();
       
        try {
           
                while(set.next()){
                    ObjectNode node = mapper.createObjectNode();
    
                    node.put("title", set.getString(2));
                    node.put("time", set.getTimestamp(3).toString());
                    node.put("text", set.getString(4));
                    node.put("starred", ""+set.getBoolean(5)+"");
                    node.put("id",set.getString(6));
                    array.add(node);
                    
                }
                return mapper.writeValueAsString(array);
           
        } catch (SQLException | JsonProcessingException e) {

            e.printStackTrace();
        }
        return "[]";
    }
    public String SearchStared(String userId){
        String tableName= "texts";
        List<String> columns= new ArrayList<String>();
        String optional = "ORDER BY date DESC";
        Map<String,String> contrains = new LinkedHashMap<String,String>();
        List<String> conditions= new ArrayList<String>();
        List<String> booleans= new ArrayList<String>();
        columns.add("*");
        contrains.put("userid",connection.setString(userId));
        contrains.put("stared","true");
        conditions.add("=");
        conditions.add("=");
        booleans.add("AND");
        // String query = "SELECT * FROM texts WHERE userid = '" + userId+"' AND stared ORDER BY time DESC";
        // System.out.println(query+": get");
        ObjectMapper mapper = new ObjectMapper();
        ResultSet set= connection.get(tableName, columns, contrains, conditions, booleans, optional);
        ArrayNode array  = mapper.createArrayNode();
       
        try {
            while(set.next()){
                    ObjectNode node = mapper.createObjectNode();
    
                    node.put("title", set.getString(2));
                    node.put("time", set.getTimestamp(3).toString());
                    node.put("text", set.getString(4));
                    node.put("stared", ""+(set.getBoolean(5)?"true":"")+"");
                    node.put("id",set.getString(6));
                    array.add(node);
                   
                }
            return mapper.writeValueAsString(array);
        } catch (SQLException | JsonProcessingException e ) {

            e.printStackTrace();
        }
        return "[]";
    }
    public  String  getNotes(String userId,String date,String type,String key) throws Exception {
        String fullJoin= "texts FULL JOIN  medias on texts.id = medias.id";
        List<String> columns= new ArrayList<String>();
        String optional = "ORDER BY time DESC";
        Map<String,String> contrains = new LinkedHashMap<String,String>();
        List<String> conditions= new ArrayList<String>();
        List<String> booleans= new ArrayList<String>();
        columns.add("*");
        contrains.put("userid",connection.setString(userId));
        contrains.put("Date(time)",connection.setString(date));
        conditions.add("=");
        conditions.add("=");
        booleans.add("AND");
        // String query = "SELECT * FROM texts WHERE userid = '" + userId+"' AND Date(time)='"+date+"' ORDER BY time DESC";
        // System.out.println(query+": get");
        ObjectMapper mapper = new ObjectMapper();
        ResultSet set= connection.get(fullJoin, columns, contrains, conditions, booleans, optional);
        ArrayNode array  = mapper.createArrayNode();
       
        try {
            if(!set.isBeforeFirst()){
                ObjectNode node = mapper.createObjectNode();
                node.put("tittle", "");
                node.put("time", new Timestamp(System.currentTimeMillis()).toString());
                node.put("text", "");
                node.put("media","");
                node.put("stared", "");
                node.put("id","");
                array.add(node);
            }
            else
            {
                while(set.next()){
                    ObjectNode node = mapper.createObjectNode();
                    String enctryptedTitle = set.getString(2);
                    String enctryptedText= set.getString(4);
                    String enctryptedMedia = set.getString(8);
                    String title = decrypt.message(enctryptedTitle, key, userId, decrypt.DECRYPT_MODE);
                    String text = decrypt.message(enctryptedText, key, userId, decrypt.DECRYPT_MODE);
                    String media = decrypt.message(enctryptedMedia, key, userId, decrypt.DECRYPT_MODE);
                    System.out.println("title\n"+ title+ "text\n"+ text+ "media\n"+ media);
                    node.put("title", title);
                    node.put("time", set.getTimestamp(3).toString());
                    node.put("text",text);
                    node.put("media",media);
                    node.put("stared", ""+(set.getBoolean(5)?"true":"")+"");
                    node.put("id",set.getString(6));
                    array.add(node);
                   
                }
            }
            return mapper.writeValueAsString(array);
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return "[]";
    }
    
   
}

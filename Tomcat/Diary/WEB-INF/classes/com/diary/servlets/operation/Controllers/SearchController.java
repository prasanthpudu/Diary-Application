package com.diary.servlets.operation.Controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.diary.connection.DataBase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.servlet.http.HttpServletRequest;

public class SearchController {
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
        String query= "SELECT DISTINCT Date(time) FROM texts WHERE userid='"+userId+"' AND  Date(time) <'"+yesterday+"'  ORDER BY date DESC";
        System.out.println(query);
        
       ResultSet set =  connection.get(query);
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
    public String searchBetween(HttpServletRequest request){
        String userId=request.getParameter("userid");
        String startDate = request.getParameter("start");
        String endDate = request.getParameter("end");
        String query = "SELECT DISTINCT ON (Date(time)) * FROM texts WHERE userid = '"+ userId+"' AND Date(time) >'"+startDate+"' AND Date(time) < '"+endDate+"'  ORDER BY Date(time) DESC";
        System.out.println(query);
        ResultSet set= connection.get(query);
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
        String query = "SELECT * FROM texts WHERE userid = '" + userId+"' AND stared ORDER BY time DESC";
        System.out.println(query+": get");
        ObjectMapper mapper = new ObjectMapper();
        ResultSet set= connection.get(query);
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
   
}

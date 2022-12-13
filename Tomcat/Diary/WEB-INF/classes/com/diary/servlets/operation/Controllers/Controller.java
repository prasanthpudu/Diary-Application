package com.diary.servlets.operation.Controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.diary.connection.DataBase;
import com.diary.encryption.Hash;
import com.diary.models.Text;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.servlet.http.HttpServletRequest;

public class Controller {
    private static  Controller object = null;
    private Controller(){

    }
    public synchronized static Controller getController(){
                if(object == null){
                    object = new Controller();  
                }
        return object;
    }
    static DataBase connection = DataBase.getDataBaseConnection();
    public  String  getNotes(HttpServletRequest req) throws JsonProcessingException{
        String userId = req.getParameter("userid");
        String date =req.getParameter("date");
        String type = req.getParameter("type");
        String query = "SELECT * FROM texts WHERE userid = '" + userId+"' AND Date(time)='"+date+"' ORDER BY time DESC";
        System.out.println(query+": get");
        ObjectMapper mapper = new ObjectMapper();
        ResultSet set= connection.get(query);
        ArrayNode array  = mapper.createArrayNode();
       
        try {
            System.out.println(set.getFetchSize()+"size");
            if(type.equals("edit")&&!set.isBeforeFirst()){
                ObjectNode node = mapper.createObjectNode();
                node.put("tittle", "");
                node.put("time", new Timestamp(System.currentTimeMillis()).toString());
                node.put("text", "");
                node.put("stared", "");
                node.put("id","");
                array.add(node);
            }
            else
            {
                while(set.next()){
                    ObjectNode node = mapper.createObjectNode();
    
                    node.put("title", set.getString(2));
                    node.put("time", set.getTimestamp(3).toString());
                    node.put("text", set.getString(4));
                    node.put("stared", ""+(set.getBoolean(5)?"true":"")+"");
                    node.put("id",set.getString(6));
                    array.add(node);
                   
                }
            }
            return mapper.writeValueAsString(array);
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return null;
    }
    public  int doSave(HttpServletRequest req) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()));
        String body = reader.readLine();
        ObjectMapper mapper = new ObjectMapper();
        Text json = mapper.readValue(body,Text.class);
        
        String userId  = json.getUserId();
        String time = new Timestamp(System.currentTimeMillis()).toString();
        String id =  json.getId();
        String activityId = Hash.toSHA1(time+userId);
        int status;
       
            if(id==null){
                String query = "INSERT INTO activity VALUES('"+activityId+"','"+userId+"','"+time+"','"+activityId+"','create')";
                System.out.println("query :"+query);
                connection.update(query);
                status = createNew(json,time,activityId);
            }
           else{
            String query = "INSERT INTO activity VALUES('"+activityId+"','"+userId+"','"+time+"','"+id+"','edit')";
               
                System.out.println("query :"+query);
                connection.update(query);
                status = updateText(json,id);
           }
        if(status==200){
            return 200;
        }
        return 404;
    }

    public int createNew(Text text,String time,String id){
        String query ="INSERT INTO texts VALUES('"+text.getUserId()+"','"+text.getTitle()+"','"+time+"','"+text.getText()+"',"+false+",'"+id+"')";  
        System.out.println("query :"+query);  
        int status=connection.update(query);
        return status;
    }
    public int updateText(Text text,String id){
        String query ="UPDATE texts SET title='"+text.getTitle()+"',text='"+text.getText()+"' WHERE id='"+id+"'";  
        System.out.println("query :"+query);  
        int status=connection.update(query);
        return status;
    }
   
}

package com.diary.servlets.operation.Controllers;

import java.sql.Timestamp;

import com.diary.connection.DataBase;
import com.diary.encryption.Hash;

public class OptionController {
    DataBase connection = DataBase.getDataBaseConnection();
    private static  OptionController object = null;
    private OptionController(){

    }
    public synchronized static OptionController getOptionController(){
                if(object == null){
                    object = new OptionController();  
                }
        return object;
    }
    public int deleteNote(String userId,String noteId){
        String time = new Timestamp(System.currentTimeMillis()).toString();
        String activityId = Hash.toSHA1(time+userId);
        String query = "INSERT INTO activity VALUES('"+activityId+"','"+userId+"','"+time+"','"+noteId+"','delete')";
        System.out.println("query"+query);
        connection.update(query);
        query ="DELETE FROM texts WHERE userid ='"+userId+"' AND id='"+noteId+"'";  
        System.out.println("query :"+query);  
        int status=connection.update(query);
        return status;
    }
    public int starNote(String userId,String noteId){
        String query = "UPDATE texts SET stared = NOT stared WHERE userid ='"+userId+"' AND id='"+noteId+"'";
        System.out.println(query);
        int status = connection.update(query);
        return status;
    }
}

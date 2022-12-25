package com.diary.server.operation;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.diary.server.database.DataBase;
import com.diary.encryption.EncryptDecrypt;
import com.diary.encryption.Hash;

public class OptionController {
    
    DataBase connection = DataBase.getDataBaseConnection();
    private static OptionController object = null;

    private OptionController() {

    }

    public synchronized static OptionController getOptionController() {
        if (object == null) {
            object = new OptionController();
        }
        return object;
    }

    public int deleteNote(String userId, String noteId) {
        String time = new Timestamp(System.currentTimeMillis()).toString();
        String activityId = Hash.toSHA1(time + userId, "SHA-1");
        String tableName = "activity";
        List<String> values = new ArrayList<String>();
        Map<String, String> constrains = new LinkedHashMap<String, String>();
        List<String> conditions = new ArrayList<String>();
        List<String> booleans = new ArrayList<String>();
        values.add(connection.setString(activityId));
        values.add(connection.setString(userId));
        values.add(connection.setString(time));
        values.add(connection.setString(noteId));
        values.add(connection.setString("delete"));
        // String query = "INSERT INTO activity
        // VALUES('"+activityId+"','"+userId+"','"+time+"','"+noteId+"','delete')";
        // System.out.println("query"+query);
        connection.Insert(tableName, null, values);
        
        tableName = "medias";
        constrains.put("id", connection.setString(noteId));
    
        conditions.add("=");
        // query ="DELETE FROM texts WHERE userid ='"+userId+"' AND id='"+noteId+"'";
        // System.out.println("query :"+query);
        int status = connection.delete(tableName, constrains, conditions, booleans);
         status = connection.delete(tableName, constrains, conditions, booleans);

        return status;
    }

    public int starNote(String userId, String noteId) {
        String tableName = "texts";

        Map<String, String> constrains = new LinkedHashMap<String, String>();
        Map<String, String> updateColumns = new LinkedHashMap<String, String>();
        List<String> conditions = new ArrayList<String>();
        List<String> booleans = new ArrayList<String>();
        updateColumns.put("stared", "NOT stared");
        constrains.put("userid", connection.setString(userId));
        constrains.put("id", connection.setString(noteId));
        conditions.add("=");
        conditions.add("=");
        booleans.add("AND");
        // String query = "UPDATE texts SET stared = NOT stared WHERE userid
        // ='"+userId+"' AND id='"+noteId+"'";
        // System.out.println(query);
        int status = connection.update(tableName, updateColumns, constrains, conditions, booleans);
        return status;
    }
}

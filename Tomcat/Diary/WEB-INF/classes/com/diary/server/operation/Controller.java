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
import com.diary.models.Text;

public class Controller {
    public static final  String ENCRYPT_MODE = "encrypt";
    public static final String DECRYPT_MODE = "decrypt";

    private static Controller object = null;

    private Controller() {

    }

    public synchronized static Controller getController() {
        if (object == null) {
            object = new Controller();
        }
        return object;
    }

    static DataBase connection = DataBase.getDataBaseConnection();

    public String doSave(Text json, String key) throws Exception {

        String userId = json.getUserId();
        String time = new Timestamp(System.currentTimeMillis()).toString();
        String id = json.getId();
        String activityId = Hash.toSHA1(time + userId, "SHA-1");
        int status;
        String tableName = "activity";
        List<String> values = new ArrayList<String>();
        values.add(connection.setString(activityId));
        values.add(connection.setString(userId));
        values.add(connection.setString(time));
        if (id == null) {
            values.add(connection.setString(activityId));
            values.add(connection.setString("create"));

            // String query = "INSERT INTO activity
            // VALUES('"+activityId+"','"+userId+"','"+time+"','"+noteId+"','delete')";
            // System.out.println("query"+query);
            connection.Insert(tableName, null, values);
            // String query = "INSERT INTO activity VALUES('" + activityId + "','" + userId + "','" + time + "','"
            //         + activityId + "','create')";
            // System.out.println("query :" + query);
            status = createNew(json, time, activityId, key);
        } else {
            values.add(connection.setString(id));
            String query = "INSERT INTO activity VALUES('" + activityId + "','" + userId + "','" + time + "','" + id
                    + "','edit')";
            values.add(connection.setString("edit"));

            System.out.println("query :" + query);
            connection.Insert(tableName, null, values);
            status = updateText(json, id, key);
        }
        if (status == 200) {
            return "saved";
        }
        return "not-saved";
    }

    public int createNew(Text text, String time, String id, String key) throws Exception {
        String tableName = "texts";
        String titleEncrypted =message(text.getTitle(), key,text.getUserId(),ENCRYPT_MODE);
        String textEncrypted = message(text.getText(), key,text.getUserId(),ENCRYPT_MODE);
        String mediaEncypted = message(text.getMedia(), key,text.getUserId(),ENCRYPT_MODE);

        List<String> values = new ArrayList<String>();
        values.add(connection.setString(text.getUserId()));
        values.add(connection.setString(titleEncrypted));
        values.add(connection.setString(time));
        values.add(connection.setString(textEncrypted));

        values.add("false");
        values.add(connection.setString(id));

        // String query ="INSERT INTO texts
        // VALUES('"+text.getUserId()+"','"+text.getTitle()+"','"+time+"','"+text.getText()+"',"+false+",'"+id+"')";
        // System.out.println("query :"+query);
        int status = connection.Insert(tableName, null, values);
        values.clear();
        tableName = "medias";
        values.add(connection.setString(id));
        values.add(connection.setString(mediaEncypted));
        status = connection.Insert(tableName, null, values);
        return status;
    }

    public int updateText(Text text, String id, String key) throws Exception {
        String tableName = "texts";

        String titleEncrypted =message(text.getTitle(), key,text.getUserId(),ENCRYPT_MODE);
        String textEncrypted = message(text.getText(), key,text.getUserId(),ENCRYPT_MODE);
        String mediaEncypted = message(text.getMedia(), key,text.getUserId(),ENCRYPT_MODE);
        Map<String, String> updateColumns = new LinkedHashMap<String, String>();
        updateColumns.put("title", connection.setString(titleEncrypted));
        updateColumns.put("text", connection.setString(textEncrypted));

        Map<String, String> constrains = new LinkedHashMap<String, String>();
        constrains.put("id", connection.setString(id));
        List<String> conditions = new ArrayList<String>();
        conditions.add("=");
        // String query ="UPDATE texts SET
        // title='"+text.getTitle()+"',text='"+text.getText()+"' WHERE id='"+id+"'";
        // System.out.println("query :"+query);

        int status = connection.update(tableName, updateColumns, constrains, conditions, null);
        updateColumns.clear();
        tableName = "medias";
        updateColumns.put("media", connection.setString(mediaEncypted));
        status = connection.update(tableName, updateColumns, constrains, conditions, null);
        return status;
    }

    public static String message(String message, String key, String userId, String mode) throws Exception {
        String result = null, tableName = "encryption";
        List<String> columns = new ArrayList<String>();
        Map<String, String> constrains = new LinkedHashMap<String, String>();
        List<String> conditions = new ArrayList<String>();
        columns.add("*");
        constrains.put("userId",connection.setString(userId));
        conditions.add("=");
        ResultSet set = connection.get(tableName, columns, constrains, conditions, null, null);
        set.next();
        String encryptedKey = set.getString(2);
        System.out.println("encryptedKey: " + encryptedKey+"keu"+key);
        String secretKey = EncryptDecrypt.decrypt(encryptedKey, key);
        if (mode.equals("encrypt")) {
            result = EncryptDecrypt.encrypt(message, secretKey);

        } else if (mode.equals("decrypt")) {
            result = EncryptDecrypt.decrypt(message, secretKey);
        }
        return result;
    }
}

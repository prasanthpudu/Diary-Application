package com.diary.server.database;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

import java.util.List;
import java.util.Map;

public class DataBase {
    private static DataBase object = null;

    private DataBase() {
    }

    public synchronized static DataBase getDataBaseConnection() {

        if (object == null) {
            object = new DataBase();
        }
        return object;
    }

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /// operation of database ---

    // insert
    public String setString(String value){
        return "'"+value+"'";
    }
    public int Insert(String tableName, String[] columns, List<String> values) {
        String query = "INSERT INTO " + tableName;
        boolean first = true;
        if(columns!=null){
            query+="(";
            for (String column : columns) {
                if (first) {
                    query += column;
                    first = false;
                    continue;
                }
                query += "," + column;
            }
            query += ") ";
        }
        
       
       
       query+=" VALUES (";
        first = true;
        for (String value : values) {
            if (first) {
                query += value;
                first = false;
                continue;
            }
            query += "," + value;
        }
        query += ") ";
        System.out.println("statement: " + query);
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/diary", "postgres",
                "0")) {
            Statement statement = connection.createStatement();
            statement.execute(query);
            return 200;
        } catch (SQLException e) {
            e.printStackTrace();
            return 400;
        } catch (Exception e) {
            return 500;
        }
    }

    // delete
    public int delete(String tableName, Map<String, String> contrains, List<String> conditions, List<String> booleans) {
        String query = "DELETE FROM " + tableName;
        boolean first = true;
        int i = 0;
        for (Map.Entry<String, String> column : contrains.entrySet()) {
            if (first) {
                query += " WHERE " + column.getKey() + conditions.get(i) + column.getValue() + " "
                        + (booleans != null && booleans.size() > i ? " "+booleans.get(i)+" ":"");
               first=false;
            } else {
                query += " " + column.getKey() + conditions.get(i)+ column.getValue() + " "
                        + (booleans != null && booleans.size() > i ? " "+booleans.get(i)+" ":"");
            }
            i++;
        }
        System.out.println("statement: " + query);
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/diary", "postgres",
                "0")) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return 400;
        }
        return 200;
    }

    // delete
    public int update(String tableName, Map<String, String> updates, Map<String, String> contrains,
            List<String> conditions, List<String> booleans) {
        String query = " UPDATE " + tableName;
        boolean first = true;
        int i = 0;
        first = true;
        for (Map.Entry<String, String> column : updates.entrySet()) {
            if (first) {
                query += " SET " + column.getKey() + "=" + column.getValue();
                first=false;
            } else {
                query += " SET " + column.getKey() + "=" + column.getValue();
            }
            i++;
        }
        first = true;
        for (Map.Entry<String, String> column : contrains.entrySet()) {
            if (first) {
                query += " WHERE " + column.getKey() + conditions.get(i) + column.getValue() + " "
                        + (booleans != null && booleans.size() > i ?" "+booleans.get(i)+" ":"");
              
            } else {
                query += " " + column.getKey() + conditions.get(i)  + column.getValue() + " "
                        + (booleans != null && booleans.size() > i ? " "+booleans.get(i)+" ":"");
            }
            i++;
        }

        System.out.println("statement: " + query);
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/diary", "postgres",
                "0")) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return 400;
        }
        return 200;
    }

    // insert with param
    public ResultSet get(String tableName, List<String> columns, Map<String, String> contrains, List<String> conditions,
            List<String> booleans,String optional) {
        String query = "SELECT ";
        boolean first = true;
        int i = 0;
        for (String column : columns) {
            if (first) {
                query += column;
                continue;
            } else {
                query += "," + column;
            }
        }
        query += " FROM " + tableName;
        first = true;
        for (Map.Entry<String, String> column : contrains.entrySet()) {
           
            if (first) {
                query += " WHERE " + column.getKey() + conditions.get(i)  + column.getValue() + " "
                        + (booleans != null && booleans.size() > i ?" "+booleans.get(i)+" ":"");
                first=false;
               
            } else {
                query += " " + column.getKey() + conditions.get(i) + column.getValue() + " "
                        + (booleans != null && booleans.size() > i ?" "+booleans.get(i)+" ":"");
            }
            i++;
        }
        query+=(optional!=null?" "+optional+" ":"");
        System.out.println("statement: " + query);
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/diary", "postgres",
                "0")) {
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery(query);
            return set;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
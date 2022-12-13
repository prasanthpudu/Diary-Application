import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import com.diary.connection.DataBase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Controller {
    DataBase connection = DataBase.getDataBaseConnection();
    public void doOpertion(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()));
        String body = reader.readLine();
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("body  :"+body);
        JsonNode node =mapper.readTree(body);
        String userId = node.get("userid").asText();
        String type = node.get("type").asText();
        String data = node.get("data").asText();

        if(type.equals("upadate"))
        {
            Date date = new Date(Long.parseLong(data));
            String read = connection.getText(userId, date);
            if(read==null){
                resp.setStatus(404);
                return;
            }
            ObjectNode objNode = mapper.createObjectNode();
            objNode.put("data", read);
            resp.setStatus(200);
            resp.getWriter().write(mapper.writeValueAsString(objNode));
        }
        if(type.equals("update")){
            boolean update= connection.updateText(data, userId);
            if(update) resp.getWriter().write("success");
            else {
                resp.getWriter().write("failure");
                resp.setStatus(500);
            }
        }
    }
    public String doRead(String userId, String data) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Date date = new Date(Long.parseLong(data));
            String read = connection.getText(userId, date);
            if(read==null){
                return null;
            }
            ObjectNode objNode = mapper.createObjectNode();
            objNode.put("data", read);
         return mapper.writeValueAsString(objNode);
    }
   

}

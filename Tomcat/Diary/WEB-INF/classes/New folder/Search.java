import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.time.LocalDate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Search extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DataBase connection = DataBase.getDataBaseConnection();
        ObjectMapper mapper = new ObjectMapper();
        resp.addHeader("Access-Control-Allow-Origin", DataBase.host);
        resp.addHeader("Access-Control-Allow-Credentials", "true");
        BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()));
        String body = reader.readLine();
        System.out.println("body  :"+body);
        JsonNode node = mapper.readTree(body);
        String userId = node.get("userId").asText();
        String fromDate = node.get("fromDate").asText();
        String toDate = node.get("toDate").asText();
        if(!fromDate.equals("null") && !toDate.equals("null")){
            Date start = new Date(Long.parseLong(fromDate));
            Date end  = new Date(Long.parseLong(toDate));
            String json = connection.searchTextBetween(userId, start, end);
            resp.getWriter().write(json);
        }
        else{
            String byDate = node.get("byDate").asText();
            String lastDate = node.get("lastDate").asText();
            Date start = new Date(Long.parseLong(byDate));
            Date end  = new Date(Long.parseLong(lastDate));
            String json = connection.searchTextBetween(userId, start, end);
            resp.getWriter().write(json);
        }
    }
}

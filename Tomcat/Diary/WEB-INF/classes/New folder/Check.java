

import java.sql.Statement;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
public class Check  extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId= req.getParameter("userid");
        resp.addHeader("Access-Control-Allow-Origin", DataBase.host);
        resp.addHeader("Access-Control-Allow-Credentials", "true");
        DataBase connection = DataBase.getDataBaseConnection();
        int  status = connection.check(userId);
        resp.setStatus(status);
        resp.getWriter().write(status);
    }
}


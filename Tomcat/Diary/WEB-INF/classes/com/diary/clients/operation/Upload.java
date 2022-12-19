package com.diary.clients.operation;
import java.io.IOException;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import com.diary.server.operation.UploadController;

import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Upload extends HttpServlet{
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("calling upload");
        boolean isMultipart = ServletFileUpload.isMultipartContent(req);
        String userId = req.getParameter("userid");
        String type= req.getParameter("type");
        String date = req.getParameter("date");
        String fileType = req.getParameter("filetype");
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        if (isMultipart) {
            List<FileItem> items = upload.parseRequest(new ServletRequestContext(req));
            UploadController.getController().uploadFile(items,userId,type,date);
        }
    }
}

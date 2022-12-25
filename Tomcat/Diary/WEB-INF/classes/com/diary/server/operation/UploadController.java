package com.diary.server.operation;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.FileItem;

import com.diary.models.User;
import com.diary.server.authentication.Controllers.Controller;

public class UploadController {
    private static UploadController object = null;

    private UploadController() {

    }

    public synchronized static UploadController getController() {
        if (object == null) {
            object = new UploadController();
        }
        return object;
    }

    public void uploadFile(List<FileItem> items, String userId, String type,  String date) throws IOException {
        Iterator<FileItem> iterator = items.iterator();
        while (iterator.hasNext()) {
            FileItem item = (FileItem) iterator.next();
            if (!item.isFormField()) {
                String fileName = item.getName();
                String fileType = item.getContentType().substring(0,item.getContentType().indexOf("/"));
                System.out.println("type=" + fileType);
                File path = null;
                switch (type) {
                    case "profile":
                        User user = new User();
                        user.setUserId(userId);
                        user.setProfilePic("true");
                        path = new File("../webapps/Diary/assets/profile/"+fileName+".jpg");
                        Controller.getController().UpdateBio(user);
                        break;
                    case "media":
                        System.out.println("filetype"+fileType);
                        path = new File("../webapps/Diary/assets/" + userId + "/" + type + "/"+fileType+"/"
                                + date + "/" + fileName);
                        break;
                }
                if (!path.exists()) {
                    boolean status = path.mkdirs();
                }

                System.out.println(path.getAbsolutePath());
                try {
                    item.write(path);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

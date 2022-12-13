package com.diary.filters;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import com.diary.models.User;
public class FilterController {
    public static int validateLogin(String userId,String password) throws JsonProcessingException {
        //validating the inputs ---
        User user = new User();
       
        if(user.setPassword(password)){
            return 200;
        }
        user.print();
        return 400;
    }
    public static int validateRegister(List<String> details){
        System.out.println("executing user");
        User user = new User();
        if(user.setUserName(details.get(0))){
            System.out.println("1");
            if(user.setPassword(details.get(1))){
                System.out.println("2");
                if(user.setDateOfBirth(details.get(2))){
                    System.out.println("3");
                    if(user.setEmail(details.get(3))){
                        System.out.println("4");
                        user.setGender(details.get(4));
                        user.setLocation(details.get(5));
                        user.setPhoneno(details.get(6));
                        return 200;
                    }
                }
            }
        }
        user.print();
        return 400;
    }
}

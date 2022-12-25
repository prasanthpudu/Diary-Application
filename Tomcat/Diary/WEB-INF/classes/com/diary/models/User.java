package com.diary.models;
import java.sql.Date;
import java.time.LocalDate;

public class User {
    private String userId;
    private String userName;
    private LocalDate dateOfBirth;
    private String password;
    private String location;
    private String email;
    private String gender;
    private String phoneno;
    private String profilePic;
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public String getProfilePic() {
        return profilePic;
    }
    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
    public String getEmail() {
        return email;
    }
    public boolean setEmail(String email) {
        if(email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
            this.email = email;
            return true;
        }
        return false;
    }
    public String getGender() {
        return gender;
    }
    public boolean setGender(String gender) {
        if(gender.equalsIgnoreCase("male")||gender.equalsIgnoreCase("female")||gender.equalsIgnoreCase("other")){
            this.gender = gender;
            return true;
        }
        this.gender = null;
        return false;
    }
    public String getPhoneno() {
        return phoneno;
    }
    public boolean setPhoneno(String phoneno) {
        if(phoneno.matches("^\\d{5,12}$")){
            this.phoneno = phoneno;
            return true;
        }
        return false;
    }
    public String getUserId() {
        return userId;
    }
    public boolean setUserId(String userId) {
        this.userId=userId;
        return true;
    }
    public String getUserName() {
        return userName;
    }
    public boolean setUserName(String userName) {
        if(userName.matches("[a-zA-Z]+")){
            this.userName = userName;
            return true;
        }
        return false;
    }
    public String getDateOfBirth() {
        if(dateOfBirth==null){
            return null;
        }
        return dateOfBirth.toString();
    }
    public boolean setDateOfBirth(String dateOfBirth) {
        int day,month,year;
        try {
        year = Integer.parseInt(dateOfBirth.substring(0, 4));
        month = Integer.parseInt(dateOfBirth.substring(5, 7));
        day = Integer.parseInt(dateOfBirth.substring(8));

            this.dateOfBirth = LocalDate.of(year, month, day);
            LocalDate today = LocalDate.now();
            System.out.println(this.dateOfBirth.compareTo(today));
            if(this.dateOfBirth.compareTo(today)>0){
                this.dateOfBirth = null;
                return false;
            }
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
    public String getPassword() {
        return password;
    }
    public boolean setPassword(String password) {
        if(password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$")){
            this.password = password;
            return true;
        }
        return false;
    } 
    public boolean setLocation(String location) {
        if(location.matches("[a-zA-Z]+")){
            this.location = location;
            return true;
        }
        return false;
    }
    public String getLocation() {
        return location;
    }
    public void print(){
        System.out.println("name "+userName+" password "+password+" location "+location +"dateOfBirth  "+dateOfBirth+" location "+location+" phoneno "+phoneno);
    }
}

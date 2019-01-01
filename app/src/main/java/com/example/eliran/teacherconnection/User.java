package com.example.eliran.teacherconnection;

import java.util.ArrayList;

public class User {

    String name,lastname,email, phone, city,type,password;
    int accountType;

    public User(String name, String last, String username, String phone, String city, String type, String password, int accountType) {
        this.name = (""+name.charAt(0)).toUpperCase()+name.substring(1).toLowerCase();
        this.lastname = (""+last.charAt(0)).toUpperCase()+last.substring(1).toLowerCase();
        this.email = username;
        this.phone = phone;
        this.city = city;
        this.type = type;
        this.password = password;
        this.accountType = accountType;
    }

    public User(){

    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", last='" + lastname + '\'' +
                ", username='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", city='" + city + '\'' +
                ", type='" + type + '\'' +
                ", password='" + password + '\'' +
                ", accountType=" + accountType +
                '}';
    }

    public  String getUinfo(){return name+" "+lastname;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String username) {
        this.email = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }
}

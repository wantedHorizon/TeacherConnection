package com.example.eliran.teacherconnection;

import java.util.Date;

public class Cinfo {

    String user_info,uID,usrType, date;


    public Cinfo(String user_info, String uID, String usrType, String date) {
        this.user_info = user_info;
        this.uID = uID;
        this.usrType = usrType;
        this.date = date;
    }

    public Cinfo() {
    }

    public String getUser_info() {
        return user_info;
    }

    public void setUser_info(String user_info) {
        this.user_info = user_info;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getUsrType() {
        return usrType;
    }

    public void setUsrType(String usrType) {
        this.usrType = usrType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

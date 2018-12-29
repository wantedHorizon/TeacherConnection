package com.example.eliran.teacherconnection;

import android.app.Application;

import java.util.ArrayList;

public class ApplicationClass extends Application {
    public static ArrayList<User> users=new ArrayList<User>();
    @Override
    public void onCreate() {
        super.onCreate();

        //users=new ArrayList<User>();



    }

    public static ArrayList<User> filterByID(int id){

        return getUsersFilter(users.get(id).type,users.get(id).city);



    }

    public static ArrayList<User> getUsersFilter(String type, String city)

    {
        User x;
        ArrayList<User> list=new ArrayList<>();
        for (int i = 0; i <users.size() ; i++) {
            x=users.get(i);
            if(!x.getType().equals(type)&&x.getCity().equals(city))
                list.add(x);
        }

        return list;
    }

   public static void  addUser(User u){
        users.add(u);

   }


}

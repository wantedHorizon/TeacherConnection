package com.example.eliran.teacherconnection;

import android.app.Application;

import java.util.ArrayList;

public class ApplicationClass extends Application {
    public static ArrayList<User> users=new ArrayList<User>();
    @Override
    public void onCreate() {
        super.onCreate();

        //users=new ArrayList<User>();
        users.add(new User("eliran ","amzalag","eliran1994","0503050360","tel aviv","Student","1234",1));
        users.add(new User("eliran"," amzalag","eliran1994","0503050360","tel aviv","Student","1234",1));

        users.add(new User("eli ","cohen","eliran1994","0503050360","tel aviv","Teacher","1234",0));

        users.add(new User("eliran"," amzalag","eliran1994","0503050360","tel aviv","Student","1234",1));
        users.add(new User("me","ss","x","0503050360","tel aviv","Teacher","1111",0));

        users.add(new User("eliran ","amzalag","eliran1994","0503050360","tel aviv","Student","1234",1));
        users.add(new User("mosh","aviv","eliran1994","0503050360","tel aviv","Student","1234",1));


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

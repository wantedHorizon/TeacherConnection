package com.example.eliran.teacherconnection;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class TeacherConnection extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

      //  FirebaseDatabase.getInstance().setPersistenceEnabled(true );
    }
}

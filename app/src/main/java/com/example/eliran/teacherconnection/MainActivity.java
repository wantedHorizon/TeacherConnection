package com.example.eliran.teacherconnection;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.SupportActionModeWrapper;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity   {

    TextView et1,et2;
    Button login ,register,exx;
    Toast t;
    Toolbar toolbar1;
    private FirebaseAuth mAuth;//fire base auth
    ImageView  picHat;

    String idCheck;
    public static  final String MY_PREF_FILENAME="com.example.eliran.teacherconnection.DATA";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);
        toolbar1=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar1);
        getSupportActionBar().setTitle("Home");

        SharedPreferences editor =getSharedPreferences(MY_PREF_FILENAME,MODE_PRIVATE);
        idCheck=editor.getString("user","-1");
        Toast.makeText(this,"user:"+idCheck,Toast.LENGTH_SHORT).show();
        picHat=findViewById(R.id.picHat);
        picHat.setColorFilter(getResources().getColor(R.color.lightBlue));

/*
        if(Integer.parseInt(idCheck)>-1){

            Intent intent = new Intent(MainActivity.this,
                    com.example.eliran.teacherconnection.Workspace.class  );

            intent.putExtra("userID",Integer.parseInt(idCheck) );
            startActivity(intent);

        }*/



            exx=findViewById(R.id.exx);
        exx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = getSharedPreferences(MY_PREF_FILENAME, MODE_PRIVATE).edit();

                editor.putString("user", "-1");
                editor.commit();

                FirebaseAuth.getInstance().signOut();



            }
        });
        et1=findViewById(R.id.tvTitle);
        et2=findViewById(R.id.tvLogReg);
        t.makeText(this,"login",Toast.LENGTH_SHORT);

        login=findViewById(R.id.btnLogin_main);
        register=findViewById(R.id.btnRegister_main);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               // Intent intent = new Intent(MainActivity.this,com.example.eliran.teacherconnection.Conniction.MyConnections.class );
             //   startActivity(intent);
                Intent intent = new Intent(MainActivity.this,com.example.eliran.teacherconnection.Main2Login.class  );
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent = new Intent(MainActivity.this,
                        com.example.eliran.teacherconnection.Main2Register.class  );
                startActivity(intent);
            }
        });

    }


}

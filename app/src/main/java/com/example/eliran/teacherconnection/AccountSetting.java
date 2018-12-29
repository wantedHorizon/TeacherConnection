package com.example.eliran.teacherconnection;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class AccountSetting extends AppCompatActivity {
    EditText firstname , lastname , phone ,email,pass;
    String Sname,Slastname,Sphone,Semail,Spass,Scity,Stype;
    private  DatabaseReference mUserDatabase;
    private FirebaseUser  mCurrentUser;
    TextView txt22;
    public  ArrayList<User> users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting2);

        firstname=findViewById(R.id.etName_setting);
        lastname=findViewById(R.id.etSettingLastname);
        phone=findViewById(R.id.etSettingPhone);
        email=findViewById(R.id.etSettingMail);
        pass=findViewById(R.id.etSettingPASS);
       users =new ArrayList<>();
       txt22=findViewById(R.id.txt22);

        mCurrentUser=FirebaseAuth.getInstance().getCurrentUser();
        String cUID=mCurrentUser.getUid();
        mUserDatabase=FirebaseDatabase.getInstance().getReference().child("users")/*.child(cUID)*/;
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                /*

                Sname=dataSnapshot.child("name").getValue().toString();
                Slastname=dataSnapshot.child("lastname").getValue().toString();
                Spass=dataSnapshot.child("password").getValue().toString();
                Sphone=dataSnapshot.child("phone").getValue().toString();
                Semail=dataSnapshot.child("email").getValue().toString();
              //  Sphone=dataSnapshot.child("phone").getValue().toString();*/

                String str1="";

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                  //str1+=snapshot.getKey().toString()+" ||";

                    Sname=snapshot.child("name").getValue().toString();
                    Slastname=snapshot.child("lastname").getValue().toString();
                    Spass=snapshot.child("password").getValue().toString();
                    Sphone=snapshot.child("phone").getValue().toString();
                    Semail=snapshot.child("email").getValue().toString();
                  //  Scity=snapshot.child("city").getValue().toString();
                  // Stype= snapshot.child("type").getValue().toString();
                    User x= new User(Sname,Slastname,Semail,Sphone,"tel aviv","Teacher","",0);
                    users.add(x);
                   // str1+=x.toString();


                }
                ApplicationClass.users=users;
             //   txt22.setText(str1);



                    Intent intent = new Intent(AccountSetting.this,
                            com.example.eliran.teacherconnection.Workspace.class  );
                    startActivity(intent);



              /*  firstname.setText(Sname);
                lastname.setText(Slastname);
                pass.setText(Spass);
                phone.setText(Sphone);
                email.setText(Semail);*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });








    }
}

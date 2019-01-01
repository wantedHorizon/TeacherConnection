package com.example.eliran.teacherconnection;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import java.util.jar.Attributes;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class AccountSetting extends AppCompatActivity {
    public static final String MY_PREF_FILENAME = "com.example.eliran.teacherconnection.DATA";

    EditText firstname , lastname , phone ,city,pass;
    String Sname,Slastname,Sphone,Semail,Spass,Scity,Stype;
    private  DatabaseReference mUserDatabase;
    private FirebaseUser  mCurrentUser;
    TextView txt22;
    Boolean math=false,eng=false,sch=false;
    CheckBox cbMath,cbEng,cbSch;
    Button btnApply;
    private FirebaseUser current_user;

    public  ArrayList<User> users;

    Toolbar toolbar1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting2);

         toolbar1 = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar1);
        getSupportActionBar().setTitle("Account Setting");

        firstname=findViewById(R.id.etName_setting);
        lastname=findViewById(R.id.etSettingLastname);
        phone=findViewById(R.id.etSettingPhone);
        city=findViewById(R.id.etSettingCity);
        pass=findViewById(R.id.etSettingPASS);
        cbMath=findViewById(R.id.cbMath);
        cbEng=findViewById(R.id.cbEng);
        cbSch=findViewById(R.id.cbSci);
        btnApply=findViewById(R.id.btnApply);

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cbMath.isChecked())
                    math=true;

                else math=false;

                if(cbEng.isChecked())
                    eng=true;

                else eng=false;

                if(cbSch.isChecked())
                    sch=true;

                else sch=false;

                Toast.makeText(AccountSetting.this,math+" "+eng+" "+sch+" ",Toast.LENGTH_SHORT).show();

                Sname=firstname.getText().toString().trim().toLowerCase();
                Slastname=lastname.getText().toString().toLowerCase();
                Sphone=phone.getText().toString().trim().toLowerCase();
                Scity=city.getText().toString().trim().toLowerCase();

                if(Scity.isEmpty() ||Slastname.isEmpty()||Sphone.isEmpty()||Sname.isEmpty())
                    Toast.makeText(AccountSetting.this,"Error please fill all parts",Toast.LENGTH_SHORT).show();

                else if (sch==false&&math==false&&eng==false)
                    Toast.makeText(AccountSetting.this,"please choose one subject",Toast.LENGTH_SHORT).show();

                else
                    update(Sname,Slastname,Sphone,Scity,math,eng,sch);






            }
        });






       //users =new ArrayList<>();
       txt22=findViewById(R.id.txt22);

        mCurrentUser=FirebaseAuth.getInstance().getCurrentUser();
        String cUID=mCurrentUser.getUid();
        mUserDatabase=FirebaseDatabase.getInstance().getReference().child("users").child(cUID);
        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Sname=dataSnapshot.child("name").getValue().toString();
                Slastname=dataSnapshot.child("lastname").getValue().toString();
                Spass=dataSnapshot.child("password").getValue().toString();
                Sphone=dataSnapshot.child("phone").getValue().toString();
                Scity=dataSnapshot.child("city").getValue().toString();
                Sphone=dataSnapshot.child("phone").getValue().toString();

                firstname.setText(Sname);
                lastname.setText(Slastname);
                phone.setText(Sphone);
                city.setText(Scity);
                pass.setText(Spass);


                //String str1="";

                //for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                  //str1+=snapshot.getKey().toString()+" ||";
/*
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
              //  ApplicationClass.users=users;*/
             //   txt22.setText(str1);


/*
                    Intent intent = new Intent(AccountSetting.this,
                            com.example.eliran.teacherconnection.Workspace.class  );
                    startActivity(intent);*/



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








    }// end OnCREATE



    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu, menu );
        menu.findItem(R.id.menuSettingAccount).setVisible(false);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId()==R.id.menuLogoutBTN){
            SharedPreferences.Editor editor = getSharedPreferences(MY_PREF_FILENAME, MODE_PRIVATE).edit();

            editor.putString("user", "-1");
            editor.putString("type","-1");
            editor.commit();

            FirebaseAuth.getInstance().signOut();

            Intent intent = new Intent(com.example.eliran.teacherconnection.AccountSetting.this,
                    com.example.eliran.teacherconnection.MainActivity.class);
            startActivity(intent);
            finish();

        }
        else if(item.getItemId()==R.id.menuHomeBTN) {
            Intent intent = new Intent(com.example.eliran.teacherconnection.AccountSetting.this,
                    com.example.eliran.teacherconnection.Workspace.class);
            startActivity(intent);

        }

        else if( item.getItemId()==R.id.menuMyConnections){
            Intent intent = new Intent(com.example.eliran.teacherconnection.AccountSetting.this,
                    com.example.eliran.teacherconnection.Conniction.MyConnections.class);
            startActivity(intent);


        }

        return  true;
    }


    public void update(String name,String last,String phone,String city ,boolean math,boolean eng ,boolean sch){


        current_user=FirebaseAuth.getInstance().getCurrentUser();
        String cuID=current_user.getUid();
        //Toast.makeText(Main2Register.this,cuID+" --"+email,Toast.LENGTH_SHORT).show();
        mUserDatabase=FirebaseDatabase.getInstance().getReference().child("users").child(cuID);
        mUserDatabase.child("city").setValue(city);
        mUserDatabase.child("name").setValue(name);
        mUserDatabase.child("lastname").setValue(last);
        mUserDatabase.child("phone").setValue(phone);

        mUserDatabase.child("sub").child("math").setValue(math);
        mUserDatabase.child("sub").child("eng").setValue(math);
        mUserDatabase.child("sub").child("sci").setValue(math);

        Intent intent = new Intent(com.example.eliran.teacherconnection.AccountSetting.this
                ,com.example.eliran.teacherconnection.Workspace.class  );
        startActivity(intent);


    }


    public void setUsertype(String type){

        SharedPreferences.Editor editor =getSharedPreferences(MY_PREF_FILENAME,MODE_PRIVATE).edit();
        // editor.putString("user",ss);


        editor.putString("type",type);
        editor.commit();

    }
}

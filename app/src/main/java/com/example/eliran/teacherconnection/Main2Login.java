package com.example.eliran.teacherconnection;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

public class Main2Login extends AppCompatActivity {

    Button btnLogin ;
    //progress dialog
    ProgressDialog mRegProgress;
    EditText user,password;
    String Suser,Spass;
    private FirebaseAuth mAuth;//fire base auth
    DatabaseReference mUserDatabase;
    String idCheck,type;//Local LOgin
    public static  final String MY_PREF_FILENAME="com.example.eliran.teacherconnection.DATA";

   // private ProgressDialog mLoginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_login);

        //checks if already connected

        SharedPreferences editor =getSharedPreferences(MY_PREF_FILENAME,MODE_PRIVATE);
        idCheck=editor.getString("user","-1");
        Toast.makeText(this,idCheck,Toast.LENGTH_SHORT).show();

        mAuth = FirebaseAuth.getInstance();
        mUserDatabase=FirebaseDatabase.getInstance().getReference().child("users");
/*

        if(Integer.parseInt(idCheck)>-1){

            Intent intent = new Intent(com.example.eliran.teacherconnection.Main2Login.this,
                    com.example.eliran.teacherconnection.Workspace.class  );

            intent.putExtra("userID",Integer.parseInt(idCheck) );
            startActivity(intent);

        }*/

        user=findViewById(R.id.etUsername);
        password=findViewById((R.id.etPassword));

        btnLogin=findViewById(R.id.btnLogin_loginPage);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Suser=user.getText().toString().trim();
                Spass=password.getText().toString().trim();

                if(Suser.isEmpty()||Spass.isEmpty()){
                    Toast.makeText(Main2Login.this,"Please fill all fields",Toast.LENGTH_LONG).show();

                    //sign in online methood



                }

                else {
                    mRegProgress=new ProgressDialog(Main2Login.this);
                    mRegProgress.setTitle("Signing In");
                    mRegProgress.setMessage("Please wait while we are login you in ");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();

                    mAuth.signInWithEmailAndPassword(Suser, Spass)
                            .addOnCompleteListener(Main2Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information



                                        String devToken=FirebaseInstanceId.getInstance().getToken();
                                        String currentUser=mAuth.getUid();
                                        mUserDatabase.child(currentUser).child("device_token").setValue(devToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                String ss=mAuth.getUid().toString();
                                                SharedPreferences.Editor editor =getSharedPreferences(MY_PREF_FILENAME,MODE_PRIVATE).edit();
                                                editor.putString("user",ss);

                                                findType();
                                                //  editor.putString("type","");
                                                editor.commit();
                                                Toast.makeText(Main2Login.this, "signInWithEmail:success",Toast.LENGTH_SHORT).show();
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                Intent intent = new Intent(com.example.eliran.teacherconnection.Main2Login.this,
                                                        com.example.eliran.teacherconnection.Workspace.class);

                                                //  intent.putExtra("userID", x);
                                                mRegProgress.dismiss();
                                                startActivity(intent);
                                                finish();

                                            }
                                        });



                                        // updateUI(user);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        // Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        mRegProgress.hide();
                                        Toast.makeText(Main2Login.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        //updateUI(null);
                                    }

                                    // ...
                                }
                            });







                    //old Local login check
                   /* int x=checkLogin(Suser,Spass);

                    if(x==-1){
                      //  Toast.makeText(Main2Login.this,"login failure  "+x,Toast.LENGTH_LONG).show();

                    }
                    else{/*
                        Intent intent = new Intent(Main2Login.this,
                                com.example.eliran.teacherconnection.Workspace.class  );
                        intent.putExtra("type", x.getType());
                        intent.putExtra("city",x.getCity());
                        intent.putExtra("user",Suser);
                        intent.putExtra("pass",Spass);
                        intent.putExtra("name",x.getFullname());
                        startActivity(intent);*/
                       // Toast.makeText(Main2Login.this,"Login succesful:"+Suser+";"+Spass,Toast.LENGTH_LONG).show();

                /*        SharedPreferences.Editor editor =getSharedPreferences(MY_PREF_FILENAME,MODE_PRIVATE).edit();
                        editor.putString("user",""+x);
                        editor.commit();
                        Intent intent = new Intent(com.example.eliran.teacherconnection.Main2Login.this,
                                com.example.eliran.teacherconnection.AccountSetting.class);

                        intent.putExtra("userID", x);
                        startActivity(intent);
                        finish();



                    }*/

                }
            }
        });


    }

    public void findType(){

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String cUID = mCurrentUser.getUid().toString();
        // this.id = cUID;
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //String type;

        if (currentUser != null) {
            DatabaseReference mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(cUID);

            mUserDatabase.addValueEventListener(new ValueEventListener() {




                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                     type=dataSnapshot.child("type").getValue().toString()+"";
                    SharedPreferences.Editor editor =getSharedPreferences(MY_PREF_FILENAME,MODE_PRIVATE).edit();
                   // editor.putString("user",ss);


                    editor.putString("type",type);
                    editor.commit();




                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    databaseError.toString();

                }
            });



        }

    }

    public void onStart() {
        super.onStart();


        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //
        if(currentUser!=null){//if user is loged out

            Intent intent =new Intent(Main2Login.this,
                    com.example.eliran.teacherconnection.Workspace.class);// need to be changed to workspace

                startActivity(intent);
                finish();
        }


       // updateUI(currentUser);*/
    }



    public   int checkLogin(String username,String password){
        User x;

        for (int i = 0; i <ApplicationClass.users.size()-1 ; i++) {
            x =ApplicationClass.users.get(i);
            System.out.println(x);
            if( (username.equals(x.getEmail()))    &&password.equals(x.password))
                return i;

        }


        return  -1;


    }
}

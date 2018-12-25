package com.example.eliran.teacherconnection;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Main2Activity extends AppCompatActivity {
    Button addUser;

    private DatabaseReference mDatabase;
    EditText email,pass;

    private FirebaseAuth mAuth;
    private  FirebaseAuth.AuthStateListener authStateListener;

    public static  final  String [] types={"Teacher","Student"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        email=findViewById(R.id.email);
        addUser=findViewById(R.id.addUser);
        pass=findViewById(R.id.pass);
        mAuth = FirebaseAuth.getInstance();

        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mAuth.createUserWithEmailAndPassword(email.getText().toString().trim(), pass.getText().toString().trim())
                        .addOnCompleteListener(Main2Activity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(Main2Activity.this,"REG sucses",Toast.LENGTH_SHORT).show();
                                        //FirebaseUser user = mAuth.getCurrentUser();
                                  //  updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast.makeText(Main2Activity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }

                                // ...
                            }
                        });


            }
        });





        // mDatabase = FirebaseDatabase.getInstance().getReference();

        /*
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mDatabase.child("Accounts").setValue(etbb.getText().toString().trim());
                }
        });*/




    }



    @Override
    public void onStart() {
        super.onStart();

        /*
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //
        if(currentUser==null){//if user is loged out

            Intent intent =new Intent(Main2Activity.this,
                    com.example.eliran.teacherconnection.Main2Login.class);

                startActivity(intent);
                finish();
        }
       // updateUI(currentUser);*/
    }










    private void writeNewUser(String userId, String name, String email) {
      User user=new   User("mosh","aviv","eliran1994","0503050360","tel aviv","Student","1234",1);

        mDatabase.child("users").child(userId).setValue(user);
    }
}

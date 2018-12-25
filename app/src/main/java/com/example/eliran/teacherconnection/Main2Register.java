package com.example.eliran.teacherconnection;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


public class Main2Register extends AppCompatActivity {
    AutoCompleteTextView regType;
    ImageView arrow;
    EditText regFirst,regLast, regPass,regCity,regEmail,regPhone;
    public static  final  String [] types={"Teacher","Student"};
    private FirebaseAuth mAuth;//fire base auth
    private  FirebaseAuth.AuthStateListener authStateListener;

    Button btnReg1;
    String name="",pass="",city="",type="",email="",phone="",last="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_register);
        //check login
        mAuth = FirebaseAuth.getInstance();


        regFirst=findViewById(R.id.regFirstname);
        regLast=findViewById(R.id.regLastname);
        regCity=findViewById(R.id.regCity);
        regPass=findViewById(R.id.regPass);
        regType=findViewById(R.id.regType);
        regEmail=findViewById(R.id.regEmail);
        regPhone=findViewById(R.id.regPhone);
        arrow=findViewById(R.id.iwArrow);

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,types);
        regType.setAdapter(arrayAdapter);

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regType.showDropDown();
            }
        });

        regType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regType.showDropDown();
            }
        });


        btnReg1=findViewById(R.id.btnReg1);



        btnReg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=regFirst.getText().toString().trim();
                last=regLast.getText().toString().trim();
                pass=regPass.getText().toString().trim();
                type=regType.getText().toString().trim();
                city=regCity.getText().toString().trim();
                email=regEmail.getText().toString().trim();
                phone=regPhone.getText().toString().trim();


                if(name.isEmpty()||last.isEmpty()||pass.isEmpty()||type.isEmpty()||city.isEmpty()||email.isEmpty()||phone.isEmpty()){
                    Toast.makeText(Main2Register.this,"Please fill all fields",Toast.LENGTH_LONG).show();
                }
                else  if(!type.equals("Student")&&!type.equals("Teacher"))
                    Toast.makeText(Main2Register.this,"Type incorrect",Toast.LENGTH_LONG).show();
                else{
                    int s;
                    if(type.equals("Student"))
                        s=1;
                    else s=0;


                   User u=new User(name,last,email,phone,city,type,pass,s);
                   ApplicationClass.addUser(u);

                    mAuth.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(Main2Register.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(Main2Register.this,"REG sucses",Toast.LENGTH_SHORT).show();
                                        //FirebaseUser user = mAuth.getCurrentUser();
                                        //  updateUI(user);
                                    } else {
                                        // If sign in fails, display a message to the user.

                                        Toast.makeText(Main2Register.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        //updateUI(null);
                                    }

                                    // ...
                                }
                            });
                    Toast.makeText(Main2Register.this,"Added user:"+email,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Main2Register.this,
                            com.example.eliran.teacherconnection.Main2Login.class  );
                    startActivity(intent);
                }
            }
        });



    }
}

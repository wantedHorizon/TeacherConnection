package com.example.eliran.teacherconnection;

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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Main2Activity extends AppCompatActivity {
    Button addUser;

    private DatabaseReference mDatabase;
    EditText etbb;

    public static  final  String [] types={"Teacher","Student"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        etbb=findViewById(R.id.etnn);
        addUser=findViewById(R.id.addUser);
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mDatabase.child("Accounts").setValue(etbb.getText().toString().trim());
                }
        });




    }

    private void writeNewUser(String userId, String name, String email) {
      User user=new   User("mosh","aviv","eliran1994","0503050360","tel aviv","Student","1234",1);

        mDatabase.child("users").child(userId).setValue(user);
    }
}

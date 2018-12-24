package com.example.eliran.teacherconnection;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Workspace extends AppCompatActivity  implements  UserAdapter.ItemClicked{
    public static  final String MY_PREF_FILENAME="com.example.eliran.teacherconnection.DATA";
    ImageView tel;
    Button logout;
    TextView tvType, tvName, tvPhone,tvUsername,tvCity,welcom;
    FragmentManager fragmentManager;
    Fragment listFrag,detailFrag;
    int id=-1;
    User user;
    int userType;//0 - teacher , 1 student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

/*
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
           String user=extras.getString("user");
            String type=extras.getString("type");
            String city=extras.getString("city");
            String pass=extras.getString("pass");
            String name=extras.getString("name");

            if(!name.isEmpty())
                Toast.makeText(Workspace.this,"Welcome "+name+"here are some "+type+"s"
                        ,Toast.LENGTH_LONG).show();


        }*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("userID");}
        Toast.makeText(this, "ID:"+id, Toast.LENGTH_SHORT).show();


        if (id == -1) {
                Toast.makeText(this, "ERR:invlid ID ;login out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(com.example.eliran.teacherconnection.Workspace.this,
                        com.example.eliran.teacherconnection.Main2Login.class);
                startActivity(intent);

            }
            else {
                if (id >= 0)
                    user = ApplicationClass.users.get(id);

                welcom=findViewById(R.id.welcomWorkspace);
                if(user.getAccountType()==0)
                    welcom.setText("Welcome dear "+user.getName()+"\nHere are some Student for you in "+user.getCity());
                else
                    welcom.setText("Welcome dear "+user.getName()+"\nHere are some students for you in "+user.getCity());
                tvName = findViewById(R.id.tvName);

                tvUsername = findViewById(R.id.tvUsername);
                tvCity = findViewById(R.id.tvCity1);
                tvPhone = findViewById(R.id.tvPhone1);
                tvType = findViewById(R.id.tvType);
                tel=findViewById(R.id.iwTel);


                logout=findViewById(R.id.btnLogout);
                logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences.Editor editor =getSharedPreferences(MY_PREF_FILENAME,MODE_PRIVATE).edit();

                        editor.putString("user","-1");
                        editor.commit();

                        Intent intent = new Intent(com.example.eliran.teacherconnection.Workspace.this,
                                com.example.eliran.teacherconnection.MainActivity.class  );
                        startActivity(intent);

                    }
                });

            fragmentManager = this.getSupportFragmentManager();


                listFrag = (ListFrag) fragmentManager.findFragmentById(R.id.listFrag);
                detailFrag = fragmentManager.findFragmentById(R.id.detailFrag);

                fragmentManager.beginTransaction()
                        .show(listFrag)
                        .hide(detailFrag)
                        .commit();


                tel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tvPhone.getText().toString()));
                        startActivity(intent);
                    }
                });
            }

    }


    @Override
    public void onBackPressed() {
        if(listFrag.isVisible()) {

            super.onBackPressed();

        }
        else
        fragmentManager.beginTransaction()
                .show(listFrag)
                .hide(detailFrag)
                .commit();


    }

    @Override
    public void onItemClicked(int index) {

        Toast.makeText(com.example.eliran.teacherconnection.Workspace.this,"asds",Toast.LENGTH_SHORT);
        User a=ApplicationClass.users.get(index);

        tvName.setText(ApplicationClass.users.get(index).getName()+" "+ApplicationClass.users.get(index).getLast());

        tvType.setText(ApplicationClass.users.get(index).getType());

        tvUsername.setText(ApplicationClass.users.get(index).getUsername());

        tvPhone.setText(a.getPhone());


        tvCity.setText(ApplicationClass.users.get(index).getCity());


        fragmentManager.beginTransaction()
                .hide(listFrag)
                .show(detailFrag)
                .commit();


    }


    public int getId() {
        return id;
    }
}

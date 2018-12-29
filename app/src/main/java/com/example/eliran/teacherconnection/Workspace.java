package com.example.eliran.teacherconnection;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Workspace extends AppCompatActivity  /*implements  UserAdapter.ItemClicked */{
    public static final String MY_PREF_FILENAME = "com.example.eliran.teacherconnection.DATA";
    ImageView tel;
    Button logout;
    TextView tvType, tvName, tvPhone, tvEmail, tvCity, welcom;
    FragmentManager fragmentManager;
    Fragment listFrag, detailFrag;
    String id = "",usrCity="",usrName,usrLast;
    User user,userShow;
    int userType=-1;//0 - teacher , 1 student;
     DatabaseReference mUserDatabase;
     FirebaseUser mCurrentUser;

    FirebaseAuth mAuth;

    Toolbar toolbar1;

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
        toolbar1=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar1);
        getSupportActionBar().setTitle("Home");

        //checks user info
        usercheck();




            welcom = findViewById(R.id.welcomWorkspace);

            tvName = findViewById(R.id.tvName);

            tvEmail = findViewById(R.id.tvEmail);
            tvCity = findViewById(R.id.tvCity1);
            tvPhone = findViewById(R.id.tvPhone1);
            tvType = findViewById(R.id.tvType);
            tel = findViewById(R.id.iwTel);




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



    @Override
    public void onBackPressed() {
        if (listFrag.isVisible()) {

            super.onBackPressed();

        } else
            fragmentManager.beginTransaction()
                    .show(listFrag)
                    .hide(detailFrag)
                    .commit();


    }

        //if iten from rec list in pressed this method switch the layout and sets tv to user pressed
    public void onItemClicked(User u,String user_id) {/*

        Toast.makeText(com.example.eliran.teacherconnection.Workspace.this, "asds", Toast.LENGTH_SHORT);
        User a = ApplicationClass.users.get(index);*/

        tvName.setText(u.getName());

        tvType.setText(u.getType());

        tvEmail.setText(u.getEmail());

        tvPhone.setText(u.getPhone());


        tvCity.setText(u.getCity());


        fragmentManager.beginTransaction()
                .hide(listFrag)
                .show(detailFrag)
                .commit();

    }



        //viewholder recview

    public static class samViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        View mView;
        ImageView ivMake;
        TextView tvName, tvCity;

        public samViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;

            ivMake = itemView.findViewById(R.id.ivType);
            tvName = itemView.findViewById(R.id.tvName);
            tvCity = itemView.findViewById(R.id.tvCity);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {




                }
            });
        }


        @Override
        public void onClick(View view) {

        }
    }


    public User getUserShow() {
        return userShow;
    }

    public void setUserShow(User userShow) {
        this.userShow = userShow;
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    public void usercheck() {
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String cUID = mCurrentUser.getUid().toString();
       // this.id = cUID;
        FirebaseUser currentUser = mAuth.getCurrentUser();


        if (currentUser != null) {
            mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(cUID);

            mUserDatabase.addValueEventListener(new ValueEventListener() {


                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  usrCity = dataSnapshot.child("city").getValue().toString()+"";
                  usrName= dataSnapshot.child("name").getValue().toString()+"";
                  usrLast= dataSnapshot.child("lastname").getValue().toString()+"";
                    String types=dataSnapshot.child("type").getValue().toString()+"";
                    if (types.equals("teacher"))
                        userType = 0;
                    else userType = 1;
                    Toast.makeText(Workspace.this,id+"\n"+usrCity+"\n"+userType,Toast.LENGTH_LONG).show();

                    if (userType == 0)
                        welcom.setText("Welcome dear " + usrName + "\nHere are some Student for you in " + usrCity);
                    else if(userType==1)
                        welcom.setText("Welcome dear " + usrName + "\nHere are some Teacers for you in " + usrCity);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    databaseError.toString();

                }
            });


        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu, menu );
        menu.findItem(R.id.menuHomeBTN).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);

         if(item.getItemId()==R.id.menuLogoutBTN){
             SharedPreferences.Editor editor = getSharedPreferences(MY_PREF_FILENAME, MODE_PRIVATE).edit();

             editor.putString("user", "-1");
             editor.commit();

             FirebaseAuth.getInstance().signOut();

             Intent intent = new Intent(com.example.eliran.teacherconnection.Workspace.this,
                     com.example.eliran.teacherconnection.MainActivity.class);
             startActivity(intent);
             finish();

         }
         else {
             Intent intent = new Intent(com.example.eliran.teacherconnection.Workspace.this,
                     com.example.eliran.teacherconnection.AccountSetting.class);
             startActivity(intent);

         }

         return  true;
    }
}
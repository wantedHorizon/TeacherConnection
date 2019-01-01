package com.example.eliran.teacherconnection.Conniction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliran.teacherconnection.Cinfo;
import com.example.eliran.teacherconnection.R;
import com.example.eliran.teacherconnection.User;
import com.example.eliran.teacherconnection.Workspace;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

public class MyConnections extends AppCompatActivity {

    ImageView tel,yellowmail;
    Button logout,btnFriendREQ,btnDecline;
    TextView tvType, tvName, tvPhone, tvEmail, tvCity, welcom,sub;
    FragmentManager fragmentManager;
    Fragment listFrag, detailFrag;
    String id = "",usrCity="",usrName,usrLast,type="x";
    User user,userShow;
    int userType=-1;//0 - teacher , 1 student;
    DatabaseReference mUserDatabase,friendRequsetDatabase,mConnectionsDatabase,mNotificationDatabase,mUserDatabase2;
    public User  tempUser;
    FirebaseUser mCurrentUser;
    int currentState=0;//0-not friends_no connection at all,
    //3 -friends  , 2 -im wating for answer i sent,
    //1 other user sent me request

    FirebaseAuth mAuth;

    AppBarLayout barLayout;
    //progress dialog
    ProgressDialog mRegProgress,mFriendLoadProgress;

    Toolbar toolbar1;
    public String currentWatchedUser;

    public static final String MY_PREF_FILENAME = "com.example.eliran.teacherconnection.DATA";

    Toolbar mToolbar;
    ViewPager mViewPager;
    TabLayout mTab;
    SectionPagerAdapter mSectionPagerAdapter;
    //android.support.v7.widget.Toolbar toolbar1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_connections);


        mViewPager=findViewById(R.id.tab_pager);
        mSectionPagerAdapter=new SectionPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionPagerAdapter);
       mSectionPagerAdapter.getItem(3);
        mTab=(TabLayout)findViewById(R.id.main_tabs);
        mTab.setupWithViewPager(mViewPager);



        tvName = findViewById(R.id.tvName2);

        tvEmail = findViewById(R.id.tvEmail2);
        tvCity = findViewById(R.id.tvCity12);
        tvPhone = findViewById(R.id.tvPhone12);
        tvType = findViewById(R.id.tvType2);
        tel = findViewById(R.id.iwTel2);
        yellowmail=findViewById(R.id.iwYellowmail2);

        btnFriendREQ=findViewById(R.id.friendRequestBtn2);
        btnDecline=findViewById(R.id.btnDeclineconnection2);
        btnDecline.setVisibility(View.GONE);
        sub=findViewById(R.id.tvSub4);

       // sub=findViewById(R.id.tvSub);
        friendRequsetDatabase=FirebaseDatabase.getInstance().getReference().child("Connection_req");

        mCurrentUser=FirebaseAuth.getInstance().getCurrentUser();
        // mUserDatabase.keepSynced(true);
        mConnectionsDatabase=FirebaseDatabase.getInstance().getReference().child("Connections");
        mNotificationDatabase=FirebaseDatabase.getInstance().getReference().child("notifications");

       fragmentManager = this.getSupportFragmentManager();

        detailFrag = fragmentManager.findFragmentById(R.id.connectionDetail);
            //toolbar1.setVisibility(View.INVISIBLE);

        barLayout=findViewById(R.id.appBarLayout);
        //barLayout.setVisibility(View.GONE);
        fragmentManager.beginTransaction()

                .hide(detailFrag)
                .commit();



        //toolbar
        toolbar1=(android.support.v7.widget.Toolbar)findViewById(R.id.main_page_toolbar);
        setSupportActionBar(toolbar1);
        getSupportActionBar().setTitle("My Connections");

        tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tvPhone.getText().toString()));
                startActivity(intent);
            }
        });
        yellowmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail="";
                mail=tvEmail.getText().toString();
                Uri uri = Uri.parse("mailto:" +mail )
                        .buildUpon()
                        .appendQueryParameter("subject", "Work")
                        .appendQueryParameter("body", "Hello ,my name is "+usrName +"\nLets meetup :)")
                        .build();

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
                startActivity(Intent.createChooser(emailIntent, "Private lessom schedule"));

            }
        });


        //friend req button add friend,accept request
        btnFriendREQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnFriendREQ.setEnabled(false);

                mUserDatabase=FirebaseDatabase.getInstance().getReference().child("users").child(currentWatchedUser);
                mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //-------------------NOT Friends -----------------------------------
                        if(currentState==0){


                            final String currentDate = DateFormat.getTimeInstance().format(new Date());

                            final HashMap<String,String > data3=new HashMap<>();
                            data3.put("uID",currentWatchedUser);
                            data3.put("type","sent");
                            data3.put("user_info",tvName.getText().toString());
                            data3.put("usrType",tvType.getText().toString().toLowerCase());
                            data3.put("date",currentDate.toString());
                            data3.put("req_type","sent");

                            final HashMap<String,String > data4=new HashMap<>();
                            data4.put("uID",mCurrentUser.getUid());
                            data4.put("type","recived");
                            data4.put("user_info",usrName+" "+usrLast);
                            data4.put("usrType",type);
                            data4.put("date",currentDate.toString());
                            data4.put("req_type","recived");


                            friendRequsetDatabase.child(mCurrentUser.getUid()).child(currentWatchedUser)
                                    .setValue(data3).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        friendRequsetDatabase.child(currentWatchedUser).child(mCurrentUser.getUid())
                                                .setValue(data4).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //both updates succesfull

                                                //sending notification of connection request
                                                String user_id=mCurrentUser.getUid();
                                                HashMap<String,String > notificationData=new HashMap<>();
                                                notificationData.put("from",user_id);
                                                notificationData.put("type","request");
                                                 /*   notificationData.put("user_info",usrName+" "+usrLast);
                                                    notificationData.put("usrType",type);*/





                                                mNotificationDatabase.child(currentWatchedUser).push().setValue(notificationData)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {


                                                                Toast.makeText(MyConnections.this,"friend req sent :)",Toast.LENGTH_SHORT)
                                                                        .show();
                                                                btnFriendREQ.setEnabled(true);
                                                                currentState=2;
                                                                btnFriendREQ.setText("cancel Connection request");


                                                            }
                                                        });

                                            }
                                        });
                                    }

                                    else {
                                        Toast.makeText(MyConnections.this,"friend req failed :)",Toast.LENGTH_SHORT)
                                                .show();

                                    }
                                }
                            });

                        }// e2nd if  current==0

                        //-------------------Cancel Friend Request----------------------------------------
                        else if (currentState==2){
                            friendRequsetDatabase.child(mCurrentUser.getUid()).child(currentWatchedUser)
                                    .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    friendRequsetDatabase.child(currentWatchedUser).child(mCurrentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            btnFriendREQ.setText("Connection Request");

                                            btnFriendREQ.setEnabled(true);
                                            currentState=0;

                                            Toast.makeText(MyConnections.this,"friend req cancel :)",Toast.LENGTH_SHORT)
                                                    .show();

                                        }
                                    });
                                }
                            });
                        }

                        //___________________Awaiting Answer_____________________
                        else if(currentState==1){//other user sent me request i need to answer


                            btnDecline.setVisibility(View.GONE);

                            final String currentDate = DateFormat.getTimeInstance().format(new Date());
                            // mCurrentUser.getUid();


                            final HashMap<String,String > data2=new HashMap<>();
                            data2.put("uID",currentWatchedUser);
                            data2.put("type","request");
                            data2.put("user_info",tvName.getText().toString());
                            data2.put("usrType",tvType.getText().toString().toLowerCase());
                            data2.put("date",currentDate.toString());
                                                 /*   notificationData.put("user_info",usrName+" "+usrLast);
                                                    notificationData.put("usrType",type);*/

                            //setting a new connection
                            mConnectionsDatabase.child(mCurrentUser.getUid()).child(currentWatchedUser).setValue(data2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    HashMap<String,String > data1=new HashMap<>();
                                    data1.put("uID",mCurrentUser.getUid());
                                    data1.put("type","request");
                                    data1.put("user_info",usrName+" "+usrLast);
                                    data1.put("usrType",type);
                                    data1.put("date",currentDate.toString());


                                    mConnectionsDatabase.child(currentWatchedUser).child(mCurrentUser.getUid()).setValue(data1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            //Deleting the request because we are already friends

                                            friendRequsetDatabase.child(mCurrentUser.getUid()).child(currentWatchedUser)
                                                    .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    friendRequsetDatabase.child(currentWatchedUser).child(mCurrentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(MyConnections.this,"You are now  friends",Toast.LENGTH_SHORT).show();



                                                            btnFriendREQ.setText("Unfriend this Person");
                                                            btnFriendREQ.setEnabled(true);





                                                        }


                                                    });
                                                }


                                            });
                                        }
                                    });



                                }
                            });



                        }


                        else if(currentState==3){//we need to delete this friendship

                            //Deleting the request because we are already friends

                            mConnectionsDatabase.child(mCurrentUser.getUid()).child(currentWatchedUser)
                                    .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mConnectionsDatabase.child(currentWatchedUser).child(mCurrentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(MyConnections.this,"connection deleted",Toast.LENGTH_SHORT).show();



                                            btnFriendREQ.setText("Send Connection Request");
                                            currentState=0;
                                            btnFriendREQ.setEnabled(true);






                                        }


                                    });
                                }


                            });


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });



        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDecline.setVisibility(View.GONE);

            }
        });




    }//end Oncreate

//__________________________________________Regular function _________________________________________________

    //if iten from rec list in pressed this method switch the layout and sets tv to user pressed
    public void onItemClicked(Cinfo X, String user_id) {/*


        Toast.makeText(com.example.eliran.teacherconnection.Workspace.this, "asds", Toast.LENGTH_SHORT);
        User a = ApplicationClass.users.get(index);*/

       // mFriendLoadProgress.show();

        final String sss=user_id;




        //getting user data
        mUserDatabase2 = FirebaseDatabase.getInstance().getReference().child("users").child(user_id);

        mUserDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {



            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String uname,ulast,ucity,utype,umail,uphone;
                ucity=(  dataSnapshot.child("city").getValue().toString()+"");
             uname=(    dataSnapshot.child("name").getValue().toString()+"");
           ulast=(      dataSnapshot.child("lastname").getValue().toString()+"");
              utype= dataSnapshot.child("type").getValue().toString()+"";
             umail=(    dataSnapshot.child("email").getValue().toString()+"");
             uphone=(    dataSnapshot.child("phone").getValue().toString()+"");
               // type=types;

                int ux;
                if (utype.equals("teacher"))
                    ux = 0;
                else ux = 1;
                //  Toast.makeText(Workspace.this,id+"\n"+usrCity+"\n"+userType,Toast.LENGTH_LONG).show();

                   /* if (userType == 0)
                        welcom.setText("Welcome dear " + usrName + "\nHere are some Student for you in " + usrCity);
                    else if(userType==1)
                        welcom.setText("Welcome dear " + usrName + "\nHere are some Teacers for you in " + usrCity);*/

                tempUser=new User(uname,ulast,umail,uphone,ucity,utype,"564564",ux);
                String n=tempUser.getName();
                String l =tempUser.getLastname();
                String t=tempUser.getType();
                String e=tempUser.getEmail();
                String c=tempUser.getCity();
                n=(n.charAt(0)+"").toUpperCase()+(n.substring(1)).toLowerCase();
                l=(l.charAt(0)+"").toUpperCase()+(l.substring(1)).toLowerCase();
                t=(t.charAt(0)+"").toUpperCase()+(t.substring(1)).toLowerCase();
                //  e=(e.charAt(0)+"").toUpperCase()+(e.substring(1)).toLowerCase();
                c=(c.charAt(0)+"").toUpperCase()+(c.substring(1)).toLowerCase();
                tvName.setText(n+" "+l);

                tvType.setText(t);

                tvEmail.setText(e);

                tvPhone.setText(tempUser.getPhone());
                dataSnapshot.getRef();

                tvCity.setText(c);

                currentWatchedUser =sss;
                setSubjects(sss);
                fragmentManager.beginTransaction()


                        .show(detailFrag)
                        .commit();

                barLayout.setVisibility(View.GONE);

                // mFriendLoadProgress.hide();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.toString();

            }
        });







    }

    private void setSubjects(final String user_id) {

        DatabaseReference mUserDatabase1 = FirebaseDatabase.getInstance().getReference().child("users").child(user_id).child("sub");
        mUserDatabase1.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //checking other user subjects
                Boolean math=Boolean.parseBoolean(dataSnapshot.child("math").getValue().toString());
                Boolean eng=Boolean.parseBoolean(dataSnapshot.child("eng").getValue().toString());
                Boolean sci=Boolean.parseBoolean(dataSnapshot.child("sci").getValue().toString());
                String str="";

                if(math)str+="Math\n";
                if(eng)str+="English\n ";
                if(sci)str+="Science";

                sub.setText(str);

                //_______________Check_Connection_status____________

                friendRequsetDatabase.child(mCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                        if(dataSnapshot.hasChild(user_id)){

                            String req_type=dataSnapshot.child(user_id).child("req_type").getValue().toString();
                            mFriendLoadProgress.hide();

                            if(req_type.equals("recived")){

                                currentState=1;
                                btnFriendREQ.setText("Accept Friend Request");
                            }
                            else if(req_type.equals("sent")){
                                btnFriendREQ.setText("cancel friend request");
                            }



                        }

                        else{//checks if already friends

                            mConnectionsDatabase.child(mCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild(user_id)){
                                        btnFriendREQ.setText("Cancel this Connection");

                                        currentState=3;//already friends
                                    }
                                    else{//set regular mode

                                        btnFriendREQ.setText("Send Connection Request");
                                        currentState=0;


                                    }

                                    btnDecline.setVisibility(View.GONE);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        mFriendLoadProgress.hide();

                    }
                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mFriendLoadProgress.hide();

            }
        });
    }




    @Override
    public void onBackPressed() {
        if (detailFrag.isVisible()) {
            fragmentManager.beginTransaction()

                    .hide(detailFrag)
                    .commit();
            barLayout.setVisibility(View.VISIBLE);

        }
        else {
            super.onBackPressed();



        }


    }






    // _______________________cheks user data _____________________________
    public void usercheck() {
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String cUID = mCurrentUser.getUid().toString();
        // this.id = cUID;
        FirebaseUser currentUser = mAuth.getCurrentUser();
        ;


        if (currentUser != null) {
            mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(cUID);

            mUserDatabase.addValueEventListener(new ValueEventListener() {



                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    usrCity = dataSnapshot.child("city").getValue().toString()+"";
                    usrName= dataSnapshot.child("name").getValue().toString()+"";
                    usrLast= dataSnapshot.child("lastname").getValue().toString()+"";
                    String types=dataSnapshot.child("type").getValue().toString()+"";
                    type=types;
                    if (types.equals("teacher"))
                        userType = 0;
                    else userType = 1;
                    //  Toast.makeText(Workspace.this,id+"\n"+usrCity+"\n"+userType,Toast.LENGTH_LONG).show();

                   /* if (userType == 0)
                        welcom.setText("Welcome dear " + usrName + "\nHere are some Student for you in " + usrCity);
                    else if(userType==1)
                        welcom.setText("Welcome dear " + usrName + "\nHere are some Teacers for you in " + usrCity);*/



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    databaseError.toString();

                }
            });



        }
    }



    public  String getType(){
        SharedPreferences editor =getSharedPreferences(MY_PREF_FILENAME,MODE_PRIVATE);
        String type1=editor.getString("type","-1");

        return type1;

    }



    //viewholder recyview

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














    @Override
    protected void onStart() {
        super.onStart();
        usercheck();


    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}

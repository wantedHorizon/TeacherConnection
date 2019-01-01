package com.example.eliran.teacherconnection;

import android.app.ProgressDialog;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class Workspace extends AppCompatActivity  /*implements  UserAdapter.ItemClicked */{
    public static final String MY_PREF_FILENAME = "com.example.eliran.teacherconnection.DATA";
    ImageView tel,yellowmail;
    Button logout,btnFriendREQ,btnDecline;
    TextView tvType, tvName, tvPhone, tvEmail, tvCity, welcom,sub,subTitle;
    FragmentManager fragmentManager;
    Fragment listFrag, detailFrag;
    String id = "",usrCity="",usrName,usrLast,type="x";
    User user,userShow;
    int userType=-1;//0 - teacher , 1 student;
     DatabaseReference mUserDatabase,friendRequsetDatabase,mConnectionsDatabase,mNotificationDatabase;

     FirebaseUser mCurrentUser;
     int currentState=0;//0-not friends_no connection at all,
    //3 -friends  , 2 -im wating for answer i sent,
    //1 other user sent me request

    FirebaseAuth mAuth;


    //progress dialog
    ProgressDialog mRegProgress,mFriendLoadProgress;

    Toolbar toolbar1;
    public String currentWatchedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
     //   usercheck();
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
        getSupportActionBar().setTitle("HOME");
        //loading screen
        mRegProgress=new ProgressDialog(this);
        mRegProgress.setTitle("Loading  Data");
        mRegProgress.setMessage("Please wait while data is downloaded");
        mRegProgress.setCanceledOnTouchOutside(false);
        mRegProgress.show();

        mFriendLoadProgress=new ProgressDialog(this);
        mFriendLoadProgress.setTitle("Loading  Friend Data");
        mFriendLoadProgress.setMessage("Please wait while data is downloaded");
        mFriendLoadProgress.setCanceledOnTouchOutside(false);
        //checks user info

        sub=findViewById(R.id.tvSub);
        friendRequsetDatabase=FirebaseDatabase.getInstance().getReference().child("Connection_req");

        mCurrentUser=FirebaseAuth.getInstance().getCurrentUser();
       // mUserDatabase.keepSynced(true);
        mConnectionsDatabase=FirebaseDatabase.getInstance().getReference().child("Connections");
        mNotificationDatabase=FirebaseDatabase.getInstance().getReference().child("notifications");




            welcom = findViewById(R.id.welcomWorkspace);
          welcom.addTextChangedListener(new TextWatcher() {
              @Override
              public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

              }

              @Override
              public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

              }

              @Override
              public void afterTextChanged(Editable editable) {
                  mRegProgress.dismiss();

              }
          });

            tvName = findViewById(R.id.tvName);

            tvEmail = findViewById(R.id.tvEmail);
            tvCity = findViewById(R.id.tvCity1);
            tvPhone = findViewById(R.id.tvPhone1);
            tvType = findViewById(R.id.tvType);
            tel = findViewById(R.id.iwTel);
            yellowmail=findViewById(R.id.iwYellowmail);

            tvEmail.setVisibility(View.GONE);
           // tvCity.setVisibility(View.GONE);
            tvPhone.setVisibility(View.GONE);
            tel.setVisibility(View.GONE);
            yellowmail.setVisibility(View.GONE);
            subTitle=findViewById(R.id.tvSubjects);

            btnFriendREQ=findViewById(R.id.friendRequestBtn);
            btnDecline=findViewById(R.id.btnDeclineconnection);
            btnDecline.setVisibility(View.GONE);



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


                                                                    Toast.makeText(Workspace.this,"friend req sent :)",Toast.LENGTH_SHORT)
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
                                            Toast.makeText(Workspace.this,"friend req failed :)",Toast.LENGTH_SHORT)
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

                                                Toast.makeText(Workspace.this,"friend req cancel :)",Toast.LENGTH_SHORT)
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
                                                                Toast.makeText(Workspace.this,"You are now  friends",Toast.LENGTH_SHORT).show();



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
                                                Toast.makeText(Workspace.this,"connection deleted",Toast.LENGTH_SHORT).show();



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



        }//End OnCREATE






    @Override
    public void onBackPressed() {
        if (listFrag.isVisible()) {

            super.onBackPressed();


        } else {

            fragmentManager.beginTransaction()
                    .show(listFrag)
                    .hide(detailFrag)
                    .commit();
        }


    }

        //if iten from rec list in pressed this method switch the layout and sets tv to user pressed
    public void onItemClicked(User u,String user_id) {/*


        Toast.makeText(com.example.eliran.teacherconnection.Workspace.this, "asds", Toast.LENGTH_SHORT);
        User a = ApplicationClass.users.get(index);*/

        mFriendLoadProgress.show();

        String n=u.getName();
        String l =u.getLastname();
        String t=u.getType();


        if(t.toLowerCase().equals("students")){
            subTitle.setText("Seeking help in :");
        }
        else{
            subTitle.setText("Master of :");}



        String e=u.getEmail();
        String c=u.getCity();
        n=(n.charAt(0)+"").toUpperCase()+(n.substring(1)).toLowerCase();
        l=(l.charAt(0)+"").toUpperCase()+(l.substring(1)).toLowerCase();
        t=(t.charAt(0)+"").toUpperCase()+(t.substring(1)).toLowerCase();
      //  e=(e.charAt(0)+"").toUpperCase()+(e.substring(1)).toLowerCase();
        c=(c.charAt(0)+"").toUpperCase()+(c.substring(1)).toLowerCase();
        tvName.setText(n+" "+l);

        tvType.setText(t);

        tvEmail.setText(e);

        tvPhone.setText(u.getPhone());


        tvCity.setText(c);
        currentWatchedUser =user_id;
        setSubjects(user_id);
        fragmentManager.beginTransaction()
                .hide(listFrag)
                .show(detailFrag)
                .commit();

        mFriendLoadProgress.hide();

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


    public User getUserShow() {
        return userShow;
    }

    public void setUserShow(User userShow) {
        this.userShow = userShow;
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


    //______________________________sets the option menu_________________________

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu, menu );
        menu.findItem(R.id.menuHomeBTN).setVisible(false);
        return true;
    }
//checks type frome saved file
   public  String getType(){
       SharedPreferences editor =getSharedPreferences(MY_PREF_FILENAME,MODE_PRIVATE);
       String type1=editor.getString("type","-1");

        return type1;

    }


    //________________________________________item from top menu bar selection _____________________________________________________
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);

         if(item.getItemId()==R.id.menuLogoutBTN){
             SharedPreferences.Editor editor = getSharedPreferences(MY_PREF_FILENAME, MODE_PRIVATE).edit();

             editor.putString("user", "-1");
             editor.putString("type","-1");
             editor.commit();

             FirebaseAuth.getInstance().signOut();

             Intent intent = new Intent(com.example.eliran.teacherconnection.Workspace.this,
                     com.example.eliran.teacherconnection.MainActivity.class);
             startActivity(intent);
             finish();

         }
         else if (item.getItemId()==R.id.menuMyConnections){
             Intent intent = new Intent(com.example.eliran.teacherconnection.Workspace.this,
                     com.example.eliran.teacherconnection.Conniction.MyConnections.class);
             startActivity(intent);
            // finish();

         }
         else if(item.getItemId()==R.id.menuSettingAccount){
             Intent intent = new Intent(com.example.eliran.teacherconnection.Workspace.this,
                     com.example.eliran.teacherconnection.AccountSetting.class);
             startActivity(intent);
             //finish();

         }


         return  true;
    }
}
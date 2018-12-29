package com.example.eliran.teacherconnection;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class SampleRec extends AppCompatActivity {

    private RecyclerView mAccountsRV;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<User,SampleRec.samViewHolder> mAccountsAdapter;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_rec);
        setTitle("Users");
        mDatabase=FirebaseDatabase.getInstance().getReference().child("users");
        mDatabase.keepSynced(true);
        mAccountsRV=(RecyclerView)findViewById(R.id.sample_rec_list);

        DatabaseReference personRef=FirebaseDatabase.getInstance().getReference().child("users");

        Query personQuery =personRef.orderByKey();




        mAccountsRV.setHasFixedSize(true);
        mAccountsRV.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions personOptions=new FirebaseRecyclerOptions.Builder<User>().setQuery(personQuery,User.class).build();

        mAccountsAdapter=new FirebaseRecyclerAdapter<User, SampleRec.samViewHolder>(personOptions) {
            @Override
            protected void onBindViewHolder(@NonNull SampleRec.samViewHolder holder, int position, @NonNull User model) {

                holder.itemView.setTag(model.getEmail());

                holder.tvName.setText(model.getName());
                holder.tvCity.setText(model.getEmail());

                if (model.getType().toLowerCase().equals("student")) {
                    holder.ivMake.setImageResource(R.drawable.student_vec);
                } else {
                    holder.ivMake.setImageResource(R.drawable.scool_vec);
                }


            }

            @NonNull
            @Override
            public SampleRec.samViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout2, parent, false);

                return new SampleRec.samViewHolder(view);
            }


        };

        mAccountsRV.setAdapter(mAccountsAdapter);










    }//END onCreate

    @Override
    public void onStart() {
        super.onStart();
       // FirebaseUser currentUser = mAuth.getCurrentUser();

        //

            mAccountsAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        mAccountsAdapter.stopListening();


    }
    public static class samViewHolder extends RecyclerView.ViewHolder
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

                    //  activity.onItemClicked(users.indexOf((User) view.getTag()));

                }
            });
        }



    }

        }//End FirebaseAdapter








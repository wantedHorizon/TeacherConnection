package com.example.eliran.teacherconnection;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.google.android.gms.flags.impl.SharedPreferencesFactory.getSharedPreferences;

import android.content.SharedPreferences;



/**
 * A simple {@link Fragment} subclass.
 */
public class ListFrag extends Fragment {

    public static final String MY_PREF_FILENAME = "com.example.eliran.teacherconnection.DATA";

    RecyclerView recyclerView;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference mDataBaseReference;
    TextView tvType, tvName, tvPhone, tvEmail, tvCity;
    String usrtype;
    public RecyclerView mAccountsRV;
    public DatabaseReference mDatabase,xx;
    public FirebaseRecyclerAdapter<User,Workspace.samViewHolder> mAccountsAdapter;
    public FirebaseAuth mAuth;
  public  String tt="";
    FirebaseRecyclerOptions options;

    View view;
    int xid;


    public ListFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        view=inflater.inflate(R.layout.fragment_list, container, false);
        return view;
    }


    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

       // xid=((Workspace)this.getActivity()).id;
        mAccountsRV = (RecyclerView) view.findViewById(R.id.list1);
        mAccountsRV.setHasFixedSize(true);


        mDatabase=FirebaseDatabase.getInstance().getReference().child("users");

        mDatabase.keepSynced(true);
       // mAccountsRV=(RecyclerView)findViewById(R.id.list1);


        DatabaseReference personRef=FirebaseDatabase.getInstance().getReference().child("users");
        mAuth = FirebaseAuth.getInstance();
       // String mCurrentUser = FirebaseAuth.getInstance().getCurrentUser().toString();
        //DatabaseReference xxx=FirebaseDatabase.getInstance().getReference().child("users").child(mCurrentUser).child("type").getKey().toString();




       usrtype= ((Workspace) getActivity()).getType();
       String wantedType="";
       if(usrtype.equals("student")) wantedType="teacher";
       else if (usrtype.equals("teacher"))wantedType="student";
       else{

       }



        Query personQuery =personRef.orderByChild("type").equalTo(wantedType);
       // personQuery.orderByValue();


       // getActivity().

        mAccountsRV.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this.getActivity());
        mAccountsRV.setLayoutManager(layoutManager);
       // mAccountsRV.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        FirebaseRecyclerOptions personOptions=new FirebaseRecyclerOptions.Builder<User>().setQuery(personQuery,User.class).build();




          // usrtype=((Workspace)getActivity()).type;
         /*  if(usrtype.equals("x"))
               ((Workspace)getActivity()).usercheck();*/


        mAccountsAdapter=new FirebaseRecyclerAdapter<User, Workspace.samViewHolder>(personOptions) {
            @Override
            protected void onBindViewHolder  (@NonNull Workspace.samViewHolder holder, int position, @NonNull final User model) {

                if(model.getType().toLowerCase().equals(""))
                    holder.mView.setVisibility(View.INVISIBLE);

                else   {
                    holder.itemView.setTag(model.getEmail());



                    holder.tvName.setText(model.getName() + " " + model.getLastname());
                    holder.tvCity.setText("Town: " + model.getCity());

                    if (model.getType().toLowerCase().equals("student")) {
                        holder.ivMake.setImageResource(R.drawable.student_vec);
                    } else {
                        holder.ivMake.setImageResource(R.drawable.scool_vec);
                    }

                    final String user_id = getRef(position).getKey();
                    holder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ((Workspace) getActivity()).onItemClicked(model, user_id);

                        }
                    });





            }}

            @NonNull
            @Override
            public Workspace.samViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);

                return new Workspace.samViewHolder(view);
            }


        };
        mAccountsRV.setAdapter(mAccountsAdapter);








        /*
        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        if(xid>=0) {
            ArrayList<User> list = ApplicationClass.filterByID(xid);
            myAdapter = new UserAdapter(this.getActivity(), list);
        }
        else
            myAdapter = new UserAdapter(this.getActivity(), ApplicationClass.users);

                // 3 myAdapter = new UserAdapter(this.getActivity(), ApplicationClass.users);

        recyclerView.setAdapter(myAdapter);*/
    }



    public void onStart() {
        super.onStart();
        //FirebaseUser currentUser = mAuth.getCurrentUser();



        //
      // usrtype=((Workspace)getActivity()).type;
        mAccountsAdapter.startListening();

    }


    @Override
    public void onStop() {
        super.onStop();
        mAccountsAdapter.stopListening();




    }

    public void notifyDataChanged()
    {
        mAccountsAdapter.notifyDataSetChanged();
    }

}


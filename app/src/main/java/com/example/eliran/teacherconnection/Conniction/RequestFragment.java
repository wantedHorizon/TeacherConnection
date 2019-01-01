package com.example.eliran.teacherconnection.Conniction;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eliran.teacherconnection.Cinfo;
import com.example.eliran.teacherconnection.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestFragment extends Fragment {
    FirebaseUser mCurrentUser;

    RecyclerView.LayoutManager layoutManager;
    DatabaseReference mDataBaseReference;
    TextView tvType, tvName, tvPhone, tvEmail, tvCity;
    String usrtype;
    public RecyclerView mAccountsRV;
    public DatabaseReference mDatabase,xx;
    public FirebaseRecyclerAdapter<Cinfo,MyConnections.samViewHolder> mAccountsAdapter;
    public FirebaseAuth mAuth;
    public  String tt="";
    FirebaseRecyclerOptions options;

    View view;


    public RequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_request, container, false);
        return view;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



    mCurrentUser=FirebaseAuth.getInstance().getCurrentUser();
    // xid=((Workspace)this.getActivity()).id;
    mAccountsRV = (RecyclerView) view.findViewById(R.id.listRecConnection);
        mAccountsRV.setHasFixedSize(true);


    //   mDatabase=FirebaseDatabase.getInstance().getReference().child("users");

    //   mDatabase.keepSynced(true);
    // mAccountsRV=(RecyclerView)findViewById(R.id.list1);

    String myID=mCurrentUser.getUid();
    DatabaseReference personRef=FirebaseDatabase.getInstance().getReference().child("Connection_req").child(myID);
    mAuth = FirebaseAuth.getInstance();

    /*usrtype= ((MyConnections) getActivity()).getType();
    String wantedType="";
        if(usrtype.equals("student")) wantedType="teacher";
        else if (usrtype.equals("teacher"))wantedType="student";
        else{

    }*/


     Query personQuery =personRef.orderByChild("type").equalTo("recived");
   // Query personQuery=personRef.orderByValue();


    // getActivity().

        mAccountsRV.setHasFixedSize(true);
    layoutManager = new LinearLayoutManager(this.getActivity());
        mAccountsRV.setLayoutManager(layoutManager);
    // mAccountsRV.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    FirebaseRecyclerOptions personOptions=new FirebaseRecyclerOptions.Builder<Cinfo>().setQuery(personQuery,Cinfo.class).build();



    mAccountsAdapter=new FirebaseRecyclerAdapter<Cinfo, MyConnections.samViewHolder>(personOptions) {
        @Override
        protected void onBindViewHolder  (@NonNull MyConnections.samViewHolder holder, int position, @NonNull final Cinfo model) {



            // holder.itemView.setTag(model.getEmail());
            String n,ln,t;
            n=model.getUser_info();
                    /*
                    n=(n.charAt(0)+"").toUpperCase()+n.substring(1);
                    ln=model.getLastname().toLowerCase();
                    ln=(""+ln.charAt(0)+"").toUpperCase()+ln.substring(1);
                    t=model.getCity().toLowerCase();
                    t=(""+t.charAt(0)+"").toUpperCase()+t.substring(1);

                    */

            holder.tvName.setText(n );
            holder.tvCity.setText("");


                    if (model.getUsrType().toLowerCase().equals("student")) {
                        holder.ivMake.setImageResource(R.drawable.student_vec);
                    } else {
                        holder.ivMake.setImageResource(R.drawable.scool_vec);
                    }

                    final String user_id = getRef(position).getKey();
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // ((Workspace) getActivity()).onItemClicked(model, user_id);

                }
            });





        }

        @NonNull
        @Override
        public MyConnections.samViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);

            return new MyConnections.samViewHolder(view);
        }


    };
        mAccountsRV.setAdapter(mAccountsAdapter);









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


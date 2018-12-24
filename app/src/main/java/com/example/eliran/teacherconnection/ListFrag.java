package com.example.eliran.teacherconnection;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFrag extends Fragment {


    RecyclerView recyclerView;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager layoutManager;
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
        xid=((Workspace)this.getActivity()).id;
        recyclerView = view.findViewById(R.id.list1);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        if(xid>=0) {
            ArrayList<User> list = ApplicationClass.filterByID(xid);
            myAdapter = new UserAdapter(this.getActivity(), list);
        }
        else
            myAdapter = new UserAdapter(this.getActivity(), ApplicationClass.users);


        recyclerView.setAdapter(myAdapter);
    }

    public void notifyDataChanged()
    {
        myAdapter.notifyDataSetChanged();
    }

}

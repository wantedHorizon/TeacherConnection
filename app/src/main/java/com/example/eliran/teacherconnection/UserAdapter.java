package com.example.eliran.teacherconnection;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    @NonNull

    private ArrayList<User> users;

    ItemClicked activity;

    public interface ItemClicked
    {
        void onItemClicked(int index);
    }


    public UserAdapter(Context context, ArrayList<User> users) {
        this.users = users;
        this.activity = (ItemClicked)context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView ivMake;
        TextView tvName, tvCity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivMake = itemView.findViewById(R.id.ivType);
            tvName = itemView.findViewById(R.id.tvName);
            tvCity = itemView.findViewById(R.id.tvCity);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    activity.onItemClicked(users.indexOf((User) view.getTag()));

                }
            });
        }
    }
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag(users.get(i));

        viewHolder.tvName.setText(users.get(i).getName());
        viewHolder.tvCity.setText(users.get(i).getCity());

        if (users.get(i).getType().equals("Student"))
        {
            viewHolder.ivMake.setImageResource(R.drawable.student_vec);
        }

        else
        {
            viewHolder.ivMake.setImageResource(R.drawable.scool_vec);
        }

    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}

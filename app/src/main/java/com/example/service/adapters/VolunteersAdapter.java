package com.example.service.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.service.R;
import com.example.service.activities.UserDetailsActivity;
import com.example.service.models.User;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

public class VolunteersAdapter extends RecyclerView.Adapter<VolunteersAdapter.ViewHolder> {

    public static final String TAG = "VolunteersAdapter";

    private Context context;
    private List<ParseUser> users;

    public VolunteersAdapter(Context context, List<ParseUser> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_pic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ParseUser user = users.get(position);
        holder.bind(user);

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // ui views
        private TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            tvName = itemView.findViewById(R.id.item_user_name);
        }

        // bind user
        public void bind(ParseUser parseUser) {
            User user = new User(parseUser);
            tvName.setText(user.getName());
        }


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            if (position != RecyclerView.NO_POSITION) {
                ParseUser pUser = users.get(position);
                User user = new User(pUser);
                Intent intent = new Intent(context, UserDetailsActivity.class);
                intent.putExtra(User.class.getSimpleName(), Parcels.wrap(user));
                context.startActivity(intent);
            }
        }
    }


}


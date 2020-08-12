package com.example.charibee.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.service.R;
import com.example.charibee.activities.UserDetailsActivity;
import com.example.charibee.models.User;
import com.parse.ParseFile;
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
        return Math.min(10, users.size());
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // ui views
        private ImageView ivAvatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ivAvatar = itemView.findViewById(R.id.item_user_pic_avatar);
        }

        // bind user
        public void bind(ParseUser parseUser) {
            User user = new User(parseUser);

            // set profile pic
            ParseFile profilePic = (ParseFile) user.getProfilePic();
            if (profilePic != null) {
                Glide.with(context)
                        .load(httpToHttps(profilePic.getUrl()))
                        .circleCrop()
                        .into(ivAvatar);
            } else {
                Log.e(TAG, "couldn't load profile pic");
            }
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

    // converts http link to https
    private String httpToHttps(String url) {
        if (url == null) {
            return "";
        }

        if (url.contains("https")) {
            return url;
        }

        String httpsUrl = "https" + url.substring(4);
        return httpsUrl;
    }


}


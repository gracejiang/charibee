package com.example.service.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.service.R;
import com.example.service.activities.UserDetailsActivity;
import com.example.service.models.User;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

public class CommunityUsersAdapter extends RecyclerView.Adapter<CommunityUsersAdapter.ViewHolder> {

    public static final String TAG = "CommunityUsersAdapter";

    // data
    private Context context;
    private List<ParseUser> users;

    public CommunityUsersAdapter(Context context, List<ParseUser> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public CommunityUsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new CommunityUsersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ParseUser pUser = users.get(position);
        holder.bind(pUser);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvName;
        private ImageView ivAvatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // set views
            tvName = itemView.findViewById(R.id.item_message_name);
            ivAvatar = itemView.findViewById(R.id.item_message_iv_avatar);

            // set listener
            itemView.setOnClickListener(this);
        }

        public void bind(ParseUser pUser) {
            User user = new User(pUser);
            tvName.setText(user.getName());

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

        // view user
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                ParseUser pUser = users.get(position);
                Log.i(TAG, pUser.getObjectId());
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

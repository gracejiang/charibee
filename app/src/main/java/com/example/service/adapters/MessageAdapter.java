package com.example.service.adapters;

import android.content.Context;
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
import com.example.service.models.User;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final String TAG = "MessageAdapter";

    // data
    private Context context;
    private List<ParseUser> withMsgs;

    public MessageAdapter(Context context, List<ParseUser> withMsgs) {
        this.context = context;
        this.withMsgs = withMsgs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
        return new MessageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ParseUser pUser = withMsgs.get(position);
        holder.bind(pUser);
    }

    @Override
    public int getItemCount() {
        return withMsgs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivAvatar;
        private TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.item_message_iv_avatar);
            tvName = itemView.findViewById(R.id.item_message_name);
        }

        public void bind(ParseUser pUser) {
            User user = new User(pUser);
            tvName.setText(user.getUsername());

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
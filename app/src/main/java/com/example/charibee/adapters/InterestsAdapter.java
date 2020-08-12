package com.example.charibee.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.charibee.models.User;
import com.example.service.R;
import com.parse.ParseUser;

import java.util.List;

public class InterestsAdapter extends RecyclerView.Adapter<InterestsAdapter.ViewHolder> {

    public static final String TAG = "InterestsAdapter";

    // data
    private Context context;
    private String[] interests;
    private List<Boolean> checkedInterests;

    // current user
    ParseUser currentParseUser = ParseUser.getCurrentUser();
    User currentUser = new User(currentParseUser);

    public InterestsAdapter(Context context, String[] interests, List<Boolean> checkedInterests) {
        this.context = context;
        this.interests = interests;
        this.checkedInterests = checkedInterests;
    }

    public List<Boolean> getCheckedInterests() {
        return checkedInterests;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_interest, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String interest = interests[position];
        holder.bind(interest);
    }

    @Override
    public int getItemCount() {
        return interests.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // set views
            tvName = itemView.findViewById(R.id.item_interest_tv);

            // set current user values
            currentParseUser = ParseUser.getCurrentUser();
            currentUser = new User(currentParseUser);

            // set listeners
            itemView.setOnClickListener(this);
        }

        public void bind(String interest) {
            tvName.setText(interest);

            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                setColor(tvName, checkedInterests.get(position));
            }
        }

        private void setColor(TextView tv, boolean isChecked) {
            if (isChecked) {
                tv.setTextColor(context.getResources().getColor(R.color.black));
            } else {
                tv.setTextColor(context.getResources().getColor(R.color.light_grey));
            }
        }

        // view org
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                // check/uncheck
                boolean newCheck = !checkedInterests.get(position);
                checkedInterests.set(position, newCheck);
                setColor(tvName, newCheck);
            }
        }

    }
}


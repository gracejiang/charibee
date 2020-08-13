package com.example.charibee.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.charibee.models.User;
import com.example.service.R;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterestsAdapter extends RecyclerView.Adapter<InterestsAdapter.ViewHolder> {

    public static final String TAG = "InterestsAdapter";

    // data
    private Context context;
    private String[] interests;
    private List<Boolean> checkedInterests;
    private Map<String, Integer> interestIcons = new HashMap<>();

    // current user
    ParseUser currentParseUser = ParseUser.getCurrentUser();
    User currentUser = new User(currentParseUser);

    public InterestsAdapter(Context context, String[] interests, List<Boolean> checkedInterests) {
        this.context = context;
        this.interests = interests;
        this.checkedInterests = checkedInterests;

        setUpInterestIconsMap();
    }

    private void setUpInterestIconsMap() {
        interestIcons.put("Advocacy & Human Rights", R.drawable.category_advocacy);
        interestIcons.put("Animals", R.drawable.category_animals);
        interestIcons.put("Arts & Culture", R.drawable.category_art);
        interestIcons.put("Children & Youth", R.drawable.category_children);
        interestIcons.put("Community", R.drawable.category_community);
        interestIcons.put("Crisis Support", R.drawable.category_crisis);
        interestIcons.put("Disaster Relief", R.drawable.category_disaster);
        interestIcons.put("Education & Literacy", R.drawable.category_education);
        interestIcons.put("Emergency & Safety", R.drawable.category_emergency);
        interestIcons.put("Environment", R.drawable.category_environment);
        interestIcons.put("Faith-Based", R.drawable.category_faith);
        interestIcons.put("Health & Medicine", R.drawable.category_health);
        interestIcons.put("Homeless & Housing", R.drawable.category_homeless);
        interestIcons.put("Hunger", R.drawable.category_hunger);
        interestIcons.put("Immigrants & Refugees", R.drawable.category_immigration);
        interestIcons.put("International", R.drawable.category_international);
        interestIcons.put("Justice & Legal", R.drawable.category_justice);
        interestIcons.put("LGBTQ+", R.drawable.category_lgbtq);
        interestIcons.put("Misc & Other", R.drawable.category_misc);
        interestIcons.put("People with Disabilities", R.drawable.category_disabilities);
        interestIcons.put("Politics", R.drawable.category_politics);
        interestIcons.put("Race & Ethnicity", R.drawable.category_race);
        interestIcons.put("Seniors", R.drawable.category_seniors);
        interestIcons.put("Special Needs", R.drawable.category_special_needs);
        interestIcons.put("Technology", R.drawable.category_technology);
        interestIcons.put("Women", R.drawable.category_women);
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
        private ImageView ivIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // set views
            tvName = itemView.findViewById(R.id.item_interest_tv);
            ivIcon = itemView.findViewById(R.id.item_interest_icon);

            // set current user values
            currentParseUser = ParseUser.getCurrentUser();
            currentUser = new User(currentParseUser);

            // set listeners
            itemView.setOnClickListener(this);
        }

        public void bind(String interest) {
            tvName.setText(interest);
            // Log.i(TAG, interest + " : " + interestIcons.get(interest));
            ivIcon.setBackgroundResource(interestIcons.get(interest));

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


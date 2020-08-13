package com.example.charibee.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.charibee.models.User;
import com.example.service.R;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterestsIconAdapter extends RecyclerView.Adapter<InterestsIconAdapter.ViewHolder> {

    public static final String TAG = "InterestsIconAdapter";

    // data
    private Context context;
    private List<String> interests;
    private Map<String, Integer> interestIcons = new HashMap<>();

    // current user
    ParseUser currentParseUser = ParseUser.getCurrentUser();
    User currentUser = new User(currentParseUser);

    public InterestsIconAdapter(Context context, List<String> interests) {
        this.context = context;
        this.interests = interests;
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

    @NonNull
    @Override
    public InterestsIconAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_icon_interest, parent, false);
        return new InterestsIconAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String interest = interests.get(position);
        holder.bind(interest);
    }

    public int getItemCount() {
        return interests.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private String interest;
        private ImageView ivIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // set views
            ivIcon = itemView.findViewById(R.id.item_icon_interest_iv);

            // set current user values
            currentParseUser = ParseUser.getCurrentUser();
            currentUser = new User(currentParseUser);

            // set on click listener to self
            itemView.setOnClickListener(this);
        }

        public void bind(String interestString) {
            interest = interestString;
            ivIcon.setBackgroundResource(interestIcons.get(interestString));
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(context, interest, Toast.LENGTH_SHORT).show();
        }
    }
}


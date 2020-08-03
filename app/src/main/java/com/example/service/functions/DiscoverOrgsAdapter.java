package com.example.service.functions;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.service.R;
import com.example.service.activities.OrganizationDetailsActivity;
import com.example.service.models.Organization;
import com.example.service.models.User;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

public class DiscoverOrgsAdapter extends RecyclerView.Adapter<DiscoverOrgsAdapter.ViewHolder> {

    public static final String TAG = "DiscoverOrgsAdapter";

    public interface OnLongClickListener {
        void onItemLongClicked(int position);
    }

    private Context context;
    private List<Organization> orgs;
    private OnLongClickListener longClickListener;

    public DiscoverOrgsAdapter(Context context, List<Organization> orgs, OnLongClickListener longClickListener) {
        this.context = context;
        this.orgs = orgs;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_org, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Organization org = orgs.get(position);
        holder.bind(org);

    }

    @Override
    public int getItemCount() {
        return orgs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ParseUser currentParseUser;
        User currentUser;

        private TextView tvName;
        private TextView tvCategory;
        private TextView tvTagline;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // set views
            tvName = itemView.findViewById(R.id.item_org_name);
            tvCategory = itemView.findViewById(R.id.item_org_category);
            tvTagline = itemView.findViewById(R.id.item_org_tagline);

            // set current user values
            currentParseUser = ParseUser.getCurrentUser();
            currentUser = new User(currentParseUser);

            // when user swipes right
            itemView.setOnTouchListener(new OnSwipeTouchListener(context) {

                // when user swipes right, join org
                @Override
                public void onSwipeRight() {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Organization org = orgs.get(position);
                        org.addVolunteer(currentParseUser);
                        currentUser.addOrg(org);

                        makeMessage("You have successfully joined the group " + org.getName() + "!");
                    }
                }

                // when user swipes left, leave org
                @Override
                public void onSwipeLeft() {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Organization org = orgs.get(position);

                        // remove org from current user
                        currentUser.removeOrg(org);

                        // remove current user from org
                        org.removeVolunteer(currentParseUser);

                        // tell user successfully left org
                        makeMessage("You have successfully left the group " + org.getName() + "!");
                    }
                }

                // when users clicks
                @Override
                public void onClick() {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Organization org = orgs.get(position);
                        Intent intent = new Intent(context, OrganizationDetailsActivity.class);
                        intent.putExtra(Organization.class.getSimpleName(), Parcels.wrap(org));
                        context.startActivity(intent);
                    }
                }
            });


        }

        public void bind(Organization org) {
            tvName.setText(org.getName());
            tvCategory.setText(org.getCategory());
            tvTagline.setText(org.getTagline());
        }


    }

    private void makeMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

    }


}


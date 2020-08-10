package com.example.service.adapters;

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
import com.google.android.material.card.MaterialCardView;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

public class DiscoverOrgsAdapter extends RecyclerView.Adapter<DiscoverOrgsAdapter.ViewHolder> {

    public static final String TAG = "DiscoverOrgsAdapter";

    // data
    private Context context;
    private List<Organization> orgs;

    // current user
    ParseUser currentParseUser = ParseUser.getCurrentUser();
    User currentUser = new User(currentParseUser);

    public DiscoverOrgsAdapter(Context context, List<Organization> orgs) {
        this.context = context;
        this.orgs = orgs;
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

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private MaterialCardView cardView;
        private TextView tvName;
        private TextView tvCategory;
        private TextView tvTagline;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // set views
            cardView = itemView.findViewById(R.id.item_org_card_view);
            tvName = itemView.findViewById(R.id.item_org_name);
            tvCategory = itemView.findViewById(R.id.item_org_category);
            tvTagline = itemView.findViewById(R.id.item_org_tagline);

            // set current user values
            currentParseUser = ParseUser.getCurrentUser();
            currentUser = new User(currentParseUser);

            // set listeners
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void bind(Organization org) {
            tvName.setText(org.getName());
            tvCategory.setText(org.getCategory());
            tvTagline.setText(org.getTagline());
        }

        // view org
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Organization org = orgs.get(position);
                Intent intent = new Intent(context, OrganizationDetailsActivity.class);
                intent.putExtra(Organization.class.getSimpleName(), Parcels.wrap(org));
                context.startActivity(intent);
            }
        }

        // join or leave org
        @Override
        public boolean onLongClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Organization org = orgs.get(position);
                if (!currentUser.containsOrg(org.getObjectId())) {
                    cardView.setBackgroundResource(R.drawable.card_view_border_green);
                    joinOrg(org);
                    new java.util.Timer().schedule(new java.util.TimerTask() {
                        @Override
                        public void run() {
                            cardView.setBackgroundResource(R.drawable.card_view_no_border);
                        }},
                            1500
                    );
                } else {
                    cardView.setBackgroundResource(R.drawable.card_view_border_red);
                    leaveOrg(org);
                    new java.util.Timer().schedule(new java.util.TimerTask() {
                       @Override
                       public void run() {
                           cardView.setBackgroundResource(R.drawable.card_view_no_border);
                       }},
                            1500
                    );
                }
            }
            return true;
        }
    }

    private void makeMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private void joinOrg(Organization org) {
        org.addVolunteer(currentParseUser);
        currentUser.addOrg(org);
        makeMessage("You have successfully joined the group " + org.getName() + "!");

    }

    private void leaveOrg(Organization org) {
        currentUser.removeOrg(org);
        org.removeVolunteer(currentParseUser);
        makeMessage("You have successfully left the group " + org.getName() + "!");
    }

}


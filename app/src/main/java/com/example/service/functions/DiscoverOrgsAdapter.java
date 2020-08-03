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

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvName;
        private TextView tvCategory;
        private TextView tvTagline;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            tvName = itemView.findViewById(R.id.item_org_name);
            tvCategory = itemView.findViewById(R.id.item_org_category);
            tvTagline = itemView.findViewById(R.id.item_org_tagline);

            // when user swipes
            itemView.setOnTouchListener(new OnSwipeTouchListener(context) {
                @Override
                public void onSwipeRight() {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        ParseUser currentParseUser = ParseUser.getCurrentUser();
                        User currentUser = new User(currentParseUser);

                        Organization org = orgs.get(position);
                        org.addVolunteer(currentParseUser);
                        currentUser.addOrg(org);

                        Toast.makeText(context, "You have successfully joined the group " + org.getName() + "!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        public void bind(Organization org) {
            tvName.setText(org.getName());
            tvCategory.setText(org.getCategory());
            tvTagline.setText(org.getTagline());
        }


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
    }


}


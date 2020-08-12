package com.example.charibee.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.charibee.activities.OrganizationDetailsActivity;
import com.example.charibee.models.Organization;
import com.example.service.R;

import org.parceler.Parcels;

import java.util.List;

public class HomeOrgsAdapter extends RecyclerView.Adapter<HomeOrgsAdapter.ViewHolder> {

    public static final String TAG = "HomeOrgsAdapter";

    private Context context;
    private List<Organization> orgs;

    public HomeOrgsAdapter(Context context, List<Organization> orgs) {
        this.context = context;
        this.orgs = orgs;
    }

    @NonNull
    @Override
    public HomeOrgsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_org, parent, false);
        return new HomeOrgsAdapter.ViewHolder(view);
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
        private TextView tvTagline;
        private TextView tvCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            tvName = itemView.findViewById(R.id.item_org_name);
            tvTagline = itemView.findViewById(R.id.item_org_tagline);
            tvCategory = itemView.findViewById(R.id.item_org_category);
        }

        public void bind(Organization org) {
            tvName.setText(org.getName());
            tvTagline.setText(org.getTagline());
            tvCategory.setText(org.getCategory());
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
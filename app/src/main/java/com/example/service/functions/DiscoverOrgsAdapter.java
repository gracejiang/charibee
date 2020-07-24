package com.example.service.functions;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.service.OrganizationDetailsActivity;
import com.example.service.R;
import com.example.service.models.Organization;

import org.parceler.Parcels;

import java.util.List;

public class DiscoverOrgsAdapter extends RecyclerView.Adapter<DiscoverOrgsAdapter.ViewHolder> {

    public static final String TAG = "DiscoverOrgsAdapter";

    private Context context;
    private List<Organization> orgs;

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

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvName;
        private TextView tvDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            tvName = itemView.findViewById(R.id.item_org_name);
            tvDescription = itemView.findViewById(R.id.item_org_description);
        }

        public void bind(Organization org) {
            tvName.setText(org.getName());
            tvDescription.setText(org.getDescription());
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
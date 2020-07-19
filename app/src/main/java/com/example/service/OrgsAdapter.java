package com.example.service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.service.models.Organization;

import java.util.List;

public class OrgsAdapter extends RecyclerView.Adapter<OrgsAdapter.ViewHolder> {

    private Context context;
    private List<Organization> orgs;

    public OrgsAdapter(Context context, List<Organization> orgs) {
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

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.item_org_name);
            tvDescription = itemView.findViewById(R.id.item_org_description);
        }

        public void bind(Organization org) {
            tvName.setText(org.getName());
            tvDescription.setText(org.getDescription());
        }
    }


}

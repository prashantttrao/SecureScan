package com.example.securescan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder> {

    private final List<AppItem> appList;

    public AppAdapter(List<AppItem> appList) {
        this.appList = appList;
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_app, parent, false);
        return new AppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {
        AppItem app = appList.get(position);

        holder.nameTextView.setText(app.getName());

        String subtitle = app.getPackageName() + " • " + app.getPermissionCount() + " Permissions";
        holder.packageTextView.setText(subtitle);

        holder.iconImageView.setImageDrawable(app.getIcon());

        // --- RISK BADGE ---
        GradientDrawable badge = new GradientDrawable();
        badge.setShape(GradientDrawable.OVAL);

        if (app.getRiskLevel() == RiskEngine.RISK_HIGH) {
            badge.setColor(Color.parseColor("#FF3B30"));
        } else if (app.getRiskLevel() == RiskEngine.RISK_MEDIUM) {
            badge.setColor(Color.parseColor("#FF9500"));
        } else {
            badge.setColor(Color.parseColor("#34C759"));
        }
        holder.riskBadge.setBackground(badge);

        // --- CLICK LISTENER ---
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, DetailActivity.class);

                intent.putExtra("appName", app.getName());
                intent.putExtra("packageName", app.getPackageName());
                intent.putExtra("riskLevel", app.getRiskLevel());
                intent.putExtra("targetSdkVersion", app.getTargetSdkVersion()); // Pass SDK Version

                intent.putStringArrayListExtra("permissions", new ArrayList<>(app.getPermissions()));

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    static class AppViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView packageTextView;
        ImageView iconImageView;
        View riskBadge;

        public AppViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.itemAppName);
            packageTextView = itemView.findViewById(R.id.itemPackageName);
            iconImageView = itemView.findViewById(R.id.itemAppIcon);
            riskBadge = itemView.findViewById(R.id.riskBadgePlaceholder);
        }
    }
}
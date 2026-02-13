package com.example.securescan;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button scanButton;
    private RecyclerView appRecyclerView;
    private AppAdapter appAdapter;
    private List<AppItem> appList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Views
        scanButton = findViewById(R.id.scanButton);
        appRecyclerView = findViewById(R.id.appRecyclerView);

        // Setup Info Button -> Opens the new AboutActivity
        ImageButton infoButton = findViewById(R.id.infoButton);
        if (infoButton != null) {
            infoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openAboutPage();
                }
            });
        }

        // Setup RecyclerView
        appRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        appList = new ArrayList<>();
        appAdapter = new AppAdapter(appList);
        appRecyclerView.setAdapter(appAdapter);

        // Button Listener
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanUserVisibleApps();
            }
        });
    }

    // --- NEW METHOD TO OPEN ABOUT PAGE ---
    private void openAboutPage() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    private void scanUserVisibleApps() {
        PackageManager pm = getPackageManager();
        appList.clear();

        Toast.makeText(this, "Scanning apps...", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolvedApps = pm.queryIntentActivities(intent, 0);

        for (ResolveInfo resolveInfo : resolvedApps) {
            String appName = resolveInfo.loadLabel(pm).toString();
            String packageName = resolveInfo.activityInfo.packageName;
            Drawable icon = resolveInfo.loadIcon(pm);
            List<String> permissionsList = new ArrayList<>();
            int targetSdkVersion = 0;

            try {
                PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);

                // Get SDK Version
                targetSdkVersion = packageInfo.applicationInfo.targetSdkVersion;

                if (packageInfo.requestedPermissions != null) {
                    permissionsList.addAll(Arrays.asList(packageInfo.requestedPermissions));
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            // Calculate Risk
            int riskLevel = RiskEngine.calculateRisk(permissionsList, targetSdkVersion);

            // Add to list
            AppItem app = new AppItem(appName, packageName, icon, permissionsList, riskLevel, targetSdkVersion);
            appList.add(app);
        }

        // Sort by Risk Level
        Collections.sort(appList, new Comparator<AppItem>() {
            @Override
            public int compare(AppItem o1, AppItem o2) {
                int riskComparison = Integer.compare(o2.getRiskLevel(), o1.getRiskLevel());
                if (riskComparison != 0) return riskComparison;
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });

        appAdapter.notifyDataSetChanged();
    }
}
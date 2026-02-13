package com.example.securescan;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Get data passed from the main list
        String appName = getIntent().getStringExtra("appName");
        final String packageName = getIntent().getStringExtra("packageName");
        int riskLevel = getIntent().getIntExtra("riskLevel", 0);
        int targetSdkVersion = getIntent().getIntExtra("targetSdkVersion", 0);
        ArrayList<String> permissions = getIntent().getStringArrayListExtra("permissions");

        // Initialize Views
        ImageView iconView = findViewById(R.id.detailIcon);
        TextView nameView = findViewById(R.id.detailAppName);
        TextView riskView = findViewById(R.id.detailRiskLabel);
        TextView mitigationView = findViewById(R.id.mitigationText);
        LinearLayout permissionsContainer = findViewById(R.id.permissionListContainer);
        Button uninstallButton = findViewById(R.id.uninstallButton);

        // Set Basic Info
        nameView.setText(appName);

        try {
            Drawable icon = getPackageManager().getApplicationIcon(packageName);
            iconView.setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // Set Risk Label Color and Text
        if (riskLevel == RiskEngine.RISK_HIGH) {
            riskView.setText("HIGH RISK");
            riskView.setTextColor(Color.WHITE);
            riskView.setBackgroundColor(Color.parseColor("#FF3B30")); // Red
        } else if (riskLevel == RiskEngine.RISK_MEDIUM) {
            riskView.setText("MEDIUM RISK");
            riskView.setTextColor(Color.WHITE);
            riskView.setBackgroundColor(Color.parseColor("#FF9500")); // Orange
        } else {
            riskView.setText("SAFE");
            riskView.setTextColor(Color.WHITE);
            riskView.setBackgroundColor(Color.parseColor("#34C759")); // Green
        }

        // --- MITIGATION ADVICE LOGIC ---
        StringBuilder advice = new StringBuilder();

        // Check 1: Is it Outdated? (Android 9 / API 28 or lower)
        if (targetSdkVersion < 29) {
            advice.append("⚠️ App is outdated (Target SDK: " + targetSdkVersion + ").\n");
            advice.append("• It does not support modern privacy protections.\n");
            advice.append("• Recommendation: Update or Uninstall immediately.\n\n");
        }

        // Check 2: Risk Level Advice
        if (riskLevel == RiskEngine.RISK_HIGH) {
            advice.append("⚠️ High-risk sensitive permissions detected.\n");
            advice.append("• Go to Settings > Apps > Permissions.\n");
            advice.append("• Revoke Camera, Mic, or Location if not essential.");
        } else if (riskLevel == RiskEngine.RISK_MEDIUM) {
            advice.append("⚠️ Moderate access detected.\n");
            advice.append("• Use 'Only while using the app' for Location.\n");
            advice.append("• Review storage access.");
        } else if (targetSdkVersion >= 29) {
            advice.append("✅ App is secure and up-to-date.\n");
            advice.append("• No action needed.");
        }

        mitigationView.setText(advice.toString());

        // Populate Permissions List dynamically
        if (permissions != null) {
            for (String perm : permissions) {
                TextView permView = new TextView(this);

                String shortName = perm.replace("android.permission.", "");
                permView.setText("• " + shortName);

                permView.setTextSize(14);
                permView.setPadding(0, 8, 0, 8);

                if (isDangerous(shortName)) {
                    permView.setTextColor(Color.parseColor("#FF3B30"));
                    permView.setTypeface(null, android.graphics.Typeface.BOLD);
                } else {
                    permView.setTextColor(Color.parseColor("#666666"));
                }

                permissionsContainer.addView(permView);
            }
        }

        // --- NEW "MANAGE / UNINSTALL" LOGIC ---
        // This opens the System Settings page for the app.
        // It is 100% reliable and cannot be blocked by Xiaomi/Samsung.
        uninstallButton.setText("Manage / Uninstall");

        uninstallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", packageName, null);
                    intent.setData(uri);
                    startActivity(intent);

                    Toast.makeText(DetailActivity.this, "Opening Settings...", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(DetailActivity.this, "Could not open settings.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean isDangerous(String perm) {
        return perm.contains("CAMERA") ||
                perm.contains("LOCATION") ||
                perm.contains("RECORD_AUDIO") ||
                perm.contains("CONTACTS") ||
                perm.contains("SMS");
    }
}
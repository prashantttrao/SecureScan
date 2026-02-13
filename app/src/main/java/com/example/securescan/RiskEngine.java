package com.example.securescan;

import java.util.List;

public class RiskEngine {

    public static final int RISK_LOW = 0;
    public static final int RISK_MEDIUM = 1;
    public static final int RISK_HIGH = 2;

    public static int calculateRisk(List<String> permissions, int targetSdkVersion) {

        // 1. Check for Outdated SDK (Security Risk)
        // Android 9 (API 28) and below are considered insecure/legacy
        if (targetSdkVersion < 29) {
            return RISK_HIGH;
        }

        if (permissions == null || permissions.isEmpty()) {
            return RISK_LOW;
        }

        int riskScore = 0;

        for (String permission : permissions) {
            // High Risk Permissions
            if (permission.contains("CAMERA") ||
                    permission.contains("RECORD_AUDIO") ||
                    permission.contains("ACCESS_FINE_LOCATION") ||
                    permission.contains("READ_CONTACTS") ||
                    permission.contains("READ_SMS") ||
                    permission.contains("SEND_SMS")) {

                return RISK_HIGH; // If it has ANY of these, it's immediately High Risk
            }

            // Medium Risk Permissions
            if (permission.contains("ACCESS_COARSE_LOCATION") ||
                    permission.contains("READ_EXTERNAL_STORAGE") ||
                    permission.contains("WRITE_EXTERNAL_STORAGE") ||
                    permission.contains("READ_PHONE_STATE")) {

                riskScore++;
            }
        }

        // If it has multiple medium risks, we consider it High Risk
        if (riskScore >= 3) return RISK_HIGH;
        if (riskScore >= 1) return RISK_MEDIUM;

        return RISK_LOW;
    }
}
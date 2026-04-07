package com.codefellas.cargrab.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtil {

    private final SharedPreferences sharedPreferences;

    public PreferenceUtil(Context context) {
        this.sharedPreferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
    }

    public void setData(String data) {
        sharedPreferences.edit().putString("id", data).apply();
    }

    public void setAccountID(int id) {
        sharedPreferences.edit().putInt("account_id", id).apply();
    }

    public int getAccountID() {
        return sharedPreferences.getInt("account_id", -1);
    }

    public void setRole(String role) {
        sharedPreferences.edit().putString("role", role).apply();
    }

    public String getRole() {
        return sharedPreferences.getString("role", null);
    }
}
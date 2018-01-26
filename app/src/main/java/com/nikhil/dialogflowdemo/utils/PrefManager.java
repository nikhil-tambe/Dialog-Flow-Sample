package com.nikhil.dialogflowdemo.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by nikhil on 7/1/18.
 */

public class PrefManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    // Shared preferences file name
    private static final String PREF_NAME = "spf_app";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String KEY_AGENT_NAME = "KEY_AGENT_NAME";
    private static final String KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN";

    public PrefManager(Context context) {
        this.context = context;
        pref = this.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.apply();
    }

    public boolean isFirstTimeLaunch() {
        return true; //pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setAgentDetailsToken(String agentName, String accessToken){
        editor.putString(KEY_AGENT_NAME, agentName).apply();
        editor.putString(KEY_ACCESS_TOKEN, accessToken).apply();
    }

    public String getAccessToken(){
        return pref.getString(KEY_ACCESS_TOKEN, "");
    }

    public String getAgentName(){
        return pref.getString(KEY_AGENT_NAME, "");
    }

}
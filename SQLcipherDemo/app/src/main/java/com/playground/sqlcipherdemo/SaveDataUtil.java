package com.playground.sqlcipherdemo;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Talha Hasan Zia on 11-Jan-18.
 * <p></p><b>Description:</b><p></p> Utility class for handling SharedPreferences operations.
 * <p></p>
 * <b>Public Methods:</b><p></p> Only listing to public methods usage.
 */
public class SaveDataUtil {

    private Context context;
    private SharedPreferences sharedPreferences;
    private final String securityPhrase = "security";

    public SaveDataUtil(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public void setSecurityPhrase(String data) {
        sharedPreferences.edit().putString(securityPhrase, data).apply();
    }

    public String getSecurityPhrase() {
        return sharedPreferences.getString(securityPhrase,null);

    }


}

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

    private Context context; // app context
    private SharedPreferences sharedPreferences; // SharedPreferences object


    private final String securityPhrase = "security";  // key for SharedPreferences field


    /**
     * Construtor call. This will initialize SharedPreferences object also, using MODE_PRIVATE,
     * and Package Name as SharedPreferences file key (name).
     * @param context app's context
     */
    public SaveDataUtil(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }


    // sets security phrase in shared preferences
    public void setSecurityPhrase(String data) {
        sharedPreferences.edit().putString(securityPhrase, data).apply();
    }

    // gets security phrase from preferences
    public String getSecurityPhrase() {
        return sharedPreferences.getString(securityPhrase,null);

    }


}

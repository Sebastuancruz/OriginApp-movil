package virtualdevelopercloud.origin_app_v;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;



public class SessionManager {
    SharedPreferences pref;


    SharedPreferences.Editor editor;


    Context _context;


    int PRIVATE_MODE = 0;


    private static final String PREF_NAME = "reg";


    private static final String IS_LOGIN = "IsLoggedIn";


    public static final String KEY_NAME = "Name";


    public static final String KEY_EMAIL = "Email";


    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void createLoginSession(String Name, String Email){

        editor.putBoolean(IS_LOGIN, true);


        editor.putString(KEY_NAME, Name);


        editor.putString(KEY_EMAIL, Email);


        editor.commit();
    }


    public void checkLogin(){

        if(!this.isLoggedIn()){

            Intent i = new Intent(_context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }

    }




    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        return user;
    }


    public void logoutUser(){
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, MainActivity.class);

       // i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        _context.startActivity(i);
    }







    public String getUser(){

        return pref.getString(KEY_NAME,"");
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void LogIN(){
        editor.remove(KEY_NAME);
    }


}
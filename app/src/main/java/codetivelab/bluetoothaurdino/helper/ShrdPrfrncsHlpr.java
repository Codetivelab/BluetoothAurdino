package codetivelab.bluetoothaurdino.helper;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ShrdPrfrncsHlpr {
    Context mContext;
    public ShrdPrfrncsHlpr(Context mContext) {
        this.mContext=mContext;
    }

    public void saveTrue() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        Boolean statusLocked = prefs.edit().putBoolean("locked", true).commit();
    }

    public Boolean getBoolean(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        if(!prefs.getBoolean("locked", false)){
            return false;
        }else {
            return true;
        }

    }
}

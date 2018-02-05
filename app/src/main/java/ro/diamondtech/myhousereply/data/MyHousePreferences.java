package ro.diamondtech.myhousereply.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import ro.diamontech.myhousereply.R;

/**
 * Created by user1 on 24/01/2018.
 */

public class MyHousePreferences {




 //   Returns true if the user has selected metric temperature display
    public static boolean isMetric(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String keyForUnits = context.getString(R.string.pref_units_key);
        String defaultUnits = context.getString(R.string.pref_units_metric);
        String preferredUnits = sp.getString(keyForUnits, defaultUnits);
        String metric = context.getString(R.string.pref_units_metric);

        boolean userPrefersMetric = false;
        if (metric.equals(preferredUnits)) {
            userPrefersMetric = true;
        }

        return userPrefersMetric;
    }

    //type of device in use (default is set on my custom old device
    public static String getPreferredDeviceSelection(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String keyForDeviceSet = context.getString(R.string.pref_device_set_key);
        String defaultDeviceSet = context.getString(R.string.pref_device_set_default);

        return sp.getString(keyForDeviceSet, defaultDeviceSet);
    }

    //get Server address with databases which is local or remote
    public static String getPreferredServerAddress(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String keyForServerAddress = context.getString(R.string.pref_server_address_key);
        String defaultServerAddress = context.getString(R.string.pref_server_address_default);//only local (remote not implemented yet)

        return sp.getString(keyForServerAddress, defaultServerAddress);
    }

}

package com.williamcomartin.plexpyremote.Helpers;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.williamcomartin.plexpyremote.ApplicationController;
import com.williamcomartin.plexpyremote.Helpers.Exceptions.NoServerException;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by wcomartin on 2016-11-14.
 */

public class UrlHelpers {
    private static final SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getInstance().getApplicationContext());

    public static String getImageUrl (String image, String width, String height, String fallback){
        try {
            return UrlHelpers.getHostPlusAPIKey()
                    + "&cmd=pms_image_proxy&width=" + width
                    + "&height=" + height
                    + "&img=" + image
                    + "&fallback=" + fallback;
        } catch (NoServerException | MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getImageUrl (String image, String width, String height){
        return getImageUrl(image, width, height, "poster");
    }

    public static String getHost () throws NoServerException, MalformedURLException {
        if(SP.getString("server_settings_address", "").trim().equals("")){
            throw new NoServerException();
        }

        String protocol = SP.getBoolean("server_settings_ssl", false) ? "https" : "http";
        String host = SP.getString("server_settings_address", "").trim();
        int port = Integer.parseInt(SP.getString("server_settings_port", "0"));
        String path = SP.getString("server_settings_path", "").trim();

        URL url = new URL(protocol, host, port, path);
        return url.toString();
//        return SP.getString("server_settings_address", "").trim();
    }

    public static String getHostPlusAPIKey () throws NoServerException, MalformedURLException {
        return getHost() + "/api/v2?apikey=" + SP.getString("server_settings_apikey", "").trim();
    }
}

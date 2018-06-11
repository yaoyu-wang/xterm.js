package com.example.android.test_tasti_menu;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {
    private static final String menuServer = "https://tasti-admin-staging.herokuapp.com/api/menu.json";

    private static final String format = "json";
    private static final String units = "metric";

    final static String QUERY_PARAM = "q";
    final static String LAT_PARAM = "lat";
    final static String LON_PARAM = "lon";
    final static String FORMAT_PARAM = "mode";
    final static String UNITS_PARAM = "units";
    final static String DAYS_PARAM = "cnt";

    final static String POST_METHOD = "POST";
    final static String signInServer = "https://tasti-admin-staging.herokuapp.com/sign_in";


    public static URL menuBuildUrl(){
        Uri builtUri = Uri.parse(menuServer).buildUpon()
                .appendQueryParameter(FORMAT_PARAM, format)
                .appendQueryParameter(UNITS_PARAM, units)
                .build();
        URL url = null;
        try{
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e){
            e.printStackTrace();
        }

        return url;
    }

    public static String menuGetResponseFromUrl(URL url) throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try{
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput){
                return scanner.next();
            }
            else{
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}

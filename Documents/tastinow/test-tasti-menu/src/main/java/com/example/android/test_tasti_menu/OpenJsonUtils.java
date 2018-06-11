package com.example.android.test_tasti_menu;

import android.content.ContentValues;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public class OpenJsonUtils {
    public static String[] getTempMenuStringsFromJson(Context context, String menu, String category)
            throws JSONException {
        final String CATEGORIES = "categories";
        final String ITEMS = "menu_items";

        String[] parsedMenuData = null;
        Log.v("menu", menu);
        JSONObject menuJson = new JSONObject(menu);

        JSONArray menuArray = menuJson.getJSONArray(CATEGORIES);
        parsedMenuData = new String[1];

        for (int i = 0; i < menuArray.length(); i++){
            //The header of one category
            JSONObject oneMenu = menuArray.getJSONObject(i);
            String name = oneMenu.getString("name");
            if (!name.equals(category)) continue;
            //String id = oneMenu.getString("id");
            //parsedMenuData[0] = name + "  " + id + "\n";

            //All the items in one category
            String thisCategoryStr = oneMenu.toString();
            Log.v("item", thisCategoryStr);
            JSONObject categoryObject = new JSONObject(thisCategoryStr);
            JSONArray categoryArray = categoryObject.getJSONArray(ITEMS);
            parsedMenuData = new String[categoryArray.length()];
            for (int j = 0; j < categoryArray.length(); j++){
                JSONObject oneItem = categoryArray.getJSONObject(j);
                String item_name = oneItem.getString("name");
                parsedMenuData[j] = item_name;
            }
            //parsedMenuData[0] += "\n";
        }

        return parsedMenuData;
    }

    public static String[] returnCategories(Context context, String menu) throws JSONException {
        final String CATEGORIES = "categories";
        String[] parsedMenuData = null;
        JSONObject menuJson = new JSONObject(menu);

        JSONArray menuArray = menuJson.getJSONArray(CATEGORIES);
        parsedMenuData = new String[menuArray.length()];

        for (int i = 0; i < menuArray.length(); i++) {
            JSONObject oneMenu = menuArray.getJSONObject(i);
            String name = oneMenu.getString("name");
            parsedMenuData[i] = name;
        }
        return parsedMenuData;
    }
}

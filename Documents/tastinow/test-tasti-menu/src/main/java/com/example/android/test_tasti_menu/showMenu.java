package com.example.android.test_tasti_menu;

import android.util.Log;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.net.URL;

public class showMenu extends AppCompatActivity{

    public String[] items;
    private RecyclerView mNumbersList;
    private MenuItemAdapter mAdapter;
    private TextView mMenuTextView;
    private TextView mErrorMsg;

    private ProgressBar mLoading;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //mMenuTextView = (TextView)findViewById(R.id.tv_menu_item);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)){
            String[] textEntered = intentThatStartedThisActivity.getStringArrayExtra(Intent.EXTRA_TEXT);
            Log.v("Error", textEntered[0]);
            Log.v("Error", textEntered[1]);
        }

        mErrorMsg = (TextView) findViewById(R.id.tv_error_msg);

        mLoading = (ProgressBar) findViewById(R.id.pb_loading);

        getCategories();

        while (items == null) {}

        Spinner spinner = (Spinner) findViewById(R.id.planets_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new showMenu.CategorySpinner());

        //loadMenu("Indian");
    }

    private void getCategories(){
        new GetCategories().execute();
    }

    private void loadMenu(String category){
        showMenuData();
        new GetMenu(category).execute();
    }

    private void showMenuData(){
        mErrorMsg.setVisibility(View.INVISIBLE);
        //mMenuTextView.setVisibility(View.VISIBLE);
    }

    private void showErrorMsg(){
        mErrorMsg.setVisibility(View.VISIBLE);
        //mMenuTextView.setVisibility(View.INVISIBLE);
    }

    public class GetMenu extends AsyncTask<String, Void, String[]> {

        public String category;

        public GetMenu(String category) {
            this.category = category;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... params) {
            URL menuRequestUrl = NetworkUtils.menuBuildUrl();
            try{
                String jsonMenuResponse = NetworkUtils.menuGetResponseFromUrl(menuRequestUrl);
                String[] tempMenuResult = OpenJsonUtils.getTempMenuStringsFromJson(showMenu.this, jsonMenuResponse, category);
                return tempMenuResult;
            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] menu) {
            mLoading.setVisibility(View.INVISIBLE);
            if (menu != null){
                showMenuData();
                mAdapter = new MenuItemAdapter(menu);
                mNumbersList = (RecyclerView) findViewById(R.id.rv_menu_data);
                LinearLayoutManager layoutManager = new LinearLayoutManager(showMenu.this);
                mNumbersList.setLayoutManager(layoutManager);
                mNumbersList.setHasFixedSize(true);
                mNumbersList.setAdapter(mAdapter);
                //for (String singleMenu : menu){
                //mMenuTextView.append(singleMenu + "\n\n\n");
                //}
            }
            else{
                showErrorMsg();
            }
        }
    }

    public class GetCategories extends AsyncTask<String, Void, String[]> {
        @Override
        protected String[] doInBackground(String... params) {
            URL menuRequestUrl = NetworkUtils.menuBuildUrl();
            try{
                String jsonMenuResponse = NetworkUtils.menuGetResponseFromUrl(menuRequestUrl);
                String[] tempMenuResult = OpenJsonUtils.returnCategories(showMenu.this, jsonMenuResponse);
                setItems(tempMenuResult);
                return tempMenuResult;
            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
    }

    public void setItems(String[] menuResults){
        this.items = new String[menuResults.length];
        for (int i = 0; i < menuResults.length; i++){
            this.items[i] = menuResults[i];
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.choosing_category, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sp_choose_category) {
            //mMenuTextView.setText("");
            //loadMenu();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class CategorySpinner extends Activity implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            // An item was selected. You can retrieve the selected item using
            // parent.getItemAtPosition(pos)
            String category = (String)parent.getItemAtPosition(pos);
            //mMenuTextView.setText("");
            loadMenu(category);
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
            String category = (String)parent.getItemAtPosition(0);
            //mMenuTextView.setText("");
            loadMenu(category);
        }
    }
}

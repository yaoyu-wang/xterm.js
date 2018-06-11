package com.example.android.test_tasti_menu;

import android.util.Log;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import java.net.URLEncoder;
import java.util.Map;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    private Button mLogInButton;
    private EditText mEmail;
    private EditText mPassword;
    private TextView mErrorMsg;
    private boolean success;
    private boolean ready;
    private String[] params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        success = false;

        mLogInButton = (Button)findViewById(R.id.button_log_in);
        mEmail = (EditText)findViewById(R.id.email_value);
        mPassword = (EditText)findViewById(R.id.password_value);
        params = new String[2];

        mLogInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ready = false;
                mErrorMsg = (TextView) findViewById(R.id.tv_login_error);
                mErrorMsg.setVisibility(View.INVISIBLE);
                String emailEntered = mEmail.getText().toString();
                String passwordEntered = mPassword.getText().toString();
                params[0] = emailEntered;
                params[1] = passwordEntered;
                logIn();

                while(!ready){}

                //if (params[0].equals("yaoyuw@tastinow.com") && params[1].equals("yaoyudevpw")) {
                if (success){

                    Context context = MainActivity.this;

                    Class menuActivity = showMenu.class;

                    Intent startShowMenu = new Intent(context, menuActivity);

                    startShowMenu.putExtra(Intent.EXTRA_TEXT, params);

                    startActivity(startShowMenu);

                }

                else{
                    showErrorMsg();
                }
            }
        });
    }

    public void logIn(){
        new LogIn(params).execute();
    }

    public void showErrorMsg(){
        mErrorMsg = (TextView) findViewById(R.id.tv_login_error);
        mErrorMsg.postDelayed(new Runnable() {
            public void run() {
                mErrorMsg.setVisibility(View.VISIBLE);
            }
        }, 1000);
    }

    public class LogIn extends AsyncTask<String, Void, String[]> {

        public String[] logInInfo;

        public LogIn(String[] params) {
            this.logInInfo = params;
        }

        public String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
            StringBuilder feedback = new StringBuilder();
            boolean first = true;

            for (Map.Entry<String, String> entry : params.entrySet()){
                if (first){
                    first = false;
                }
                else{
                    feedback.append("&");
                }

                feedback.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                feedback.append("=");
                feedback.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }

            Log.v("Error", feedback.toString());

            return feedback.toString();
        }

        public void getLogInData() throws IOException{
            HashMap<String, String> params = new HashMap<>();
            params.put("email", logInInfo[0]);
            params.put("password", logInInfo[1]);

            URL url = new URL("https://tasti-admin-staging.herokuapp.com/api/users/sign_in");
            HttpURLConnection client = null;
            try{
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");
                //client.setRequestProperty("multipart/form-data", "https://tasti-admin-staging.herokuapp.com/api/users/sign_in;charset=UTF-8");
                client.setDoInput(true);
                client.setDoOutput(true);

                OutputStream os = client.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8")
                );
                writer.write(getPostDataString(params));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = client.getResponseCode();

                Log.v("Error", String.valueOf(responseCode));

                if (responseCode == HttpURLConnection.HTTP_OK){
                    success = true;
                }

                ready = true;

            } catch (MalformedURLException e){
                e.printStackTrace();
            } finally{
                if (client != null){
                    client.disconnect();
                }
            }
        }

        @Override
        protected String[] doInBackground(String... params) {
            try{
                getLogInData();
            } catch (IOException e){
                e.printStackTrace();
            }
            /*
            URL logInRequestUrl = NetworkUtils.logInBuildUrl();
            try{
                String jsonLogInResponse = NetworkUtils.logInGetResponseFromUrl(logInRequestUrl);
                String[] logInResult = OpenJsonUtils.getLogInStringsFromJson(MainActivity.this, jsonLogInResponse, logInInfo);
                return logInResult;
            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
            */
            return new String[0];
        }

        @Override
        protected void onPostExecute(String[] menu) {
            /*
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
            */
        }
    }
}

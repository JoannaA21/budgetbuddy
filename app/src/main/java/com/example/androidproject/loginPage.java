package com.example.androidproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.ScatteringByteChannel;

import javax.net.ssl.HttpsURLConnection;

public class loginPage extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
    }

    public void loginbtn(View view) {
        new FetchData().execute("http://143.198.237.154/api/login");
//        Intent intent = new Intent(this, profilePage.class);
//        startActivity(intent);
    }


    private class FetchData extends AsyncTask<String, Void, String> {
        String email =  editTextEmail.getText().toString();
        String password =  editTextPassword.getText().toString();

        JSONObject userlogin = new JSONObject();



        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection urlConnection = null;

            try {
                userlogin.put("email", email);
                userlogin.put("password", password);

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            try {
                URL url = new URL(urls[0]);
                urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.connect();


                urlConnection.setRequestProperty("Content-Type", "application/json"); //set content type
                urlConnection.setRequestProperty("Accept", "application/json"); //only accepts json

                urlConnection.setDoOutput(true); // true if you intend to use the URL connection for output

                //send request to API
                try(DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream())){
                    dataOutputStream.writeBytes(userlogin.toString());
                }

                //response from API
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String  line;
                    while((line = br.readLine()) != null) {
                        Log.e("response: ", line);
                        System.out.println(line);
                    }
//                OutputStream outputStream = new BufferedOutputStream(urlConnection.getOutputStream()); //output stream handles data flowing out of a program
//                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));//writes contents of outputStream
//                writer.write(userlogin.toString()); //data
//
//
//                writer.flush(); //scans writer(which contains data)
//                writer.close();
//                outputStream.close();
//                Log.e("response: ", urlConnection.getResponseMessage());
//                urlConnection.disconnect();


            } catch(Exception e) {
                try {
                    throw new Exception(e);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }

            return null;

        }
    }

    public void CreateAccount(View view) {
        Intent intent = new Intent(this, registerPage.class);
        startActivity(intent);
    }
}
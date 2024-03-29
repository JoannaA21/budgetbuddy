package com.example.androidproject;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class loginPage extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Execute the PostRequestAsyncTask when the button is clicked
                new ProfileRequestAsyncTask().execute("http://143.198.237.154:3001/api/login");
            }
        });
        redirectWhenSuccessfulLogin();
    }


    protected class ProfileRequestAsyncTask extends AsyncTask<String, Void, String> {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        JSONObject userlogin = new JSONObject();

        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection urlConnection = null;
            String response = null;

            try {
                userlogin.put("email", email);
                userlogin.put("password", password);

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            try {
                // Create a HttpURLConnection object and set it up for a POST request
                URL url = new URL(urls[0]); // params[0] should be the URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true); // Allow writing data to the connection

                // Set any headers required by the server
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");

                // Convert JSON data to a string
                String jsonString = userlogin.toString();
                Log.d("PostRequestAsyncTask", "Request jsonString: " + jsonString);

                // Write the data to be sent in the request body
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(jsonString);
                outputStream.flush();
                outputStream.close();

                // Get the response from the server and read it
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder responseBuilder = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        responseBuilder.append(inputLine);
                    }
                    in.close();
                    response = responseBuilder.toString();
                } else {
                    response = "POST request failed with response code: " + responseCode;
                }

                // Close the connection
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                response = "Exception: " + e.getMessage();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            // Handle the response here
            if (result != null) {
                // Response received successfully, save info to Internal storage
                // For example, you can parse the JSON response and update UI components
                try {
                    JSONObject jsonResponse = new JSONObject(result); //result in JSON
                    writeToInternalStorage("credential.txt", result); //Save result in String to the internal storage

                } catch (JSONException e) {
                    e.printStackTrace();
                    // Handle JSON parsing error
                }

                Log.d("PostRequestAsyncTask", "Response: " + result);
            } else {
                // Handle error case, e.g., API request failed
                Log.d("PostRequestAsyncTask", "Response: " + result);
            }
        }

        public void writeToInternalStorage(String fileName, String content) {
            File path = getApplicationContext().getFilesDir();

            try {
                FileOutputStream writer = new FileOutputStream(new File(path, fileName));
                writer.write(content.getBytes());
                Log.d("Internal Storage", "Credentials: " + content);
                redirectWhenSuccessfulLogin();
            }catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void redirectWhenSuccessfulLogin() {
        Intent intent = new Intent(this, dashboardPageRoot.class);     //Change this to dashboard
        startActivity(intent);
    }
    public void CreateAccount(View view) {
        Intent intent = new Intent(this, registerPage.class);
        startActivity(intent);
    }
}
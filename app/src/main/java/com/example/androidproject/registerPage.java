package com.example.androidproject;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class registerPage extends AppCompatActivity {
    EditText input_firstname, input_lastname, input_email, input_password, input_confirmpw;
    Button registerButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        input_firstname = findViewById(R.id.input_firstname);
        input_lastname = findViewById(R.id.input_lastname);
        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);
        input_confirmpw = findViewById(R.id.input_confirmpw);
        registerButton = findViewById(R.id.registerButton);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateForm()) {
                    // Execute AsyncTask to send registration request
                    new RegisterRequestAsyncTask().execute("http://143.198.237.154:3001/api/createuser");
                    LoginPage(v);
                }
            }
        });
    }

    private boolean validateForm() {
        String fname = input_firstname.getText().toString().trim();
        String lname = input_lastname.getText().toString().trim();
        String email = input_email.getText().toString().trim();
        String password = input_password.getText().toString().trim();
        String confirmpw = input_confirmpw.getText().toString();

        // Validation of user input
        if (TextUtils.isEmpty(fname)) {
            input_firstname.setError("First name is required");
            return false;
        }

        if (TextUtils.isEmpty(lname)) {
            input_lastname.setError("Last name is required");
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            input_email.setError("Email is required");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            input_password.setError("Password is required");
            return false;
        }

        if (!password.equals(confirmpw)) {
            input_confirmpw.setError("Passwords do not match");
            return false;
        }

        return true;
    }

    protected class RegisterRequestAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection urlConnection = null;
            String response = null;

            try {
                // Retrieve user input
                String fname = input_firstname.getText().toString();
                String lname = input_lastname.getText().toString();
                String email = input_email.getText().toString();
                String password = input_password.getText().toString();

                // Create JSON object with user data
                JSONObject createAccount = new JSONObject();

                createAccount.put("fname", fname);
                createAccount.put("lname", lname);
                createAccount.put("email", email);
                createAccount.put("password", password);

                // Create connection and send request
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true); // Allow writing data to the connection
                connection.setRequestProperty("Content-Type", "application/json");

                String jsonString = createAccount.toString(); //convert to String

                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(jsonString);
                outputStream.flush();
                outputStream.close();

                // Read response from server
                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    StringBuilder responseBuilder = new StringBuilder();

                    String line;

                    while ((line = in.readLine()) != null) {
                        responseBuilder.append(line);
                    }

                    in.close();

                    response = responseBuilder.toString();

                } else {
                    response = "POST request failed with response code: " + responseCode;
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                response = "Exception: " + e.getMessage();
            }

            return response;
        }

        protected void onPostExecute(String result) {
            //NEED TO IMPLEMENT...
            if(result != null) {
                Log.d("RegisterRequestAsyncTask", "Response: Successful " + result);
            }else {
                Log.d("RegisterRequestAsyncTask", "Response: Unsuccessful " + result);
            }
        }

    }


    //Triggered when Login textView is clicked. Screen redirects to Login page
    public void LoginPage(View view) {
        Intent intent = new Intent(this, loginPage.class);
        startActivity(intent);
    }

}

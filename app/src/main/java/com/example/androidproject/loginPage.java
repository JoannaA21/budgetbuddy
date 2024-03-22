package com.example.androidproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class loginPage extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

//        Button loginButton = findViewById(R.id.loginButton);
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Execute the PostRequestAsyncTask when the button is clicked
//                new ProfileRequestAsyncTask().execute("http://143.198.237.154:3001/api/login");
//            }
//        });
    }

    protected class ProfileRequestAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String response = null;
            try {
                // Create a HttpURLConnection object and set it up for a POST request
                URL url = new URL(urls[0]); // params[0] should be the URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true); // Allow writing data to the connection

                // Set any headers required by the server
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");

                // Construct JSON data using JsonBuilder
                JSONObject jsonData = buildJsonData();
                Log.d("PostRequestAsyncTask", "Request jsonData: " + jsonData);

                // Convert JSON data to a string
                String jsonString = jsonData.toString();
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
            } catch (JSONException | IOException e) {
                e.printStackTrace();
                response = "Exception: " + e.getMessage();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            // Handle the response here
            if (result != null) {
                // Response received successfully, do something with it
                // For example, you can parse the JSON response and update UI components
//                try {
//                    JSONObject jsonResponse = new JSONObject(result);
//                    // Parse the JSON response and extract data
//                    String message = jsonResponse.optString("message");
//                    // Update UI components with the extracted data
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    // Handle JSON parsing error
//                }

                Log.d("PostRequestAsyncTask", "Response: " + result);
            } else {
                // Handle error case, e.g., API request failed
                Log.d("PostRequestAsyncTask", "Response: " + result);
            }
        }

        private JSONObject buildJsonData() throws JSONException {
            // Construct JSON data using JsonBuilder
            JSONObject jsonData = new JSONObject();
            jsonData.put("email", "sampinto@gmail.com");
            jsonData.put("password", "12345");
            // Add more key-value pairs as needed
            return jsonData;
        }
    }
}

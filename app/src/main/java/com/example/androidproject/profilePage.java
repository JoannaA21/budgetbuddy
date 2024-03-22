package com.example.androidproject;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class profilePage extends AppCompatActivity {
//    public class Profile {
//        private String name;
//        private String id;
//        private String email;
//    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        try {

            JSONObject credentialIS = getInternalStorage("credential.txt");
            JSONObject retrieve = credentialIS.getJSONObject("details");


            Log.d("creds", "Creds id: " + retrieve.getString("id") +" " + retrieve.getString("email"));
        } catch(JSONException e) {
            throw new RuntimeException(e);
        }


//        Profile details = gson.fromJson(credentialIS, Profile.class);
//        String id = details.id;
//        Log.d("creds", "Creds id: " + id );
    }


    //Get Internal Storage from App
    public JSONObject getInternalStorage(String fileName) {
        File path = getApplicationContext().getFilesDir();
        File readFrom = new File(path, fileName);
        byte[] content = new byte[(int) readFrom.length()];

        try {
            FileInputStream stream = new FileInputStream(readFrom);
            stream.read(content);
            String json = new String(content);
            return new JSONObject(json);
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }
//    public String getInternalStorage(String fileName) {
//        File path = getApplicationContext().getFilesDir();
//        File readFrom = new File(path, fileName);
//        byte[] content = new byte[(int)readFrom.length()];
//
//        try{
//            FileInputStream stream = new FileInputStream(readFrom);
//            stream.read(content);
//            String json = new Gson().toJson(new String (content)); //Credentials.txt to JSON
//            return json;
//        } catch(IOException e) {
//            throw new RuntimeException(e);
//        }
//    }



}

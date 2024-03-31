package com.example.androidproject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Utilities {
    public static void writeToInternalStorage(Context context, String fileName, String content) {
        File path = context.getFilesDir();

        FileOutputStream writer = null;
        try {
            writer = new FileOutputStream(new File(path, fileName));
            writer.write(content.getBytes());
            Log.d("Internal Storage", "Content written to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error writing to internal storage", Toast.LENGTH_SHORT).show();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String fetchDataFromUrl(String url) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String response = null;

        try {
            URL apiUrl = new URL(url);
            urlConnection = (HttpURLConnection) apiUrl.openConnection();
            urlConnection.setRequestMethod("GET");

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();

            if (inputStream == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            response = buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }

    public static boolean isFileExists(Context context,String fileName) {
        File path = context.getFilesDir();
        File file = new File(path, fileName);
        return file.exists();
    }

    public static JSONArray getJSONArrayFromInternalStorage(Context context,String fileName) {
        File path = context.getFilesDir();
        File file = new File(path, fileName);
        byte[] content = new byte[(int) file.length()];

        try {
            FileInputStream stream = new FileInputStream(file);
            stream.read(content);
            String json = new String(content);

            if (json.trim().startsWith("[")) {
                return new JSONArray(json);
            } else {
                // Log an error or handle the invalid JSON content
                Log.e("JSON Parsing", "Invalid JSON array content");
                return new JSONArray();
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    public static JSONObject getInternalStorage(Context context,String fileName) {
        File path = context.getFilesDir();
        File readFrom = new File(path, fileName);
        byte[] content = new byte[(int) readFrom.length()];

        try {

            FileInputStream stream = new FileInputStream(readFrom);
            stream.read(content);
            String json = new String(content);
            Log.d("This is thw typw", json.getClass().getSimpleName());
            Log.d("This is thw value", json);

            JSONObject test = new JSONObject(json);

            //Log.d("This is inside the internal s", test.getClass().getSimpleName());
            return test;
           // return new JSONObject(json);
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }
}

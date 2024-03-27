package com.example.androidproject;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profile#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class profile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profile.
     */
    // TODO: Rename and change types and number of parameters
    public static profile newInstance(String param1, String param2) {
        profile fragment = new profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public profile() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view,@Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView user_fullName, user_Email;
        user_fullName = view.findViewById(R.id.user_fullName);
        user_Email = view.findViewById(R.id.user_Email);
        String id = null;

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


        //For- after login
        try {
            JSONObject credentialIS = getInternalStorage("credential.txt");
            JSONObject retrieve = credentialIS.getJSONObject("details");
            id = retrieve.getString("id");

            user_fullName.setText(retrieve.getString("fname") + " " + retrieve.getString("lname"));
            user_Email.setText(retrieve.getString("email"));
           //Log.d("user", "info " + credentialIS.getString("details"));
            Log.d("creds", "Creds id: " + retrieve.getString("id") +" " + retrieve.getString("email"));
        } catch(JSONException e) {
            throw new RuntimeException(e);
        }


        new GoalsRequestAsynTask().execute("http://143.198.237.154:3001/api/getusergoal/" + id);
        try{
             JSONObject profilegoal = getInternalStorage("profilegoal.txt");
            Log.d("info","info " + profilegoal);

            try {
                writeToInternalStorage("profilegoal.txt", "{}");
            }catch(RuntimeException e) {
                e.printStackTrace();
            }

        }catch (RuntimeException e) {
            throw new RuntimeException(e);

        }


    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

//getApplicationContext()
    public JSONObject getInternalStorage(String fileName) {
        File path = getActivity().getFilesDir();
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

    protected class GoalsRequestAsynTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null; //reads file line by line
            String response = null;

            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream(); //read file
                StringBuilder buffer = new StringBuilder();


                if(inputStream == null) {
                    Toast.makeText(getActivity(), "Unable to connect.", Toast.LENGTH_SHORT).show();
                    return null;
                }

                //BufferReader reads JSON file from url
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                response = buffer.toString();

                if(urlConnection != null) {
                    urlConnection.disconnect(); //free up connection
                }

                if(reader != null) {
                    try{
                        reader.close();
                    } catch(IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                if(inputStream != null) {
                    inputStream.close();
                }


            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return response;
        }


        @Override
        public void onPostExecute(String jsonData) {
            //super.onPostExecute(jsonData);
            if (jsonData != null) {
                try {
                    JSONObject jsonResponse = new JSONObject(jsonData); //jsonData contents in JSON
                    String message = jsonResponse.optString("message"); //jsonData contents in String
                    writeToInternalStorage("profilegoal.txt", jsonData); //Save result in String to the internal storage

                } catch (JSONException e) {
                    e.printStackTrace();
                    // Handle JSON parsing error
                }
            }
        }
    }



    public void writeToInternalStorage(String fileName, String content) {
        //File path = getApplicationContext().getFilesDir();
        File path = getActivity().getFilesDir();
        try {
            FileOutputStream writer = new FileOutputStream(new File(path, fileName));
            writer.write(content.getBytes());
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
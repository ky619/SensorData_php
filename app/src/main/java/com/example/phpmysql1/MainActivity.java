package com.example.phpmysql1;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity {

    String myJSON;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_DAT = "dat";

    JSONArray irs = null;

    ArrayList<HashMap<String, String>> irsList;

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = (ListView) findViewById(R.id.listView);
        irsList = new ArrayList<>();
        getData("http://192.168.1.113/data.php");


    }


    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            irs = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < irs.length(); i++) {
                JSONObject c = irs.getJSONObject(i);
                String dat = c.getString(TAG_DAT);


                HashMap<String, String> irs = new HashMap<>();

                irs.put(TAG_DAT, dat);

                irsList.add(irs);
            }

            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, irsList, R.layout.list_item,
                    new String[]{TAG_DAT},
                    new int[]{R.id.dat}
            );

            list.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getData(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                myJSON = result;
                showList();
            }
        }

        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

}




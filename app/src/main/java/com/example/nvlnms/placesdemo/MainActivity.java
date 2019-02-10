package com.example.nvlnms.placesdemo;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    URL reqUrl = null;
    String res = null;
    JSONObject jobj;
    private List<PlaceInfo> plist=new ArrayList<>();
    private RecyclerView rv;
    private PlacesAdapter pAdapter;
    ImageView img;

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
        }
        return haveConnectedWifi;
    }

    private boolean checkMobileData(){
        boolean mobileDataEnabled = false;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            Class cmClass = Class.forName(cm.getClass().getName());
            Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true);
            // get the setting for "mobile data"
            mobileDataEnabled = (Boolean)method.invoke(cm);
            return mobileDataEnabled;
        } catch (Exception e) {
           Toast.makeText(this,"within catch of checkMobileData",Toast.LENGTH_LONG).show();
        }
        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img=(ImageView)findViewById(R.id.noResult);
        img.setVisibility(View.INVISIBLE);

        if(haveNetworkConnection()==false && checkMobileData()==false)
        {
            img.setVisibility(View.VISIBLE);
            img.setImageResource(R.drawable.nointernet);
        }

        else {
            Intent i = getIntent();
            String type = i.getStringExtra("type");
            Double lat=i.getDoubleExtra("lat",0.0);
            Double lon=i.getDoubleExtra("lon",0.0);

            try {
                reqUrl = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+lat+","+lon+"&radius=500&type=" + type +
                        "&keyword=" + "" + "&key=AIzaSyDBB6GngTfK61rV_8ArGtMcHd-qAGQE1jU");
            } catch (MalformedURLException e) {
                Toast.makeText(this, "Mal formed exception", Toast.LENGTH_LONG).show();
            }

            new RetrieveFeedTask().execute(reqUrl);
        }
    }

    class RetrieveFeedTask extends AsyncTask<URL, Void, String> {

        private Exception exception;
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(URL... url) {

            try {
                HttpURLConnection urlConnection = (HttpURLConnection) url[0].openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    int num;
                    char ch;

                    while((num=bufferedReader.read())!=-1)
                    {
                        if((num>=65 && num<=90) || (num>=97 && num<=122)) {
                            ch = (char) num;
                            stringBuilder.append(ch);
                        }
                        else if(num>=48 && num<=57)
                            stringBuilder.append((char)num);
                        else if(num!=10)
                        stringBuilder.append((char)num);
                    }

                    bufferedReader.close();
                    String res=stringBuilder.toString();
                    return stringBuilder.toString();
                }
                finally {
                    //urlConnection.disconnect();
                }
            }
            catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if (response == null) {
                progressBar.setVisibility(View.GONE);
                img.setImageResource(R.drawable.box);
            }
            else{
            progressBar.setVisibility(View.GONE);
            Log.i("INFO", response);
            jsonConverter(response);}
        }
    }

    public void jsonConverter(String res){
        rv=(RecyclerView)findViewById(R.id.recycler_view);

        pAdapter=new PlacesAdapter(plist);
        RecyclerView.LayoutManager pLm=new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(pLm);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        rv.setAdapter(pAdapter);

        try {
            String urlref = "";
            jobj = new JSONObject(res);
            JSONArray result = jobj.getJSONArray("results");

            if (result.equals(new JSONArray("[]")) == true){
                img.setVisibility(View.VISIBLE);
                img.setImageResource(R.drawable.box);
            }

            else {
                for (int i = 0; i < res.length(); i++) {
                    urlref = null;

                    String name = result.getJSONObject(i).getString("name").toString();
                    String address = result.getJSONObject(i).getString("vicinity").toString();

                    if (result.equals("[]") == true) {
                        img.setImageResource(R.drawable.box);
                    }

                    else {
                        if (result.getJSONObject(i).has("photos") == true) {
                            JSONArray pic = result.getJSONObject(i).getJSONArray("photos");
                            urlref = pic.getJSONObject(0).getString("photo_reference").toString();
                        }

                        PlaceInfo pi = new PlaceInfo(name, address, urlref);
                        plist.add(pi);
                        pAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
        catch(JSONException e)
        {
            pAdapter.notifyDataSetChanged();
        }
    }
}

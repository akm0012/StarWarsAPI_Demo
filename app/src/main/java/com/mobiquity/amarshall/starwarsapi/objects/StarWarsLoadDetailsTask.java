package com.mobiquity.amarshall.starwarsapi.objects;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by amarshall on 7/17/15.
 */
public class StarWarsLoadDetailsTask extends AsyncTask<String, Void, String> {

    private Context mContext;

    public interface StarWarsLoadDetailListener {
        public void display_data(String data);
        public void set_details_loading(boolean _isLoading);
        public void load_person_info(int _person_id);
    }

    public StarWarsLoadDetailsTask(Context _context) {

        mContext = _context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (mContext instanceof StarWarsLoadDetailListener) {
            ((StarWarsLoadDetailListener) mContext).set_details_loading(true);
        }
    }

    @Override
    protected String doInBackground(String... num) {

        String baseURL = "http://swapi.co/api/";
        String query = "";
        String data = "";

        try {
            query = baseURL + num[0];

            Log.i("tag", "Query: " + query);

            URL url = new URL(query);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();

            // Start getting data
            InputStream inputStream = conn.getInputStream();
            data = IOUtils.toString(inputStream);
            inputStream.close();

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i("tag", "Data: " + data);

        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (mContext instanceof StarWarsLoadDetailListener) {
            ((StarWarsLoadDetailListener) mContext).display_data(s);
            ((StarWarsLoadDetailListener) mContext).set_details_loading(false);
        }
    }
}

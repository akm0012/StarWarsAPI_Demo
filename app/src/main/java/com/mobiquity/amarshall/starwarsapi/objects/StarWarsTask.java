package com.mobiquity.amarshall.starwarsapi.objects;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.mobiquity.amarshall.starwarsapi.utils.FileObjectManager;
import com.mobiquity.amarshall.starwarsapi.utils.NetworkCheck;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by amarshall on 7/17/15.
 */
public class StarWarsTask extends AsyncTask<String, Void, String> {

    private Context mContext;

    public interface StarWarsListener {
        public void displayPersonInfo(String data);

        public void set_name_list(String data);

        public void set_list_loading(boolean _is_loading);
    }

    public StarWarsTask(Context _context) {

        mContext = _context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (mContext instanceof StarWarsListener) {
//            ((StarWarsListener) mContext).set_list_loading(true);
        }

    }

    @Override
    protected String doInBackground(String... num) {

        String baseURL = "http://swapi.co/api/";
        String query = "";
        String data = "";

        try {
            if (NetworkCheck.getStatus(mContext) < 0) {

                // Not in cache
                Toast.makeText(mContext, "No Network Connectivity.",
                        Toast.LENGTH_LONG).show();
            }

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

        if (mContext instanceof StarWarsListener) {
            ((StarWarsListener) mContext).set_name_list(s);
            ((StarWarsListener) mContext).set_list_loading(false);
        }
    }
}

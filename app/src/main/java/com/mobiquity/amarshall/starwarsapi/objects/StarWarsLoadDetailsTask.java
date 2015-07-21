package com.mobiquity.amarshall.starwarsapi.objects;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.mobiquity.amarshall.starwarsapi.fragments.Data_Display_Fragment;
import com.mobiquity.amarshall.starwarsapi.utils.FileObjectManager;
import com.mobiquity.amarshall.starwarsapi.utils.NetworkCheck;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by amarshall on 7/17/15.
 */
public class StarWarsLoadDetailsTask extends AsyncTask<String, Void, Entry> {

    private Context mContext;

    public interface StarWarsLoadDetailListener {
        public void display_data(Entry entry);

        public void load_person_info(int _person_id);
    }

    public StarWarsLoadDetailsTask(Context _context) {

        mContext = _context;
    }

    @Override
    protected Entry doInBackground(String... num) {

        String baseURL = "http://swapi.co/api/";
        String query = "";
        String data = "";
        Entry entry_out = null;
        String cache_id = "" + num[0].substring(7);

        Log.d("tag", "Cache ID: " + cache_id);

        try {

            // Check and see if we have a connection
            if (NetworkCheck.getStatus(mContext) < 0) { // If Not connected

                Entry cached_entry = FileObjectManager.get_from_cache(cache_id, mContext);

                // Check the cache
                if (cached_entry != null) {

                    cached_entry.setCached(true);

                    // Return the cached Entry
                    return cached_entry;
                }

                return null;
            }

            // Else we go out a fetch from API
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

            // Create the Entry from the JSON data
            entry_out = new Entry();
            JSONObject json = new JSONObject(data);

            entry_out.setmID(num[0]);
            entry_out.setmName(json.getString("name"));
            entry_out.setmHeight(json.getString("height"));
            entry_out.setmMass(json.getString("mass"));
            entry_out.setmHairColor(json.getString("hair_color"));
            entry_out.setmSkinColor(json.getString("skin_color"));
            entry_out.setmEyeColor(json.getString("eye_color"));
            entry_out.setmBirthYear(json.getString("birth_year"));
            entry_out.setmGender(json.getString("gender"));

            entry_out.setCached(false);

            // Add to out cache
            FileObjectManager.add_to_cache(cache_id, entry_out, mContext);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("tag", "Data: " + data);

        return entry_out;
    }

    @Override
    protected void onPostExecute(Entry _entry) {
        super.onPostExecute(_entry);

        if (mContext instanceof StarWarsLoadDetailListener) {
            ((StarWarsLoadDetailListener) mContext).display_data(_entry);
        }
    }
}

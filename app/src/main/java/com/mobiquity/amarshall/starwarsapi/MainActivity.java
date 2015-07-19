package com.mobiquity.amarshall.starwarsapi;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.mobiquity.amarshall.starwarsapi.fragments.Data_Display_Fragment;
import com.mobiquity.amarshall.starwarsapi.fragments.Name_List_Fragment;
import com.mobiquity.amarshall.starwarsapi.interfaces.Name_List_Interface;
import com.mobiquity.amarshall.starwarsapi.objects.StarWarsLoadDetailsTask;
import com.mobiquity.amarshall.starwarsapi.objects.StarWarsTask;
import com.mobiquity.amarshall.starwarsapi.utils.NetworkCheck;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends Activity implements StarWarsTask.StarWarsListener, StarWarsLoadDetailsTask.StarWarsLoadDetailListener {

    private int page_count = 1;
    private ArrayList<String> name_array_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name_array_list = new ArrayList<String>();


        String[] name_array = new String[name_array_list.size()];
        name_array = name_array_list.toArray(name_array);

        Log.e("tag", "Name Array List: " + name_array_list.toString());

        Name_List_Fragment name_list_fragment = Name_List_Fragment
                .newInstance(name_array);
        Data_Display_Fragment data_display_fragment = Data_Display_Fragment
                .newInstance();

        getFragmentManager().beginTransaction()
                .replace(R.id.left_container, name_list_fragment, Name_List_Fragment.TAG)
                .replace(R.id.right_container, data_display_fragment, Data_Display_Fragment.TAG)
                .commit();

        // Check for Network Connection
        if (NetworkCheck.getStatus(this) >= 0) {
            StarWarsTask starWarsTask = new StarWarsTask(this);

            starWarsTask.execute("people/");
        } else {
            Toast.makeText(this.getApplicationContext(), "No Network Connectivity.",
                    Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void displayPersonInfo(String data) {

        Log.d("tag", "Displaying Person Info: " + data);

    }

    @Override
    public void set_name_list(String data) {

        Log.d("tag", "(set_name_list) Data: " + data);

        try {
            JSONObject json = new JSONObject(data);

            JSONArray results = json.getJSONArray("results");
            String next = json.getString("next");

            Log.e("tag", "Results length: " + results.length());

            for (int i = 0; i < results.length(); i++) {
                Log.e("tag", "Adding name: " + results.getJSONObject(i).getString("name"));
                ((Name_List_Fragment) getFragmentManager().findFragmentByTag(Name_List_Fragment.TAG))
                        .refresh_name_list(results.getJSONObject(i).getString("name"));
            }

            page_count++;
            Log.i("tag", "Page Count: " + page_count);
            if (next.compareTo("null") != 0) {
                // Check for Network Connection
                if (NetworkCheck.getStatus(this) >= 0) {
                    StarWarsTask starWarsTask = new StarWarsTask(this);
                    starWarsTask.execute("people/?page=" + page_count);
                } else {
                    Toast.makeText(this.getApplicationContext(), "No Network Connectivity.",
                            Toast.LENGTH_LONG).show();
                }

            }

            else {
                Log.e("tag", "DONE LOADING!!!");
                Name_List_Fragment frag = (Name_List_Fragment) getFragmentManager().findFragmentByTag(Name_List_Fragment.TAG);
                frag.set_loading(false);
            }

            Log.e("tag", "Array List: " + name_array_list.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void load_person_info(int _person_id) {

        int id = _person_id;

        StarWarsLoadDetailsTask starWarsLoadDetailsTask = new StarWarsLoadDetailsTask(this);

        // Fixes an error with the API where ID of 17 is missing
        if (_person_id >= 17) {
            id = _person_id + 1;
        }

        Log.i("tag", " Loading ID: " + id);

        if (NetworkCheck.getStatus(this) >= 0) {
            starWarsLoadDetailsTask.execute("people/" + id);
        } else {
            Toast.makeText(this.getApplicationContext(), "No Network Connectivity.",
                    Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void display_data(String data) {

        Log.d("tag", "(display_data) Data: " + data);

        try {
            JSONObject json = new JSONObject(data);

            Data_Display_Fragment frag = (Data_Display_Fragment) getFragmentManager().findFragmentByTag(Data_Display_Fragment.TAG);
            frag.set_name(json.getString("name"));
            frag.set_height(json.getString("height"));
            frag.set_mass(json.getString("mass"));
            frag.set_hair_color(json.getString("hair_color"));
            frag.set_skin_color(json.getString("skin_color"));
            frag.set_eye_color(json.getString("eye_color"));
            frag.set_birth_year(json.getString("birth_year"));
            frag.set_gender(json.getString("gender"));

            frag.go_to_url(json.getString("name"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void set_list_loading(boolean _is_loading) {
//        Name_List_Fragment frag = (Name_List_Fragment) getFragmentManager().findFragmentByTag(Name_List_Fragment.TAG);
//        frag.set_loading(_is_loading);
    }

    @Override
    public void set_details_loading(boolean _isLoading) {
        Data_Display_Fragment frag = (Data_Display_Fragment) getFragmentManager().findFragmentByTag(Data_Display_Fragment.TAG);
        frag.set_loading(_isLoading);
    }
}

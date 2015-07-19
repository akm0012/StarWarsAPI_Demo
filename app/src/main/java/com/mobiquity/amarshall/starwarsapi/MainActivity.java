package com.mobiquity.amarshall.starwarsapi;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.mobiquity.amarshall.starwarsapi.fragments.Name_List_Fragment;
import com.mobiquity.amarshall.starwarsapi.interfaces.Name_List_Interface;
import com.mobiquity.amarshall.starwarsapi.objects.StarWarsTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends Activity implements StarWarsTask.StarWarsListener, Name_List_Interface {

    private int mNameIndex = 0;
    private int page_count = 1;
    private ArrayList<String> name_array_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name_array_list = new ArrayList<String>();

        StarWarsTask starWarsTask = new StarWarsTask(this);

        starWarsTask.execute("people/");

        mNameIndex = 0;

        String[] name_array = new String[name_array_list.size()];
        name_array = name_array_list.toArray(name_array);

        Log.e("tag", "Name Array List: " + name_array_list.toString());

        Name_List_Fragment name_list_fragment = Name_List_Fragment
                .newInstance(name_array);

        getFragmentManager().beginTransaction()
                .replace(R.id.left_container, name_list_fragment, Name_List_Fragment.TAG)
                .commit();

    }

    @Override
    public void displayPersonInfo(String data) {

        Log.d("tag", "Displaying Person Info: " + data);

//        ((TextView) findViewById(R.id.textView)).setText(data);

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
//                name_array_list.add(results.getJSONObject(i).getString("name"));
                ((Name_List_Fragment) getFragmentManager().findFragmentByTag(Name_List_Fragment.TAG))
                        .refresh_name_list(results.getJSONObject(i).getString("name"));
            }

            page_count++;
            Log.i("tag", "Page Count: " + page_count);
            if (next != null) {
                StarWarsTask starWarsTask = new StarWarsTask(this);
                starWarsTask.execute("people/?page=" + page_count);
            }

            Log.e("tag", "Array List: " + name_array_list.toString());



        } catch (JSONException e) {
            e.printStackTrace();
        }


//        name_list = data;

    }

    @Override
    public void load_person_info(int _person_id) {

        int id = _person_id;

        StarWarsTask starWarsTask = new StarWarsTask(this);

        // Fixes an error with the API where ID of 17 is missing
        if (_person_id >= 17 ) {
            id = _person_id + 1;
        }

        Log.i("tag", " Loading ID: " + id);

        starWarsTask.execute("people/" + id);

    }
}

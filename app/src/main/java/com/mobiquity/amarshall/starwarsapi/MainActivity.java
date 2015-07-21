package com.mobiquity.amarshall.starwarsapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.mobiquity.amarshall.starwarsapi.fragments.Data_Display_Fragment;
import com.mobiquity.amarshall.starwarsapi.fragments.Name_List_Fragment;
import com.mobiquity.amarshall.starwarsapi.interfaces.Name_List_Interface;
import com.mobiquity.amarshall.starwarsapi.objects.Entry;
import com.mobiquity.amarshall.starwarsapi.objects.StarWarsLoadDetailsTask;
import com.mobiquity.amarshall.starwarsapi.objects.StarWarsTask;
import com.mobiquity.amarshall.starwarsapi.utils.FileObjectManager;
import com.mobiquity.amarshall.starwarsapi.utils.NetworkCheck;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/*
BUG: something wierd going on when I start in LANDSCAPE MODE
 */

public class MainActivity extends Activity implements StarWarsTask.StarWarsListener, StarWarsLoadDetailsTask.StarWarsLoadDetailListener {

    private int page_count = 1;
    private ArrayList<String> name_array_list;
    private boolean landscape = false;

    public interface Name_List_Listener {
        public void refresh_name_list(String _newName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.container).getTag().equals("landscape")) {
            landscape = true;
        } else {
            landscape = false;
        }

        name_array_list = new ArrayList<String>();


        String[] name_array = new String[name_array_list.size()];
        name_array = name_array_list.toArray(name_array);

        Log.d("tag", "Name Array List: " + name_array_list.toString());

        if (landscape) {

            // Check to see if the fragment is there
            if (getFragmentManager().findFragmentByTag(Data_Display_Fragment.TAG) == null) {

                Log.i("tag", "Making new Data Display Fragment");

                Data_Display_Fragment data_display_fragment = Data_Display_Fragment
                        .newInstance();


                getFragmentManager().beginTransaction()
                        .replace(R.id.right_container, data_display_fragment, Data_Display_Fragment.TAG)
                        .commit();
            } else {
//                Log.i("tag", "Using old Data Display Fragment");
//
//                getFragmentManager().beginTransaction()
//                        .replace(R.id.right_container, getFragmentManager().findFragmentByTag(Data_Display_Fragment.TAG))
//                        .commit();
            }

            if (getFragmentManager().findFragmentByTag(Name_List_Fragment.TAG) == null) {

                Log.i("tag", "Making new Name List Fragment");

                Name_List_Fragment name_list_fragment = Name_List_Fragment
                        .newInstance(name_array);

                getFragmentManager().beginTransaction()
                        .replace(R.id.left_container, name_list_fragment, Name_List_Fragment.TAG)
                        .commit();

            } else {

//                Log.i("tag", "Using old Name List Fragment");
//
//                getFragmentManager().beginTransaction()
//                        .replace(R.id.left_container, getFragmentManager().findFragmentByTag(Name_List_Fragment.TAG))
//                        .commit();

            }
        } else {
            if (getFragmentManager().findFragmentByTag(Name_List_Fragment.TAG) != null) {

//                Log.i("tag", "Using old Name Fragment Fragment");
//
//                getFragmentManager().beginTransaction()
//                        .replace(R.id.left_container, getFragmentManager().findFragmentByTag(Name_List_Fragment.TAG))
//                        .commit();

            } else {
                Log.i("tag", "Making new Name List Fragment");

                Name_List_Fragment name_list_fragment = Name_List_Fragment
                        .newInstance(name_array);

                getFragmentManager().beginTransaction()
                        .replace(R.id.left_container, name_list_fragment, Name_List_Fragment.TAG).commit();
            }

        }

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
    public void set_name_list(String data) {

        Log.d("tag", "(set_name_list) Data: " + data);

        try {
            JSONObject json = new JSONObject(data);

            JSONArray results = json.getJSONArray("results");
            String next = json.getString("next");

            Log.d("tag", "Results length: " + results.length());

            for (int i = 0; i < results.length(); i++) {
                Log.d("tag", "Adding name: " + results.getJSONObject(i).getString("name"));

                if (getFragmentManager().findFragmentByTag(Name_List_Fragment.TAG) instanceof Name_List_Listener) {
                    Log.e("tag", "Inside IF statement.");
                    ((Name_List_Listener) getFragmentManager().findFragmentByTag(Name_List_Fragment.TAG)).refresh_name_list(results.getJSONObject(i).getString("name"));
                }



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

            } else {
                Log.d("tag", "DONE LOADING!!!");
                Name_List_Fragment frag = (Name_List_Fragment) getFragmentManager().findFragmentByTag(Name_List_Fragment.TAG);
                frag.set_loading(false);
            }

            Log.d("tag", "Array List: " + name_array_list.toString());


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

        starWarsLoadDetailsTask.execute("people/" + id);


    }

    @Override
    public void display_data(Entry _entry) {

        // Check to see if we are in landscape or portrait
        // Pass Entry to a new Intent
        if (!landscape) {
            Bundle entryBundle = new Bundle();
            entryBundle.putSerializable("entry", _entry);

            Intent showDetail = new Intent(this, DetailsActivity.class);

            showDetail.putExtras(entryBundle);
            startActivity(showDetail);
        } else {

            if (_entry == null) {

                // Not in cache
                Toast.makeText(this, "No Network Connectivity. Nothing Cached",
                        Toast.LENGTH_LONG).show();

                return;
            }

            if (_entry.isCached()) {
                Toast.makeText(this, "Showing Cached Result.",
                        Toast.LENGTH_LONG).show();
            }

            Data_Display_Fragment frag = (Data_Display_Fragment) getFragmentManager().findFragmentByTag(Data_Display_Fragment.TAG);

            frag.set_name(_entry.getmName());
            frag.set_height(_entry.getmHeight());
            frag.set_mass(_entry.getmMass());
            frag.set_hair_color(_entry.getmHairColor());
            frag.set_skin_color(_entry.getmSkinColor());
            frag.set_eye_color(_entry.getmEyeColor());
            frag.set_birth_year(_entry.getmBirthYear());
            frag.set_gender(_entry.getmGender());

            frag.go_to_url(_entry.getmName());
        }
    }


}

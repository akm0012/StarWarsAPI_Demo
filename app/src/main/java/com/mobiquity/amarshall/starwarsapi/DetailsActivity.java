package com.mobiquity.amarshall.starwarsapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mobiquity.amarshall.starwarsapi.fragments.Data_Display_Fragment;
import com.mobiquity.amarshall.starwarsapi.fragments.Name_List_Fragment;
import com.mobiquity.amarshall.starwarsapi.objects.Entry;


public class DetailsActivity extends Activity {

    Entry entry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Data_Display_Fragment data_display_fragment = Data_Display_Fragment
                .newInstance();

        getFragmentManager().beginTransaction()
                .replace(R.id.details_port_container, data_display_fragment, Data_Display_Fragment.TAG)
                .commit();

        Intent launchIntent = getIntent();

        if (launchIntent != null) {


            entry = (Entry) launchIntent.getExtras().getSerializable("entry");


        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        display_data(entry);

    }

    public void display_data(Entry _entry) {

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

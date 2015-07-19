package com.mobiquity.amarshall.starwarsapi;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.mobiquity.amarshall.starwarsapi.objects.StarWarsTask;


public class MainActivity extends Activity implements StarWarsTask.StarWarsListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StarWarsTask starWarsTask = new StarWarsTask(this);
        starWarsTask.execute("5");

    }

    @Override
    public void displayInfo(String data) {

        ((TextView) findViewById(R.id.textView)).setText(data);

    }
}

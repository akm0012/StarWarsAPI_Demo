package com.mobiquity.amarshall.starwarsapi.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mobiquity.amarshall.starwarsapi.R;
import com.mobiquity.amarshall.starwarsapi.interfaces.Name_List_Interface;
import com.mobiquity.amarshall.starwarsapi.objects.StarWarsTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by amarshall on 7/17/15.
 */
public class Name_List_Fragment extends Fragment {

    public static final String TAG = "Name_List_Frag";
    private int page_count = 1;
    private ArrayList<String> name_array_list;

    private Activity mContainingActivity;

    private ArrayAdapter<String> nameAdapter;

    // Factory
    public static Name_List_Fragment newInstance(String[] _names) {

        Bundle args = new Bundle();
        args.putStringArray("names", _names);

        Name_List_Fragment frag = new Name_List_Fragment();
        frag.setArguments(args);

        return frag;
    }

    @Override
    public void onAttach(Activity _activity) {
        super.onAttach(_activity);

        if (_activity instanceof Name_List_Interface) {
            mContainingActivity = _activity;
        } else {
            throw new IllegalArgumentException("Must implement Name_List_Interface"); // This should never happen
        }

    }

    @Override
    public View onCreateView(LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState) {

        name_array_list = new ArrayList<>();

        View view = _inflater.inflate(R.layout.selector_frag, _container, false);

//        String[] name_list = getArguments().getStringArray("names");

//        ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(mContainingActivity, android.R.layout.simple_list_item_1, name_list);
        nameAdapter = new ArrayAdapter<String>(mContainingActivity, android.R.layout.simple_list_item_1, name_array_list);


        ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(nameAdapter);

        listView.setOnItemClickListener(name_selector);

        return view;

    }

    private AdapterView.OnItemClickListener name_selector = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> _adapterView, View _view, int _position, long _id) {

            Log.i("tag", "onItemClick: " + _adapterView.getItemAtPosition(_position));


            // Because we checked we know this is a Name_List_Interface
            ((Name_List_Interface) mContainingActivity).load_person_info(_position + 1);

        }
    };


    public void refresh_name_list(String _newName) {
        Log.d("tag", "(refresh_name_list) New Name: " + _newName);

        nameAdapter.add(_newName);

        nameAdapter.notifyDataSetChanged();

    }
}

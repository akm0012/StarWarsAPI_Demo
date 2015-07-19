package com.mobiquity.amarshall.starwarsapi.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobiquity.amarshall.starwarsapi.R;
import com.mobiquity.amarshall.starwarsapi.interfaces.Name_List_Interface;

/**
 * Created by amarshall on 7/17/15.
 */
public class Name_List_Fragment extends Fragment {

    private Activity mContainingActivity;

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
        }
        else {
            throw new IllegalArgumentException("Must implement Name_List_Interface"); // This should never happen
        }

    }

    @Override
    public View onCreateView(LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState) {
        return super.onCreateView(_inflater, _container, _savedInstanceState);

        View view = _inflater.inflate(R.layout.selector_frag, _container, false);

    }
}

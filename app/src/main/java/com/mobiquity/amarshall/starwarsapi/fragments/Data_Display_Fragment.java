package com.mobiquity.amarshall.starwarsapi.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mobiquity.amarshall.starwarsapi.R;

/**
 * Created by Andrew on 7/19/15.
 */
public class Data_Display_Fragment extends Fragment {

    public static final String TAG = "Data_Display_Tag";
    private Activity mContainingActivity;

    // UI Views
    private WebView webView;
    private TextView textView_name;
    private TextView textView_height;
    private TextView textView_mass;
    private TextView textView_hair_color;
    private TextView textView_skin_color;
    private TextView textView_eye_color;
    private TextView textView_birth_year;
    private TextView textView_gender;
    private ProgressBar progressBar;

    // Factory
    public static Data_Display_Fragment newInstance() {

        Data_Display_Fragment frag = new Data_Display_Fragment();

        return frag;
    }

    @Override
    public void onAttach(Activity _activity) {
        super.onAttach(_activity);

//        if (_activity instanceof Name_List_Interface) {
        mContainingActivity = _activity;
//        } else {
//            throw new IllegalArgumentException("Must implement Name_List_Interface"); // This should never happen
//        }

    }

    @Override
    public View onCreateView(LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState) {


        View view = _inflater.inflate(R.layout.data_display_frag, _container, false);

        // Get handles to all out views
        webView = (WebView) view.findViewById(R.id.webView);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());

        textView_name = (TextView) view.findViewById(R.id.textView_name);
        textView_height = (TextView) view.findViewById(R.id.textView_height);
        textView_mass = (TextView) view.findViewById(R.id.textView_mass);
        textView_hair_color = (TextView) view.findViewById(R.id.textView_hair_color);
        textView_skin_color = (TextView) view.findViewById(R.id.textView_skin_color);
        textView_eye_color = (TextView) view.findViewById(R.id.textView_eye_color);
        textView_birth_year = (TextView) view.findViewById(R.id.textView_birth_year);
        textView_gender = (TextView) view.findViewById(R.id.textView_gender);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar_data);

        go_to_url("");

        return view;
    }

    public void go_to_url(String _url) {

        webView.loadUrl("https://www.google.com/search?q=" + _url + " Star Wars");
    }

    public void set_name(String _name) {
        textView_name.setText("Name: " + _name);
    }

    public void set_height(String _height) {
        textView_height.setText("Height: " + _height);
    }

    public void set_mass(String _mass) {
        textView_mass.setText("Mass: " + _mass);
    }

    public void set_hair_color(String _hair_color) {
        textView_hair_color.setText("Hair Color: " + _hair_color);
    }

    public void set_skin_color(String _skin_color) {
        textView_skin_color.setText("Skin Color: " + _skin_color);
    }

    public void set_eye_color(String _eye_color) {
        textView_eye_color.setText("Eye Color: " + _eye_color);
    }

    public void set_birth_year(String _birth_year) {
        textView_birth_year.setText("Birth Year: " + _birth_year);
    }

    public void set_gender(String _gender) {
        textView_gender.setText("Gender: " + _gender);
    }

    public void set_loading(Boolean _is_loading) {

        if (_is_loading) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }


    }


}

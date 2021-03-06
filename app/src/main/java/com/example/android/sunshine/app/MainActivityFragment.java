package com.example.android.sunshine.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;

import com.example.android.sunshine.app.data.WeatherContract;
import com.example.android.sunshine.app.data.WeatherProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements  LoaderManager.LoaderCallbacks<Cursor>{

    //String[] jola =null;
    private ForecastAdapter boo;
    private static final String[] FORECAST_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.LocationEntry.COLUMN_COORD_LAT,
            WeatherContract.LocationEntry.COLUMN_COORD_LONG,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID
    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    static final int COL_WEATHER_ID = 0;
    static final int COL_WEATHER_DATE = 1;
    static final int COL_WEATHER_DESC = 2;
    static final int COL_WEATHER_MAX_TEMP = 3;
    static final int COL_WEATHER_MIN_TEMP = 4;
    static final int COL_LOCATION_SETTING = 5;
    static final int COL_WEATHER_CONDITION_ID = 6;
    static final int COL_COORD_LAT = 7;
    static final int COL_COORD_LONG = 8;
    static final int COLUMN_WEATHER_ID=9;

    public MainActivityFragment() {
    }

    public void onCreate(Bundle savedInstanceData)
    {
        super.onCreate(savedInstanceData);
        setHasOptionsMenu(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview =  inflater.inflate(R.layout.fragment_main, container, false);
        String locationSetting = Utility.getPreferredLocation(getActivity());

        // Sort order:  Ascending, by date.
        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
        Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(
                locationSetting, System.currentTimeMillis());

        Cursor cur = getActivity().getContentResolver().query(weatherForLocationUri,
                null, null, null, sortOrder);
        boo =new ForecastAdapter(getActivity(),cur,0);



        ListView listview = (ListView)rootview.findViewById(R.id.listview_forecast);
        listview.setAdapter(boo);

        getLoaderManager().initLoader(0, null, this);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                                        // if it cannot seek to that position.
                                               Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                                if (cursor != null) {
                                        String locationSetting = Utility.getPreferredLocation(getActivity());
                                        Intent intent = new Intent(getActivity(), DetailActivity.class)
                                                        .setData(WeatherContract.WeatherEntry.buildWeatherLocationWithDate(
                                                                locationSetting, cursor.getLong(COL_WEATHER_DATE)
                                                                ));
                                        startActivity(intent);
                                    }
        }});
        return rootview;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater)
    {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    public void updateWeather(){
        FetchWeatherTask weatherTask = new FetchWeatherTask(getActivity());
        String location = Utility.getPreferredLocation(getActivity());
        weatherTask.execute(location);
        //getLoaderManager().initLoader(0,null,this);
    }

    //We do not need this. We have a database now.
  //  public void onStart()
    //{
   //     super.onStart();
   //     updateWeather();
   // }
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id= item.getItemId();
        if(id==R.id.action_refresh)
        {
            updateWeather();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseuri;
        baseuri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(Utility.getPreferredLocation(getContext()), System.currentTimeMillis());
        String sortOrder =WeatherContract.WeatherEntry.COLUMN_DATE+" ASC ";

        return new CursorLoader(getActivity(),baseuri,FORECAST_COLUMNS,null,null,sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            boo.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
            boo.swapCursor(null);
    }

    public void onLocationChanged(){
        updateWeather();
        getLoaderManager().restartLoader(0,null,this);
    }
}
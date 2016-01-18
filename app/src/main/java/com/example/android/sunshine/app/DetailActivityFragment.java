package com.example.android.sunshine.app;

//import android.app.LoaderManager;
//import android.content.CursorLoader;
import android.content.Intent;
//import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.ShareActionProvider;

import com.example.android.sunshine.app.data.WeatherContract;

import org.w3c.dom.Text;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private Uri uribase;
    private String mForecast;
    private TextView layout;
    private View rootView;
    class placeHolder{
        public final TextView datew;
        public final TextView hightemp;
        public final TextView lowtemp;
        public final TextView humidity;
        public final TextView pressure;
        public final TextView windspeed;
        public final ImageView iconview;
        public final TextView desc;

            placeHolder(View view)
            {
                datew=(TextView) view.findViewById(R.id.list_item_date_textview);
                hightemp =(TextView) view.findViewById(R.id.list_item_high_textview);
                lowtemp =(TextView) view.findViewById(R.id.list_item_low_textview);
                humidity =(TextView) view.findViewById(R.id.list_item_humidity_textview);
                pressure = (TextView)view.findViewById(R.id.list_item_pressure_textview);
                windspeed = (TextView) view.findViewById(R.id.list_item_wind_textview);
                iconview =(ImageView) view.findViewById(R.id.list_item_icon);
                desc =(TextView) view.findViewById(R.id.list_item_forecast_textview);

            }
    }

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
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_DEGREES,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID


    };

    private static final int COL_WEATHER_ID =0;
    private static final int COL_WEATHER_DATE=1;
    private static final int COL_WEATHER_DESC =2;
    private static final int COL_WEATHER_MAX_TEMP=3;
    private static final int COL_WEATHER_MIN_TEMP=4;
    private static final int COL_WEATHER_HUMIDITY=5;
    private static final int COL_WEATHER_PRESSURE=6;
    private static final int COL_WEATHER_WIND_SPEED=7;
    private static final int COl_WEATHER_WIND_DIRECTION=8;
    public DetailActivityFragment() {
    }

    public void onCreate(Bundle savedInstanceData)
    {
        super.onCreate(savedInstanceData);
        setHasOptionsMenu(true);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         rootView = inflater.inflate(R.layout.detailelements, container, false);
        //Intent intent = getActivity().getIntent();
        //String message = intent.getDataString();
        //uribase=Uri.parse(message);
       // Log.v("Sour", message);
        //layout = (TextView) rootView.findViewById(R.id.details_textview);
        //layout.setText(message);
        placeHolder place = new placeHolder(rootView);
        rootView.setTag(place);
        getLoaderManager().initLoader(0,null,this);
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Intent intent = getActivity().getIntent();
        if(intent==null)
        {
            return null;
        }
        return new CursorLoader(getActivity(),intent.getData(),FORECAST_COLUMNS,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //ForecastAdapter ass = new ForecastAdapter(getActivity().getApplicationContext(),data,0);
        if(!data.moveToFirst()) return ;
        placeHolder place = (placeHolder) rootView.getTag();
        double high = data.getDouble(COL_WEATHER_MAX_TEMP);
        double low = data.getDouble(COL_WEATHER_MIN_TEMP);
        boolean isMetric = Utility.isMetric(getContext());
        String format =Utility.getFriendlyDayString(getContext(),data.getLong(COL_WEATHER_DATE));                               //Utility.formatDate(data.getLong(COL_WEATHER_DATE));
        String highLowStr = Utility.formatTemperature(getContext(),high, isMetric) + "/" + Utility.formatTemperature(getContext(),low, isMetric);
        String desc = data.getString(COL_WEATHER_DESC);
        double humidity = data.getDouble(COL_WEATHER_HUMIDITY);
        double pressure = data.getDouble(COL_WEATHER_PRESSURE);
        String wind = Utility.getFormattedWind(getContext(),data.getFloat(COL_WEATHER_WIND_SPEED),data.getFloat(COl_WEATHER_WIND_DIRECTION));
        place.iconview.setImageResource(Utility.getArtResourceForWeatherCondition(data.getInt(data.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID))));
        place.datew.setText(format);
        place.hightemp.setText(getContext().getString(R.string.format_temperature,high));
        place.lowtemp.setText(getContext().getString(R.string.format_temperature,low));
        place.humidity.setText(getContext().getString(R.string.format_humidity,humidity));
        place.desc.setText(desc);
        place.pressure.setText(getContext().getString(R.string.format_pressure,pressure));
        place.windspeed.setText(wind);
        mForecast=highLowStr+"Dipayan Deb is the King of the world";

        //mForecast= format+" "+highLowStr+" "+desc;
        //layout.setText(mForecast);

        if(mShareActionProvider!=null){
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private ShareActionProvider mShareActionProvider;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.sharemenu,menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);

        mShareActionProvider =(ShareActionProvider) MenuItemCompat.getActionProvider(item);
        if(mForecast!=null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }

    }

    private Intent createShareForecastIntent()
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        //Intent k = getActivity().getIntent();
        String string = mForecast;
        intent.putExtra(Intent.EXTRA_TEXT, string);
        return intent;

    }
}

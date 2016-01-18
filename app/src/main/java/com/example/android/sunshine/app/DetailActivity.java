package com.example.android.sunshine.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.support.v7.widget.ShareActionProvider;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
/**
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
 **/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
    //private ShareActionProvider mShareActionProvider;
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_detail, menu);

       // MenuItem item = menu.findItem(R.id.menu_item_share);

       // mShareActionProvider =(ShareActionProvider) MenuItemCompat.getActionProvider(item);
       // setShareIntent(createShareForecastIntent());
        return true;
    }

    //private Intent createShareForecastIntent()
   // {
     //   Intent intent = new Intent(Intent.ACTION_SEND);
       // intent.setType("text/plain");
        //Intent k = getIntent();
       // String string = k.getStringExtra("String");
       // intent.putExtra(Intent.EXTRA_STREAM,string +" #Shunshine");
       // return intent;

    //}


    // Call to update the share intent
    //private void setShareIntent(Intent shareIntent) {
    //    if (mShareActionProvider != null) {
    //        mShareActionProvider.setShareIntent(shareIntent);
    //    }
    //    else{
    //        Log.v("IS null","Is null");
    //    }
//    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id= item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if(id==R.id.menu_item_share)
        {
            Log.v("IS fucked up","kutta");
        }
        return super.onOptionsItemSelected(item);
    }

}

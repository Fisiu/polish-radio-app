package pl.fidano.apps.polishradio;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pl.fidano.apps.polishradio.models.Radio;

public class PolishRadio extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener, Response.Listener<JSONArray>, Response.ErrorListener, AdapterView.OnItemClickListener {

    private final String TAG = "PolishRadioActivity";

    private final String API_URL = "http://testradio.fidano.pl/api/v1/radios";

    private ListView list;
    private RadiosAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<Radio> feed = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polish_radio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list = (ListView) findViewById(android.R.id.list);
        list.setOnItemClickListener(this);
        feed.add(new Radio("Logo url", "Name", "Url", "Stream url"));
        adapter = new RadiosAdapter(this, feed);
        list.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Action", null).show();
                stopService(new Intent(getApplicationContext(), PlayerService.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        VolleySingleton.getInstance(this).addToRequestQueue(new JsonArrayRequest(API_URL, this, this));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.polish_radio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRefresh() {
        VolleySingleton.getInstance(this).addToRequestQueue(new JsonArrayRequest(API_URL, this, this));
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mSwipeRefreshLayout.setRefreshing(false);
        Log.d("FETCH", error.getMessage());
        Toast.makeText(getBaseContext(), "Fetching data failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONArray response) {
        mSwipeRefreshLayout.setRefreshing(false);
        // response is a json array
        int size = response.length();
        Log.d("API RESPONSE", response.toString());
        Toast.makeText(getBaseContext(), "Items in array: " + size, Toast.LENGTH_SHORT).show();

        feed.clear();
        for (int i = 0; i < size; i++) {
            try {
                JSONObject obj = response.getJSONObject(i);
                feed.add(new Radio(obj.getString("logo_url"), obj.getString("name"), obj.getString("url"), obj.getString("stream_url")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Radio radio = (Radio) parent.getItemAtPosition(position);

        Intent intent = new Intent(this, PlayerService.class);
        intent.setAction("pl.fidano.apps.polishradio.action.PLAY");
        intent.putExtra("radio", radio);

        startService(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(TAG, "running onStop...");
//        stopService(new Intent(this, PlayerService.class));

    }
}

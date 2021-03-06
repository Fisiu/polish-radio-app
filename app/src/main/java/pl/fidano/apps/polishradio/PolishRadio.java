package pl.fidano.apps.polishradio;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import pl.fidano.apps.polishradio.models.Radio;
import pl.fidano.apps.polishradio.ui.RadioListFragment;

public class PolishRadio extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener, RadioListFragment.OnListFragmentInteractionListener {

    private static final String TAG = "PolishRadioActivity";
    private static final String FRAGMENT_TAG = "ListContainer";

    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Activity onStart()");

        setContentView(R.layout.activity_polish_radio);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
//        mSwipeRefreshLayout.setOnRefreshListener(this);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        RadioListFragment fragment = new RadioListFragment();
        transaction.replace(R.id.container, fragment, FRAGMENT_TAG);
        transaction.commit();

        initReceiver();
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
//        VolleySingleton.getInstance(this).addToRequestQueue(new JsonArrayRequest(API_URL, this, this));
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "running onStop...");

        super.onStop();
    }

    @Override
    public void onListFragmentInteraction(Radio item) {
        Log.d(TAG, "onListFragmentInteraction: " + item.getName());

        // play selected radio
        // TODO: Migrate to exoplayer
        Intent intent = new Intent(this, PlayerService.class);
        intent.setAction("pl.fidano.apps.polishradio.action.PLAY");
        intent.putExtra("radio", item);

        startService(intent);

    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "running onDestroy...");

        finishReceiver();

        super.onDestroy();
    }

    private void initReceiver() {
        mReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(mReceiver, filter);
    }

    private void finishReceiver() {
        unregisterReceiver(mReceiver);
    }
}

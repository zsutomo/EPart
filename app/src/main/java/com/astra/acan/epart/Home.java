package com.astra.acan.epart;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentTransaction fragmenTransaction;
    private TextView tv_userId;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        fragmenTransaction = getSupportFragmentManager().beginTransaction();
        fragmenTransaction.add(R.id.frame_layout, new DataEstimasiFragment());
        fragmenTransaction.commit();
        getSupportActionBar().setTitle("DataEstimasi");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        tv_userId = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_id);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        tv_userId.setText(firebaseUser.getEmail());


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finishAffinity();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
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

        switch (item.getItemId()) {
            case R.id.nav_uploadFile:

                Intent intent = new Intent(this, UploadFileActivity.class);
                startActivity(intent);
//                fragmenTransaction = getSupportFragmentManager().beginTransaction();
//                fragmenTransaction.replace(R.id.frame_layout, new UploadFile());
//                fragmenTransaction.commit();
//                getSupportActionBar().setTitle("Upload File");
//                item.setChecked(true);
                break;

            case R.id.nav_datapart:
                fragmenTransaction = getSupportFragmentManager().beginTransaction();
                fragmenTransaction.replace(R.id.frame_layout, new DataPartFragment());
                fragmenTransaction.commit();
                getSupportActionBar().setTitle("Data Part");
                item.setChecked(true);
                break;

            case R.id.nav_datacart:
                fragmenTransaction = getSupportFragmentManager().beginTransaction();
                fragmenTransaction.replace(R.id.frame_layout, new DataCartFragment());
                fragmenTransaction.commit();
                getSupportActionBar().setTitle("Data Cart");
                item.setChecked(true);
                break;

            case R.id.nav_dataestimasi:
                fragmenTransaction = getSupportFragmentManager().beginTransaction();
                fragmenTransaction.replace(R.id.frame_layout, new DataEstimasiFragment());
                fragmenTransaction.commit();
                getSupportActionBar().setTitle("Data Estimasi");
                item.setChecked(true);
                break;

            case R.id.nav_logout:
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                logout();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(Home.this, LoginActivity.class);
        startActivity(intent);
    }
}

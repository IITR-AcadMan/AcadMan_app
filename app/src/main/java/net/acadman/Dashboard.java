package net.acadman;

import android.app.Dialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setVisibility(View.INVISIBLE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                   startActivityForResult(intent, 1);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);


    }

    @Override
    public void onBackPressed() {
        FragmentManager fmfm=getSupportFragmentManager();
        Fragment jj=fmfm.findFragmentById(R.id.baseforfragment);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(jj instanceof Files){
            fm.beginTransaction().add(R.id.baseforfragment,new Courses()).commit();
        } else if(jj instanceof Courses){
            fm.beginTransaction().add(R.id.baseforfragment,new Home()).commit();
        }else if(jj instanceof Schedule){
            fm.beginTransaction().add(R.id.baseforfragment,new Home()).commit();
        } else if(jj instanceof Profile){
            fm.beginTransaction().add(R.id.baseforfragment,new Home()).commit();
        } else if(jj instanceof Prefrences){
            fm.beginTransaction().add(R.id.baseforfragment,new Home()).commit();
        }
        else{finishAffinity();
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();




        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

       if (id == R.id.nav_home) {
        fm.beginTransaction().add(R.id.baseforfragment,new Home()).commit();
        } else if (id == R.id.nav_schedule) {
           fm.beginTransaction().add(R.id.baseforfragment,new Schedule()).commit();
           FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
           fab.setVisibility(View.INVISIBLE);

        } else if (id == R.id.nav_courses) {
           fm.beginTransaction().add(R.id.baseforfragment,new Courses()).commit();

        } else if (id == R.id.nav_profile) {
           fm.beginTransaction().add(R.id.baseforfragment,new Profile()).commit();
           FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
           fab.setVisibility(View.INVISIBLE);
        } else if (id == R.id.nav_Prefrences) {
           fm.beginTransaction().add(R.id.baseforfragment,new Prefrences()).commit();
           FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
           fab.setVisibility(View.INVISIBLE);
        } else if (id == R.id.nav_logout) {
           FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
           fab.setVisibility(View.INVISIBLE);
             Logout logut = new Logout(this);
           logut.logging_out(Dashboard.this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {

        switch(requestCode){

            case 1:

                if(resultCode==RESULT_OK){

                    final AlertDialog  comment1= new AlertDialog.Builder(Dashboard.this)
                                    .setTitle("Comment")
                                    .setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .setNegativeButton("Cancel",null)
                                    .setView(R.layout.comment_dialog)
                                    .create();
                                  comment1.show();
                    comment1.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v){
                            EditText commentedittext = (EditText) (comment1).findViewById(R.id.commentedittext);
                            Intent str_up_ser  = new Intent (Dashboard.this,Upload_service.class);
                            str_up_ser.putExtra("uri",data.getData().toString());
                            str_up_ser.putExtra("comment",commentedittext.getText().toString().trim());
                            startService(str_up_ser);
                        comment1.dismiss();

                        }});


                }
                break;

        }
    }
}

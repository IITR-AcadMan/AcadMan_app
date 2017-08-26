package net.acadman;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.io.File;


public class Entry extends AppCompatActivity {
    ImageView logo;
    ImageView appname;
    String ff,tid,token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        logo = (ImageView)findViewById(R.id.logo);
        appname = (ImageView) findViewById(R.id.appname);
        Animation rotate = AnimationUtils.loadAnimation(this,R.anim.entrylogo);
        Animation fadeout = AnimationUtils.loadAnimation(this,R.anim.appname);
        logo.startAnimation(rotate);
        appname.startAnimation(fadeout);
        Handler login = new Handler();
        SharedPreferences mainprop = this.getSharedPreferences("Personal_Data", this.MODE_PRIVATE);
         tid  = mainprop.getString("tid", "null");
         token  = mainprop.getString("token","null");
        File dd = new File(Environment.getExternalStorageDirectory() + "/Android/data/", "net.acadman");
        if (!dd.exists()) {
            dd.mkdir();
        }
        try{
        File temp_clear = new File(Environment.getExternalStorageDirectory() + "/Android/data/net.acadman/"+"temp");
        temp_clear.delete();}
        catch (Exception e){}

        login.postDelayed(new Runnable() {
            @Override
            public void run() {

if(tid.equals("null")){
                Intent Login= new Intent(Entry.this,Login.class);
                startActivity(Login);
finish();}
                else{
    Intent Login= new Intent(Entry.this,Dashboard.class);
    startActivity(Login);
    finish();
}

            }
        },2250);
/*
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("**///*");*/
        //  startActivityForResult(intent, 7);
        //Entry.testing jj=new testing();
       // jj.execute();



      /*  ActivityCompat.requestPermissions(Entry.this,
                new String[]{Manifest.permission.READ_SMS},
                1);*/


 /* DownloadData cc = new DownloadData(getApplicationContext());
                cc.execute();*/
/*Intent j  = new Intent (this,Upload_service.class);
        startService(j);*/




    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        switch (requestCode) {

            case 7:

                if (resultCode == RESULT_OK) {

             /*       String PathHolder = data.getData().toString();

                    SharedPreferences.Editor editr = getSharedPreferences("uri", MODE_PRIVATE).edit();
                    editr.putString("uri", PathHolder);
                    editr.apply();
                    Intent j  = new Intent (this,Upload_service.class);
                    startService(j);

//                    Log.e("dddd", );

*/
             }
                break;

        }
    }

































}

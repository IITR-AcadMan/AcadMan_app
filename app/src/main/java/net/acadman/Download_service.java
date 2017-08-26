package net.acadman;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

public class Download_service extends Service {
Intent intent;
    Notification.Builder notificationBuilder;
    NotificationManager notificationManager;
    String tidsp1,tokensp1;

    public Download_service() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");

    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        this.intent = intent;
        notificationManager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
        SharedPreferences cousp = getApplicationContext().getSharedPreferences("Personal_Data",MODE_PRIVATE);
        tidsp1  = cousp.getString("tid", "null");
        tokensp1 = cousp.getString("token","null");


        notificationBuilder = new Notification.Builder(getApplicationContext());
        notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Downloading: " + intent.getStringExtra("filename"))
                .setContentText("Filesize: "+ android.text.format.Formatter.formatFileSize(getApplicationContext(), Long.parseLong(intent.getStringExtra("filesize"))))
                        .setProgress(0,0,true);
        Notification notification = notificationBuilder.build();
        notificationManager.notify(13, notification);
        Downlioad_files downrq = new Downlioad_files();
        downrq.execute();



        return super.onStartCommand(intent, flags, startId);
    }
    public class Downlioad_files extends AsyncTask{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            notificationBuilder.setProgress(0,0, true);
            notificationManager.notify(13, notificationBuilder.build());
        }

        @Override
        protected Object doInBackground(Object[] params) {
DownloadData downloding = new DownloadData(Download_service.this,intent.getStringExtra("fileid"),tidsp1,tokensp1);
            downloding.bindlitener(new DownloadData.Downlistener() {
                @Override
                public void downloaded(long num) {
                    publishProgress((int) ((num /  Float.parseFloat(intent.getStringExtra("filesize") )) * 90));
                }
            });
            downloding.download_ser();
return  null;
        }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        Intent intente = new Intent("status");
        sendBroadcast(intente);
        File checksize = new File(Environment.getExternalStorageDirectory() + "/Android/data/net.acadman/",intent.getStringExtra("fileid"));
        if(!checksize.exists()||checksize.length()!=Long.parseLong(intent.getStringExtra("filesize"))){
            notificationBuilder.setProgress(100,100, false);

            notificationBuilder.setContentTitle("Failed");
            notificationBuilder.setOngoing(false);
           try {checksize.delete();}
           catch (Exception e){}
        }
        else {

            notificationBuilder.setProgress(100, 100, false);
            notificationBuilder.setOngoing(false);
            notificationBuilder.setContentTitle("Successfully Downloaded");
        }        notificationManager.notify(13, notificationBuilder.build());
        stopSelf();
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
        notificationBuilder.setProgress(100,
                Integer.parseInt(String.valueOf(values[0])), false);
        notificationManager.notify(13, notificationBuilder.build());
    }
}}

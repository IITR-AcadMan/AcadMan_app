package net.acadman;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.IntDef;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.List;

public class Upload_service extends Service {
    Notification.Builder notificationBuilder;
    NotificationManager notificationManager;
    Intent intent;
    String up_compl;
    String tidsp,tokensp;
    public Upload_service() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.intent = intent;
       notificationManager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
        SharedPreferences uploadsp = getApplicationContext().getSharedPreferences("Personal_Data", getApplicationContext().MODE_PRIVATE);
        tidsp  = uploadsp.getString("tid", "null");
        tokensp = uploadsp.getString("token","null");


        notificationBuilder = new Notification.Builder(getApplicationContext());
        notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Uploading: "+give_size_name(intent.getStringExtra("uri"),2))
                .setContentText("Filesize: "+android.text.format.Formatter.formatFileSize(this,Long.parseLong(give_size_name(intent.getStringExtra("uri"),1))))
                .setProgress(0,0,true);



        Notification notification = notificationBuilder.build();
        notificationManager.notify(12, notification);
        Log.e("lll",intent.getStringExtra("uri"));
        Upload_service.testing f = new testing();
        f.execute();
        return super.onStartCommand(intent, flags, startId);

    }

    public class testing extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            notificationBuilder.setProgress(100,0, false);
            notificationManager.notify(12, notificationBuilder.build());
        }

        @Override
        protected Object doInBackground(Object[] params) {

            String charset = "UTF-8";


            String requestURL  = Constants.website;

            try {
                UploadData multipart = new UploadData(requestURL, charset ,getApplicationContext());
                SharedPreferences pddref = getApplicationContext().getSharedPreferences("temp", MODE_PRIVATE);
                String sendcouse  = pddref.getString("sendcourse", "null");

                multipart.addFormField("reqid", "10");
                multipart.addFormField("tid",tidsp);
                multipart.addFormField("token",tokensp);
                multipart.addFormField("course",sendcouse);
                multipart.addFormField("comment",intent.getStringExtra("comment"));
                multipart.bindlitener(new UploadData.ProgressListener() {
                    @Override
                    public void transferred(long num) {
                        publishProgress((int) ((num / (float) Float.parseFloat(give_size_name(intent.getStringExtra("uri"),1))) * 90));
                    }
                });

                multipart.addFilePart("file", intent.getStringExtra("uri"));

                List<String> response = multipart.finish();






                for (String line : response) {
                    up_compl = line;
                }
            } catch (Exception ex) {
                notificationManager.cancel(12);
                notificationBuilder.setOngoing(false);
                notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
                notificationBuilder .setContentTitle("Failed");
                notificationBuilder.setContentText("Try Again");
                notificationManager.notify(12,notificationBuilder.build());


            }


            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            notificationBuilder.setProgress(100,100, false);
            notificationBuilder.setContentTitle("Successfully Uploaded");
            notificationBuilder.setOngoing(false);
            notificationManager.notify(12, notificationBuilder.build());
            stopSelf();

        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
            notificationBuilder.setProgress(100,
                    Integer.parseInt(String.valueOf(values[0])), false);
            notificationManager.notify(12, notificationBuilder.build());
         //   Toast.makeText(Upload_service.this, String.valueOf(values[0]), Toast.LENGTH_SHORT).show();
        }
    }
public String give_size_name(String uri,int a){
    Uri fileUri = Uri.parse(uri);
String filesize="";
    String filename="";
    String[] projection = {MediaStore.MediaColumns.SIZE,MediaStore.MediaColumns.DISPLAY_NAME};

    ContentResolver cr = this.getApplicationContext().getContentResolver();
    Cursor metaCursor = cr.query(fileUri, projection, null, null, null);
    if (metaCursor != null) {
        try {
            if (metaCursor.moveToFirst()) {
                filesize = metaCursor.getString(0);
                filename = metaCursor.getString(1);
            }
        } finally {
            metaCursor.close();
        }
    }
    if(a==1){
return filesize;}
else { return filename;}
}}

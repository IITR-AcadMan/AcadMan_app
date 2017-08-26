package net.acadman;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Kaishu on 8/18/2017.
 */

public class DownloadData  {
    File DownloadFile;
    Context a;
    String fileid;
    Notification.Builder notificationBuilder;
    NotificationManager notificationManager;
    long Downloadbytes;
    Downlistener dwnlos;
    HttpURLConnection httpURLConnection;
    String tidsp;
    String tokensp;

    public DownloadData(Context s,String fileid,String tid,String token) {
        this.a = s;
        this.fileid = fileid;
        this.tidsp = tid;
        this.tokensp=token;

    }
    public void bindlitener(Downlistener aaa){
        this.dwnlos=aaa;
    }
public void download_ser(){
        try {

            final URL downloadFileUrl = new URL(Constants.website);
          httpURLConnection = (HttpURLConnection) downloadFileUrl.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);


            String parameters = "reqid=" + URLEncoder.encode("13", "UTF-8") + "&tid=" + URLEncoder.encode(tidsp, "UTF-8") + "&token=" + URLEncoder.encode(tokensp, "UTF-8") + "&id=" + URLEncoder.encode(fileid, "UTF-8");

            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.writeBytes(parameters);
            wr.flush();
            wr.close();


                File dd = new File(Environment.getExternalStorageDirectory() + "/Android/data/", "net.acadman");
                if (!dd.exists()) {
                    dd.mkdir();
                }

                DownloadFile = new File(Environment.getExternalStorageDirectory() + "/Android/data/net.acadman/", fileid);
                DownloadFile.createNewFile();
                final FileOutputStream fileOutputStream = new FileOutputStream(DownloadFile);
                final byte buffer[] = new byte[16 * 1024 * 1024];

                final InputStream inputStream = httpURLConnection.getInputStream();

                int len1 = 0;
                while ((len1 = inputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, len1);
                    Downloadbytes += len1;
                    this.dwnlos.downloaded(Downloadbytes);
                }
                fileOutputStream.flush();
                fileOutputStream.close();

        } catch (Exception e) {

Log.e("failed",e.toString());
            DownloadFile = null;
        }
    }
    public  interface Downlistener {
        void downloaded(long num);
    }
}

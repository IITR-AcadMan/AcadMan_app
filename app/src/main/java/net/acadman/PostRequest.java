package net.acadman;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Kaishu on 8/4/2017.
 */

class PostRequest extends AsyncTask {
    HttpURLConnection connection = null;
String[] keys;
    String[] values;
    String output;
public static JsonListener jsonlistener;
    PostRequest(String[] keys,String[] values) {
        this.keys=keys;
        this.values=values;
    }

    protected Object doInBackground(Object... params) {
        try {
            this.connection = (HttpURLConnection) new URL(Constants.website).openConnection();
            this.connection.setRequestMethod("POST");
            this.connection.setDoOutput(true);
            this.connection.setDoInput(true);
          StringBuilder  urlParameters = new StringBuilder("");



           for(int a=0; a<keys.length ;a++){
               urlParameters.append(keys[a]);
               urlParameters.append(URLEncoder.encode(values[a],"UTF-8"));

           }


            DataOutputStream wr = new DataOutputStream(this.connection.getOutputStream());
            wr.writeBytes(urlParameters.toString());
            wr.flush();
            wr.close();
            BufferedReader rd = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));
            StringBuffer response = new StringBuffer();
            while (true) {
                String line = rd.readLine();
                if (line == null) {
                    break;
                }
                response.append(line);
                output = response.toString();
                Log.e("kaishu", response.toString());
            }
            rd.close();
        } catch (Exception e) {
            Log.e("kaishu", e.toString());
        } finally {
            this.connection.disconnect();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        jsonlistener.jsonreceived(output);
    }

    public  void bindListener(JsonListener listener) {
        jsonlistener = listener;
    }
}
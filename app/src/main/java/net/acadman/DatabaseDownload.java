package net.acadman;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Kaishu on 8/18/2017.
 */


public class DatabaseDownload extends SQLiteOpenHelper {
    public DatabaseDownload(Context context){

        super(context,"Main",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String ppp = "CREATE TABLE Download_Files (id integer PRIMARY KEY,course,name, status INTEGER DEFAULT 0)";
Log.e("ddd","dddd");
        db.execSQL(ppp);


    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldversion ,int newversion){

    }


}

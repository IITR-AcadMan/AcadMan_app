package net.acadman;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Testing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        FragmentManager fm = getSupportFragmentManager();
        Fragment d= new DefaultSchedule();
        fm.beginTransaction()
                .add(R.id.frame,d)
                .commit();
    }
}

package com.example.taskmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class Test extends AppCompatActivity {

    DrawerLayout drawerlayout2;
    ImageButton imagebut;
    NavigationView navview2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        drawerlayout2 = (DrawerLayout) findViewById(R.id.drawerlayout2);
        imagebut = (ImageButton)  findViewById(R.id.navbut);

        navview2 = (NavigationView) findViewById(R.id.navview2);

        imagebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerlayout2.open();
            }
        });

        navview2.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemid = item.getItemId();

                if (itemid == R.id.nav_tasks) {
                    Toast.makeText(Test.this,"tasks",Toast.LENGTH_SHORT).show();
                    Log.d("hjhhdfdg","8393897449");
                }
                if (itemid == R.id.nav_add) {
                    Toast.makeText(Test.this,"add",Toast.LENGTH_SHORT).show();
                }
                if (itemid == R.id.nav_collections) {
                    Toast.makeText(Test.this,"collections",Toast.LENGTH_SHORT).show();
                }
                if (itemid == R.id.nav_logout) {
                    Toast.makeText(Test.this,"logout",Toast.LENGTH_SHORT).show();
                }
                if (itemid == R.id.nav_receive) {
                    Toast.makeText(Test.this,"receive",Toast.LENGTH_SHORT).show();
                }
                if (itemid == R.id.nav_send) {
                    Toast.makeText(Test.this,"send",Toast.LENGTH_SHORT).show();
                }
                if (itemid == R.id.nav_settings) {
                    Toast.makeText(Test.this,"settings",Toast.LENGTH_SHORT).show();
                }
                drawerlayout2.close();
                return false;
            }
        });


    }
}
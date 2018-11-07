package com.datingmiage.app.datingmiage.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.datingmiage.app.datingmiage.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void searchContact(View view) {
        Intent intent = new Intent(getBaseContext(), SearchContactActivity.class);
        startActivity(intent);
    }

    public void contactByNum(View view) {
        Intent intent = new Intent(getBaseContext(), ContactNumberActivity.class);
        startActivity(intent);
    }
}

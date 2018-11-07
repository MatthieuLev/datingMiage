package com.datingmiage.app.datingmiage.controller;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.datingmiage.app.datingmiage.R;

public class ContactNumberActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_number);
        PermissionsManager.showPermissionDialog(this.getBaseContext(),this);
    }


    // send SMS se déclanche lorsque l'on click sur le bouton d'envoi des coordonnées
    public void sendSMS(View view) {
        EditText numeroET = findViewById(R.id.editText);
        String numero = numeroET.getText().toString();

        String message = SmsSender.sendSMSForRDV(this, numero);
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("Retourner au menu principal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(intent);
                    }
                })
                .show();
    }
}
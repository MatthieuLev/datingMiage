package com.datingmiage.app.datingmiage.controller;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.datingmiage.app.datingmiage.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContactNumberActivity extends AppCompatActivity {
    GPSTracker gps;
    private final static int PERMISSION_LOCATION_REQUEST_CODE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_number);
        showPermissionDialog();
    }


    // send SMS se déclanche lorsque l'on click sur le bouton d'envoi des coordonnées
    public void sendSMS(View view){
        EditText numeroET = findViewById(R.id.editText);
        String numero = numeroET.getText().toString();
        // compilation de la regex du numero de telephone français
        Pattern p = Pattern.compile("^(?:(?:\\+|00)33[\\s.-]{0,3}(?:\\(0\\)[\\s.-]{0,3})?|0)[1-9](?:(?:[\\s.-]?\\d{2}){4}|\\d{2}(?:[\\s.-]?\\d{3}){2})$");
        // création d'un moteur de recherche
        Matcher m = p.matcher(numero);
        // lancement de la recherche de toutes les occurrences
        boolean b = m.matches();
        // si recherche fructueuse
        if (!b) {
            Toast.makeText(getApplicationContext(), "Number " + numero + " is not valide " + numero, Toast.LENGTH_SHORT).show();
            return;
        }

        gps = new GPSTracker(ContactNumberActivity.this);

        // Check if GPS enabled
        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            String message = "My Location is - \nLat: " + latitude + "\nLong: " + longitude;


            SmsManager.getDefault().sendTextMessage(numero, null, message, null, null);
        } else {
            // Can't get location.
            // GPS or network is not enabled.
            // Ask user to enable GPS/network in settings.
            gps.showSettingsAlert();
        }
    }

    private void showPermissionDialog() {
        if (!GPSTracker.checkPermission(this)) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.SEND_SMS,
                    },
                    PERMISSION_LOCATION_REQUEST_CODE);
        }
    }
}
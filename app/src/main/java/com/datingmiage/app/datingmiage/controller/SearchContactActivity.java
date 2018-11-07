package com.datingmiage.app.datingmiage.controller;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.datingmiage.app.datingmiage.R;
import com.datingmiage.app.datingmiage.contactsMultiPickerLibrary.ContactPickerActivity;
import com.datingmiage.app.datingmiage.contactsMultiPickerLibrary.ContactResult;

import java.util.ArrayList;

public class SearchContactActivity extends Activity {
    private final static int PERMISSION_LOCATION_REQUEST_CODE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_contact);
        showPermissionDialog();
        startActivityForResult(new Intent(this, ContactPickerActivity.class), 1302);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1302 && RESULT_OK == resultCode) {
            processContacts((ArrayList<ContactResult>)
                    data.getSerializableExtra(ContactPickerActivity.CONTACT_PICKER_RESULT));
        } else if (RESULT_CANCELED == resultCode) {
            if (data != null && data.hasExtra("error")) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Contact selection canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void processContacts(ArrayList<ContactResult> contacts) {
        for (ContactResult contactResult : contacts) {
            for (ContactResult.ResultItem item : contactResult.getResults()) {
                sendSMS(item.getResult());
            }
        }

    }

    public void sendSMS(String numero) {
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
package fr.miage.app.meetingmiage;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import java.util.ArrayList;

public class MeetingCreationActivity extends AppCompatActivity {
    private static final int PLACE_PICKER_REQUEST = 1;
    private static final int CONTACT_PICKER_REQUEST = 2;
    private LinearLayout linearLayoutContacts, linearLayoutPhoneNb;
    private MutableDateTime startDateTime, endDateTime;
    private Button buttonStartDate, buttonStartTime, buttonEndTime, buttonLocation;
    private double latitude, longitude;
    private ArrayList<String> phonesNumbers;
    private boolean isStartDateValid,isEndDateValid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_creation);

        PermissionsManager.showPermissionDialog(this.getBaseContext(), this);

        phonesNumbers = new ArrayList<>();
        resetMeeting();

        linearLayoutContacts = findViewById(R.id.listViewContacts);
        linearLayoutPhoneNb = findViewById(R.id.listViewPhoneNb);
        buttonStartDate = findViewById(R.id.buttonStartDate);
        buttonStartTime = findViewById(R.id.buttonStartTime);
        buttonEndTime = findViewById(R.id.buttonEndTime);
        buttonLocation = findViewById(R.id.buttonLocation);
        refreshDates();
    }


    private void resetMeeting() {
        startDateTime = new MutableDateTime(new DateTime());
        endDateTime = new MutableDateTime(new DateTime());
        startDateTime.addHours(1);
        endDateTime.addHours(2);

    }


    public void addContact(View view) {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, CONTACT_PICKER_REQUEST);
    }


    public void addPhoneNb(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ENTER A PHONE NUMBER");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String number = input.getText().toString();
                phonesNumbers.add(number);
                addToLinearLayout(linearLayoutPhoneNb, number);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    public void setLocation(View view) {
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        }
    }


    public void pickStartDate(View view) {
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                startDateTime.year().set(year);
                startDateTime.monthOfYear().set(monthOfYear + 1);
                startDateTime.dayOfMonth().set(dayOfMonth);

                endDateTime.year().set(year);
                endDateTime.monthOfYear().set(monthOfYear + 1);
                endDateTime.dayOfMonth().set(dayOfMonth);
                refreshDates();
            }
        }, startDateTime.getYear(), startDateTime.getMonthOfYear() - 1, startDateTime.getDayOfMonth()).show();
    }


    public void pickStartTime(View view) {
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                startDateTime.hourOfDay().set(hourOfDay);
                startDateTime.minuteOfHour().set(minute);
                refreshDates();
            }
        }, startDateTime.getHourOfDay(), startDateTime.getMinuteOfHour(), false).show();
    }


    public void pickEndTime(View view) {
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                endDateTime.hourOfDay().set(hourOfDay);
                endDateTime.minuteOfHour().set(minute);
                refreshDates();
            }
        }, endDateTime.getHourOfDay(), endDateTime.getMinuteOfHour(), false).show();
    }


    private void refreshDates() {
        buttonStartDate.setText(startDateTime.toString("d MMMM, yyyy"));
        buttonStartTime.setText(startDateTime.toString("h:mm a"));
        buttonEndTime.setText(endDateTime.toString("h:mm a"));

        //If time is invalid, make red
        if (startDateTime.isBeforeNow()) {
            buttonStartTime.setTextColor(Color.RED);
            Toast.makeText(this, "This date has already expired", Toast.LENGTH_LONG).show();
            isStartDateValid = false;
        } else {
            buttonStartTime.setTextColor(Color.WHITE);
            isStartDateValid = true;
        }
        if (endDateTime.isBefore(startDateTime) || !endDateTime.isAfter(startDateTime)) {
            buttonEndTime.setTextColor(Color.RED);
            Toast.makeText(this, "The end date must be later than the start date", Toast.LENGTH_LONG).show();
            isEndDateValid = false;
        } else {
            buttonEndTime.setTextColor(Color.WHITE);
            isEndDateValid = true;
        }
    }


    public void sendMeeting(View view) {
        boolean allChecksAreOk = true;
        Log.d("myDebug", "Sms will be send to : " + phonesNumbers.toString());

        if (phonesNumbers.size() < 1) {
            Toast.makeText(this, "You have not added a contact or number", Toast.LENGTH_LONG).show();
            Log.d("myDebug", "You have not added a contact or number");
            allChecksAreOk = false;
        }

        if (buttonLocation.getText().equals(getResources().getString(R.string.locationLabel))){
            Toast.makeText(this, "You have to select a location", Toast.LENGTH_LONG).show();
            Log.d("myDebug", "You have to select a location " + buttonLocation.getText());
            allChecksAreOk = false;
        }

        if (!isStartDateValid || !isEndDateValid){
            Toast.makeText(this, "You have to select a valid date", Toast.LENGTH_LONG).show();
            Log.d("myDebug", "You have to select a valid date");
            allChecksAreOk = false;
        }

        for (String number : phonesNumbers) {
            if (!(SmsSender.isValidNumber(this,number))){
                Toast.makeText(this, number + " is not a valid number", Toast.LENGTH_LONG).show();
                allChecksAreOk = false;
            }
        }

        if (allChecksAreOk) {
            Log.d("myDebug","All checks are ok");
            for (String number : phonesNumbers) {
                sendSMSForRDV(number);
            }
        }
    }


    private void sendSMSForRDV(String numero) {
        String date = buttonStartDate.getText().toString() + " \nfrom "
                + buttonStartTime.getText().toString() + " \nto "
                + buttonEndTime.getText().toString();
        String message = "MeetingMiage : I'm asking you out on a meeting at the coordinates \n" + latitude + ",\n" + longitude
                + "\n on : " + date;
        SmsSender.sendSMS( numero, message);
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("Back to the meeting creation menu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
        switch (requestCode) {
            case PLACE_PICKER_REQUEST:
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(this, resultIntent);
                    latitude = place.getLatLng().latitude;
                    longitude = place.getLatLng().longitude;
                    buttonLocation.setText(latitude + "," + longitude);
                }
                break;
            case CONTACT_PICKER_REQUEST:
                if (resultCode == RESULT_OK) {
                    // Get the URI that points to the selected contact
                    Uri contactUri = resultIntent.getData();
                    // We only need the NUMBER column, because there will be only one row in the result
                    String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.Contacts.DISPLAY_NAME};

                    // Perform the query on the contact to get the NUMBER column
                    Cursor cursor = getContentResolver()
                            .query(contactUri, projection, null, null, null);
                    cursor.moveToFirst();

                    // Retrieve the phone number from the NUMBER column
                    int columnNumber = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    String number = cursor.getString(columnNumber);

                    int columnName = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                    String name = cursor.getString(columnName);

                    // Do something with the phone number...
                    phonesNumbers.add(number);
                    addToLinearLayout(linearLayoutContacts, name + " - " + number);
                    cursor.close();
                }
                break;
        }
    }


    private void addToLinearLayout(LinearLayout linearLayout, final String number) {
        LayoutInflater inflater = getLayoutInflater();
        final View viewUser = inflater.inflate(R.layout.list_item_user, null);
        ImageButton buttonRemoveContact = viewUser.findViewById(R.id.buttonRemoveContact);

        final LinearLayout linearTemp = linearLayout;
        buttonRemoveContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phonesNumbers.remove(number);
                Log.d("myDebug", "Remove " + number);
                linearTemp.removeView(viewUser);
            }
        });
        TextView textViewUserName = viewUser.findViewById(R.id.textViewUserDisplayName);
        textViewUserName.setText(number);
        linearLayout.addView(viewUser);
    }
}

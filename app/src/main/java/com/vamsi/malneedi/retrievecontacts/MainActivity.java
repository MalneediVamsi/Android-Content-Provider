package com.vamsi.malneedi.retrievecontacts;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private ArrayList<Contact> contacts;
    private Button button;
    private ListView listView;
    private Context context;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.retry_btn);
        listView = (ListView) findViewById(R.id.list);
        context = this;
        progressDialog = new ProgressDialog(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkContactPermission();
            }
        });

        contacts = new ArrayList<Contact>();

        checkContactPermission();
    }

    public void loadContact() {
        progressDialog.setMessage("Fetching contacts...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Cursor cursor;
                String[] projection = new String[]
                        {ContactsContract.Contacts._ID,
                                ContactsContract.Contacts.DISPLAY_NAME,
                                ContactsContract.Contacts.HAS_PHONE_NUMBER};
                Uri allContacts = ContactsContract.Contacts.CONTENT_URI;
                CursorLoader cursorLoader = new CursorLoader(context, allContacts, projection, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
                cursor = cursorLoader.loadInBackground();

                //display
                PrintContacts(cursor);
            }
        }, 100);

    }

    private void checkContactPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.

        } else {
            listView.setVisibility(View.VISIBLE);
            button.setVisibility(View.GONE);
            loadContact();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    button.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    loadContact();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    button.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                    Toast.makeText(this, "Permission denied. Retry!", Toast.LENGTH_LONG).show();
                }
                break;

            default:
                break;

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private void PrintContacts(Cursor c) {
        //contact id, name,no
        if (c.moveToFirst()) {
            do {
                //get contact id, name
                String contactID = c.getString(c.getColumnIndex(
                        ContactsContract.Contacts._ID));
                String contactDisplayName =
                        c.getString(c.getColumnIndex(
                                ContactsContract.Contacts.DISPLAY_NAME));
                Log.v("Content Providers", contactID + ", " +
                        contactDisplayName);

                String contactDisplayPhone = "";
                //get no
                int hasPhone =
                        c.getInt(c.getColumnIndex(
                                ContactsContract.Contacts.HAS_PHONE_NUMBER));
                if (hasPhone == 1) {
                    Cursor phoneCursor =
                            getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " +
                                            contactID, null, null);

                    while (phoneCursor.moveToNext()) {
                        Log.v("Content Providers",
                                contactDisplayPhone = phoneCursor.getString(
                                        phoneCursor.getColumnIndex(
                                                ContactsContract.CommonDataKinds.Phone.NUMBER)));
                    }
                    phoneCursor.close();
                }
                contacts.add(new Contact(contactDisplayName, contactID, contactDisplayPhone));
            } while (c.moveToNext());
        }

        ContactsAdapter adapter = new ContactsAdapter(context, contacts);
        listView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
        progressDialog.dismiss();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}


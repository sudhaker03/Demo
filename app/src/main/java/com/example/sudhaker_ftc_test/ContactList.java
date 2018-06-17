package com.example.sudhaker_ftc_test;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sudhaker_ftc_test.adapter.ContactAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactList extends AppCompatActivity  {

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1001;
    private ListView listView;

    private ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        listView = (ListView) findViewById(R.id.lv_Contact_List);
        ((Button) findViewById(R.id.btn_done)).setOnClickListener(clickListener);

        if (!checkPermissions()) {
            requestPermissions();
        } else {
            new ContactLoader().execute();
        }
    }




    public class ContactLoader extends AsyncTask<Void, String, Void> {

        ArrayList<String> contactData = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... voids) {

            Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
            while (cursor.moveToNext()) {
                try {
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    publishProgress(name);
                } catch (Exception e) {
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            contactData.add(values[0]);
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new ContactAdapter(contactData);
            listView.setAdapter(adapter);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new ContactLoader().execute();
                } else {
                    Toast.makeText(this, "Allow Contact Read First", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_CONTACTS);

        if (shouldProvideRationale) {
            Log.i("ContactList", "Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.read_contact_rational, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(ContactList.this,
                                    new String[]{Manifest.permission.READ_CONTACTS},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            Log.i("ContactList", "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(ContactList.this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_SHORT)
                .setAction(getString(actionStringId), listener).show();
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (adapter != null) {
                ArrayList<String> selectedList = adapter.getSelectedList();
                StringBuilder builder = new StringBuilder();
                for (String str : selectedList) {
                    builder.append(builder.length() == 0 ? str : "," + str);
                }
                Intent intent = new Intent();
                intent.putExtra("contact", builder.toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    };
}

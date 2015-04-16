package edu.asu.mscs.ashastry.contactsui;

/**
 * Copyright 2015 Aneesh Shastry
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Purpose: To View / Add a Contact using an Android application
 *
 * @author : Aneesh Shastry  mailto:ashastry@asu.edu
 *           MS Computer Science, CIDSE, IAFSE, Arizona State University
 * @version : March 2, 2015
 */


import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends ActionBarActivity {
    ArrayAdapter<String> arr_adapter;
    List<String> namesList = new ArrayList<String>();
    int flag = 0;
    String phone;
    String email;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupSpinner();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setupSpinner();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         int id = item.getItemId();
    if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setupSpinner() {



        String whereName = ContactsContract.Data.MIMETYPE + " = ? ";
        String[] whereNameParams = new String[]{ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE};
        Cursor cur = getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, whereName, whereNameParams, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);
        namesList.clear();
        while (cur.moveToNext()) {
            namesList.add((String) cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME)));
        }
        cur.close();

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        arr_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, namesList);
        spinner.setAdapter(arr_adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                ;


                String whereName = ContactsContract.Data.MIMETYPE + " = ? ";
                String[] whereNameParams = new String[]{ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE};
                Cursor cur = getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, whereName, whereNameParams, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);

                while (cur.moveToNext()) {
                    String name = (String) cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME));
                    if (name.equals(item)) {
                        ((EditText) findViewById(R.id.fName)).setText(item);
                        ((EditText) findViewById(R.id.phoneNum)).setText(getPhone(item));
                        ((EditText) findViewById(R.id.emailID)).setText(getEmail(item));
                        break;
                    }

                }
                cur.close();


                android.util.Log.d(this.getClass().toString(), "Item selected :" + item);
                Toast.makeText(MainActivity.this, "Name:" + item + " selected", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private String getEmail(String aName) {
        String ret = "noEmail";
        //  Find contact for aName.
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
              "DISPLAY_NAME = '" + aName + "'", null, null);



        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur
                        .getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur
                        .getString(cur
                                .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    Cursor eCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                    + " = ?", new String[]{id}, null);
                    while (eCur.moveToNext()) {

                        Cursor emailCur = cr.query(
                                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                                new String[]{id}, null);
                        while (emailCur.moveToNext()) {
                            String email = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                            ret= email;

                        }

                        emailCur.close();
                    }
                eCur.close();

            }
        }
        return ret;
    }



    private String getPhone(String aName){
        String ret = "noPhone";
        //  Find contact for aName.
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
                "DISPLAY_NAME = '" + aName + "'", null, null);
        if (cursor.moveToFirst()) {
            String contactId =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

            Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
            while (phones.moveToNext()) {
                String number = phones.getString(phones.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER));
                ret = number;

                break;

            }
            phones.close();
        }
        cursor.close();
        return ret;
    }



    public void clearFields (View v){
        ((EditText)findViewById(R.id.fName)).setText("");
        ((EditText)findViewById(R.id.phoneNum)).setText("");
        ((EditText)findViewById(R.id.emailID)).setText("");

    }

    public void addContact(View v){
        flag = 0;
         name = ((EditText)findViewById(R.id.fName)).getText().toString();
         phone = ((EditText)findViewById(R.id.phoneNum)).getText().toString();
         email = ((EditText)findViewById(R.id.emailID)).getText().toString();

        if(namesList.contains(name)){
            flag = 1;
            new AlertDialog.Builder(this)
                    .setTitle("Duplicate name exists")
                    .setMessage("Do you want to Edit or Create the contact ? ")
                    .setPositiveButton("Create New", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                            intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                            intent.putExtra(ContactsContract.Intents.Insert.PHONE, (phone));
                            intent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
                            intent.putExtra(ContactsContract.Intents.Insert.EMAIL, (email));
                            intent.putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
                            intent.putExtra(ContactsContract.Intents.Insert.NAME, (name));
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Open List to Edit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_INSERT_OR_EDIT);
                            intent.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);
                            intent.putExtra(ContactsContract.Intents.Insert.PHONE, (phone));
                            intent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
                            intent.putExtra(ContactsContract.Intents.Insert.EMAIL, (email));
                            intent.putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
                            intent.putExtra(ContactsContract.Intents.Insert.NAME, (name));
                            startActivity(intent);

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        if(name.equals("") || phone.equals("") || email.equals("")) {
            flag = 1;
            new AlertDialog.Builder(this)
                    .setTitle("Incomplete Input")
                    .setMessage("Continue without completing all fields ?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            flag = 0;
                            Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                            intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                            intent.putExtra(ContactsContract.Intents.Insert.PHONE, (phone));
                            intent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
                            intent.putExtra(ContactsContract.Intents.Insert.EMAIL, (email));
                            intent.putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
                            intent.putExtra(ContactsContract.Intents.Insert.NAME, (name));
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        if(flag == 0) {
            Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
            intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
            intent.putExtra(ContactsContract.Intents.Insert.PHONE, (phone));
            intent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
            intent.putExtra(ContactsContract.Intents.Insert.EMAIL, (email));
            intent.putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
            intent.putExtra(ContactsContract.Intents.Insert.NAME, (name));
            startActivity(intent);
        }

    }


}


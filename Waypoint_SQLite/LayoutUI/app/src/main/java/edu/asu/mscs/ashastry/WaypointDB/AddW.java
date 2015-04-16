package edu.asu.mscs.ashastry.WaypointDB;

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
 * Purpose: To Add/View/Modify a Waypoint using SQLite database
 *
 * @author : Aneesh Shastry  mailto:ashastry@asu.edu
 *           MS Computer Science, CIDSE, IAFSE, Arizona State University
 * @version : February 23, 2015
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.*;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayDeque;
import java.util.ArrayList;


public class AddW extends ActionBarActivity {

    Button cancelButton;
    Button saveButton;
    Button deleteButton;
    Intent intent;
    int duplicateFlag = 0;
    int emptyFlag = 0;

    Waypoint wpIn;
    Waypoint wpSaved;
    String tempName;
    int editFlag = 0;

    ArrayList<String> wpList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        cancelButton = (Button) findViewById(R.id.cancel);
        saveButton = (Button) findViewById(R.id.save);
        deleteButton = (Button) findViewById(R.id.delete);
        ((TextView)findViewById(R.id.titleName)).setText("Add Waypoint");
        saveButton.setText("Add");
        tempName = "";
        editFlag = 0;

      if ((Waypoint) getIntent().getSerializableExtra("wpObject") != null) {
            wpIn = (Waypoint) getIntent().getSerializableExtra("wpObject");

            ((EditText) findViewById(R.id.newLattitude)).setText(Double.toString(wpIn.getLat()));
            ((EditText) findViewById(R.id.newLongitude)).setText(Double.toString(wpIn.getLon()));
            ((EditText) findViewById(R.id.newElevation)).setText(Double.toString(wpIn.getEle()));
            ((EditText) findViewById(R.id.newName)).setText(wpIn.getName());
            ((EditText) findViewById(R.id.newAddress)).setText(wpIn.getAddress());
            ((EditText) findViewById(R.id.newCategory)).setText(wpIn.getCategory());
            tempName = wpIn.getName();

            if(editFlag == 0){
                ((EditText) findViewById(R.id.newLattitude)).setFocusable(false);
                ((EditText) findViewById(R.id.newLongitude)).setFocusable(false);
                ((EditText) findViewById(R.id.newElevation)).setFocusable(false);
                ((EditText) findViewById(R.id.newName)).setFocusable(false);
                ((EditText) findViewById(R.id.newAddress)).setFocusable(false);
                ((EditText) findViewById(R.id.newCategory)).setFocusable(false);
                saveButton.setText("Edit");
                ((TextView)findViewById(R.id.titleName)).setText("View Waypoint");
            }
        }else{
            deleteButton.setEnabled(false);
        }

        intent = new Intent(this, MainActivity.class);


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(intent);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener()
        {
            Double newLat, newLong, newEle;
            String newName, newAddr, newCat;

            @Override
            public void onClick(View arg0)
            {
                if(saveButton.getText().equals("Edit")){
                    editFlag = 1;
                    ((EditText) findViewById(R.id.newLattitude)).setFocusable(true);
                    ((EditText) findViewById(R.id.newLongitude)).setFocusable(true);
                    ((EditText) findViewById(R.id.newElevation)).setFocusable(true);
                    ((EditText) findViewById(R.id.newName)).setFocusable(true);
                    ((EditText) findViewById(R.id.newAddress)).setFocusable(true);
                    ((EditText) findViewById(R.id.newCategory)).setFocusable(true);

                    ((EditText) findViewById(R.id.newLattitude)).setFocusableInTouchMode(true);
                    ((EditText) findViewById(R.id.newLongitude)).setFocusableInTouchMode(true);
                    ((EditText) findViewById(R.id.newElevation)).setFocusableInTouchMode(true);
                    ((EditText) findViewById(R.id.newName)).setFocusableInTouchMode(true);
                    ((EditText) findViewById(R.id.newAddress)).setFocusableInTouchMode(true);
                    ((EditText) findViewById(R.id.newCategory)).setFocusableInTouchMode(true);
                    saveButton.setText("Update");
                    ((TextView)findViewById(R.id.titleName)).setText("Edit Waypoint");

                }else {
                    wpList = new ArrayList<String>();
                    wpList = (ArrayList<String>) getIntent().getStringArrayListExtra("wpList");
                    duplicateFlag = 0;
                    emptyFlag = 0;
                    if( ((EditText) findViewById(R.id.newLattitude)).getText().toString().equals("") ||
                            ((EditText) findViewById(R.id.newLongitude)).getText().toString().equals("") ||
                            ((EditText) findViewById(R.id.newElevation)).getText().toString().equals("") ||
                            ((EditText) findViewById(R.id.newName)).getText().toString().equals("") ||
                            ((EditText) findViewById(R.id.newAddress)).getText().toString().equals("") ||
                            ((EditText) findViewById(R.id.newCategory)).getText().toString().equals("")){

                        emptyFlag = 1;

                    }

                    if(emptyFlag == 0){
                        newLat = Double.parseDouble(((EditText) findViewById(R.id.newLattitude)).getText().toString());
                        newLong = Double.parseDouble(((EditText) findViewById(R.id.newLongitude)).getText().toString());
                        newEle = Double.parseDouble(((EditText) findViewById(R.id.newElevation)).getText().toString());
                        newName = ((EditText) findViewById(R.id.newName)).getText().toString();
                        newAddr = ((EditText) findViewById(R.id.newAddress)).getText().toString();
                        newCat = ((EditText) findViewById(R.id.newCategory)).getText().toString();

                        wpSaved = new Waypoint(newLat, newLong, newEle, newName, newAddr, newCat);

                        if(wpList.contains(newName) && !tempName.equals(newName)){
                            duplicateFlag = 1;
                        }

                    }

                    android.util.Log.d(this.getClass().toString(),"Duplicate : " + duplicateFlag);
                    if(emptyFlag == 1){
                        new AlertDialog.Builder(AddW.this)
                                .setTitle("Incomplete input")
                                .setMessage("Please provide complete input.")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                    }else if(duplicateFlag == 1) {
                        new AlertDialog.Builder(AddW.this)
                                .setTitle("Invalid Waypoint name")
                                .setMessage("Waypoint '"+ newName + "' already exists. Please use a different name.")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    } else{
                        if (saveButton.getText().equals("Update")) {
                            if (tempName.equals(newName) && tempName != "") {
                                //Update existing
                                intent.putExtra("wpUpdated", wpSaved);
                                startActivity(intent);
                            } else {
                                //Replace existing
                                intent.putExtra("wpUpdated", wpSaved);
                                intent.putExtra("Extra_Message", "delete:" + tempName);
                                startActivity(intent);
                            }
                            //Updating an existing entry
                        } else if (saveButton.getText().equals("Add")) {
                            //Adding a new entry

                            intent.putExtra("wpAddNew", wpSaved);
                            startActivity(intent);

                        }
                    }
                }
             }
        });


        deleteButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View arg0)
            {

                new AlertDialog.Builder(AddW.this)
                        .setTitle("Delete waypoint")
                        .setMessage("Are you sure you want to delete waypoint " + tempName)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                intent.putExtra("wpDeleted", wpIn);
                                intent.putExtra("Extra_Message", "delete:" + tempName);
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
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
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

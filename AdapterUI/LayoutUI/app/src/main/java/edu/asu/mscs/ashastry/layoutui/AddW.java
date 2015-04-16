package edu.asu.mscs.ashastry.layoutui;

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
 * Purpose: To Add/View/Modify a Waypoint using adapters
 *
 * @author : Aneesh Shastry  mailto:ashastry@asu.edu
 *           MS Computer Science, CIDSE, IAFSE, Arizona State University
 * @version : February 16, 2015
 */

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.*;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class AddW extends ActionBarActivity {

    Button cancelButton;
    Button saveButton;
    Button deleteButton;
    Intent intent;

    Waypoint wpIn;
    Waypoint wpSaved;
    EditText et;
    String tempName;
    int editFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        cancelButton = (Button) findViewById(R.id.cancel);
        saveButton = (Button) findViewById(R.id.save);
        deleteButton = (Button) findViewById(R.id.delete);
        ((TextView)findViewById(R.id.titleName)).setText("Add Waypoint");
        tempName = "";
        editFlag = 0;

        saveButton.setText("Save");

        if ((Waypoint) getIntent().getSerializableExtra("wpObject") != null) {
            wpIn = (Waypoint) getIntent().getSerializableExtra("wpObject");

            ((EditText) findViewById(R.id.newLattitude)).setText(Double.toString(wpIn.getLat()));
            ((EditText) findViewById(R.id.newLongitude)).setText(Double.toString(wpIn.getLon()));
            ((EditText) findViewById(R.id.newElevation)).setText(Double.toString(wpIn.getEle()));
            ((EditText) findViewById(R.id.newName)).setText(wpIn.getName());
            ((EditText) findViewById(R.id.newAddress)).setText(wpIn.getAddress());
            ((EditText) findViewById(R.id.newCategory)).setText(wpIn.getCategory());
        //    ((TextView)findViewById(R.id.titleName)).setText("Update Waypoint");
            tempName = wpIn.getName();

       //     saveButton.setText("Update");

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






      //  cancelButton = (Button) findViewById(R.id.cancel);
      //  saveButton = (Button) findViewById(R.id.save);
        intent = new Intent(this, MainActivity.class);


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(intent);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener()
        {

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
                    ((TextView)findViewById(R.id.titleName)).setText("Update Waypoint");

                }else {

                    Double newLat, newLong, newEle;
                    String newName, newAddr, newCat;
                    newLat = Double.parseDouble(((EditText) findViewById(R.id.newLattitude)).getText().toString());
                    newLong = Double.parseDouble(((EditText) findViewById(R.id.newLongitude)).getText().toString());
                    newEle = Double.parseDouble(((EditText) findViewById(R.id.newElevation)).getText().toString());
                    newName = ((EditText) findViewById(R.id.newName)).getText().toString();
                    newAddr = ((EditText) findViewById(R.id.newAddress)).getText().toString();
                    newCat = ((EditText) findViewById(R.id.newCategory)).getText().toString();


                    wpSaved = new Waypoint(newLat, newLong, newEle, newName, newAddr, newCat);
                    //Toast.makeText(null,"Waypoint added",Toast.LENGTH_SHORT);
                    if (tempName.equals(newName) && tempName != "") {
                        intent.putExtra("wpSaved", wpSaved);
                        startActivity(intent);
                    } else {
                        intent.putExtra("wpSaved", wpSaved);
                        intent.putExtra("Extra_Message", "delete:" + tempName);
                        startActivity(intent);
                    }

                }

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View arg0)
            {


                //Toast.makeText(null,"Waypoint added",Toast.LENGTH_SHORT);
                intent.putExtra("Extra_Message","delete:"+wpIn.getName());
                //intent.putExtra("wpSaved",wpSaved);
                startActivity(intent);


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

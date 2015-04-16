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

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends ListActivity {

    public Waypoint wpAdded;
    public Waypoint wpFromdb;
    public Waypoint wpUpdated;
    ArrayList<String> waypointList = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        waypointList.add("Add New Waypoint");
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, waypointList);
        setListAdapter(adapter);


        if ((Waypoint) getIntent().getSerializableExtra("wpAddNew") != null) { //check if a a new wp is added
           wpAdded = (Waypoint) getIntent().getSerializableExtra("wpAddNew");
           saveWaypoint(wpAdded); //Save the new waypoint
           getListofNames(); // Refresh the View list
         }

        if ((Waypoint) getIntent().getSerializableExtra("wpUpdated") != null) { //chhck if waypoint is updated
            wpUpdated = (Waypoint) getIntent().getSerializableExtra("wpUpdated");
            if((String)getIntent().getStringExtra("Extra_Message") != null){ // check for extra messages whule updating
                String message = (String)getIntent().getStringExtra("Extra_Message");
                if(message.split(":")[0].equals("delete") ) {
                    try {
                        String itemD = message.split(":")[1];
                //        android.util.Log.d("Delete", itemD);
                        deleteWaypoint(itemD); // first delete the existing waypoint
                        saveWaypoint(wpUpdated); // then update the waypoint
                        getListofNames(); // refresh the View list
                        Toast.makeText(MainActivity.this, "'" +itemD + "' updated", Toast.LENGTH_SHORT).show();
                    } catch (IndexOutOfBoundsException ex) {

                    }
                }
            } else{

                updateWaypoint(wpUpdated);
                getListofNames();
            }
        }

        if ((Waypoint) getIntent().getSerializableExtra("wpDeleted") != null) {
            if((String)getIntent().getStringExtra("Extra_Message") != null){ // check for extra messages whule updating
                String message = (String)getIntent().getStringExtra("Extra_Message");
                if(message.split(":")[0].equals("delete") ) {
                    try {
                        String itemD = message.split(":")[1];
                  //      android.util.Log.d("Delete", itemD);
                        deleteWaypoint(itemD); // first delete the existing waypoint
                        Toast.makeText(MainActivity.this, "'" + itemD + "' deleted", Toast.LENGTH_SHORT).show();
                    } catch (IndexOutOfBoundsException ex) {

                    }
                }
            }
        }
        getListofNames();
    }


    // To get the list of existing waypoints from the database
    public void getListofNames(){
        List<String> nameList = new ArrayList<String>();
        waypointdb db = new waypointdb(this);
        try {
            db.copyDB();
            SQLiteDatabase crsDB = db.openDB();
            Cursor c = crsDB.rawQuery("SELECT * FROM waypoint", null);
            String temp = "";
            waypointList.clear();
            waypointList.add("Add New Waypoint");
            while (c.moveToNext()) {
                String lattitude = c.getString(0);
                String longitude = c.getString(1);
                String elevation = c.getString(2);
                String address= c.getString(3);
                String category = c.getString(4);
                String name = c.getString(5);
                waypointList.add(name);
                temp = temp + "\n Lat:" + lattitude + "\tLon:" + longitude  + "\tEle:" + elevation + "\n Name:" + name + "\tAdd:" + address  + "\tCat:" + category;
            }
            adapter.notifyDataSetChanged();
            android.util.Log.w("onCreate read db", "found waypoints:" + temp);
            c.close();
            crsDB.close();
            db.close();
        }
        catch(java.sql.SQLException sqle){
            android.util.Log.w("Caught SQLException:",sqle.getMessage());
        }catch(Exception ex){
            android.util.Log.w("Caught Exception:",ex.getMessage());
        }
    }
    @Override
    protected void onListItemClick(ListView l, View v1, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        if(position == 0){
             addWaypoint(v1);
        }else{
            openWaypoint(v1,item);
        }
        if(position != 0) {
            Toast.makeText(this,"Opening '" + item + "' from database", Toast.LENGTH_SHORT).show();
        }
    }

    public void saveWaypoint(Waypoint w){

        waypointdb db = new waypointdb(this);
        try {
            db.copyDB();
            SQLiteDatabase crsDB = db.openDB();

            crsDB.execSQL("INSERT INTO waypoint VALUES " +
                    "('"+Double.toString(w.getLat())+"','" +
                    Double.toString(w.getLon())+"','"  +
                    Double.toString(w.getEle())+"','"  +
                    w.getAddress() +"','" + w.getCategory() + "','" + w.getName()  + "')");
            android.util.Log.d(this.getClass().toString(), "Adding a new waypoint : "+ w.getName());
            crsDB.close();
            db.close();
        }
        catch(java.sql.SQLException sqle){
            android.util.Log.w("Caught SQLException:",sqle.getMessage());
        }catch(Exception ex){
            android.util.Log.w("Caught Exception:",ex.getMessage());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
           try {
               saveJSON();
           } catch (Exception e){
               android.util.Log.w("Caught Exception:", e.getMessage());
           }

            return true;
        }
        else if(id == R.id.action_exit){
            Toast.makeText(MainActivity.this, "Closing App WaypointDB", Toast.LENGTH_LONG).show();
            this.finish();
            System.exit(0);
        }

        return super.onOptionsItemSelected(item);
    }


    public void addWaypoint(View v){
        Intent intent = new Intent(this, AddW.class);
        getListofNames();
        intent.putStringArrayListExtra("wpList",(ArrayList<String>)waypointList);
        startActivity(intent);

    }

  /*  public void openWaypoint(View v,String wpName){
        getWaypoint(wpName);

    }*/

    public void  openWaypoint(View v,String wayPointName){
        String lattitude = null;
        String longitude = null;
        String elevation = null;
        String name = null;
        String address = null;
        String category = null;

        waypointdb db = new waypointdb(this);
        try {
            db.copyDB();
            SQLiteDatabase crsDB = db.openDB();
            Cursor c = crsDB.rawQuery("SELECT * FROM waypoint where name =\"" +wayPointName+"\"" ,null );
            String temp = "";

            while (c.moveToNext()) {
                lattitude = c.getString(0);
                longitude = c.getString(1);
                elevation = c.getString(2);
                address= c.getString(3);
                category = c.getString(4);
                name = c.getString(5);
            }

            wpFromdb = new Waypoint(Double.parseDouble(lattitude),Double.parseDouble(longitude),Double.parseDouble(elevation),
                                    name, address, category);


            adapter.notifyDataSetChanged();

            c.close();
            crsDB.close();
            db.close();
        }
        catch(java.sql.SQLException sqle){
            android.util.Log.w("Caught SQLException:",sqle.getMessage());
        }catch(Exception ex){
            android.util.Log.w("Caught Exception:",ex.getMessage());
        }
        Intent intent = new Intent(MainActivity.this, AddW.class);
        intent.putExtra("wpObject",wpFromdb );

        getListofNames();
        intent.putStringArrayListExtra("wpList",(ArrayList<String>)waypointList);
        startActivity(intent);
    }


    public void deleteWaypoint(String wpName){
        waypointdb db = new waypointdb(this);
        try {
            db.copyDB();
            SQLiteDatabase crsDB = db.openDB();
            crsDB.execSQL("DELETE from waypoint where name = \"" + wpName + "\"");
            android.util.Log.d(this.getClass().toString(), "Deleted the waypoint : "+ wpName);
            crsDB.close();
            db.close();
        }
        catch(java.sql.SQLException sqle){
            android.util.Log.w("Caught SQLException:",sqle.getMessage());
        }catch(Exception ex){
            android.util.Log.w("Caught Exception:",ex.getMessage());
        }
    }


    public void updateWaypoint(Waypoint wp){
        waypointdb db = new waypointdb(this);
        try {
            db.copyDB();
            SQLiteDatabase crsDB = db.openDB();
            crsDB.execSQL("Update waypoint set lattitude = \"" + Double.toString(wp.getLat()) + "\"," +
                    "longitude = \"" + Double.toString(wp.getLon()) + "\"," +
                    "elevation = \"" + Double.toString(wp.getEle()) + "\"," +
                    "address = \"" + wp.getAddress() + "\"," +
                    "category = \"" + wp.getCategory() + "\" " +
                    "where name = \"" + wp.getName() + "\"");
            android.util.Log.d(this.getClass().toString(), "Updated waypoint : "+ wp.getName());
            crsDB.close();
            db.close();
        }
        catch(java.sql.SQLException sqle){
            android.util.Log.w("Caught SQLException:",sqle.getMessage());
        }catch(Exception ex){
            android.util.Log.w("Caught Exception:",ex.getMessage());
        }

    }


    public void saveJSON() throws JSONException, FileNotFoundException {

        waypointdb db = new waypointdb(this);

        try {
            db.copyDB();
            SQLiteDatabase crsDB = db.openDB();
            Cursor c = crsDB.rawQuery("SELECT * FROM waypoint", null);
            c.moveToFirst();
            JSONObject waypointJSON = new JSONObject();
            JSONArray wpArray = new JSONArray();
            int i = 0;
            while (!c.isAfterLast()) {
                JSONObject waypoint = new JSONObject();
                try {
                    waypoint.put("Lattitude", c.getString(c.getColumnIndex("lattitude")));
                    waypoint.put("Longitude", c.getString(c.getColumnIndex("longitude")));
                    waypoint.put("Elevation", c.getString(c.getColumnIndex("elevation")));
                    waypoint.put("Address", c.getString(c.getColumnIndex("address")));
                    waypoint.put("Category", c.getString(c.getColumnIndex("category")));
                    waypoint.put("Name", c.getString(c.getColumnIndex("name")));
                    c.moveToNext();
                    wpArray.put(i, waypoint);
                    i++;
                } catch (JSONException e) {
                    android.util.Log.d("Caught JSON Exception:", null);
                    e.printStackTrace();
                }
            }
            waypointJSON.put("WaypointDetails", wpArray);

            File file = new File(Environment.getExternalStorageDirectory(),"Waypoint.json");
            if(file.exists()) {
                file.delete();
            }
            file.createNewFile();
            FileOutputStream fo = new FileOutputStream(file, true);
            fo.write(waypointJSON.toString().getBytes());
            fo.close();

            Toast.makeText(MainActivity.this, "JSON file exported to " + Environment.getExternalStorageDirectory()+
                    "/Waypoint.json", Toast.LENGTH_LONG).show();
        } catch (java.sql.SQLException sqle) {
            android.util.Log.w("Caught SQLException:", sqle.getMessage());
        } catch (FileNotFoundException fe){
            android.util.Log.w("Caught File Exception:", fe.getMessage());
        } catch (Exception ex) {
            android.util.Log.w("Caught Exception:", ex.getMessage());
        }
    }
}



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

import android.app.ListActivity;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends ListActivity {

    public Waypoint wp;
    public Waypoint wpV;
    public Waypoint wpAdded;
    public Waypoint retrievedWaypoint;
    public String[] readNameResult;
    List<String> waypointList = new ArrayList<String>();
    ArrayAdapter<String> adapter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        waypointList.add("Add New Waypoint");
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, waypointList);
        setListAdapter(adapter);



        //load the ListView




        //check if a wp is saved
        if ((Waypoint) getIntent().getSerializableExtra("wpSaved") != null) {
           wpAdded = (Waypoint) getIntent().getSerializableExtra("wpSaved");
           saveWaypoint(wpAdded);
         }

        if((String)getIntent().getStringExtra("Extra_Message") != null){
            String message = (String)getIntent().getStringExtra("Extra_Message");
            if(message.split(":")[0].equals("delete") ){
                try {
                    String itemD = message.split(":")[1];


                    android.util.Log.d("Delete", itemD);
                    deleteWaypoint(itemD);
                    Toast.makeText(MainActivity.this, itemD + " deleted", Toast.LENGTH_SHORT).show();
                }
                catch(IndexOutOfBoundsException ex){

                }
            }
        }

        getListofNames();
       // loadWaypointView();
    }

    @Override
    protected void onListItemClick(ListView l, View v1, int position, long id) {

        String item = (String) getListAdapter().getItem(position);
        if(position == 0){
         addWaypoint(v1);
        }else{
            openWaypoint(v1,item);
        }
        android.util.Log.d("Position : ", String.valueOf(position) );

        Toast.makeText(this, item + " selected", Toast.LENGTH_SHORT).show();
    }






    public void getListofNames(){
        String URLOut = this.getString(R.string.url_string);
        MyTaskParams mp = new MyTaskParams(URLOut, "getNames");
        AsyncCalc ac = (AsyncCalc) new AsyncCalc().execute(mp);


    }

    public void saveWaypoint(Waypoint w){
        String URLOut = this.getString(R.string.url_string);
        MyTaskParams mp = new MyTaskParams(URLOut, "add");
        AsyncCalc ac = (AsyncCalc) new AsyncCalc().execute(mp);


    }

    public void loadWaypointView(){

        getListofNames();

        waypointList.clear();
        waypointList.add("Add New Waypoint");



        //get list of waypoints
        if(readNameResult != null) {

            for (int i = 0; i < readNameResult.length; i++) {

                waypointList.add(readNameResult[i]);
            }
        }


        adapter.notifyDataSetChanged();
    }

    public void reloadList(){

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


    public void addWaypoint(View v){
        //wp = new Waypoint();
        Intent intent = new Intent(this, AddW.class);
      //  intent.putExtra("wpObject",wp);
        startActivity(intent);

    }

    public void openWaypoint(View v,String wpName){
        getWaypoint(wpName);

    }

    public void  getWaypoint(String wayPointName){
        String URLOut = this.getString(R.string.url_string);
        MyTaskParams mp = new MyTaskParams(URLOut, "get",wayPointName);
        AsyncCalc ac = (AsyncCalc) new AsyncCalc().execute(mp);
    }

    public void deleteWaypoint(String wpName){
        String URLOut = this.getString(R.string.url_string);
        MyTaskParams mp = new MyTaskParams(URLOut, "remove",wpName);
        AsyncCalc ac = (AsyncCalc) new AsyncCalc().execute(mp);
    }







    private static class MyTaskParams {
        String newURL;
        String operatorV;
        String waypointname;

        public String getWaypointname() {
            return waypointname;
        }

        public void setWaypointname(String waypointname) {
            this.waypointname = waypointname;
        }

        MyTaskParams(String url, String op) {
            this.newURL = url;
            this.operatorV = op;

        }
        MyTaskParams(String url, String op, String wpname) {
            this.newURL = url;
            this.operatorV = op;
            this.waypointname = wpname;

        }

        public String getNewURL() {
            return newURL;
        }

        public void setNewURL(String newURL) {
            this.newURL = newURL;
        }

        public String getOperatorV() {
            return operatorV;
        }

        public void setOperatorV(String operatorV) {
            this.operatorV = operatorV;
        }
    }




    public void runStub(View v) {
        String url = this.getString(R.string.url_string);

            String operator = "getnames";
       //     URL URLOut = new URL(this.getString(R.string.url_string));
            String URLOut = this.getString(R.string.url_string);
            MyTaskParams mp = new MyTaskParams(URLOut, operator);
            AsyncCalc ac = (AsyncCalc) new AsyncCalc().execute(mp);
        try {


            WaypointServerStub cjc = new WaypointServerStub(url);


        }catch (Exception e) {
            System.out.println("Oops, you didn't enter the right stuff");
        }
    }


    private class AsyncCalc extends AsyncTask<MyTaskParams, Integer, Double> {


        protected Double doInBackground(MyTaskParams... mtp) {
            try {
                 WaypointServerStub cjc = new WaypointServerStub(mtp[0].getNewURL());

                String opn = mtp[0].getOperatorV();

                    if (opn.equalsIgnoreCase("remove")) {
                           boolean result = cjc.remove(mtp[0].getWaypointname());
                        String[] result1 = cjc.getNames();

                        refreshView(result1);

                        //  System.out.println("response: "+result);
                    }else if(opn.equalsIgnoreCase("add")) {
                        boolean result = cjc.add(wpAdded);


                    } else if (opn.equalsIgnoreCase("resetWaypoints")) {
                        boolean result = cjc.resetWaypoints();

                    } else if (opn.equalsIgnoreCase("get")) {
                        retrievedWaypoint = cjc.get(mtp[0].getWaypointname());
                        Intent intent = new Intent(MainActivity.this, AddW.class);

                        intent.putExtra("wpObject",retrievedWaypoint );
                        startActivity(intent);


                    } else if (opn.equalsIgnoreCase("getNames")) {
                        String[] result = cjc.getNames();
                        readNameResult = result;

                        refreshView(result);
                    } else if (opn.equalsIgnoreCase("getCategoryNames")) {
                        String[] result = cjc.getCategoryNames();
                        for (int i = 0; i < result.length; i++) {
                            System.out.println("response[" + i + "] is " + result[i]);
                        }
                    } else if (opn.equalsIgnoreCase("getNamesInCategory")) {
                        //   String[] result = cjc.getNamesInCategory(st.nextToken());
                        //    for (int i=0; i< result.length; i++){
                        //      System.out.println("response["+i+"] is "+result[i]);
                        //}
                    } else if (opn.equalsIgnoreCase("dist")) {
                        //  double result = cjc.distanceGCTo(st.nextToken(),st.nextToken());
                        //   System.out.println("response: "+result);
                    } else if (opn.equalsIgnoreCase("bear")) {
                        //   double result = cjc.bearingGCInitTo(st.nextToken(),
                        //           st.nextToken());
                        // System.out.println("response: "+result);
                    }
                   // System.out.print("Enter end or {remove|get|getNames|getCategoryNames|getNamesInCategory|dist|bear} arg1 arg2 \nEg  remove Toreros >");
                    // inStr = stdin.readLine();
                    // st = new StringTokenizer(inStr);
                    // opn = st.nextToken();


            }

            catch(Exception ex){
              //  ((TextView)findViewById(R.id.messageView)).setText("Error Creating Async task");

            }
            return 0.0;
        }

        public void refreshView(String[] result){
            waypointList.clear();
            waypointList.add("Add New Waypoint");
            for (int i = 0; i < result.length; i++) {
                System.out.println("response[" + i + "] is " + result[i]);
                android.util.Log.d("Name-"+i ,  readNameResult[i]);
                waypointList.add(readNameResult[i]);

            }
            adapter.notifyDataSetChanged();


        }

        protected void onPostExecute(Double res){
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(2);

          //  ((TextView)findViewById(R.id.displayNum)).setText(nf.format(res));

        }
    }

    public void cancelButton(View v){
        this.finish();

    }







}

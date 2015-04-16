package edu.asu.mscs.ashastry.json;

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
 * Purpose:
 *
 * @author : Aneesh Shastry  mailto:ashastry@asu.edu
 *           MS Computer Science, CIDSE, IAFSE, Arizona State University
 * @version : February 9, 2015
 */


import org.json.JSONArray;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.URL;


public class calculatorStub {
    int errorF = 0;
    private JsonRpcRequestViaHttp server;
    private static int id=0;
    calculatorStub(URL url,int errorFlag){
        server = new JsonRpcRequestViaHttp(url);
        this.errorF = errorFlag;
    }

    public double add(double left, double right) {
        double res = 0.0;
        try{
            JSONArray ja = new JSONArray();
            ja.put(0,left).put(1,right);
            String callStr = this.packageCalcCall("add",ja);
            android.util.Log.d("Client --> Server",callStr);
            String retStr = server.call(callStr);
            android.util.Log.d("Server --> Client",retStr);
            JSONObject result = new JSONObject(retStr);
            res = result.optDouble("result");

        }
        catch(ConnectException ex1){
            errorF = 1;
        }
        catch(Exception ex){

            android.util.Log.d(this.getClass().getSimpleName(),"Error with JSON add method");
        }
        return res;
    }



    public double sub(double left, double right){
        double res = 0.0;
        try{
            JSONArray ja = new JSONArray();
            ja.put(0,left).put(1,right);
            String callStr = this.packageCalcCall("subtract",ja);
            android.util.Log.d("Client --> Server",callStr);
            String retStr = server.call(callStr);
            android.util.Log.d("Server --> Client",retStr);
            JSONObject result = new JSONObject(retStr);
            res = result.optDouble("result");

        }
        catch(ConnectException ex1){
            errorF = 1;
        }
        catch(Exception ex){
            android.util.Log.e("Sub","Error in subtract call function");

        }

        return res;

    }

    public double mul(double left, double right){
        double res = 0.0;
        try{
            JSONArray ja = new JSONArray();
            ja.put(0,left).put(1,right);

            String callStr = this.packageCalcCall("multiply",ja);
            android.util.Log.d("Client --> Server",callStr);
            String retStr = server.call(callStr);
            android.util.Log.d("Server --> Client",retStr);
            JSONObject result = new JSONObject(retStr);
            res = result.optDouble("result");


        }
        catch(ConnectException ex1){
            errorF = 1;
        }
        catch(Exception ex){
            android.util.Log.e("Sub","Error in multiply call function");

        }
        return res;
    }

    public double div(double left, double right){
        double res = 0.0;
        try{
            JSONArray ja = new JSONArray();
            ja.put(0,left).put(1,right);
            String callStr = this.packageCalcCall("divide",ja);
            android.util.Log.d("Client --> Server",callStr);
            String retStr = server.call(callStr);
            android.util.Log.d("Server --> Client",retStr);
            JSONObject result = new JSONObject(retStr);
            res = result.optDouble("result");

        }
        catch(ConnectException ex1){
            errorF = 1;
        }
        catch(Exception ex){
            android.util.Log.e("Sub", "Error in divide call function");

        }
        return res;
    }

    private String packageCalcCall(String oper, JSONArray args){
        String ret = "";
        try{
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("jsonrpc","2.0");
            jsonObj.put("method",oper);
            jsonObj.put("id", ++id);
            String almost = jsonObj.toString();
            String toInsert = null;
            if(args == null){
                toInsert = ",\"params\":[]";
            } else{
                toInsert = ",\"params\":" + args.toString();
            }
            String begin = almost.substring(0,almost.length()-1);
            String end = almost.substring(almost.length()-1);
            ret = begin + toInsert + end;
        }
        catch(Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(),"Error with packageCalcCall method");

        }
        return ret;
    }
}

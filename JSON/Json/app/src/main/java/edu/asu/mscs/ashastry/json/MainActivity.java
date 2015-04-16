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

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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


    private static class MyTaskParams {
        URL newURL;
        String operatorV;


        MyTaskParams(URL url, String op) {
            this.newURL = url;
            this.operatorV = op;

        }

        public URL getNewURL() {
            return newURL;
        }

        public void setNewURL(URL newURL) {
            this.newURL = newURL;
        }

        public String getOperatorV() {
            return operatorV;
        }

        public void setOperatorV(String operatorV) {
            this.operatorV = operatorV;
        }
    }
    public void calculateClicked(View v){
        if(operator == "Divide" && exp2 == 0){
            ((TextView)findViewById(R.id.messageView)).setText("Divide by zero error");
            clear();
            operator = "";
            exp1Flag = 0;
            opFlag = 0;
        }else if(exp1Flag == 1) {


            saveExp2();
            android.util.Log.d("Left Expression", Double.toString(exp1));
            android.util.Log.d("Right Expression", Double.toString(exp2));
            android.util.Log.d("Operator", operator);
            try {
                URL URLOut = new URL(this.getString(R.string.url_string));
                MyTaskParams mp = new MyTaskParams(URLOut, operator);
                AsyncCalc ac = (AsyncCalc) new AsyncCalc().execute(mp);
            }

            catch (MalformedURLException m1){
                android.util.Log.d("Error in URL","");
            }

            catch (Exception ex) {
                android.util.Log.d(this.getClass().getSimpleName(), "Error connecting to the Server");
            }

            operator = "";
            exp1Flag = 0;
            opFlag = 0;

        }
        else{
            ((TextView)findViewById(R.id.messageView)).setText("Incomplete input");
        }
    }
    public double exp1,exp2,decExp = 0.0;
    public int exp1Flag,exp2Flag = 0;
    public String operator = "";
    public int decFlag = 0;
    public int opFlag = 0;
    public int dotFlag = 0;
    public int negFlag = 0;



    public void addNumber(int n){
        String minusSign = "";
        NumberFormat nfDisplay = NumberFormat.getInstance();
        nfDisplay.setMaximumFractionDigits(0);
        if(decFlag == 1){
            decExp = decExp*10 + n;
            if(exp1Flag == 0) {
                if(negFlag == 1){
                    minusSign = "-";
                }
                ((TextView) findViewById(R.id.displayNum)).setText(minusSign + nfDisplay.format(exp1).replaceAll(",", "") + "." + nfDisplay.format(decExp).replaceAll(",", ""));
            }else{
                if(negFlag == 1){
                    minusSign = "-";
                }
                ((TextView) findViewById(R.id.displayNum)).setText(minusSign + nfDisplay.format(exp2).replaceAll(",", "") + "." + nfDisplay.format(decExp).replaceAll(",", ""));
            }
        }
        else {
            if (exp1Flag == 0) {

                exp1 = exp1 * 10 + n;
                if(negFlag == 1){
                    minusSign = "-";
                }
                ((TextView) findViewById(R.id.displayNum)).setText(minusSign + nfDisplay.format(exp1).replaceAll(",",""));


            } else {

                exp2 = exp2 * 10 + n;
                if(negFlag == 1){
                    minusSign = "-";
                }
                ((TextView) findViewById(R.id.displayNum)).setText(minusSign + nfDisplay.format(exp2).replaceAll(",",""));

            }
        }
    }
    public void clickZero(View v){
        clearErr();
        addNumber(0);
    }

    public void clickOne(View v){
        clearErr();
        addNumber(1);
    }
    public void clickTwo(View v){
        clearErr();
        addNumber(2);
    }
    public void clickThree(View v){
        clearErr();
        addNumber(3);
    }
    public void clickFour(View v){
        clearErr();
        addNumber(4);
    }
    public void clickFive(View v){
        clearErr();
        addNumber(5);
    }
    public void clickSix(View v){
        clearErr();
        addNumber(6);
    }
    public void clickSeven(View v){
        clearErr();
        addNumber(7);
    }
    public void clickEight(View v){
        clearErr();
        addNumber(8);
    }
    public void clickNine(View v){
        clearErr();
        addNumber(9);
    }
    public void clickAdd(View v){
        clearErr();
        dotFlag = 0;
        saveExp1();
        decExp = 0;
        negFlag = 0;
       if(opFlag == 0) {
           ((TextView) findViewById(R.id.displayNum)).setText("+");
           opFlag = 1;
           exp1Flag = 1;
           decFlag = 0;
           operator = "Add";

       } else
       {
           ((TextView)findViewById(R.id.messageView)).setText("Invalid input , multiple operators");
       }
    }
    public void clickSub(View v){

        clearErr();
        dotFlag = 0;
        saveExp1();
        decExp = 0;
        negFlag = 0;
        if(opFlag == 0) {
            ((TextView) findViewById(R.id.displayNum)).setText("-");
            opFlag = 1;
            exp1Flag = 1;
            decFlag = 0;
            operator = "Subtract";
        } else
        {
            ((TextView)findViewById(R.id.messageView)).setText("Invalid input , multiple operators");
        }
    }
    public void clickMul(View v){
        clearErr();
        dotFlag = 0;
        saveExp1();
        decExp = 0;
        negFlag = 0;
        if(opFlag == 0) {
        ((TextView) findViewById(R.id.displayNum)).setText("*");
            opFlag = 1;
            exp1Flag = 1;
            decFlag = 0;
            operator = "Multiply";
        } else
        {
            ((TextView)findViewById(R.id.messageView)).setText("Invalid input , multiple operators");
        }
    }
    public void clickDiv(View v){
        clearErr();
        dotFlag = 0;
        saveExp1();
        decExp = 0;
        negFlag = 0;
        if(opFlag == 0) {
            ((TextView) findViewById(R.id.displayNum)).setText("/");
            opFlag = 1;
            exp1Flag = 1;
            decFlag = 0;
            operator = "Divide";
        } else
        {
            ((TextView)findViewById(R.id.messageView)).setText("Invalid input , multiple operators");
        }
    }
    public void clickDot(View v){
        String minusSign = "";
        if(dotFlag == 0) {
            dotFlag = 1;
            clearErr();
            NumberFormat nfDisplay = NumberFormat.getInstance();
            nfDisplay.setMaximumFractionDigits(0);
            if(negFlag == 1){
                minusSign = "-";
            }
            if (exp1Flag == 0) {

                ((TextView) findViewById(R.id.displayNum)).setText(minusSign + nfDisplay.format(exp1).replaceAll(",", "") + ".");
            } else {
                ((TextView) findViewById(R.id.displayNum)).setText(minusSign + nfDisplay.format(exp2).replaceAll(",", "") + ".");
            }

            decFlag = 1;
        }
    }
    public void clickClear(View v){
        clearErr();
        clear();

    }

    public void clear(){
        decExp = 0;
        decFlag = 0;
        opFlag = 0;
        negFlag = 0;

            exp1 = 0;
            exp2 = 0;
        exp1Flag = 0;
            ((TextView) findViewById(R.id.displayNum)).setText("");

    }

    public void clickNeg(View v){
        negFlag = 1;
        ((TextView) findViewById(R.id.displayNum)).setText("-");

    }
    public void clearErr(){
        ((TextView)findViewById(R.id.messageView)).setText("");
    }
    public void saveExp1(){
        NumberFormat nfDisplay = NumberFormat.getInstance();
        nfDisplay.setMaximumFractionDigits(0);
        if(negFlag == 1) {
            exp1 = Double.parseDouble("-" + nfDisplay.format(exp1).replaceAll(",", "") + "." + nfDisplay.format(decExp).replaceAll(",", ""));
        } else{
            exp1 = Double.parseDouble(nfDisplay.format(exp1).replaceAll(",", "") + "." + nfDisplay.format(decExp).replaceAll(",", ""));
        }
    }

    public void saveExp2(){
        NumberFormat nfDisplay = NumberFormat.getInstance();
        nfDisplay.setMaximumFractionDigits(0);
        if(negFlag == 1 ) {
            exp2 = Double.parseDouble("-" +  nfDisplay.format(exp2).replaceAll(",", "") + "." + nfDisplay.format(decExp).replaceAll(",", ""));
        } else {
            exp2 = Double.parseDouble(nfDisplay.format(exp2).replaceAll(",", "") + "." + nfDisplay.format(decExp).replaceAll(",", ""));
        }
    }


    public void clickExit(View v){
        finish();
        System.exit(0);
    }
    private class AsyncCalc extends AsyncTask<MyTaskParams, Integer, Double> {

        double val = 0.0;
        int errorFlag = 0;
       protected Double doInBackground(MyTaskParams... mtp) {
            try{
                calculatorStub calc = new calculatorStub(mtp[0].getNewURL(),errorFlag);
                if(errorFlag == 1){
                    ((TextView)MainActivity.this.findViewById(R.id.messageView)).setText("Cannot connect to the server");
                }
                String oper = mtp[0].getOperatorV();
                double leftOp = exp1;
                double rightOp = exp2;
                if("Add".equals(oper)){
                    val = calc.add(leftOp,rightOp);
                }
                else if("Subtract".equals(oper)){
                    val = calc.sub(leftOp, rightOp);
                }
                else if("Multiply".equals(oper)){
                    val = calc.mul(leftOp, rightOp);
                }
                else if("Divide".equals(oper)){
                    val = calc.div(leftOp, rightOp);
                }
            }


            catch(Exception ex){
                ((TextView)findViewById(R.id.messageView)).setText("Error Creating Async task");
                ((TextView)findViewById(R.id.displayNum)).setText("");
            }
           return val;
        }

        protected void onPostExecute(Double res){
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(2);
            ((TextView)findViewById(R.id.displayNum)).setText(nf.format(res));
            exp1 = 0;
            exp2 = 0;
            decExp = 0;
        }
    }


}

package com.example.english_biscuits;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class Bin_placement_activity extends AppCompatActivity implements FetchData{
    private ListView table;
    private TableDataAdapter tableDataAdapter;
    private List<WareHouseRecord> data;
    private TextView MaterialCode;
    private TextView MaterialDesc;
    private TextView lastScan;
    private EditText batchNo;
    private EditText binNuMber;
    private boolean isInternetConnected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bin_placement);
        Log.d("create", "onCreate: create");
        table = findViewById(R.id.table_data);
        lastScan = findViewById(R.id.lascan);
        TextView date = findViewById(R.id.date);
        batchNo = findViewById(R.id.batch);
        binNuMber = findViewById(R.id.binNumber);
        /*batchNo.setShowSoftInputOnFocus(false);
        binNuMber.setShowSoftInputOnFocus(false);*/

        MaterialCode = findViewById(R.id.Mat_code);
        MaterialDesc = findViewById(R.id.Mat_desc);
        batchNo.requestFocus();
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        binNuMber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = (EditText) view;
                String binNo = editText.getText().toString();
                NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                isInternetConnected = networkInfo != null && networkInfo.isConnected();
                if(isInternetConnected) {
                    if (!binNo.equals("")) {
                        WareHouseRecord dataToBeInserted = null;
                        for (WareHouseRecord record : data) {
                            if (record.getBinNuMber().equals(binNo)) {
                                dataToBeInserted = record;
                                break;
                            }
                        }
                        if (dataToBeInserted == null) {
                            Toast.makeText(Bin_placement_activity.this, "Invalid binno", Toast.LENGTH_LONG).show();
                            editText.setText("");
                        } else {
                            AsyncHttpClient client = new AsyncHttpClient();
                            RequestParams params = new RequestParams();
                            params.put("binno", dataToBeInserted.getBinNuMber());
                            params.put("to.no", dataToBeInserted.getTo_nuMber());
                            params.put("Quantity", dataToBeInserted.getQty());
                            params.put("UnitOfMeasure", "KAR");
                            params.put("Warehouse No", "KH6");
                            params.put("LineItemNumber", dataToBeInserted.getLineIteM());
                            Log.d("params", "onFocusChange: " + params.toString());
                            Log.d("data on focus", "onFocusChange: " + dataToBeInserted.toString());


                            WareHouseRecord finalDataToBeInserted = dataToBeInserted;
                            client.post("http://192.168.9.140:8086/EBM_Middleware/test_serv", params, new JsonHttpResponseHandler() {

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    super.onSuccess(statusCode, headers, response);
                                    Log.d("bin_no", "onSuccess: " + statusCode);
                                    Log.d("bin_no", "onSuccess: " + response);
                                    Log.d("statuss", "onSuccess: " + response);
                                    try {
                                        String status = response.getString("RETURN");
                                        Toast.makeText(Bin_placement_activity.this, status, Toast.LENGTH_LONG).show();
                                        if (status.equals("ITEM_CONFIRMED") || status.equals("Transfer Order " + finalDataToBeInserted.getTo_nuMber() + " has been successfully Confirmed in SAP System")) {
                                            tableDataAdapter.reMoveRecord(binNuMber.getText().toString());
                                            lastScan.setText(batchNo.getText().toString() + " / " + binNuMber.getText().toString());
                                            binNuMber.setText("");
                                            binNuMber.requestFocus();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    super.onFailure(statusCode, headers, throwable, errorResponse);
                                    Log.d("bin_no", "onFailure: " + throwable.getMessage());
                                    binNuMber.setText("");
                                    binNuMber.requestFocus();
                                }
                            });

                        }
                    }
                }
                else{
                    Toast.makeText(Bin_placement_activity.this, "You are not connected to the internet please check your internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });
/*
        binNuMber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                Log.d("bin_no_focus", "onFocusChange: " + focus);
                if(!focus && !refresh) {
                    start++;
                    refresh = false;
                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    String binNo = ((EditText) view).getText().toString();
                    WareHouseRecord dataToBeInserted = null;
                    int tNo = 0;
                    Log.d("bin_no", "onFocusChange: tno " + tNo);
                    for (WareHouseRecord record: data) {
                        if(record.getBinNuMber().equals(binNo)){
                            dataToBeInserted = record;
                            break;
                        }
                    }
                    if(dataToBeInserted != null) {
                        params.put("binno", dataToBeInserted.getBinNuMber());
                        params.put("to.no", dataToBeInserted.getTo_nuMber());
                        params.put("Quantity", dataToBeInserted.getQty());
                        params.put("UnitOfMeasure","KAR");
                        params.put("Warehouse No","KH6");
                        params.put("LineItemNumber",dataToBeInserted.getLineIteM());

                        Log.d("bin_no", "onFocusChange: tno " + tNo);

                        Log.d("params", "onFocusChange: " + params.toString());
                        Log.d("data on focus", "onFocusChange: " + dataToBeInserted.toString());


                        WareHouseRecord finalDataToBeInserted = dataToBeInserted;
                        client.post("http://192.168.9.140:8086/EBM_Middleware/test_serv", params, new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                Log.d("bin_no", "onSuccess: " + statusCode);
                                Log.d("bin_no", "onSuccess: " + response);
                                Log.d("statuss", "onSuccess: " + response);
                                try {
                                    String status = response.getString("RETURN");
                                    Toast.makeText(Bin_placement_activity.this, status, Toast.LENGTH_LONG).show();
                                    if (status.equals("ITEM_CONFIRMED") || status.equals("Transfer Order " + finalDataToBeInserted.getTo_nuMber()  + " has been successfully Confirmed in SAP System")) {
                                        tableDataAdapter.reMoveRecord(binNuMber.getText().toString());
                                        lastScan.setText(batchNo.getText().toString() + " / " + binNuMber.getText().toString());
                                        binNuMber.setText("");
                                        binNuMber.requestFocus();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                Log.d("bin_no", "onFailure: " + throwable.getMessage());
                                binNuMber.setText("");
                                binNuMber.requestFocus();
                            }
                        });
                    }
                    else{
                        Toast.makeText(Bin_placement_activity.this, "Invalid binno", Toast.LENGTH_LONG).show();
                        EditText v = (EditText) view;
                        v.setText("");
                        boolean reF = v.requestFocus();
                        Log.d("reF", "onFocusChange: " + reF);
                    }
                };
                refresh = false;

            }
        });*/
        Date date1 = new Date();
        date.setText(date1.getDate() + "/" + (date1.getMonth() + 1) + "/" + (date1.getYear() - 100));
        Log.d("Bin", "onCreate: " + date1.getDate() + "/" + date1.getMonth() + "/" + (date1.getYear() - 100));
        table.addHeaderView(LayoutInflater.from(this).
                inflate(R.layout.header, table, false)
        );
        tableDataAdapter = new TableDataAdapter(this, R.layout.row, eMptyList(), false);
        table.setAdapter(tableDataAdapter);
        batchNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = (EditText) view;
                String batchNo = editText.getText().toString();
                NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                isInternetConnected = networkInfo != null && networkInfo.isConnected();
                if(isInternetConnected) {
                    if (!batchNo.equals("")) {
                        AsyncHttpClient client = new AsyncHttpClient();
                        RequestParams params = new RequestParams();
                        params.put("batchno", ((EditText) view).getText().toString());
                        client.get("http://192.168.9.140:8086/EBM_Middleware/test_serv", params, new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                if (response.has("array")) {
                                    Log.d("Verification", "onSuccess: " + response);
                                    DataParser dataParser = DataParser.create(Bin_placement_activity.this);
                                    boolean chk = dataParser.parseData(response, "putaway");
                                    Log.d("Verification", "onSuccess: " + chk);
                                    if (!chk) {
                                        editText.requestFocus();

                                    } else {
                                        binNuMber.requestFocus();
                                    }
                                    binNuMber.setText("");
                                } else {
                                    try {
                                        Toast.makeText(Bin_placement_activity.this, response.getString("error"), Toast.LENGTH_LONG).show();
                                    } catch (JSONException e) {
                                        Log.d("error", "onSuccess: " + e.getMessage());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                Log.d("Verification", "onFailure: " + throwable.getMessage());
                            }
                        });
                    }
                }
                else{
                    Toast.makeText(Bin_placement_activity.this, "You are not connected to the internet please check your internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });
        /*batchNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                Log.d("focus", "onFocusChange: " + focus);
                if(start == 0 || (!focus && !refresh)){
                    start++;
                    refresh = false;
                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    params.put("batchno", ((EditText) view).getText().toString());
                    client.get("http://192.168.9.140:8086/EBM_Middleware/test_serv", params, new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                if(response.has("array")){
                                    Log.d("Verification", "onSuccess: " + response);
                                    DataParser dataParser = DataParser.create(Bin_placement_activity.this);
                                    boolean chk = dataParser.parseData(response, "putaway");
                                    Log.d("Verification", "onSuccess: " + chk);
                                    if (!chk) {
                                        refresh = true;
                                        batchNo.requestFocus();

                                    } else {
                                        binNuMber.requestFocus();
                                    }
                                    binNuMber.setText("");
                                }
                                else{
                                    try {
                                        Toast.makeText(Bin_placement_activity.this, response.getString("error"), Toast.LENGTH_LONG).show();
                                    }
                                    catch (JSONException e){
                                        Log.d("error", "onSuccess: " + e.getMessage());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                Log.d("Verification", "onFailure: " + throwable.getMessage());
                            }
                        });
                }

            }
        });*/

        /*batchNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focusStatus) {
                if(focusStatus){
                    AsyncHttpClient client = new AsyncHttpClient();
                    Log.d("bat", "onFocusChange: fetching data");
                    client.get("http://192.168.233.2:8086/EBM_Middleware/test_serv", new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            Log.d("sap get", "onSuccess: " + response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            Log.d("sap get fail", "onFailure: " + errorResponse.toString());
                        }
                    });
                }
            }
        });*/
    }

    @Override
    public void onDataFetched(List<WareHouseRecord> wareHouseRecordList) {
        WareHouseRecord wareHouseRecord = wareHouseRecordList.get(0);
        Log.d("fetched", "onDataFetched: " + wareHouseRecordList.size() + wareHouseRecord.getMaterialId() + wareHouseRecord.getMaterialDesc());
        MaterialCode.setText(wareHouseRecord.getMaterialId());
        MaterialDesc.setText(wareHouseRecord.getMaterialDesc());
        data = wareHouseRecordList;
        tableDataAdapter.updateData(wareHouseRecordList);
        tableDataAdapter.notifyDataSetChanged();
    }

    @Override
    public void showErrorMessage(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        batchNo.setText("");
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.key:
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                break;
            case R.id.refresh:
              doRefresh();
                break;
        }
        return super.onOptionsItemSelected(item);
    }*/
    private List<WareHouseRecord> eMptyList(){
        List<WareHouseRecord> wareHouseRecordList = new ArrayList<>();
        wareHouseRecordList.add(new WareHouseRecord("", "", "", 0, 0, "", ""));
        return wareHouseRecordList;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("key", "onKeyDown: " + keyCode);
        if(keyCode == 508 || keyCode == 507){
         doRefresh();
        }
        return super.onKeyDown(keyCode, event);
    }
    private void doRefresh(){
            onDataFetched(eMptyList());
            binNuMber.setText("");
            binNuMber.clearFocus();
            batchNo.setText("");
            batchNo.requestFocus();
            lastScan.setText("");
    }

}
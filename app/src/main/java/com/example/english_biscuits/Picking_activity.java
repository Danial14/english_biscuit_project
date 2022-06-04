package com.example.english_biscuits;

import androidx.appcompat.app.AppCompatActivity;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class Picking_activity extends AppCompatActivity implements FetchData {
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
        setContentView(R.layout.activity_picking);
        table = findViewById(R.id.table_data);
        lastScan = findViewById(R.id.lascan);
        TextView date = findViewById(R.id.date);
        Date date1 = new Date();
        date.setText(date1.getDate() + "/" + (date1.getMonth() + 1) + "/" + (date1.getYear() - 100));
        batchNo = findViewById(R.id.batchNumber);
        binNuMber = findViewById(R.id.binNumber);
        /*batchNo.setShowSoftInputOnFocus(false);
        binNuMber.setShowSoftInputOnFocus(false);*/

        MaterialCode = findViewById(R.id.Mat_code);
        MaterialDesc = findViewById(R.id.Mat_desc);
        binNuMber.requestFocus();
        tableDataAdapter = new TableDataAdapter(this, R.layout.row, eMptyList(), true);
        table.addHeaderView(LayoutInflater.from(this).
                inflate(R.layout.header_two, table, false)

        );
        table.setAdapter(tableDataAdapter);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        binNuMber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = (EditText) view;
                String bin = editText.getText().toString();
                NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                isInternetConnected = networkInfo != null && networkInfo.isConnected();
                if(isInternetConnected) {
                    if (!bin.equals("")) {
                        AsyncHttpClient client = new AsyncHttpClient();
                        RequestParams params = new RequestParams();
                        params.put("binno", ((EditText) view).getText().toString());
                        client.get("http://192.168.9.140:8086/EBM_Middleware/test_pick", params, new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                if (response.has("array")) {
                                    Log.d("Verification", "onSuccess: " + response);
                                    DataParser dataParser = DataParser.create(Picking_activity.this);
                                    boolean chk = dataParser.parseData(response, "picking");
                                    Log.d("Verification", "onSuccess: " + chk);
                                    if (!chk) {
                                        binNuMber.requestFocus();

                                    } else {
                                        batchNo.requestFocus();
                                    }
                                    batchNo.setText("");
                                } else {
                                    try {
                                        Toast.makeText(Picking_activity.this, response.getString("error"), Toast.LENGTH_LONG).show();
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
                    Toast.makeText(Picking_activity.this, "You are not connected to the internet please check your internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });
        /*binNuMber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                Log.d("focus", "onFocusChange: " + focus);
                if(start == 0 || (!focus && !refresh)){
                    start++;
                    refresh = false;
                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    params.put("binno", ((EditText) view).getText().toString());
                    client.get("http://192.168.9.140:8086/EBM_Middleware/test_pick", params, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            if(response.has("array")){
                                Log.d("Verification", "onSuccess: " + response);
                                DataParser dataParser = DataParser.create(Picking_activity.this);
                                boolean chk = dataParser.parseData(response, "picking");
                                Log.d("Verification", "onSuccess: " + chk);
                                if (!chk) {
                                    refresh = true;
                                    binNuMber.requestFocus();

                                } else {
                                    batchNo.requestFocus();
                                }
                                batchNo.setText("");
                            }
                            else{
                                try {
                                    Toast.makeText(Picking_activity.this, response.getString("error"), Toast.LENGTH_LONG).show();
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
        batchNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = (EditText) view;
                NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                isInternetConnected = networkInfo != null && networkInfo.isConnected();
                if(isInternetConnected) {
                    if (!editText.getText().toString().equals("")) {
                        AsyncHttpClient client = new AsyncHttpClient();
                        RequestParams params = new RequestParams();
                        WareHouseRecord dataToBeInserted = null;
                        for (WareHouseRecord record : data) {
                            if (record.getBatchNuMber().equals(batchNo.getText().toString())) {
                                dataToBeInserted = record;
                                break;
                            }
                        }
                        if (dataToBeInserted != null) {
                            params.put("tono", dataToBeInserted.getTo_nuMber());
                            params.put("batchno", ((EditText) view).getText().toString());
                            params.put("binno", dataToBeInserted.getBinNuMber());
                            client.post("http://192.168.9.140:8086/EBM_Middleware/test_pick", params, new JsonHttpResponseHandler() {

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    super.onSuccess(statusCode, headers, response);
                                    try {
                                        String status = response.getString("RETURN");
                                        Log.d("sap", "onSuccess: " + status);
                                        if (status.equalsIgnoreCase("TO order confirm successfully") || status.equals("")) {
                                            Toast.makeText(Picking_activity.this, "TO order confirm successfully", Toast.LENGTH_LONG).show();
                                            tableDataAdapter.reMoveRecord(batchNo.getText().toString());
                                            lastScan.setText(binNuMber.getText().toString() + " / " + batchNo.getText().toString());
                                            batchNo.setText("");
                                            batchNo.requestFocus();
                                        } else {
                                            Toast.makeText(Picking_activity.this, status, Toast.LENGTH_LONG).show();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    super.onFailure(statusCode, headers, throwable, errorResponse);
                                    Log.d("Verification", "onFailure: " + throwable.getMessage());
                                }
                            });
                        } else {
                            Toast.makeText(Picking_activity.this, "Invalid batch no", Toast.LENGTH_LONG).show();
                            batchNo.setText("");
                            batchNo.requestFocus();
                        }
                    }
                }
                else{
                    Toast.makeText(Picking_activity.this, "You are not connected to the internet please check your internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });
        /*batchNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                Log.d("focus", "onFocusChange: " + focus);
                if(start == 0 || (!focus && !refresh)) {
                    start++;
                    refresh = false;
                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    WareHouseRecord dataToBeInserted = null;
                    for (WareHouseRecord record : data) {
                        if (record.getBatchNuMber().equals(batchNo.getText().toString())) {
                            dataToBeInserted = record;
                            break;
                        }
                    }
                    if (dataToBeInserted != null) {
                        params.put("tono", dataToBeInserted.getTo_nuMber());
                        params.put("batchno", ((EditText) view).getText().toString());
                        params.put("binno", dataToBeInserted.getBinNuMber());
                        client.post("http://192.168.9.140:8086/EBM_Middleware/test_pick", params, new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                try {
                                    String status = response.getString("RETURN");
                                    Log.d("sap", "onSuccess: " + status);
                                    if(status.equalsIgnoreCase("TO order confirm successfully") || status.equals("")) {
                                        Toast.makeText(Picking_activity.this, "TO order confirm successfully", Toast.LENGTH_LONG).show();
                                        tableDataAdapter.reMoveRecord(batchNo.getText().toString());
                                        lastScan.setText(binNuMber.getText().toString() + " / " + batchNo.getText().toString());
                                        batchNo.setText("");
                                        batchNo.requestFocus();
                                    }
                                    else{
                                        Toast.makeText(Picking_activity.this, status, Toast.LENGTH_LONG).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                Log.d("Verification", "onFailure: " + throwable.getMessage());
                            }
                        });
                    }
                    else{
                        Toast.makeText(Picking_activity.this, "Invalid batch no", Toast.LENGTH_LONG).show();
                        batchNo.setText("");
                        batchNo.requestFocus();
                    }
                }

            }
        });*/
    }


    private List<WareHouseRecord> eMptyList(){
        List<WareHouseRecord> wareHouseRecordList = new ArrayList<>();
        wareHouseRecordList.add(new WareHouseRecord("", "", "", 0, 0, "", ""));
        return wareHouseRecordList;
    }

    @Override
    public void onDataFetched(List<WareHouseRecord> wareHouseRecordList) {
        Log.d("fetched", "onDataFetched: " + wareHouseRecordList.size());
        WareHouseRecord wareHouseRecord = wareHouseRecordList.get(0);
        MaterialCode.setText(wareHouseRecord.getMaterialId());
        MaterialDesc.setText(wareHouseRecord.getMaterialDesc());
        data = wareHouseRecordList;
        tableDataAdapter.updateData(wareHouseRecordList);
        tableDataAdapter.notifyDataSetChanged();
    }

    @Override
    public void showErrorMessage(String errorMessage) {
        Toast.makeText(this, "Invalid bin number", Toast.LENGTH_LONG).show();
        binNuMber.setText("");
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
            batchNo.setText("");
            batchNo.clearFocus();
            binNuMber.setText("");
            binNuMber.requestFocus();
            lastScan.setText("");
    }
}
package com.example.english_biscuits;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DataParser {
    private static DataParser dataParser;
    private FetchData fetchData;
    private DataParser(){

    }
    public static DataParser create(FetchData fetchData){
        if(dataParser == null){
            dataParser = new DataParser();

        }
        dataParser.fetchData = fetchData;

        return dataParser;
    }
    public boolean parseData(JSONObject jsonObject, String category){
        try {
            JSONArray data = jsonObject.getJSONArray("array");
            Log.d("data", "parseData: " + data);
            List<WareHouseRecord> wareHouseRecordList = new ArrayList<>();
            if(data.length() > 0){
                if(category.equals("putaway")) {
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject obj = data.getJSONObject(i);
                        String batchNo = obj.getString("CHARG");
                        String binNuMber = obj.getString("NLPLA");
                        String Material_Id = obj.getString("MATNR");
                        int qty = obj.getInt("VSOLM");
                        int to_nuMber = obj.getInt("TANUM");
                        String MaterialDesc = obj.getString("MAKTX");
                        String lineIteM = obj.getString("TAPOS");
                        wareHouseRecordList.add(new WareHouseRecord(binNuMber, batchNo, Material_Id, qty, to_nuMber, MaterialDesc, lineIteM));
                    }
                }
                else if(category.equals("picking")){
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject obj = data.getJSONObject(i);
                        String batchNo = obj.getString("CHARG");
                        String binNuMber = obj.getString("VLPLA");
                        String Material_Id = obj.getString("MATNR");
                        int qty = obj.getInt("VSOLM");
                        int to_nuMber = obj.getInt("TANUM");
                        String MaterialDesc = obj.getString("MAKTX");
                        String lineIteM = obj.getString("TAPOS");
                        wareHouseRecordList.add(new WareHouseRecord(binNuMber, batchNo, Material_Id, qty, to_nuMber, MaterialDesc, lineIteM));
                    }
                }
            fetchData.onDataFetched(wareHouseRecordList);
            }
            else{
                fetchData.showErrorMessage("Invalid batch no");
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }
}

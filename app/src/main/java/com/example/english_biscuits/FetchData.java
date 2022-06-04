package com.example.english_biscuits;

import java.util.List;

public interface FetchData {
    public void onDataFetched(List<WareHouseRecord> wareHouseRecordList);
    public void showErrorMessage(String errorMessage);
}

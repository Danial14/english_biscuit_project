package com.example.english_biscuits;

public class WareHouseRecord {
    private String binNuMber;
    private String batchNuMber;
    private String MaterialId;
    private int qty;
    private int to_nuMber;
    private String MaterialDesc;
    private String lineIteM;

    public WareHouseRecord(String binNuMber, String batchNuMber, String materialId, int qty, int to_nuMber, String MaterialDesc, String lineIteM) {
        this.binNuMber = binNuMber;
        this.batchNuMber = batchNuMber;
        MaterialId = materialId;
        this.qty = qty;
        this.to_nuMber = to_nuMber;
        this.MaterialDesc = MaterialDesc;
        this.lineIteM = lineIteM;
    }

    public String getBinNuMber() {
        return binNuMber;
    }

    public String getBatchNuMber() {
        return batchNuMber;
    }

    public String getMaterialId() {
        return MaterialId;
    }

    public int getQty() {
        return qty;
    }

    public int getTo_nuMber() {
        return to_nuMber;
    }

    public String getLineIteM() {
        return lineIteM;
    }

    @Override
    public String toString() {
        return "WareHouseRecord{" +
                "binNuMber='" + binNuMber + '\'' +
                ", batchNuMber='" + batchNuMber + '\'' +
                ", MaterialId='" + MaterialId + '\'' +
                ", qty=" + qty +
                ", to_nuMber=" + to_nuMber +
                ", MaterialDesc='" + MaterialDesc + '\'' +
                ", lineIteM='" + lineIteM + '\'' +
                '}';
    }

    public String getMaterialDesc() {
        return MaterialDesc;
    }
}

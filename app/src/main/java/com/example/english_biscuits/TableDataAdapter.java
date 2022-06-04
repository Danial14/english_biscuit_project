package com.example.english_biscuits;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class TableDataAdapter extends ArrayAdapter {
    private Context context;
    private int resource;
    private List<WareHouseRecord> wareHouseRecordList;
    private boolean isPicking;

    public TableDataAdapter(@NonNull Context context, int resource, @NonNull List<WareHouseRecord> wareHouseRecordList, boolean isPicking) {
        super(context, resource, wareHouseRecordList);
        this.context = context;
        this.resource = resource;
        this.wareHouseRecordList = wareHouseRecordList;
        this.isPicking = isPicking;
    }

    public void reMoveRecord(String binNuMber){
        if(!isPicking){
            for (WareHouseRecord wareHouseRecord : wareHouseRecordList) {
                if (wareHouseRecord.getBinNuMber().equals(binNuMber)) {
                    wareHouseRecordList.remove(wareHouseRecord);
                    notifyDataSetChanged();
                    break;
                }
            }
        }
        else{
            for (WareHouseRecord wareHouseRecord : wareHouseRecordList) {
                if (wareHouseRecord.getBatchNuMber().equals(binNuMber)) {
                    wareHouseRecordList.remove(wareHouseRecord);
                    notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    @Override
    public int getCount() {
        return wareHouseRecordList.size();
    }

    public void updateData(List<WareHouseRecord> data){
        wareHouseRecordList = data;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        MyViewHolder viewHolder = null;
        if(row == null){
            row = LayoutInflater.from(context).inflate(resource, parent, false);
            viewHolder = new MyViewHolder(row);
            row.setTag(viewHolder);
        }
        else {
            viewHolder = (MyViewHolder) row.getTag();

        }
        if(isPicking){
            viewHolder.binNuMber.setText(wareHouseRecordList.get(position).getBatchNuMber());
        }
        else{
            viewHolder.binNuMber.setText(wareHouseRecordList.get(position).getBinNuMber());
        }
        //viewHolder.batchNuMber.setText(wareHouseRecordList.get(position).getBatchNuMber());
        //viewHolder.MaterialId.setText(wareHouseRecordList.get(position).getMaterialId());
        viewHolder.qty.setText(wareHouseRecordList.get(position).getQty() + "");
        viewHolder.to_nuMber.setText(wareHouseRecordList.get(position).getTo_nuMber() + "");

        return row;
    }

}
final class MyViewHolder{
    final TextView binNuMber;
    //final TextView batchNuMber;
    //final TextView MaterialId;
    final TextView qty;
    final TextView to_nuMber;
    final TextView batchNuMber = null;
    MyViewHolder(View view){
        to_nuMber = view.findViewById(R.id.storage_type);
        qty = view.findViewById(R.id.warehouse_nuMber);
       // MaterialId = view.findViewById(R.id.Material_desc);
       // batchNuMber = view.findViewById(R.id.Material_code);
        binNuMber = view.findViewById(R.id.Bin_nuMber);
    }
}

package com.example.english_biscuits;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class ebm_menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ebm_menu);

        Init();
    }

    private void Init() {

        AppCompatButton btn_Putaway = findViewById(R.id.btn_Putaway);
        AppCompatButton btn_Picking = findViewById(R.id.btn_Picking);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("EBM");
        actionBar.setIcon(R.drawable.logout);
       // actionBar.
        btn_Putaway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ebm_menu.this,Bin_placement_activity.class);
                startActivity(intent);
            }
        });

        btn_Picking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ebm_menu.this, Picking_activity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_two, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.logout_putaway:
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
        }
        return true;
    }
}
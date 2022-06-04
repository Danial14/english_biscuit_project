package com.example.english_biscuits;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatButton login = findViewById(R.id.login);
        EditText User = findViewById(R.id.user);
        EditText pass = findViewById(R.id.pass);

        User.setText("irfan");
        pass.setText("ir");
        ProgressBar progressBar = findViewById(R.id.pbar);
        progressBar.setVisibility(View.INVISIBLE);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

        if(User.getText().toString().equals("irfan") && pass.getText().toString().equals("ir"))
        {
            Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, ebm_menu.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(MainActivity.this,"Invaild Username",Toast.LENGTH_LONG).show();
        }
                /*progressBar.setVisibility(View.VISIBLE);
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("usrid", User.getText().toString());
                params.put("pw", pass.getText().toString());
                client.post("https://www.indus-erp.com/ords/wms/wms01/usrAuth", params, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        progressBar.setVisibility(View.INVISIBLE);
                        try {
                            String credential = response.getString("credential");
                            Log.d("success", "onSuccess: " + credential);
                            if(credential.equals("valid")){
                                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MainActivity.this, ebm_menu.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });*/
            }
        });
    }
}
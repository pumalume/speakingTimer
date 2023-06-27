package com.ingilizceevi.pickthepicture;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class GetUserActivity extends AppCompatActivity {
    ImageView myButton;
    EditText editIP;
    Button skipLoginButton;
    Button enterLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        myButton = findViewById(R.id.userCancel);
        //editIP = findViewById(R.id.enterIP);
        //enterLoginButton = findViewById(R.id.enterLogin);
        skipLoginButton = findViewById(R.id.skipLogin);
        myButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
       });
        skipLoginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GetUserActivity.this, MenuActivity.class);
                intent.putExtra("studentName", "EnglishHouse");
                intent.putExtra("studentID", 0);
                GetUserActivity.this.startActivity(intent);
            }
        });
        EntryControl entryControl = new EntryControl(GetUserActivity.this);
        entryControl.execute("");
        /*
        enterLoginButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String myIpString = editIP.getText().toString();
                EntryControl entryControl = new EntryControl(GetUserActivity.this);
                entryControl.execute(myIpString);
            }
        });

         */

    }
}

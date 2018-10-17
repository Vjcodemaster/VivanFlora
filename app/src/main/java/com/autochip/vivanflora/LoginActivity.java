package com.autochip.vivanflora;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import app_utility.VivanFloraAsyncTask;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    TextInputLayout etUserID, etPassword;
    VivanFloraAsyncTask vivanFloraAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vivanFloraAsyncTask = new VivanFloraAsyncTask();
                vivanFloraAsyncTask.execute(String.valueOf(3), "");
                /*String sUserID = etUserID.getEditText().getText().toString();
                Intent in = new Intent(LoginActivity.this, HomeScreenActivity.class);
                startActivity(in);
                finish();*/
            }
        });
        ////7894561230
    }

    void init(){
        etUserID = findViewById(R.id.et_ID);
        etPassword = findViewById(R.id.et_password);

        btnLogin = findViewById(R.id.btn_login);
    }
}

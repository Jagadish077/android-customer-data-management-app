package com.example.anmolpipes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText Username, Password;
    private Button login;
    private TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkLogin()){
                    Intent intent = new Intent(MainActivity.this, DataActivity.class);
                    startActivity(intent);
                }else{
                    error.setTextColor(getResources().getColor(R.color.Login_err));
                    error.setVisibility(View.VISIBLE);
                    error.setText(R.string.login_error);

                }
            }
        });
    }

    private void initWidgets() {
        Username = (EditText) findViewById(R.id.Username);
        Password = (EditText) findViewById(R.id.Password);
        login = (Button) findViewById(R.id.Login);
        error = (TextView) findViewById(R.id.TextError);
    }

    private Boolean checkLogin(){
        if(Username.getText().toString().matches("")){
            return false;
        }
        if(Password.getText().toString().matches("")){
            return false;
        }
        if(!(Username.getText().toString().equals("123"))){
            return false;
        }
        if(!(Password.getText().toString().equals("123"))){
            return false;
        }
        return true;
    }
}
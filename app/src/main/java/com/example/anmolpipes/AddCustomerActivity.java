package com.example.anmolpipes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.anmolpipes.addCustomerFragments.AddCustomerFragment;
import com.example.anmolpipes.addCustomerFragments.CustomerUpdateFragment;
import com.example.anmolpipes.datahelperclasses.Customer;

import java.util.Objects;

public class AddCustomerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        Intent intent = getIntent();
        boolean activate = intent.getBooleanExtra("isNewCustomer", false);
        if (activate) {
            getSupportFragmentManager().beginTransaction().replace(R.id.customer_main_fragment, new AddCustomerFragment(this)).commit();
        } else {
            Objects.requireNonNull(getSupportActionBar()).hide();
            int id = intent.getIntExtra("customerId", 0);
            Log.d("idddd", ""+id);
            getSupportFragmentManager().beginTransaction().replace(R.id.customer_main_fragment, new CustomerUpdateFragment(this, id)).commit();
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, DataActivity.class);
        startActivity(intent);
    }
}
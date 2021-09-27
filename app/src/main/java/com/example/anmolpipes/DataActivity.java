package com.example.anmolpipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.anmolpipes.datahelperclasses.Customer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class DataActivity extends AppCompatActivity {

    public dataAdapter adapter;
    private RecyclerView mDataRecyclerView;
    public List<Customer> datas;
    public SqlOperation sql;
    int backPressCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        backPressCount = 0;
        updateUi();

    }
    private void updateUi(){
        mDataRecyclerView = findViewById(R.id.recyclerView);
        //R.id.data_list is recyclerview id and we need to pass that view to create a recycler view
        mDataRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        datas = new ArrayList<>();
        sql = new SqlOperation(this);
        datas = sql.selectAllCustomer();
        adapter = new dataAdapter(datas, this);
        mDataRecyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options, menu);
        MenuItem menuItem = menu.findItem(R.id.search_action);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    if(adapter != null){
                        adapter.getFilter().filter(newText);
                    }else{
                        Toast.makeText(DataActivity.this, "Adapter is null", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }catch (Exception e){
                    Log.getStackTraceString(e);
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                logout();
                break;
            case R.id.Add_Customer:
                addCustomer();
                break;
            case R.id.gen_pdf:
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void addCustomer() {
        Intent intent = new Intent(this, AddCustomerActivity.class);
        intent.putExtra("isNewCustomer", true);
        startActivity(intent);
    }

    private void logout() {
        finish();
        System.exit(0);
    }

    @Override
    public void onBackPressed() {
        backPressCount++;
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
        if (backPressCount == 2){
            super.onBackPressed();
            finish();
        }
    }
}
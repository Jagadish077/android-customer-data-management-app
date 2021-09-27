package com.example.anmolpipes.updated.history;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.anmolpipes.DataActivity;
import com.example.anmolpipes.R;
import com.example.anmolpipes.SqlOperation;
import com.example.anmolpipes.datahelperclasses.UpdatedHistory;

import java.util.ArrayList;
import java.util.List;

public class UpdatedDataActivity extends AppCompatActivity {

    private ListView list_view;
    private TextView noHistory;
    private List<UpdatedHistory> history;
    private SqlOperation sql = new SqlOperation(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updated_data);
        list_view = findViewById(R.id.list_view);
        noHistory = findViewById(R.id.no_history);
        Intent intent = getIntent();
        int id = intent.getIntExtra("customerId", 0);
        history = new ArrayList<>();
        history = sql.getUpdatedHistory(id);
        ListAdapter ls = new ListAdapter(this, history);
        if (history != null) {
            list_view.setAdapter(ls);
        } else {
            list_view.setVisibility(View.GONE);
            noHistory.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, DataActivity.class);
        startActivity(intent);
    }
}
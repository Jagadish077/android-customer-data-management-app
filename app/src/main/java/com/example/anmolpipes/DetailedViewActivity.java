package com.example.anmolpipes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anmolpipes.datahelperclasses.Customer;
import com.example.anmolpipes.datahelperclasses.DataFields;
import com.example.anmolpipes.datahelperclasses.DateFormatter;
import com.example.anmolpipes.detailedview.DetailedViewAdapter;

import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DetailedViewActivity extends AppCompatActivity {

    private RecyclerView moreDetailedView;
    private List<List<DataFields>> groupdata = new ArrayList<List<DataFields>>();;
    private SqlOperation sql = new SqlOperation(this);
    private TextView customerName, date, total_amount, add, minus;
    private TableLayout main, childTable;
    private EditText add_money, minus_money;
    private DateFormatter formatter = new DateFormatter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);
        Intent intent = getIntent();
        int id =intent.getIntExtra("customerId", 0);
        initUiElements(id);
        try {
            getDataFromDb(id);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setUpRecylerView(id);
    }

    private void initUiElements(int id) {
        customerName = findViewById(R.id.det_customer_name);
        date = findViewById(R.id.det_date);
        total_amount = findViewById(R.id.det_total_amount);
        main = findViewById(R.id.main);
        childTable = findViewById(R.id.childTable);
        add = findViewById(R.id.add_amount);
        add_money = findViewById(R.id.add_money);
        minus = findViewById(R.id.minus_amount);
        minus_money = findViewById(R.id.minus_money);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (add_money.getText().toString().equals("")) {
                    Toast.makeText(DetailedViewActivity.this, "Invalid Amount", Toast.LENGTH_SHORT).show();
                } else {
                    boolean updatedAmount = sql.addCustomerAmount(id, Double.parseDouble(add_money.getText().toString()));
                    if (updatedAmount) {
                        Toast.makeText(DetailedViewActivity.this, "Amount Updated", Toast.LENGTH_SHORT).show();
                        try {
                            getDataFromDb(id);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        add_money.setText("");
                    } else {
                        Toast.makeText(DetailedViewActivity.this, "Something Went Wrong..!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (minus_money.getText().toString().equals("")) {
                    Toast.makeText(DetailedViewActivity.this, "Invalid Amount", Toast.LENGTH_SHORT).show();
                } else {
                    if (sql.reduceSingleCustomertAmount(id, Double.parseDouble(minus_money.getText().toString()))) {
                        try {
                            getDataFromDb(id);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        minus_money.setText("");
                    } else {
                        Toast.makeText(DetailedViewActivity.this, "Something Went Wrong..!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (childTable.getVisibility() == View.GONE) {
                    childTable.setVisibility(View.VISIBLE);
                } else {
                    childTable.setVisibility(View.GONE);
                }
            }
        });
    }

    public void getDataFromDb(int id) throws ParseException {
        Customer cust = sql.selectCustomerById(id);
       customerName.setText(cust.getName());
       date.setText(formatter.getDate(cust.getDate()));
       double total = sql.getSingleCustomerAmount(id);
       if (total != 0) {
            total_amount.setText(String.valueOf(total));
       } else {
           total_amount.setText("00");
       }

    }


    public void setUpRecylerView(int id) {
        try {
            moreDetailedView = findViewById(R.id.main_recycler_view);
            moreDetailedView.setLayoutManager(new LinearLayoutManager(this));
            groupdata = sql.selectAllProducts(id);
            moreDetailedView.setAdapter(new DetailedViewAdapter(groupdata, this));
        }catch (Exception e){
            e.printStackTrace();
            Log.e("Error", ""+e);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, DataActivity.class);
        startActivity(intent);
    }
}
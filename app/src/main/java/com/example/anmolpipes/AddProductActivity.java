package com.example.anmolpipes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import android.widget.TextView;

import com.example.anmolpipes.datahelperclasses.Customer;
import com.example.anmolpipes.datahelperclasses.DataFields;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class AddProductActivity extends AppCompatActivity {

    private FloatingActionButton add_row;
    private LinearLayout viewHolder;
    private TextView customerName, date, amount;
    public SqlOperation sql = new SqlOperation(this);
    private  Customer c;
    private List<DataFields> datas = new ArrayList<>();;
    private Button addAllProducts;
    private LinearLayout add_product_fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        add_product_fragment = findViewById(R.id.add_product_fragment);
            initComponents();
        }

    private void initComponents() {
        add_row = findViewById(R.id.add_row);
        viewHolder = findViewById(R.id.view_holder);
        customerName = findViewById(R.id.name_text);
        date = findViewById(R.id.order_text);
        amount = findViewById(R.id.amount_text);
        add_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addViewElements();
            }
        });
        Intent intent = getIntent();
        int custId = intent.getIntExtra("customerId", 0);
        c = sql.selectCustomerById(custId);
        customerName.setText(c.getName());
    }

    private void addViewElements(){
        View view = getLayoutInflater().inflate(R.layout.product_entry_design1, null, false);
        ImageButton del = view.findViewById(R.id.delete_product);
        addAllProducts = findViewById(R.id.add_all_products);
        if (!addAllProducts.isEnabled()) {
            addAllProducts.setEnabled(true);
        }
        EditText et03 = view.findViewById(R.id.et03);
        TextView t = view.findViewById(R.id.et05);
        EditText et = view.findViewById(R.id.et04);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if((!et03.getText().toString().matches("")) && (!et.getText().toString().matches(""))){
                    float q = Float.parseFloat(et03.getText().toString());
                    float r = Float.parseFloat(et.getText().toString());
                    float res = q*r;
                    t.setText(String.valueOf(res));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        addAllProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSuccess = addAllProductsToDb();
                if (isSuccess) {
                    Intent intent = new Intent(AddProductActivity.this, DetailedViewActivity.class);
                    intent.putExtra("customerId", c.getId());
                    startActivity(intent);
                } else {
                    if (addAllProducts.isEnabled()) {
                        addAllProducts.setEnabled(false);
                    }
                }
            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeViewElements(view);
            }
        });
        viewHolder.addView(view);
    }


    private boolean addAllProductsToDb(){
        if (viewHolder.getChildCount() == 0 || viewHolder.getChildCount() < 1) {
            return false;
        } else if (validateDynamicView()) {
            datas.clear();
            double totalAmount = 0;
            int prodId = sql.insertProducts(c.getId(), totalAmount);
            if (prodId != 0){
                double customerAmount = 0;
                for (int i = 0; i<viewHolder.getChildCount(); i++){
                    View childViews = viewHolder.getChildAt(i);
                    EditText et01 = childViews.findViewById(R.id.et01);
                    EditText et02 = childViews.findViewById(R.id.et02);
                    EditText et03 = childViews.findViewById(R.id.et03);
                    EditText et04 = childViews.findViewById(R.id.et04);
                    TextView et05 = childViews.findViewById(R.id.et05);
                    totalAmount = totalAmount+Float.parseFloat(et05.getText().toString());
                    datas.add(new DataFields(c.getId(), prodId, et01.getText().toString(), et02.getText().toString(), Double.parseDouble(et03.getText().toString()), Double.parseDouble(et04.getText().toString()), Double.parseDouble(et05.getText().toString())));
                    customerAmount = customerAmount + Float.parseFloat(et05.getText().toString());
                }
                double[] isInserted = sql.insertAllProducts(datas);
                if (isInserted[0] == 0.0){
                    if (isInserted[1] != 0.0){
                        sql.updateCustomerAmount(c.getId(), customerAmount);
                        boolean isUpdated = sql.updateTotalAmount(prodId, isInserted[1]);
                        if (isUpdated) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private boolean validateDynamicView() {
        for (int i = 0; i< viewHolder.getChildCount(); i++) {
            View v = viewHolder.getChildAt(i);
            EditText et01 = v.findViewById(R.id.et01);
            EditText et02 = v.findViewById(R.id.et02);
            EditText et03 = v.findViewById(R.id.et03);
            EditText et04 = v.findViewById(R.id.et04);
            if (et01.getText().toString().matches("") )  {
                return false;
            }
            if (et02.getText().toString().matches("")) {
                return false;
            }
            if (et03.getText().toString().matches("")) {
                return false;
            }

            if (et04.getText().toString().matches("")) {
                return false;
            }
        }
        return true;
    }

    private void removeViewElements(View v){
        viewHolder.removeView(v);
    }

    @Override
    public void onBackPressed() {
        if (viewHolder.getChildCount() > 0) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Anmol Pipes");
            dialog.setMessage("Added Products will not be saved. do want to continue?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(AddProductActivity.this, DataActivity.class);
                    startActivity(intent);
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).show();

        } else {
            Intent intent = new Intent(AddProductActivity.this, DataActivity.class);
            startActivity(intent);
        }
    }
}
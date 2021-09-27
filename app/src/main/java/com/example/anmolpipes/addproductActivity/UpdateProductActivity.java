package com.example.anmolpipes.addproductActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anmolpipes.DataActivity;
import com.example.anmolpipes.DetailedViewActivity;
import com.example.anmolpipes.R;
import com.example.anmolpipes.SqlOperation;
import com.example.anmolpipes.datahelperclasses.DataFields;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UpdateProductActivity extends AppCompatActivity {

    private LinearLayout update_product_data;
    private Button btn, update;
    int customerId = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);
        update_product_data = findViewById(R.id.update_product_data);
        btn = findViewById(R.id.up_add_btn);
        update = findViewById(R.id.update_product);
        Intent intent = getIntent();
        Objects.requireNonNull(getSupportActionBar()).hide();
        List<DataFields> data = new ArrayList<>();
        data = (List<DataFields>) intent.getSerializableExtra("productData");
        customerId = data.get(0).getCustomerId();
        Log.d("CustomerId", ""+customerId);
        initUiElements(data);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addView();
            }
        });

        addDynamicView(data);
    }

    private void initUiElements(List<DataFields> data) {
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputData()) {
                    update.setVisibility(View.GONE);
                } else {
                    updateDataToDb(data);
                }

            }
        });
    }

    private void updateDataToDb(List<DataFields> data) {
        Log.d("ListForUpdate", ""+data);
        SqlOperation sql = new SqlOperation(this);
        List<DataFields> updatedData = new ArrayList<>();
        for (int i = 0; i<update_product_data.getChildCount(); i++) {
            View view = update_product_data.getChildAt(i);
            EditText et01 = view.findViewById(R.id.et01);
            EditText et02 = view.findViewById(R.id.et02);
            EditText et03 = view.findViewById(R.id.et03);
            EditText et04 = view.findViewById(R.id.et04);
            TextView et05 = view.findViewById(R.id.et05);

            try {
                updatedData.add(new DataFields(data.get(0).getCustomerId(), data.get(i).getProductId(), data.get(0).getProductsId(), et01.getText().toString(), et02.getText().toString(), Double.parseDouble(et03.getText().toString()), Double.parseDouble(et04.getText().toString()), Double.parseDouble(et05.getText().toString()), data.get(0).getDelivery(), data.get(0).getTotalAmount()));

            } catch (IndexOutOfBoundsException e) {
                updatedData.add(new DataFields(data.get(0).getCustomerId(), -1, data.get(0).getProductsId(), et01.getText().toString(), et02.getText().toString(), Double.parseDouble(et03.getText().toString()), Double.parseDouble(et04.getText().toString()), Double.parseDouble(et05.getText().toString()), data.get(0).getDelivery(), data.get(0).getTotalAmount()));

            }
        }
        boolean isUpdated = sql.updateProductsData(updatedData);
        if (!isUpdated) {
            Toast.makeText(this, "Products got updated", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, DataActivity.class);
            startActivity(intent);
        }
    }

    private void addDynamicView(List<DataFields> data) {
        for (int i = 0; i<data.size(); i++) {
            View view = getLayoutInflater().inflate(R.layout.product_entry_design1, null, false);
            EditText et01 = view.findViewById(R.id.et01);
            EditText et02 = view.findViewById(R.id.et02);
            EditText et03 = view.findViewById(R.id.et03);
            EditText et04 = view.findViewById(R.id.et04);
            TextView et05 = view.findViewById(R.id.et05);
            ImageButton rem = view.findViewById(R.id.delete_product);
            rem.setEnabled(false);
            et04.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if((!et03.getText().toString().matches("")) && (!et04.getText().toString().matches(""))){
                        float q = Float.parseFloat(et03.getText().toString());
                        float r = Float.parseFloat(et04.getText().toString());
                        float res = q*r;
                        et05.setText(String.valueOf(res));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            et01.setText(data.get(i).getProductName());
            et02.setText(String.valueOf(data.get(i).getSize()));
            et03.setText(String.valueOf(data.get(i).getQuantity()));
            et04.setText(String.valueOf(data.get(i).getRate()));
            et05.setText(String.valueOf(data.get(i).getTotalAmountOfAllProduct()));
            update_product_data.addView(view);
        }
    }

    private void removeView(View view) {
        update_product_data.removeView(view);
    }

    private void addView() {
        View view = getLayoutInflater().inflate(R.layout.product_entry_design1, null, false);
        EditText et03 = view.findViewById(R.id.et03);
        EditText et04 = view.findViewById(R.id.et04);
        TextView et05 = view.findViewById(R.id.et05);
        ImageButton rem = view.findViewById(R.id.delete_product);
        rem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView(view);
            }
        });
        et04.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if((!et03.getText().toString().matches("")) && (!et04.getText().toString().matches(""))){
                    float q = Float.parseFloat(et03.getText().toString());
                    float r = Float.parseFloat(et04.getText().toString());
                    float res = q*r;
                    et05.setText(String.valueOf(res));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        update_product_data.addView(view);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, DataActivity.class);
        startActivity(intent);
    }

    public boolean validateInputData() {
        boolean err = false;
        for (int i = 0; i<update_product_data.getChildCount(); i++) {
            View inputView = update_product_data.getChildAt(i);
            EditText et01 = inputView.findViewById(R.id.et01);
            EditText et02 = inputView.findViewById(R.id.et02);
            EditText et03 = inputView.findViewById(R.id.et03);
            EditText et04 = inputView.findViewById(R.id.et04);
            if (((et01.getText().toString().matches("")||et02.getText().toString().matches(""))&&(et03.getText().toString().matches("")||et04.getText().toString().matches("")))) {
                boolean data = (!(et01.getText().toString().matches("")||et02.getText().toString().matches(""))&&!(et03.getText().toString().matches("")||et04.getText().toString().matches("")));
                Log.d("inputData", ""+data);
                update.setVisibility(View.GONE);
                err = true;
            }
        }
        if (!err) {
            update.setVisibility(View.VISIBLE);
        }
        return err;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        validateInputData();
        return super.onKeyUp(keyCode, event);
    }
}
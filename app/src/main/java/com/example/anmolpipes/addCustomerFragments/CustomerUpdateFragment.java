package com.example.anmolpipes.addCustomerFragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.anmolpipes.DataActivity;
import com.example.anmolpipes.R;
import com.example.anmolpipes.SqlOperation;
import com.example.anmolpipes.datahelperclasses.Customer;
import com.example.anmolpipes.datahelperclasses.DateFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CustomerUpdateFragment extends Fragment {


    private Context context;
    private EditText up_username, up_storeName, up_phoneNumber, up_emailId, up_totalAmount, up_date;
    private Button update_customer, close_update_customer;
    final Calendar myCalendar = Calendar.getInstance();
    DateFormatter d = new DateFormatter();
    private Customer customer;
    private Customer c;
    private final int id;
    private final SqlOperation sql;
    public CustomerUpdateFragment(Context context, int id) {
        this.context = context;
        this.id = id;
        this.sql = new SqlOperation(context);
        this.c = sql.selectCustomerById(id);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_update, container, false);
        up_username = view.findViewById(R.id.update_customerName);
        up_storeName = view.findViewById(R.id.update_storeName);
        up_phoneNumber = view.findViewById(R.id.update_phoneNumber);
        up_emailId = view.findViewById(R.id.update_customerEmail);
        up_date = view.findViewById(R.id.datePicker);
        up_totalAmount = view.findViewById(R.id.up_customer_amount);
        update_customer = view.findViewById(R.id.update_customer);
        close_update_customer = view.findViewById(R.id.close_update_customer);
        if (c != null) {
            up_username.setText(c.getName());
            up_storeName.setText(c.getStoreName());
            up_phoneNumber.setText(c.getPhone());
            up_emailId.setText(c.getEmail());
            up_date.setText(c.getDate());
            up_totalAmount.setText(String.valueOf(c.getTotalAmount()));
            update_customer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validateEditTexts()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Anmol Pipes");
                        builder.setMessage("Are u sure want to update?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                customer = new Customer(id, up_username.getText().toString(), up_emailId.getText().toString(), up_phoneNumber.getText().toString(), up_storeName.getText().toString(), up_date.getText().toString(), Double.parseDouble(up_totalAmount.getText().toString()));
                                boolean isUpdated = sql.updateCustomer(customer);
                                if (isUpdated) {
                                    Toast.makeText(context, "Customer Updated", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, DataActivity.class);
                                    context.startActivity(intent);
                                }
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();
                    } else {
                        Toast.makeText(context, "Enter all credintials properly", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            close_update_customer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DataActivity.class);
                    context.startActivity(intent);
                }
            });
        }

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                try {
                    updateLabel();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            private void updateLabel() throws ParseException {
                DateFormatter d = new DateFormatter();
                String myFormat = "MM/dd/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                up_date.setText(""+sdf.format(myCalendar.getTime())+ " "+d.getTime()+"");
            }
        };
        up_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        return view;
    }

    private boolean validateEditTexts() {
        return !up_username.getText().toString().matches("") && !up_storeName.getText().toString().matches("") &&
                !up_emailId.getText().toString().matches("") && !up_phoneNumber.getText().toString().matches("")
                && !up_totalAmount.getText().toString().matches("") && !up_date.getText().toString().matches("");
    }




}
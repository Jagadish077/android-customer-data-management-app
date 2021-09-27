package com.example.anmolpipes.addCustomerFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.anmolpipes.AddCustomerActivity;
import com.example.anmolpipes.DataActivity;
import com.example.anmolpipes.R;
import com.example.anmolpipes.SqlOperation;
import com.example.anmolpipes.datahelperclasses.Customer;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddCustomerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddCustomerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private EditText customerName, storeName, phone, email;
    private TextView error_customer;
    private Button customerSave;
    private Customer cust;
    private Context context;
    private SqlOperation sql;

    public AddCustomerFragment() {
    }
    public AddCustomerFragment(Context context) {
        // Required empty public constructor
        this.context = context;
        sql = new SqlOperation(context);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddCustomerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddCustomerFragment newInstance(String param1, String param2) {
        AddCustomerFragment fragment = new AddCustomerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_add_customer, container, false);
        customerName = view.findViewById(R.id.customerName);
        storeName = view.findViewById(R.id.storeName);
        phone = view.findViewById(R.id.phoneNumber);
        email = view.findViewById(R.id.customerEmail);
        error_customer = view.findViewById(R.id.error_customer);
        customerSave = view.findViewById(R.id.customerSave);

        customerSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidCustomer()) {
                    cust = new Customer(customerName.getText().toString(), email.getText().toString(), phone.getText().toString(), storeName.getText().toString());
                    boolean inserted = sql.insertCustomer(cust);
                    if(inserted){
                        Intent intent = new Intent(getContext(), DataActivity.class);
                        startActivity(intent);
                    }
                } else {
                    error_customer.setVisibility(View.VISIBLE);
                }
            }
        });
        return view;
    }

    private boolean isValidCustomer() {
        return !customerName.getText().toString().equals("") && !email.getText().toString().equals("") && !phone.getText().toString().equals("") && !storeName.getText().toString().equals("");
    }
}
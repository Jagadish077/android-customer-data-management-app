package com.example.anmolpipes;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anmolpipes.datahelperclasses.Customer;
import com.example.anmolpipes.datahelperclasses.DateFormatter;
import com.example.anmolpipes.updated.history.UpdatedDataActivity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

class dataAdapter extends RecyclerView.Adapter<dataAdapter.dataHolder> implements Filterable {
    public List<Customer> datas;
    public List<Customer> AllTheDatas;
    public Context context;
    DateFormatter d = new DateFormatter();
    public dataAdapter(List<Customer> data, Context context) {
        this.AllTheDatas = data;
        this.datas = new ArrayList<>(AllTheDatas);
        this.context = context;
    }


    @NonNull
    @Override
    public dataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new dataHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_list_design, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull dataHolder holder, int position) {
        holder.phone.setText(datas.get(position).getPhone());
        try {
            holder.name.setText(datas.get(position).getName());
            holder.email.setText(datas.get(position).getEmail());

            holder.storeName.setText(datas.get(position).getStoreName());
            holder.createdat.setText(d.getDate(datas.get(position).getDate()));
            holder.total_amount.setText(String.valueOf(datas.get(position).getTotalAmount()));
            boolean isExpanded = datas.get(position).isExpandable();
            holder.expandedLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    //  this is method is from filterable class which will be helpful for filtering data for searchview

    @Override
    public Filter getFilter() {
        return filter;
    }

        Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Customer> filteredList = new ArrayList<>();
            if(constraint.toString().isEmpty()){
                filteredList.addAll(AllTheDatas);
            }else{
                for (Customer d: AllTheDatas){
                    if(d.getName().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filteredList.add(d);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            datas.clear();
            datas.addAll((List<Customer>)results.values);
            notifyDataSetChanged();
        }
    };


    public  class dataHolder extends RecyclerView.ViewHolder {
        //TODO: initialize all the widgets components
        private TextView name, email, phone, storeName, createdat, total_amount;
        private RelativeLayout expandedLayout;
        private RelativeLayout isClicked;
        private ImageButton delete, more_view, addProd, history, editCustomer;
        public dataHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.customer_name);
            storeName = itemView.findViewById(R.id.customer_store_name);
            createdat = itemView.findViewById(R.id.created_at);
            phone = itemView.findViewById(R.id.phone);
            total_amount = itemView.findViewById(R.id.customer_total_amount);
            email = itemView.findViewById(R.id.Email);
            expandedLayout = itemView.findViewById(R.id.expanable);
            isClicked = itemView.findViewById(R.id.relfolded);
            delete = itemView.findViewById(R.id.delete);
            more_view = itemView.findViewById(R.id.more_view);
            addProd = itemView.findViewById(R.id.add_prod);
            history = itemView.findViewById(R.id.history);
            editCustomer = itemView.findViewById(R.id.edit_customer);
            editCustomer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AddCustomerActivity.class);
                    intent.putExtra("isNewCustomer", false);
                    intent.putExtra("customerId", datas.get(getAdapterPosition()).getId());
                    context.startActivity(intent);
                }
            });
            more_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent  = new Intent(context, DetailedViewActivity.class);
                    intent.putExtra("customerId", datas.get(getAdapterPosition()).getId());
                    context.startActivity(intent);
                }
            });
            delete.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Anmol Pipes");
                builder.setMessage("Are u sure you want to delete this customer?").setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Customer data = datas.get(getAdapterPosition());
                        SqlOperation sql = new SqlOperation(context);
                        boolean isDeleted = sql.deleteCustomer(datas.get(getAdapterPosition()).getId());
                        if (isDeleted){
                            datas.remove(data);
                            notifyDataSetChanged();
                            Toast.makeText(context, "Customer Deleted Successfully", Toast.LENGTH_LONG).show();
                            getFilter();
                        }else{
                            Toast.makeText(context, "Dataset Updation Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Cancelled..!", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                }).show();

            });

            history.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UpdatedDataActivity.class);
                    intent.putExtra("customerId", datas.get(getAdapterPosition()).getId());
                    context.startActivity(intent);
                }
            });
            isClicked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Customer data = datas.get(getAdapterPosition());
                    data.setExpandable(!data.isExpandable());
                    notifyItemChanged(getAdapterPosition());
                }
            });
            addProd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AddProductActivity.class);
                    intent.putExtra("customerId", datas.get(getAdapterPosition()).getId());
                    context.startActivity(intent);
                }
            });
        }
    }
}

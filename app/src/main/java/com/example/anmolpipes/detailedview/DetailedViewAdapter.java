package com.example.anmolpipes.detailedview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anmolpipes.AddProductActivity;
import com.example.anmolpipes.DetailedViewActivity;
import com.example.anmolpipes.R;
import com.example.anmolpipes.SqlOperation;
import com.example.anmolpipes.addproductActivity.UpdateProductActivity;
import com.example.anmolpipes.datahelperclasses.DataFields;

import java.io.Serializable;
import java.util.List;

public class DetailedViewAdapter extends RecyclerView.Adapter<DetailedViewAdapter.DetailedViewHolder> {
    private Context context;
    private List<List<DataFields>> groupdata;
    ChildDetailedViewAdapter ch;
    private SqlOperation sql;
    int pos = 0;
    public DetailedViewAdapter(List<List<DataFields>> groupdata, Context context) {
        this.context = context;
        this.groupdata = groupdata;
        this.sql  = new SqlOperation(context);
    }


    @NonNull
    @Override
    public DetailedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.more_details_view_design, parent, false);
        return new DetailedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailedViewHolder holder, int position) {
        try {
            ch = new ChildDetailedViewAdapter(groupdata.get(position), context);
            holder.child_rec_view.setLayoutManager(new LinearLayoutManager(context));
            holder.child_rec_view.setAdapter(ch);
            boolean isExpanded = groupdata.get(position).get(pos).isExpanded();
            holder.child_table_row_hidden.setVisibility(isExpanded? View.VISIBLE: View.GONE);
            holder.delivery_balance.setText(String.valueOf(groupdata.get(position).get(pos).getTotalAmount()));
            if (groupdata.get(position).get(pos).getDelivery().equals("Yes")) {
                holder.isDelivered.setTextColor(context.getResources().getColor(R.color.dark_green));
                holder.isDelivered.setText("Item Delivered");
                holder.enable_delivery.setClickable(false);
            }else {
                holder.isDelivered.setTextColor(context.getResources().getColor(R.color.design_default_color_error));
                holder.isDelivered.setText("Not Delivered");
            }

        }catch (Exception e){
            Log.e("Adapter Error", ""+e);
        }
    }

    @Override
    public int getItemCount() {
        return groupdata.size();
    }

    class DetailedViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView child_rec_view;
        private TableLayout main_table, child_table_row_hidden;
        private TableRow  main_table_row;
        private ImageButton delete_all_products, enable_delivery, edit_prod;
        private TextView isDelivered, delivery_balance;
        public DetailedViewHolder(@NonNull View itemView) {
            super(itemView);
            isDelivered = itemView.findViewById(R.id.is_delivered);
            child_rec_view = itemView.findViewById(R.id.child_rec_view);
            main_table = itemView.findViewById(R.id.main_table);
            child_table_row_hidden = itemView.findViewById(R.id.child_table_row_hidden);
            main_table_row = itemView.findViewById(R.id.main_table_row);
            delete_all_products = itemView.findViewById(R.id.delete_all_products);
            enable_delivery = itemView.findViewById(R.id.enable_delivery);
            delivery_balance = itemView.findViewById(R.id.delivery_balance);
            edit_prod = itemView.findViewById(R.id.product_edit);
            edit_prod.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UpdateProductActivity.class);
                    intent.putExtra("productData", (Serializable) groupdata.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });
            delete_all_products.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder productDialog = new AlertDialog.Builder(context);
                    productDialog.setTitle("Anmol Pipes");
                    productDialog.setMessage("Are you sure you want to delete this delivery?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            boolean isDeleted = sql.deleteAllProducts(groupdata.get(getAdapterPosition()).get(pos).getProductId());
                            if (isDeleted){
                                Toast.makeText(context, "Product Deleted", Toast.LENGTH_LONG).show();
                                groupdata.remove(getAdapterPosition()).remove(0);
                                notifyDataSetChanged();
                                notifyItemChanged(getAdapterPosition());
                            }else {
                                Toast.makeText(context, "Error while Deleting", Toast.LENGTH_LONG).show();
                            }
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();
                }
            });
            main_table_row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataFields data = groupdata.get(getAdapterPosition()).get(pos);
                    data.setExpanded(!data.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });

            enable_delivery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isUpdateDelivery = sql.enableDelivery(groupdata.get(getAdapterPosition()).get(pos).getProductId());
                    if (isUpdateDelivery) {
                        groupdata.get(getAdapterPosition()).get(pos).setDelivery("Yes");
                        notifyDataSetChanged();
                        notifyItemChanged(getAdapterPosition());
                    }
                }
            });
        }
    }
}

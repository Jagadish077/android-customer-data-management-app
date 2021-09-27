package com.example.anmolpipes.detailedview;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anmolpipes.R;
import com.example.anmolpipes.datahelperclasses.DataFields;

import java.util.List;

public class ChildDetailedViewAdapter extends RecyclerView.Adapter<ChildDetailedViewAdapter.ChildViewHolder>{
    private List<DataFields> data;
    private Context context;

    public ChildDetailedViewAdapter(List<DataFields> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChildViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.more_detailed_child_design, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChildDetailedViewAdapter.ChildViewHolder holder, int position) {
        holder.c_productName.setText(data.get(position).getProductName());
        holder.c_productSize.setText(data.get(position).getSize());
        holder.c_qty.setText(String.valueOf((float) data.get(position).getQuantity()));
        holder.c_rate.setText(String.valueOf((float) data.get(position).getRate()));
        holder.c_totalAmount.setText(String.valueOf((float) data.get(position).getTotalAmountOfAllProduct()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public int getData(int adapterPosition) {
        return 0;
    }

    public class ChildViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout child_row;
        private TextView c_productName, c_productSize, c_qty, c_rate, c_totalAmount;
        public ChildViewHolder(@NonNull View itemView) {
            super(itemView);
            child_row = itemView.findViewById(R.id.child_table_row);
            c_productName = itemView.findViewById(R.id.c_row_productName);
            c_productSize = itemView.findViewById(R.id.c_row_productSize);
            c_qty = itemView.findViewById(R.id.c_qty);
            c_rate = itemView.findViewById(R.id.c_rate);
            c_totalAmount = itemView.findViewById(R.id.c_totalAmount);
        }
    }
}

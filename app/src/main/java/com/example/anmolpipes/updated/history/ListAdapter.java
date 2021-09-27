package com.example.anmolpipes.updated.history;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.anmolpipes.R;
import com.example.anmolpipes.datahelperclasses.DateFormatter;
import com.example.anmolpipes.datahelperclasses.UpdatedHistory;

import java.text.ParseException;
import java.util.List;

public class ListAdapter  extends BaseAdapter {
    private List<UpdatedHistory> data;
    private Context context;
    private TextView updated_Id, previous_amount, updated_amount, total_amount_after_update, updated_time, updated_date;
    private DateFormatter formatter = new DateFormatter();
    public ListAdapter(Context context, List<UpdatedHistory> historyData) {
        this.context = context;
        this.data = historyData;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_design, parent, false);
        }
        updated_Id = convertView.findViewById(R.id.updated_Id);
        previous_amount = convertView.findViewById(R.id.previous_amount);
        updated_amount = convertView.findViewById(R.id.updated_amount);
        total_amount_after_update = convertView.findViewById(R.id.total_amount_after_update);
        updated_time = convertView.findViewById(R.id.updated_time);
        updated_date = convertView.findViewById(R.id.updated_date);

        updated_Id.setText(String.valueOf(data.get(position).getId()));
        previous_amount.setText(String.valueOf(data.get(position).getPreviousAmount()));
        updated_amount.setText(String.valueOf(data.get(position).getUpdatedAmount()));
        total_amount_after_update.setText(String.valueOf(data.get(position).getTotalAmount()));
        try {
            updated_time.setText(formatter.getTime(data.get(position).getDate()));
            updated_date.setText(formatter.getDate(data.get(position).getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}

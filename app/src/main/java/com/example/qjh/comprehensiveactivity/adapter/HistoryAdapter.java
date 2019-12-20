package com.example.qjh.comprehensiveactivity.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.qjh.comprehensiveactivity.R;
import com.example.qjh.comprehensiveactivity.beans.Order;

import java.util.HashMap;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {


    List<Order> orders;
    Context context;
    private DeviceAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        /*注意参数*/
        public void OnItemClick(HashMap<String, String> items);

    }

    public HistoryAdapter(Context context,List<Order> orders) {
        this.orders = orders;
        this.context = context;
    }

    public void setOnItemClick(DeviceAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.common_item_parkhistory,
                viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {


    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;

        }
    }


}

package com.example.qjh.comprehensiveactivity.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.qjh.comprehensiveactivity.R;
import com.example.qjh.comprehensiveactivity.beans.Order;

import java.util.HashMap;
import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {


    List<Order> orders;
    Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        /*注意参数*/
        public void OnItemClick(HashMap<String, String> items);

    }

    public OrderListAdapter(Context context, List<Order> orders) {
        this.orders = orders;
        this.context = context;
    }

    public void setOnItemClick(OnItemClickListener listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.common_item_fragment_order_item,
                viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            Order order=orders.get(i);
            viewHolder.tv_CarNumber.setText(order.getCarNumber());
            viewHolder.tv_startTime.setText("开始时间："+order.getStartTime());
            viewHolder.tv_endTime.setText("结束时间:");
            viewHolder.tv_price.setText(order.getPrice()+"/每小时");

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        private TextView tv_CarNumber;
        private TextView tv_startTime;
        private TextView tv_endTime;
        private TextView tv_price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            tv_CarNumber=itemView.findViewById(R.id.tv_CarNumber);
            tv_startTime=itemView.findViewById(R.id.tv_startTime);
            tv_endTime=itemView.findViewById(R.id.tv_endTime);
            tv_price=itemView.findViewById(R.id.tv_price);

        }
    }


}

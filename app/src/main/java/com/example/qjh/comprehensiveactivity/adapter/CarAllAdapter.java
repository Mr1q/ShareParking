package com.example.qjh.comprehensiveactivity.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.qjh.comprehensiveactivity.R;
import com.example.qjh.comprehensiveactivity.beans.Car;
import com.example.qjh.comprehensiveactivity.utils.CircleCheckBox;


import java.util.List;

public class CarAllAdapter extends RecyclerView.Adapter<CarAllAdapter.ViewHolder> {
    private List<Car> items_List;
    private OnItemClickListener listener;

    public CarAllAdapter(List<Car> items) {
        this.items_List = items;
    }

    public void setOnItemClick(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * 设置数据集
     *
     * @param data
     */
    public void setData(List<Car> data) {
        this.items_List = data;
    }

    /*
    接口回调
     */
    public interface OnItemClickListener {
        /*注意参数*/
        public void OnItemClick(Car items,int postition);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.common_mycars_item,
                viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.cir_chbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int postition = viewHolder.getAdapterPosition();
                    Car items = items_List.get(postition);
                    listener.OnItemClick(items,postition);
                }
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Car items = items_List.get(i);  //获取点击位置
        viewHolder.tv_MyCarAddress.setText(items.getCarAddress());
        viewHolder.tv_MyCarNumber.setText(items.getCarNumber());
        viewHolder.cir_chbox.setChecked(items.getAlways());


    }

    @Override
    public int getItemCount() {
        return items_List.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_MyCarAddress; //基站id
        TextView tv_MyCarNumber;//基站位置
        CircleCheckBox cir_chbox;
        View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            tv_MyCarAddress=(TextView)itemView.findViewById(R.id.tv_MyCarAddress);
            tv_MyCarNumber = (TextView) itemView.findViewById(R.id.tv_MyCarNumber);
            cir_chbox = (CircleCheckBox) itemView.findViewById(R.id.cir_chbox);

        }
    }
}

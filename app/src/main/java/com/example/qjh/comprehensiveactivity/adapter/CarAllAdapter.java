package com.example.qjh.comprehensiveactivity.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.qjh.comprehensiveactivity.R;
import com.example.qjh.comprehensiveactivity.beans.Car;
import com.example.qjh.comprehensiveactivity.beans.ParkingLot;
import com.example.qjh.comprehensiveactivity.utils.CircleCheckBox;


import java.util.List;

public class CarAllAdapter extends RecyclerView.Adapter<CarAllAdapter.ViewHolder> {
    private List<Car> items_List;
    private OnItemClickListener listener;
    private Context context;

    public CarAllAdapter(List<Car> items,Context context) {
        this.items_List = items;

        this.context=context;
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
        if(items.getAlways().equals("1"))
        {
            viewHolder.cir_chbox.setChecked(true);
        }else
        {
            viewHolder.cir_chbox.setChecked(false);
        }

        Glide.with(context).load(items.getCar_photoURL()).into(viewHolder.iv_car);

    }

    @Override
    public int getItemCount() {
        return items_List.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_MyCarAddress; //车辆地址
        TextView tv_MyCarNumber;//车牌号
        CircleCheckBox cir_chbox; //是否设置为默认车辆
        ImageView iv_car; //是否设置为默认车辆
        View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            tv_MyCarAddress=(TextView)itemView.findViewById(R.id.tv_MyCarAddress);
            tv_MyCarNumber = (TextView) itemView.findViewById(R.id.tv_MyCarNumber);
            cir_chbox = (CircleCheckBox) itemView.findViewById(R.id.cir_chbox);
            iv_car = (ImageView) itemView.findViewById(R.id.iv_car);

        }
    }
}

//package com.example.qjh.comprehensiveactivity.adapter;
//
//import android.content.Context;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.example.qjh.comprehensiveactivity.R;
//
//import java.util.HashMap;
//import java.util.List;
//
//public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
//
//
////  List<>  devicesList;
//    Context context;
//    private DeviceAdapter.OnItemClickListener listener;
//
//    public interface OnItemClickListener {
//        /*注意参数*/
//        public void OnItemClick(HashMap<String, String> items);
//
//    }
//
//    public HistoryAdapter( Context context) {
////        this.devicesList = items;
//        this.context=context;
//    }
//
//    public void setOnItemClick(DeviceAdapter.OnItemClickListener listener) {
//        this.listener = listener;
//    }
//
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.common_item_device,
//                viewGroup, false);
//        final ViewHolder viewHolder = new ViewHolder(view);
//        viewHolder.ly_all.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (listener != null) {
//                    int postition = viewHolder.getAdapterPosition();
////                    HashMap<String, String> items = devicesList.get(postition);
////                    listener.OnItemClick(items);
//                }
//            }
//        });
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
////        HashMap<String, String> items = devicesList.get(i);  //获取点击位置
//        viewHolder.tv_devicename.setText(items.get("name"));
//        viewHolder.tv_mac.setText(items.get("mac"));
//    }
//
//    @Override
//    public int getItemCount() {
//        return devicesList.size();
//    }
//
//    static class ViewHolder extends RecyclerView.ViewHolder {
//        private TextView tv_devicename;
//        private TextView tv_mac;
//        private LinearLayout ly_all;
//        View view;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            view = itemView;
//            tv_mac = (TextView) itemView.findViewById(R.id.tv_Mac);
//            tv_devicename = (TextView) itemView.findViewById(R.id.tv_deviceName);
//            ly_all = (LinearLayout) itemView.findViewById(R.id.ly_all);
//
//        }
//    }
//
//
//}

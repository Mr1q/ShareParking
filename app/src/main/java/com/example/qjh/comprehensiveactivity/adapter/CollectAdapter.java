package com.example.qjh.comprehensiveactivity.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.qjh.comprehensiveactivity.R;
import com.example.qjh.comprehensiveactivity.beans.ParkingLot;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.XPopupImageLoader;
import com.suke.widget.SwitchButton;

import java.io.File;
import java.util.List;

public class CollectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ParkingLot> items_List;
    private static final int VIEW = 1;
    private Context context;


    public CollectAdapter(Context context, List<ParkingLot> items) {
        this.context = context;
        this.items_List = items;
    }

    public CollectAdapter(Context context) {
        this.context = context;

    }

    public void setList(List<ParkingLot> items) {
        this.items_List = items;
    }


    /**
     * 设置数据集
     *
     * @param data
     */
    public void setData(List<ParkingLot> data) {
        this.items_List = data;
    }


    @Override
    public int getItemViewType(int position) {

        return VIEW;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.common_item_collect_item,
                viewGroup, false);
        final ViewHolderOne viewHolder = new ViewHolderOne(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ViewHolderOne) {
            ParkingLot items = items_List.get(i);  //获取点击位置
            ((CollectAdapter.ViewHolderOne) viewHolder).tv_parkLotName.setText(items.getPark_name());
            ((ViewHolderOne) viewHolder).tv_address.setText(items.getPark_address());
            ((ViewHolderOne) viewHolder).tv_price.setText(items.getPark_price() + "元/hour");
            Glide.with(context).load(items.getParklotImage()).into(((CollectAdapter.ViewHolderOne) viewHolder).iv_carimage);

            ((CollectAdapter.ViewHolderOne) viewHolder).iv_carimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new XPopup.Builder(context)
                            .asImageViewer(((CollectAdapter.ViewHolderOne) viewHolder).iv_carimage,
                                    items.getParklotImage(),
                                    true,
                                    -1,
                                    -1,
                                    50,
                                    false
                                    , new ImageLoader())
                            .show();
                }
            });


        }

    }


    @Override
    public int getItemCount() {   //限制加载100个item
        return items_List.size();
    }


    public static class ViewHolderOne extends RecyclerView.ViewHolder {
        TextView tv_parkLotName; //车位名字
        TextView tv_address; //车位地址状态
        ImageView iv_carimage;//车位图片
        TextView tv_price;//价格
        View view;

        public ViewHolderOne(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            tv_parkLotName = (TextView) itemView.findViewById(R.id.tv_parkLotName);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            iv_carimage = (ImageView) itemView.findViewById(R.id.iv_carimage);


        }
    }


    public static class ImageLoader implements XPopupImageLoader {
        @Override
        public void loadImage(int position, @NonNull Object url, @NonNull ImageView imageView) {
            //必须指定Target.SIZE_ORIGINAL，否则无法拿到原图，就无法享用天衣无缝的动画
            Glide.with(imageView).load(url).apply(new RequestOptions().placeholder(R.mipmap.ic_launcher_round).
                    override(Target.SIZE_ORIGINAL)).into(imageView);
        }

        @Override
        public File getImageFile(@NonNull Context context, @NonNull Object uri) {
            try {
                return Glide.with(context).downloadOnly().load(uri).submit().get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


}

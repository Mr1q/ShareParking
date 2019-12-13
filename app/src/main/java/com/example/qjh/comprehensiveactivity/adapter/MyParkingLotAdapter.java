package com.example.qjh.comprehensiveactivity.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.qjh.comprehensiveactivity.R;
import com.example.qjh.comprehensiveactivity.beans.ParkingLot;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.XPopupImageLoader;
import com.suke.widget.SwitchButton;
import com.youth.banner.loader.ImageLoader;

import java.io.File;
import java.util.List;

public class MyParkingLotAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ParkingLot> items_List;
    private OnItemClickListener listener;
    private OnItemClickListenerAdd onItemClickListenerAdd;
    private static final int VIEW = 1;
    private static final int ADD_VIEW = 2;
    private Context context;

    public MyParkingLotAdapter(Context context, List<ParkingLot> items) {
        this.context = context;
        this.items_List = items;
    }
    public void  setList( List<ParkingLot> items)
    {
        this.items_List=items;
    }


    public void setOnItemClick(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnItemClick_Add(OnItemClickListenerAdd listener) {
        this.onItemClickListenerAdd = listener;
    }

    /**
     * 设置数据集
     *
     * @param data
     */
    public void setData(List<ParkingLot> data) {
        this.items_List = data;
    }

    /*
    接口回调
     */
    public interface OnItemClickListener {
        /*注意参数*/
        public void OnItemClick(ParkingLot items, int id);
        public void OnItemClick(ParkingLot items);
        public void OnItemClick2(ParkingLot items, int id, Boolean Sch, SwitchButton switchButton);
    }

    public interface OnItemClickListenerAdd {
        /*注意参数*/
        public void OnItemAddImage();
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowAdd(position)) {
            Log.d("getItemViewType", "getItemViewType: " + position);
            return ADD_VIEW;
        } else {
            return VIEW;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == VIEW) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.common_fragment_item_car,
                    viewGroup, false);
            final ViewHolderOne viewHolder = new ViewHolderOne(view);
            return viewHolder;
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.common_fragment_addcar,
                    viewGroup, false);
            final ViewHolderTwo viewHolderTwo = new ViewHolderTwo(view);
            return viewHolderTwo;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ViewHolderOne) {
             ParkingLot items = items_List.get(i);  //获取点击位置
            Glide.with(context).load(items.getParklotImage()).into(((ViewHolderOne) viewHolder).iv_carimage);

            ((ViewHolderOne) viewHolder).tv_parkLotName.setText("车位名称:" + items.getPark_name());
            switch (items.getShare()) {
                case "1":
                    ((ViewHolderOne) viewHolder).switch_share.setChecked(true);
                    break;
                case "0":
                    ((ViewHolderOne) viewHolder).switch_share.setChecked(false );
                    break;
            }
            if(items.getStatus().equals("1"))
            {
                ((ViewHolderOne) viewHolder).switch_open.setChecked(true);
            }else
            {
                ((ViewHolderOne) viewHolder).switch_open.setChecked(false );
            }
            switch (items.getStatus()) {
                case "1":
                    ((ViewHolderOne) viewHolder).tv_status.setText("车位状态:正在使用");
                    break;
                case "0":
                    ((ViewHolderOne) viewHolder).tv_status.setText("车位状态:无人使用");
                    break;
            }
            ((ViewHolderOne) viewHolder).tv_address.setText("地址:"+items.getPark_address() );

            ((ViewHolderOne) viewHolder).iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int postition = viewHolder.getAdapterPosition();
                        ParkingLot items = items_List.get(postition);
                        listener.OnItemClick(items,postition);
                    }
                }
            });
            ((ViewHolderOne) viewHolder).switch_open.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                    if (listener != null) {
                        int postition = viewHolder.getAdapterPosition();
                        ParkingLot items = items_List.get(postition);
                        listener.OnItemClick2(items, R.id.switch_open, isChecked, ((ViewHolderOne) viewHolder).switch_open);
                    }
                }
            });

            ((ViewHolderOne) viewHolder).switch_share.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                    if (listener != null) {
                        int postition = viewHolder.getAdapterPosition();
                        ParkingLot items = items_List.get(postition);
                        listener.OnItemClick2(items, R.id.switch_share, isChecked, ((ViewHolderOne) viewHolder).switch_share);
                    }
                }
            });
            ((ViewHolderOne) viewHolder).iv_carimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new XPopup.Builder(context)
                            .asImageViewer(((ViewHolderOne) viewHolder).iv_carimage, items.getParklotImage() , true, -1, -1, 50, false,new ImageLoader())
                            .show();

                }
            });

        } else if (viewHolder instanceof ViewHolderTwo) {
            ((ViewHolderTwo) viewHolder).addimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "添加机器", Toast.LENGTH_SHORT).show();
                    if (onItemClickListenerAdd != null) {
                        onItemClickListenerAdd.OnItemAddImage();
                    }
                }
            });
        }

    }


    private boolean isShowAdd(int position) {
//position=0 的时候  uris.size() 也等于0，返回 true，那么就是显示 添加的图片，postion =1，
//uris.size()=3 (因为 position下标从 0 开始 而且最大数量是 9 张 那么就是有3张图片 返回 false)
//以此类推
        int size = items_List.size() == 0 ? 0 : items_List.size();
        return position == size;
    }

    @Override
    public int getItemCount() {
        if (items_List.size() < 100) {
            return items_List.size() + 1;
        } else {
            return items_List.size();
        }

    }

    public static class ViewHolderOne extends RecyclerView.ViewHolder {
        TextView tv_parkLotName; //车位名字
        TextView tv_address; //车位地址状态
        TextView tv_status;//车位状态
        ImageView iv_carimage;//车位图片
        ImageView iv_delete;//删除车位
        View view;
        SwitchButton switch_share;
        SwitchButton switch_open;

        public ViewHolderOne(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            tv_parkLotName = (TextView) itemView.findViewById(R.id.tv_parkLotName);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            tv_status = (TextView) itemView.findViewById(R.id.tv_status);
            iv_carimage = (ImageView) itemView.findViewById(R.id.iv_carimage);
            iv_delete = (ImageView) itemView.findViewById(R.id.iv_delete);
            switch_share = (SwitchButton) itemView.findViewById(R.id.switch_share);  //共享
            switch_open = (SwitchButton) itemView.findViewById(R.id.switch_open);//开关


        }
    }

    public static class ViewHolderTwo extends RecyclerView.ViewHolder {

        LinearLayout addimg;

        public ViewHolderTwo(@NonNull View itemView) {
            super(itemView);
            addimg = (LinearLayout) itemView.findViewById(R.id.addimg);
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

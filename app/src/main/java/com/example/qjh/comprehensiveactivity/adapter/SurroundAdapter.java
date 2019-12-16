package com.example.qjh.comprehensiveactivity.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

import java.io.File;
import java.util.List;

public class SurroundAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ParkingLot> items_List;
    private OnItemClickListener listener;
    private OnItemClickListenerAdd onItemClickListenerAdd;
    private static final int VIEW = 1;
    private static final int ADD_VIEW = 2;
    private Context context;
    //上拉加载更多
    public static final int PULLUP_LOAD_MORE = 0;
    //正在加载中
    public static final int LOADING_MORE     = 1;
    //没有加载更多 隐藏
    public static final int NO_LOAD_MORE     = 2;
    //上拉加载更多状态-默认为0
    private int mLoadMoreStatus = 0;

    public SurroundAdapter(Context context, List<ParkingLot> items) {
        this.context = context;
        this.items_List = items;
    }

    public SurroundAdapter(Context context) {
        this.context = context;

    }

    public void setList(List<ParkingLot> items) {
        this.items_List = items;
    }


    public void setOnItemClick(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnItemClick_Add(OnItemClickListenerAdd listener) {
        this.onItemClickListenerAdd = listener;
    }
    Boolean switchs=false;
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
        public void OnItemClickToDetail(ParkingLot items);
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
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.common_item_fragment_share_item,
                    viewGroup, false);
            final ViewHolderOne viewHolder = new ViewHolderOne(view);
            return viewHolder;
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.common_fragment_upmore,
                    viewGroup, false);
            final ViewHolderTwo viewHolderTwo = new ViewHolderTwo(view);
            return viewHolderTwo;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ViewHolderOne) {
            ParkingLot items = items_List.get(i);  //获取点击位置
            ((SurroundAdapter.ViewHolderOne) viewHolder).iv_carimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new XPopup.Builder(context)
                            .asImageViewer(((SurroundAdapter.ViewHolderOne) viewHolder).iv_carimage,
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
            ((ViewHolderOne) viewHolder).ly_nagivation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int postition = viewHolder.getAdapterPosition();
                    ParkingLot items = items_List.get(postition);
                    listener.OnItemClick(items);
                }
            });
            ((ViewHolderOne) viewHolder).view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int postition = viewHolder.getAdapterPosition();
                    ParkingLot items = items_List.get(postition);
                    listener.OnItemClickToDetail(items);
                }
            });
            ((ViewHolderOne) viewHolder).iv_favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if( switchs)
                    ((ViewHolderOne) viewHolder).iv_favorite.setImageResource(R.mipmap.common_item_zanpick);
                    else
                        ((ViewHolderOne) viewHolder).iv_favorite.setImageResource(R.mipmap.common_item_zan);
                    switchs=!switchs;
                }
            });

        } else if (viewHolder instanceof ViewHolderTwo) {
            ViewHolderTwo viewHolderTwo=(ViewHolderTwo)viewHolder;
            switch(mLoadMoreStatus)
            {
                case PULLUP_LOAD_MORE:
                    viewHolderTwo.tv_loadmsg.setText("上拉加载更多...");
                    viewHolderTwo.pb_load.setVisibility(View.GONE);
                    break;
                case LOADING_MORE:
                    viewHolderTwo.tv_loadmsg.setText("正加载更多...");
                    viewHolderTwo.pb_load.setVisibility(View.VISIBLE);
                    break;
                case NO_LOAD_MORE:
                    //隐藏加载更多
                    viewHolderTwo.ly_load.setVisibility(View.GONE);
                    break;
            }
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
    public int getItemCount() {   //限制加载100个item
        if (items_List.size() < 100) {
            return items_List.size() + 1;
        } else {
            return items_List.size();
        }

    }
    /**
     * 更新加载更多状态
     * @param status
     */
    public void changeMoreStatus(int status){
        mLoadMoreStatus=status;
       notifyDataSetChanged();
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
        ImageView iv_favorite; //收藏图标
        LinearLayout ly_nagivation; //导航

        public ViewHolderOne(@NonNull View itemView) {
            super(itemView);
            view = itemView;
//            tv_parkLotName = (TextView) itemView.findViewById(R.id.tv_parkLotName);
//            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
//            tv_status = (TextView) itemView.findViewById(R.id.tv_status);
            iv_carimage = (ImageView) itemView.findViewById(R.id.iv_carimage);
            iv_favorite = (ImageView) itemView.findViewById(R.id.iv_favorite);
            ly_nagivation = (LinearLayout) itemView.findViewById(R.id.ly_nagivation);
//            iv_delete = (ImageView) itemView.findViewById(R.id.iv_delete);
//            switch_share = (SwitchButton) itemView.findViewById(R.id.switch_share);  //共享
//            switch_open = (SwitchButton) itemView.findViewById(R.id.switch_open);//开关


        }
    }

    public static class ViewHolderTwo extends RecyclerView.ViewHolder {
        private ProgressBar pb_load;
        private TextView tv_loadmsg;
        private LinearLayout ly_load;
        public ViewHolderTwo(@NonNull View itemView) {
            super(itemView);
            pb_load = (ProgressBar) itemView.findViewById(R.id.pb_load);
            tv_loadmsg = (TextView) itemView.findViewById(R.id.tv_loadmsg);
            ly_load = (LinearLayout) itemView.findViewById(R.id.ly_load);
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

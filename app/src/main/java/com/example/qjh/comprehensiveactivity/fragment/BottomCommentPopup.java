package com.example.qjh.comprehensiveactivity.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.qjh.comprehensiveactivity.R;

import com.example.qjh.comprehensiveactivity.activity.LoginActivity;
import com.example.qjh.comprehensiveactivity.adapter.CommentAdapter;
import com.example.qjh.comprehensiveactivity.adapter.SurroundAdapter;
import com.example.qjh.comprehensiveactivity.beans.BaseResponse;
import com.example.qjh.comprehensiveactivity.beans.Comment;
import com.example.qjh.comprehensiveactivity.beans.NewsRequest;
import com.example.qjh.comprehensiveactivity.beans.ParkingLot;
import com.example.qjh.comprehensiveactivity.constant.Constants;
import com.example.qjh.comprehensiveactivity.utils.view.CustomEditTextBottomPopup;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lxj.easyadapter.EasyAdapter;
import com.lxj.easyadapter.MultiItemTypeAdapter;
import com.lxj.easyadapter.ViewHolder;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.interfaces.SimpleCallback;
import com.lxj.xpopup.util.XPopupUtils;
import com.lxj.xpopup.widget.VerticalRecyclerView;


import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.baidu.navisdk.module.future.interfaces.FutureTripParams.LoadingState.FAIL;

/**
 * Description: 仿知乎底部评论弹窗
 */
public class BottomCommentPopup extends BottomPopupView {
    private  RecyclerView recyclerView;
    private ArrayList<Comment> data;
    private CommentAdapter commonAdapter;
    private Request request;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private Context context;
    private String parkId;//车位ID
    private final int SUCCESS = 1;
    private final int FAIL = -1;
    private final int Get_SUCCESS = 2;
    private final int Get_FAIL = -2;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    getData();
                    break;
                case FAIL:
                    break;
                case Get_SUCCESS:
                    ((TextView) findViewById(R.id.tv_total)).setText("全部"+count+"条评论");
                    commonAdapter = new CommentAdapter(context,data);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setAdapter(commonAdapter);
                    commonAdapter.notifyDataSetChanged();
                    break;
                case Get_FAIL:
                    break;

            }
        }
    };
    private String count=null;

    private void getData() {
        NewsRequest newsRequest = new NewsRequest(); //get请求参数
        newsRequest.setParkId(parkId); //当前用户的ID
        request = new Request.Builder().
                url(Constants.FindCommentParklot + newsRequest.toStringFindCommentParkLot()).
                build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String body = response.body().string();
                        Log.d("onResponse_body", "onResponse: " + body);
                        Gson gson = new Gson();
                        JSONObject jsonObject = new JSONObject(body);
                        String state = jsonObject.optString("state");
                        count = jsonObject.optString("count"); //评论数
                        if (state.equals("0")) {
                            handler.sendEmptyMessage(Get_FAIL);
                        } else {
                            Type jsontype = new TypeToken<BaseResponse<List<Comment>>>() {
                            }.getType();
                            BaseResponse<List<Comment>> newsListResponese
                                    = gson.fromJson(body, jsontype);
                            data.clear();
                            for (Comment comment : newsListResponese.getData()) {
                                data.add(comment);
                            }
                            handler.sendEmptyMessage(Get_SUCCESS);
                        }
                    } else {
                        handler.sendEmptyMessage(Get_FAIL);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public BottomCommentPopup(@NonNull Context context, String parkId) {
        super(context);
        this.context=context;
        this.parkId = parkId;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_bottom_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        findViewById(R.id.tv_temp).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出新的弹窗用来输入
                final CustomEditTextBottomPopup textBottomPopup = new CustomEditTextBottomPopup(getContext());
                new XPopup.Builder(getContext())
                        .autoOpenSoftInput(true)
//                        .hasShadowBg(false)
                        .setPopupCallback(new SimpleCallback() {
                            @Override
                            public void onShow() {
                            }

                            @Override
                            public void onDismiss() {
                                String comment = textBottomPopup.getComment(); //获取输入数据
                                if (!comment.isEmpty()) {
                                    toCommitComment(comment);
                                }//
//                                if (!comment.isEmpty()) {
//                                    data.add(0, comment);
//                                    commonAdapter.notifyDataSetChanged();
//                                }
                            }
                        })
                        .asCustom(textBottomPopup)
                        .show();
            }
        });

        data = new ArrayList<>();
        getData();

    }


    /**
     * 提交评论
     */
    private void toCommitComment(String content) {
        RequestBody requestBody = new FormBody.Builder()
                .add("username", LoginActivity.Username)
                .add("content", content)
                .add("parkId", parkId)
                .add("userHeadPhoto", LoginActivity.userHeadPhoto)
                .build();
        request = new Request.Builder().
                url(Constants.CommentParklot).
                post(requestBody).
                build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String body = response.body().string();
                    Log.d("onResponse_comment", "onResponse: " + body);
                    if (response.isSuccessful()) {
                        Gson gson = new Gson();
                        JSONObject jsonObject = new JSONObject(body);
                        String state = jsonObject.optString("state");
                        if (state.equals("0")) {
                            handler.sendEmptyMessage(FAIL);
                        } else {
                            handler.sendEmptyMessage(SUCCESS);
                        }
                    } else {
                        handler.sendEmptyMessage(FAIL);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    //完全可见执行
    @Override
    protected void onShow() {
        super.onShow();
    }

    //完全消失执行
    @Override
    protected void onDismiss() {

    }

    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext()) * .75f);
    }
}
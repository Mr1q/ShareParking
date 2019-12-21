package com.example.qjh.comprehensiveactivity.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.qjh.comprehensiveactivity.R;
import com.example.qjh.comprehensiveactivity.beans.Comment;
import com.example.qjh.comprehensiveactivity.beans.Order;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {


    List<Comment> commentList;
    Context context;
    public CommentAdapter(Context context, List<Comment> commentList) {
        this.commentList = commentList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.common_item_commend,
                viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            Comment comment=commentList.get(i);
        viewHolder.tv_username.setText( comment.getUsername());
        viewHolder.comment.setText( comment.getContent());
        viewHolder.tv_time.setText( comment.getTime());

        Glide.with(context).load(comment.getUserHeadPhoto()).into(((viewHolder).iv_headphoto));
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        CircleImageView iv_headphoto;
        TextView tv_username;
        TextView comment;
        TextView tv_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            iv_headphoto=itemView.findViewById(R.id.iv_headphoto);
            tv_username=itemView.findViewById(R.id.tv_username);
            comment=itemView.findViewById(R.id.comment);
            tv_time=itemView.findViewById(R.id.tv_time);

        }
    }


}

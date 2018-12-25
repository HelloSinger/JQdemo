package com.jq.btc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jq.btc.app.R;
import com.jq.btc.model.UserData;

import java.util.List;

/**
 * Create by AYD on 2018/12/18
 */
public class DialogRecyAdapter extends RecyclerView.Adapter<DialogRecyAdapter.MyViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<UserData> userDataList;

    public DialogRecyAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setUserDataList(List<UserData> userDataList) {
        this.userDataList = userDataList;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder;
        View view = inflater.inflate(R.layout.item_dialog_recy_layout, parent, false);
        holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Glide.with(context).load(R.mipmap.default_head_image).into(holder.iv_add_head);
    }

    @Override
    public int getItemCount() {
//        return userDataList == null ? 0 : userDataList.size();
        return 6;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_add_head;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_add_head = itemView.findViewById(R.id.iv_add_head);
        }
    }
}

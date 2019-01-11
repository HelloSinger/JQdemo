package com.jq.btc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    private UserData userData;
    RecyItemOnClickListener recyItemOnClickListener;


    public void setRecyItemOnClickListener(RecyItemOnClickListener recyItemOnClickListener) {
        this.recyItemOnClickListener = recyItemOnClickListener;
    }

    public DialogRecyAdapter(Context context, UserData userData) {
        this.context = context;
        this.userData = userData;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder;
        View view = inflater.inflate(R.layout.item_dialog_recy_layout, parent, false);
        holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Glide.with(context).load(R.mipmap.default_head_image).into(holder.iv_add_head);
        holder.tv_name.setText(userData.getData().getMemberList().get(position).getNickName());
        holder.ll_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyItemOnClickListener.itemOnClickListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userData == null ? 0 : userData.getData().getMemberList().size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_add_head;
        TextView tv_name;
        LinearLayout ll_user;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_add_head = itemView.findViewById(R.id.iv_add_head);
            tv_name = itemView.findViewById(R.id.tv_name);
            ll_user = itemView.findViewById(R.id.ll_user);
        }
    }

    public interface RecyItemOnClickListener {
        void itemOnClickListener(int pos);
    }
}

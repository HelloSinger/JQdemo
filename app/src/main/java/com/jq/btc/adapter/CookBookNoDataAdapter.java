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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.jq.btc.app.R;
import com.jq.btc.model.MenuModel;
import com.jq.btc.model.NoDataModel;
import com.jq.btc.utils.GlideOptions;
import com.jq.btc.utils.ImageUtil;

import java.util.List;

/**
 * Create by AYD on 2019/2/27
 */
public class CookBookNoDataAdapter extends RecyclerView.Adapter<CookBookNoDataAdapter.MyViewHolder> {

    private List<NoDataModel.DataBean.RecipesBean> menuModels;
    private Context context;

    public CookBookNoDataAdapter(Context context) {
        this.context = context;
    }

    public void setMenuModels(List<NoDataModel.DataBean.RecipesBean> menuModels) {
        this.menuModels = menuModels;
        notifyDataSetChanged();
    }

    @Override
    public CookBookNoDataAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu_layout, parent, false);
        CookBookNoDataAdapter.MyViewHolder holder = new CookBookNoDataAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CookBookNoDataAdapter.MyViewHolder holder, final int position) {
        GlideOptions glideOptions = new GlideOptions.Builder()
                .radius(4)
                .cornerType(GlideOptions.CornerType.ALL)
                .build();
//        Glide.with(context).load(menuModels.get(position).getRecipeimage())
//                .into(holder.iv_pic);
        ImageUtil.display(context, menuModels.get(position).getRecipeimage(), holder.iv_pic, glideOptions);
        holder.tv_menu_name.setText(menuModels.get(position).getRecipename());
        String recipetag = menuModels.get(position).getRecipetag().replace(",", "/");
        holder.tv_tag.setText(recipetag);
        holder.ll_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.setItemClickListener1(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuModels == null ? 0 : menuModels.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_pic;
        TextView tv_menu_name, tv_tag;
        LinearLayout ll_menu;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_pic = itemView.findViewById(R.id.iv_pic);
            tv_menu_name = itemView.findViewById(R.id.tv_menu_name);
            tv_tag = itemView.findViewById(R.id.tv_tag);
            ll_menu = itemView.findViewById(R.id.ll_menu);
        }
    }

    private OnItemClickListener1 onItemClickListener;

    public void setOnItemClickListener1(OnItemClickListener1 onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener1 {
        void setItemClickListener1(int pos);
    }
}
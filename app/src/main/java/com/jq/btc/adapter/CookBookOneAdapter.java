package com.jq.btc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jq.btc.app.R;
import com.jq.btc.model.MenuModel;
import com.jq.btc.utils.GlideOptions;
import com.jq.btc.utils.ImageUtil;

import java.util.List;


/**
 * Create by AYD on 2019/2/25
 * 推荐菜谱Adapter
 */
public class CookBookOneAdapter extends RecyclerView.Adapter<CookBookOneAdapter.MyViewHolder> {

    private List<MenuModel.DataBeanX.DataBean.RecipesBean> menuModels;
    private Context context;

    public CookBookOneAdapter(Context context) {
        this.context = context;
    }

    public void setMenuModels(List<MenuModel.DataBeanX.DataBean.RecipesBean> menuModels) {
        this.menuModels = menuModels;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
//        MenuModel.DataBeanX.DataBean.RecipesBean recipesBean = menuModels.get(position).getData().getData().getRecipes().get(position);

//        Glide.with(context).load(menuModels.get(position).getRecipeimage())
//                .apply(RequestOptions.centerCropTransform())
//                .into(holder.iv_pic);

        GlideOptions glideOptions = new GlideOptions.Builder()
                .radius(4)
                .cornerType(GlideOptions.CornerType.ALL)
                .build();
        ImageUtil.display(context, menuModels.get(position).getRecipeimage(), holder.iv_pic, glideOptions);
        holder.tv_menu_name.setText(menuModels.get(position).getRecipename());
        String recipetag = menuModels.get(position).getRecipetag().replace(",", "/");
        holder.tv_tag.setText(recipetag);
        holder.ll_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.setItemClickListener(position);
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

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void setItemClickListener(int pos);
    }
}

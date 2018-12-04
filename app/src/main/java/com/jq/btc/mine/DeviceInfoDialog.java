package com.jq.btc.mine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jq.btc.app.R;
import com.jq.btc.adapter.ProductInfoAdapter;
import com.jq.code.code.business.ScaleParser;
import com.jq.code.code.util.CallPhoneUtils;
import com.jq.code.model.ProductInfo;
import com.jq.code.model.json.JsonProductInfo;
import com.jq.code.view.dialog.BaseDialog;

import java.util.Map;

public class DeviceInfoDialog extends BaseDialog {

    private ViewHolder mViewHolder;
    private Context context;
    private ProductInfoAdapter infoAdapter;

    public DeviceInfoDialog(Context context,int i) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.activity_device_info, null);
        this.context = context;
        addView(view);
        initView(view,i);
    }

    private void initView(View view,int i) {
        mViewHolder = new ViewHolder();
        mViewHolder.company_content = (TextView) view.findViewById(R.id.company_content);
        if(i == 1){
            mViewHolder.company_content.setText("智能营养称");
        }else {
            mViewHolder.company_content.setText("智能体脂称");
        }
        mViewHolder.logo = (ImageView) view.findViewById(R.id.company_logo);
        mViewHolder.cancel = (ImageView) view.findViewById(R.id.company_cancel);
        mViewHolder.companyInfoList = (ListView) view.findViewById(R.id.company_info);
        infoAdapter = new ProductInfoAdapter(context, new ProductInfo());
        mViewHolder.companyInfoList.setAdapter(infoAdapter);
        mViewHolder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mViewHolder.companyInfoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 1){
                    Map<String,String> map = (Map<String, String>) infoAdapter.getItem(position);
                    if(!TextUtils.isEmpty(map.get("content"))){
                        CallPhoneUtils.dail(context,map.get("content"));
                    }
                }
            }
        });
        // 获取产品信息
        getProduct();
    }

    /**
     * 获取产品信息
     */
    private void getProduct() {
//        if (!NetWorkUtil.isNetworkConnected(context)) {
            setProductInfo();
//        } else {
//            api.getProductInfo(ScaleParser.getInstance(context).getScale().getProduct_id(), null, new HttpsCallback() {
//                @Override
//                public void onSuccess(Object data) {
//                    if (data != null) {
//                        JsonProductInfo info = JsonMapper.fromJson(data,JsonProductInfo.class);
//                        Map<String, JsonProductInfo> productMap =
//                                ScaleParser.getInstance(context).getProductMap();
//                        productMap.put(ScaleParser.getInstance(context).getScale().getProduct_id() + "",
//                                info);
//                        ScaleParser.getInstance(context).setProductMap(productMap);
//                        setProductInfo();
//                    } else {
//                        setProductInfo();
//                    }
//                }
//
//                @Override
//                public void onFailure(String msg, int code) {
//                    setProductInfo();
//                }
//            });
//        }
    }

    private class ViewHolder {
        TextView company_content;
        ImageView logo, cancel;
        ListView companyInfoList;
    }


    private void setProductInfo() {
        Bitmap bitmap = null;
        int product_id = ScaleParser.getInstance(context).getScale().getProduct_id();
        JsonProductInfo jsonProductInfo = ScaleParser.getInstance(context).getProductMap().get("" + product_id);
//        if (jsonProductInfo == null || jsonProductInfo.getLogo_path().trim().equals("")) {
//            bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.weight_flag_img);
//            mViewHolder.logo.setImageBitmap(bitmap);
//        } else {
//            ImageLoad.setProduct(context, mViewHolder.logo, jsonProductInfo.getLogo_path(), R.mipmap.weight_flag_img);
//        }
        if(product_id==1107000001){
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.q1_logo);
        }else if(product_id==1107000002){
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.q31_logo);
        }else if(product_id==1107000003){
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.q7_logo);
        }else{
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.weight_flag_img);
        }
        mViewHolder.logo.setImageBitmap(bitmap);

        ProductInfo productInfo = null;
        if (jsonProductInfo == null) {
            productInfo = new ProductInfo();
        } else {
                productInfo = jsonProductInfo.getZh();
        }
        if (productInfo == null) {
            productInfo = new ProductInfo();
        }
        infoAdapter.update(productInfo);

    }
}
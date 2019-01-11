package com.jq.btc.bluettooth.report.haier;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.haiersmart.user.sdk.UserUtils;
import com.jq.btc.ConstantUrl;
import com.jq.btc.app.R;
import com.jq.btc.app.R2;
import com.jq.btc.bluettooth.report.haier.item.IndexDataItem;
import com.jq.btc.model.MoreDataModel;
import com.jq.btc.myview.CircleIndicatorView;
import com.jq.btc.myview.ScrollChartView;
import com.jq.code.view.CircleImageView;
import com.jq.btc.app.R2;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * 更多数据页面
 * 2018-12-29
 */
public class BodyFatMoreDataActivity extends AppCompatActivity implements View.OnClickListener {

    private CircleImageView mRoleImage;
    private TextView mRoleName;
    private TextView mWeightTime;
    private TextView mScore;
    private TextView tv_weight;
    private ScrollChartView scroll_chart_main;
    private CircleIndicatorView civ_main;
    private ImageView iv_back;
    private LinearLayout ll_data;
    private TextView tv_no_data;

    private ProgressBar pb_metabolism, pb_bone_weight, pb_rate_of_muscle, pb_muscle_weight, pb_visceral_fat,
            pb_water_points, pb_water_content, pb_obesity, pb_bmi;

    private TextView tv_metabolism, tv_bone_weight, tv_rate_of_muscle, tv_muscle_weight, tv_visceral_fat, tv_water_points,
            tv_water_content, tv_obesity, tv_bmi;

    private TextView tv_metabolism_name, tv_bone, tv_rate_of_muscle_, tv_muscle_weight_, tv_visceral_fat_,
            tv_water_points_, tv_water_content_, tv_obesity_, tv_bmi_;

    private TextView tv_metabolism_level, tv_bone_level, tv_rate_of_muscle_level, tv_muscle_weight_level,
            tv_visceral_fat_level, tv_water_points_level, tv_water_content_level, tv_obesity_level, tv_bmi_level;

    private List<String> timeList;
    private List<Double> dataList;
    private MoreDataModel moreDataModel;
    private String useId;
    private String userName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_data_layout);
        initView();
        initData();

    }

    private void initView() {
        useId = getIntent().getStringExtra("useId");
        userName = getIntent().getStringExtra("useName");
        mRoleImage = findViewById(R2.id.mRoleImage);
        mRoleName = findViewById(R2.id.mRoleName);
        mWeightTime = findViewById(R2.id.mWeightTime);
        mScore = findViewById(R2.id.mScore);
        ll_data = findViewById(R2.id.ll_data);
        tv_no_data = findViewById(R2.id.tv_no_data);
        tv_weight = findViewById(R2.id.tv_weight);
        scroll_chart_main = findViewById(R2.id.scroll_chart_main);
        civ_main = findViewById(R2.id.civ_main);
        iv_back = findViewById(R2.id.iv_back);
        iv_back.setOnClickListener(this);
        scroll_chart_main.setLineType(ScrollChartView.LineType.LINE);
        scroll_chart_main.invalidateView();
        tv_metabolism = findViewById(R2.id.tv_metabolism);
        tv_bone_weight = findViewById(R2.id.tv_bone_weight);
        tv_rate_of_muscle = findViewById(R2.id.tv_rate_of_muscle);
        tv_muscle_weight = findViewById(R2.id.tv_muscle_weight);
        tv_visceral_fat = findViewById(R2.id.tv_visceral_fat);
        tv_water_points = findViewById(R2.id.tv_water_points);
        tv_water_content = findViewById(R2.id.tv_water_content);
        tv_obesity = findViewById(R2.id.tv_obesity);
        tv_bmi = findViewById(R2.id.tv_bmi);
        tv_metabolism_name = findViewById(R2.id.tv_metabolism_name);
        tv_metabolism_name.setText("基础代谢");
        tv_bone = findViewById(R2.id.tv_bone);
        tv_bone.setText("骨       重");
        tv_rate_of_muscle_ = findViewById(R2.id.tv_rate_of_muscle_);
        tv_rate_of_muscle_.setText("肌  肉  率");
        tv_muscle_weight_ = findViewById(R2.id.tv_muscle_weight_);
        tv_muscle_weight_.setText("肌肉重量");
        tv_visceral_fat_ = findViewById(R2.id.tv_visceral_fat_);
        tv_visceral_fat_.setText("内脏脂肪");
        tv_water_points_ = findViewById(R2.id.tv_water_points_);
        tv_water_points_.setText("水       分");
        tv_water_content_ = findViewById(R2.id.tv_water_content_);
        tv_water_content_.setText("水 含 量");
        tv_obesity_ = findViewById(R2.id.tv_obesity_);
        tv_obesity_.setText("肥  胖  度");
        tv_bmi_ = findViewById(R2.id.tv_bmi_);
        tv_bmi_.setText("B   M   I");
        tv_metabolism_level = findViewById(R2.id.tv_metabolism_level);
        tv_bone_level = findViewById(R2.id.tv_bone_level);
        tv_rate_of_muscle_level = findViewById(R2.id.tv_rate_of_muscle_level);
        tv_muscle_weight_level = findViewById(R2.id.tv_muscle_weight_level);
        tv_visceral_fat_level = findViewById(R2.id.tv_visceral_fat_level);
        tv_water_points_level = findViewById(R2.id.tv_water_points_level);
        tv_water_content_level = findViewById(R2.id.tv_water_content_level);
        tv_obesity_level = findViewById(R2.id.tv_obesity_level);
        tv_bmi_level = findViewById(R2.id.tv_bmi_level);

        pb_metabolism = findViewById(R2.id.pb_metabolism);
        pb_bone_weight = findViewById(R2.id.pb_bone_weight);
        pb_rate_of_muscle = findViewById(R2.id.pb_rate_of_muscle);
        pb_muscle_weight = findViewById(R2.id.pb_muscle_weight);
        pb_visceral_fat = findViewById(R2.id.pb_visceral_fat);
        pb_water_points = findViewById(R2.id.pb_water_points);
        pb_water_content = findViewById(R2.id.pb_water_content);
        pb_obesity = findViewById(R2.id.pb_obesity);
        pb_bmi = findViewById(R2.id.pb_bmi);

    }

    private void initData() {
        getMoreData(UserUtils.get().userId(), useId);
        mRoleName.setText(userName);
    }

    /**
     * 基础代谢
     *
     * @param s
     */
    private void interceptMetabolism(String s) {
        String[] sourceStrArray = s.split(",");
        tv_metabolism.setText(sourceStrArray[0]);
        tv_metabolism_level.setText(sourceStrArray[1]);
        for (int i = 0; i < sourceStrArray.length; i++) {
            Log.e("AYD", "interceptMetabolism: " + sourceStrArray[i]);
        }
        String max = sourceStrArray[2];
        String value = sourceStrArray[3];
        double maxs = Double.parseDouble(max);
        double values = Double.parseDouble(value);
        pb_metabolism.setMax((int) maxs);
        pb_metabolism.setProgress((int) values);
        if (sourceStrArray[1].equals("未达标")) {
            tv_metabolism_level.setBackgroundResource(R.color.corState6);
            pb_metabolism.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_drawable_f66361));
        } else if (sourceStrArray[1].equals("达标")) {
            tv_metabolism_level.setBackgroundResource(R.color.corState3);
            pb_metabolism.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_drawable_07a3d9));
        }
    }

    /**
     * 骨重
     *
     * @param s
     */
    private void interceptbone(String s) {
        String[] sourceStrArray = s.split(",");
        tv_bone_weight.setText(sourceStrArray[0]);
        tv_bone_level.setText(sourceStrArray[1]);
        String max = sourceStrArray[2];
        String value = sourceStrArray[3];
        double maxs = Double.parseDouble(max);
        double values = Double.parseDouble(value);
        pb_bone_weight.setMax((int) maxs);
        pb_bone_weight.setProgress((int) values);
        for (int i = 0; i < sourceStrArray.length; i++) {
            Log.e("AYD", "interceptbone: " + sourceStrArray[i]);
        }
        if (sourceStrArray[1].equals("偏低")) {
            tv_bone_level.setBackgroundResource(R.color.corState2);
            pb_bone_weight.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_drawable_d28672));
        } else if (sourceStrArray[1].equals("标准")) {
            tv_bone_level.setBackgroundResource(R.color.corState3);
            pb_bone_weight.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_drawable_07a3d9));
        } else if (sourceStrArray[1].equals("优")) {
            tv_bone_level.setBackgroundResource(R.color.corState8);
            pb_bone_weight.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_drawable_1e56d9));
        }
    }

    /**
     * 肌肉率
     *
     * @param s
     */
    private void interceptRateOfMuscle(String s) {
        String[] sourceStrArray = s.split(",");
        tv_rate_of_muscle.setText(sourceStrArray[0]);
        tv_rate_of_muscle_level.setText(sourceStrArray[1]);
        String max = sourceStrArray[2];
        String value = sourceStrArray[3];
        double maxs = Double.parseDouble(max);
        double values = Double.parseDouble(value);
        pb_rate_of_muscle.setMax((int) maxs);
        pb_rate_of_muscle.setProgress((int) values);
        if (sourceStrArray[1].equals("不足")) {
            tv_rate_of_muscle_level.setBackgroundResource(R.color.corState2);
            pb_rate_of_muscle.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_drawable_f66361));
        } else if (sourceStrArray[1].equals("标准")) {
            tv_rate_of_muscle_level.setBackgroundResource(R.color.corState3);
            pb_rate_of_muscle.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_drawable_07a3d9));
        } else if (sourceStrArray[1].equals("优")) {
            tv_rate_of_muscle_level.setBackgroundResource(R.color.corState8);
            pb_rate_of_muscle.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_drawable_1e56d9));
        }
    }

    /**
     * 肌肉重量
     *
     * @param s
     */
    private void interceptMuscleWeight(String s) {
        String[] sourceStrArray = s.split(",");
        tv_muscle_weight.setText(sourceStrArray[0]);
        tv_muscle_weight_level.setText(sourceStrArray[1]);
        String max = sourceStrArray[2];
        String value = sourceStrArray[3];
        double maxs = Double.parseDouble(max);
        double values = Double.parseDouble(value);
        pb_muscle_weight.setMax((int) maxs);
        pb_muscle_weight.setProgress((int) values);
        if (sourceStrArray[1].equals("不足")) {
            tv_muscle_weight_level.setBackgroundResource(R.color.corState2);
            pb_muscle_weight.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_drawable_f66361));
        } else if (sourceStrArray[1].equals("标准")) {
            tv_muscle_weight_level.setBackgroundResource(R.color.corState3);
            pb_muscle_weight.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_drawable_07a3d9));
        } else if (sourceStrArray[1].equals("优")) {
            tv_muscle_weight_level.setBackgroundResource(R.color.corState8);
            pb_muscle_weight.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_drawable_1e56d9));
        }
    }

    /**
     * 内脏脂肪
     *
     * @param s
     */
    private void interceptVisceralFat(String s) {
        String[] sourceStrArray = s.split(",");
        tv_visceral_fat.setText(sourceStrArray[0]);
        tv_visceral_fat_level.setText(sourceStrArray[1]);
        String max = sourceStrArray[2];
        String value = sourceStrArray[3];
        double maxs = Double.parseDouble(max);
        double values = Double.parseDouble(value);
        pb_visceral_fat.setMax((int) maxs);
        pb_visceral_fat.setProgress((int) values);
        if (sourceStrArray[1].equals("不足")) {
            tv_visceral_fat_level.setBackgroundResource(R.color.corState2);
            pb_visceral_fat.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_drawable_f66361));
        } else if (sourceStrArray[1].equals("标准")) {
            tv_visceral_fat_level.setBackgroundResource(R.color.corState3);
            pb_visceral_fat.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_drawable_07a3d9));
        } else if (sourceStrArray[1].equals("优")) {
            tv_visceral_fat_level.setBackgroundResource(R.color.corState8);
            pb_visceral_fat.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_drawable_1e56d9));
        }
    }

    /**
     * 水分
     *
     * @param s
     */
    private void interceptWaterPoints(String s) {
        String[] sourceStrArray = s.split(",");
        tv_water_points.setText(sourceStrArray[0]);
        tv_water_points_level.setText(sourceStrArray[1]);
        String max = sourceStrArray[2];
        String value = sourceStrArray[3];
        double maxs = Double.parseDouble(max);
        double values = Double.parseDouble(value);
        pb_water_points.setMax((int) maxs);
        pb_water_points.setProgress((int) values);
        if (sourceStrArray[1].equals("不足")) {
            tv_water_points_level.setBackgroundResource(R.color.corState2);
            pb_water_points.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_drawable_f66361));
        } else if (sourceStrArray[1].equals("标准")) {
            tv_water_points_level.setBackgroundResource(R.color.corState3);
            pb_water_points.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_drawable_07a3d9));
        } else if (sourceStrArray[1].equals("优")) {
            tv_water_points_level.setBackgroundResource(R.color.corState8);
            pb_water_points.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_drawable_1e56d9));
        }
    }

    /**
     * 水含量
     *
     * @param s
     */
    private void interceptWaterContent(String s) {
        String[] sourceStrArray = s.split(",");
        tv_water_content.setText(sourceStrArray[0]);
        tv_water_content_level.setText(sourceStrArray[1]);
        String max = sourceStrArray[2];
        String value = sourceStrArray[3];
        double maxs = Double.parseDouble(max);
        double values = Double.parseDouble(value);
        pb_water_content.setMax((int) maxs);
        pb_water_content.setProgress((int) values);
        if (sourceStrArray[1].equals("不足")) {
            tv_water_content_level.setBackgroundResource(R.color.corState2);
            pb_water_content.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_drawable_f66361));
        } else if (sourceStrArray[1].equals("标准")) {
            tv_water_content_level.setBackgroundResource(R.color.corState3);
            pb_water_content.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_drawable_07a3d9));
        } else if (sourceStrArray[1].equals("优")) {
            tv_water_content_level.setBackgroundResource(R.color.corState8);
            pb_water_content.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_drawable_1e56d9));
        }
    }

    /**
     * 肥胖度
     *
     * @param s
     */
    private void interceptObesity(String s) {
        String[] sourceStrArray = s.split(",");
        tv_obesity.setText(sourceStrArray[0]);
        tv_obesity_level.setText(sourceStrArray[1]);
        String max = sourceStrArray[2];
        String value = sourceStrArray[3];
        double maxs = Double.parseDouble(max);
        double values = Double.parseDouble(value);
        pb_obesity.setMax((int) maxs);
        pb_obesity.setProgress((int) values);
        if (sourceStrArray[1].equals("消瘦")) {
            tv_obesity_level.setBackgroundResource(R.color.corState2);
            pb_obesity.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_drawable_f66361));
        } else if (sourceStrArray[1].equals("偏瘦")) {
            tv_obesity_level.setBackgroundResource(R.color.corState2);
            pb_obesity.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_drawable_f66361));
        } else if (sourceStrArray[1].equals("标准")) {
            tv_obesity_level.setBackgroundResource(R.color.corState3);
            pb_obesity.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_drawable_07a3d9));
        } else if (sourceStrArray[1].equals("超重")) {
            tv_obesity_level.setBackgroundResource(R.color.corState4);
            pb_obesity.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_drawable_e6c878));
        } else if (sourceStrArray[1].equals("轻度")) {
            tv_obesity_level.setBackgroundResource(R.color.corState5);
            pb_obesity.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_drawable_eb876e));
        } else if (sourceStrArray[1].equals("中度")) {
            tv_obesity_level.setBackgroundResource(R.color.corState6);
            pb_obesity.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_drawable_f66361));
        } else if (sourceStrArray[1].equals("重度")) {
            tv_obesity_level.setBackgroundResource(R.color.corState7);
            pb_obesity.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_drawable_c84455));
        }

    }

    /**
     * bmi
     *
     * @param s
     */
    private void interceptBmi(String s) {
        String[] sourceStrArray = s.split(",");
        tv_bmi.setText(sourceStrArray[0]);
        tv_bmi_level.setText(sourceStrArray[1]);
        String max = sourceStrArray[2];
        String value = sourceStrArray[3];
        double maxs = Double.parseDouble(max);
        double values = Double.parseDouble(value);
        pb_bmi.setMax((int) maxs);
        pb_bmi.setProgress((int) values);
        if (sourceStrArray[1].equals("偏瘦")) {
            tv_bmi_level.setBackgroundResource(R.color.corState2);
            pb_bmi.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_drawable_d28672));
        } else if (sourceStrArray[1].equals("标准")) {
            tv_bmi_level.setBackgroundResource(R.color.corState3);
            pb_bmi.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_drawable_07a3d9));
        } else if (sourceStrArray[1].equals("偏胖")) {
            tv_bmi_level.setBackgroundResource(R.color.corState4);
            pb_bmi.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_drawable_e6c878));
        } else if (sourceStrArray[1].equals("肥胖")) {
            tv_bmi_level.setBackgroundResource(R.color.corState6);
            pb_bmi.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_drawable_f66361));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R2.id.iv_back:
                finish();
                break;
        }
    }


    /**
     * 请求个人每一天的数据
     *
     * @param famaliyId
     * @param userId
     */
    private void getMoreData(String famaliyId, String userId) {
        final IndexDataItem indexDataItem = new IndexDataItem();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("famaliyId", famaliyId);
        jsonObject.put("userId", userId);
        OkGo.<String>post(ConstantUrl.GET_CHARTLINE_DATA_URL)
                .upJson(String.valueOf(jsonObject))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        moreDataModel = new Gson().fromJson(response.body(), MoreDataModel.class);
//                        data = moreDataModel.getData();
                        Log.e("AYD", "--->" + response.body());
                        Log.e("ADY", "an" + indexDataItem.mLevelTextRes);

                        if (moreDataModel.getData().size() == 0) {
                            ll_data.setVisibility(View.GONE);
                            tv_no_data.setVisibility(View.VISIBLE);
                            return;
                        }
                        timeList = new ArrayList<>();
                        for (int i = 0; i < moreDataModel.getData().size(); i++) {
                            timeList.add(moreDataModel.getData().get(i).getScore());
                        }
                        dataList = new ArrayList<>();
                        for (int i = 0; i < moreDataModel.getData().size(); i++) {
                            dataList.add(Double.valueOf(moreDataModel.getData().get(i).getWeight()));
                        }
                        scroll_chart_main.setData(timeList, dataList);
                        scroll_chart_main.setOnScaleListener(new ScrollChartView.OnScaleListener() {
                            @Override
                            public void onScaleChanged(int position) {
                                mScore.setText(timeList.get(position));
                                tv_weight.setText(dataList.get(position) + "KG");
                                ScrollChartView.Point point = scroll_chart_main.getList().get(position);
                                civ_main.setCircleY(point.y);
                                mWeightTime.setText(moreDataModel.getData().get(position).getCreateTime());
                                interceptMetabolism(moreDataModel.getData().get(position).getMetabolism());
                                interceptbone(moreDataModel.getData().get(position).getBoneWeight());
                                interceptRateOfMuscle(moreDataModel.getData().get(position).getMuscleRate());
                                interceptMuscleWeight(moreDataModel.getData().get(position).getMuscleWeight());
                                interceptVisceralFat(moreDataModel.getData().get(position).getVisceralFat());
                                interceptWaterPoints(moreDataModel.getData().get(position).getWater());
                                interceptWaterContent(moreDataModel.getData().get(position).getWaterWeight());
                                interceptObesity(moreDataModel.getData().get(position).getObesity());
                                interceptBmi(moreDataModel.getData().get(position).getBmi());
                            }
                        });

                        //滚动到目标position
                        scroll_chart_main.smoothScrollTo(dataList.size() - 1);
                    }
                });

    }

}

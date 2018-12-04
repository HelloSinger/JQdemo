package com.jq.btc.homePage;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jq.btc.app.R;


/**
 *  分享对话框
 *
 * @author 李金华
 */
public class InitDataDialog extends Dialog {

    private TextView mPercentText;
    private ProgressBar mProgressBar;

    /**
     * 构造函数
     * @param context 上下文
     */
    public InitDataDialog(Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setContentView(R.layout.dialog_init_data);

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.TOP;
        getWindow().setAttributes(layoutParams);

        getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        setCanceledOnTouchOutside(false);
        setCancelable(false);

        mPercentText = (TextView)findViewById(R.id.percent);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar1);
    }

    public void setPercent(int percent) {
        mPercentText.setText(percent + "%");
        mProgressBar.setProgress(percent);
    }
}

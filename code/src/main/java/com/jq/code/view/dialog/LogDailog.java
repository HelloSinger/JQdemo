package com.jq.code.view.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jq.code.R;

/**
 * Created by Administrator on 2017/3/13.
 */

public class LogDailog extends BaseDialog {
    TextView textView;

    public LogDailog(Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.log_text_view, null);
        textView = (TextView) view.findViewById(R.id.log_text);
        addView(view);
        showDialog();
    }

    public void setText(String text) {
        textView.setText(text);
    }
}

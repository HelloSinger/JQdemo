package com.jq.code.view.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.jq.code.R;
import com.jq.code.view.text.CustomTextView;


public class TipDialog extends BaseDialog implements OnClickListener {

    private CustomTextView sure, cancel, tip;

    public TipDialog(Context context) {
        super(context);
        View vv = LayoutInflater.from(context).inflate(
                R.layout.item_tip_view, null);
        addView(vv);
        tip = (CustomTextView) vv.findViewById(R.id.tip_text);
        sure = (CustomTextView) vv.findViewById(R.id.tip_sure);
        cancel = (CustomTextView) vv.findViewById(R.id.tip_cancle);

        cancel.setOnClickListener(this);
        sure.setOnClickListener(this);
    }

    public void setLeftButtonBackgound(int lefDrawableId) {
        cancel.setBackgroundResource(lefDrawableId);
    }

    public void setRightButtonBackgound(int rightDrawableId) {
        sure.setBackgroundResource(rightDrawableId);
    }

    public void setButtonTextColor(int color) {
        cancel.setTextColor(color);
        sure.setTextColor(color);
    }

    public void setLeftButtonText(int textId) {
        cancel.setText(textId);
    }

    public void setRightButtonText(int textId) {
        sure.setText(textId);
    }

    public Object getText() {
        if (tip != null) {
            return tip.getText();
        }
        return null;
    }

    public void setText(String text) {
        if (tip != null) {
            tip.setText(text);
        }
    }

    public void setText(int textId) {
        if (tip != null) {
            tip.setText(textId);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == sure) {
            if (l != null) {
                l.onClick(sure);
            }
        }
        dismiss();
    }
}

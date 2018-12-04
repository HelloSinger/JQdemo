package com.jq.code.view.button;

import android.content.Context;
import android.util.AttributeSet;

import com.jq.code.R;
import com.jq.code.view.text.CustomTextView;


/**
 * Created by hfei on 2015/10/8.
 */
public class CheckButton extends CustomTextView {

    private boolean isBound;

    public CheckButton(Context context) {
        super(context);
    }

    public CheckButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setBoundState(boolean isBound){
        this.isBound = isBound;
        if(isBound){
            setBackgroundResource(R.mipmap.account_unbound);
            setText(R.string.unbound);
        }else{
            setBackgroundResource(R.mipmap.account_bound);
            setText(R.string.bound);
        }
    }

    //thirdPlatform 0--qq, 1--sina 2--weixin
    public void setThirdLoginBoundState(boolean isBound,int thirdPlatform){
        this.isBound = isBound;
        if(isBound){
            setBackgroundResource(R.mipmap.account_unbound);
            setText(R.string.unbound);
        }else{
            switch (thirdPlatform){
                case 0:
                    setBackgroundResource(R.mipmap.account_bound);
                    break;
                case 1:
                    setBackgroundResource(R.mipmap.account_bound_sina);
                    break;
                case 2:
                    setBackgroundResource(R.mipmap.account_bound_weixin);
                    break;
            }
            setText(R.string.bound);
        }
    }

    public boolean isBound(){
        return isBound;
    }
}

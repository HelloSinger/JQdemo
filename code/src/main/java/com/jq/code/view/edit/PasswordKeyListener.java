package com.jq.code.view.edit;

import android.text.InputType;
import android.text.method.NumberKeyListener;

/**
 * Created by Administrator on 2016/9/5.
 */
public class PasswordKeyListener extends NumberKeyListener {
    private final String DIGITS="0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ.,%@-_+=#*";

    @Override
    protected char[] getAcceptedChars() {
        return DIGITS.toCharArray();
    }

    @Override
    public int getInputType() {
        return InputType.TYPE_CLASS_TEXT;
    }
}
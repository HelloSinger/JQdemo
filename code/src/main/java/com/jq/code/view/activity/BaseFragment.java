package com.jq.code.view.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.jq.code.R;


/**
 * Created by Administrator on 2017/2/21.
 */

public class BaseFragment extends Fragment {
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }
}

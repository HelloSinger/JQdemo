package com.jq.code.code.util;



import com.jq.code.model.PutBase;

import java.util.Comparator;

/**
 * Created by xulj on 2016/3/28.
 */
public class PutBasePositionComparable implements Comparator<PutBase> {

    @Override
    public int compare(PutBase lhs, PutBase rhs) {

            int diff = lhs.getxPosition() - rhs.getxPosition();
            if (diff > 0) {
                return 1;
            } else if (diff < 0) {
                return -1;
            }else{
                return 0;
            }
    }
}

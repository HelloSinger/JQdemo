package com.jq.btc.helper;


import com.jq.code.model.PutBase;

import java.util.List;

/**
 * Created by Administrator on 2016/7/5.
 */
public interface HomeDataSetObserver {
    /**
     * 数据更改通知
     * @param bases 当前数据列表
     */
    void onChanged(List<PutBase> bases);

    /**
     * 该数据超出当前数据列表范围
     * @param bases 当前数据列表范围外数据
     */
    void rangeOver(PutBase bases);
}

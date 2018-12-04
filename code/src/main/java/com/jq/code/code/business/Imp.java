package com.jq.code.code.business;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/1.
 */

public abstract class Imp {
    /**
     * 子类列表
     */
    private static List<Imp> impList = new ArrayList<>();

    public Imp() {
        impList.add(this);
    }

    public abstract void fill(long account_id);

    public abstract void flip(long account_id);

    public abstract void destory();

    /**
     * 销毁子类
     */
    public static void destoryAll() {
        for (Imp imp : impList) {
            if (imp != null) imp.destory();
        }
    }

    protected ImpCallbak callback;

    public Imp addCallback(ImpCallbak callback) {
        this.callback = callback;
        return this;
    }

    public void refresh() {
    }
}

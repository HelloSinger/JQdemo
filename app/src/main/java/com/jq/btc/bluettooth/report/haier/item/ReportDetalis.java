package com.jq.btc.bluettooth.report.haier.item;

import java.util.List;

/**
 * Created by mihw on 2018/6/12.
 */

public class ReportDetalis {

    private boolean haveRn8;
    private List<Rn8Item> rn8Items;

    public ReportDetalis() {

    }

    public List<Rn8Item> getRn8Items() {
        return rn8Items;
    }

    public void setRn8Items(List<Rn8Item> rn8Items) {
        this.rn8Items = rn8Items;
    }

    public boolean isHaveRn8() {
        return haveRn8;
    }

    public void setHaveRn8(boolean haveRn8) {
        this.haveRn8 = haveRn8;
    }

}

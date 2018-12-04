package com.jq.btc.homePage.home.weight;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.jq.btc.homePage.LocalBroadcastUtil;
import com.jq.btc.homePage.home.LoadDataCallback;
import com.jq.btlib.util.ThreadUtil;
import com.jq.code.code.business.Account;
import com.jq.code.code.db.WeightDataDB;
import com.jq.code.model.DataType;
import com.jq.code.model.RoleInfo;
import com.jq.code.model.WeightEntity;

import java.util.List;

/**
 * Created by lijh on 2017/5/31.
 */

public class WeightModel implements WeightMVPContracts.Model {
    public static final String tag = "WeightModel";
    private static final int ITEMS_IN_ONE_PAGE = 20;
    private Context mContext;
    private final Handler handler;
    private List<WeightEntity> mWeightEntityList;
    /** 正在显示的角色 */
    private RoleInfo mShowRole;

    WeightModel() {
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void setContext(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public void initWeightData(final LoadDataCallback callback) {
        ThreadUtil.executeThread(new Runnable() {
            @Override
            public void run() {
                Account account = Account.getInstance(mContext);
                long accountId = account.getAccountInfo().getId();
                mShowRole = account.getRoleInfo();
                long roleId = mShowRole.getId();
                String mtype = DataType.WEIGHT.getType();

                // 说明该角色下的体重类别数据以前同步过，只需获取增量数据；但是，先从本地数据库加载以即时显示
                mWeightEntityList = WeightDataDB.getInstance(mContext).loadWeightData(accountId, roleId, mtype, ITEMS_IN_ONE_PAGE, 0);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(mWeightEntityList);
                    }
                });
            }
        });
    }

    @Override
    public RoleInfo getShowingRoleInfo() {
        return mShowRole;
    }

    @Override
    public void removeItem(int dataPosition) {
        WeightEntity entity = mWeightEntityList.remove(dataPosition);
        removeWeight(entity);
    }

    @Override
    public void loadNextPageData(final LoadDataCallback callback) {
        ThreadUtil.executeThread(new Runnable() {
            @Override
            public void run() {
                Account account = Account.getInstance(mContext);
                long accountId = account.getAccountInfo().getId();
                long roleId = account.getRoleInfo().getId();
                String mtype = DataType.WEIGHT.getType();

                final List<WeightEntity> entities = WeightDataDB.getInstance(mContext).loadWeightData(accountId, roleId, mtype,
                        ITEMS_IN_ONE_PAGE, mWeightEntityList.size());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(entities);
                    }
                });
            }
        });
    }

    @Override
    public void loadHandAddWeight(final LoadDataCallback callback) {
        ThreadUtil.executeThread(new Runnable() {
            @Override
            public void run() {
                Account account = Account.getInstance(mContext);
                long accountId = account.getAccountInfo().getId();
                long roleId = account.getRoleInfo().getId();
                String mtype = DataType.WEIGHT.getType();
                // 试着加载最后1条
                final List<WeightEntity> entities = WeightDataDB.getInstance(mContext).loadWeightData(accountId, roleId, mtype, 1, 0);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(entities);
                    }
                });
            }
        });
    }

    /**
     * 删除一条体重
     * @param entity 体重信息
     */
    private void removeWeight(final WeightEntity entity) {
        Runnable runnable = new Runnable() {
            public void run() {
                if (entity.getId() == 0) {
                    WeightDataDB.getInstance(mContext).remove(entity);
                } else {
                    entity.setDelete(1);
                    WeightDataDB.getInstance(mContext).setDeleted(entity);

                    if(Account.getInstance(mContext).isAccountLogined()) {
                    }
                }
                Intent intent = new Intent();
                intent.setAction(LocalBroadcastUtil.ACTION_DELETE_WEIGHT);
                intent.putExtra("fromWeightModel", true);
                LocalBroadcastUtil.notify(mContext, intent);
            }
        };
        ThreadUtil.executeThread(runnable);
    }

}

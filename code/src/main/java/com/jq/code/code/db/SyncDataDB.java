package com.jq.code.code.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.jq.code.model.SyncDataInfo;

import java.util.*;
import java.util.concurrent.locks.Lock;

/**
 * 记录同步状态的表
 * */
public class SyncDataDB {
    public static final String TABLE_NAME = "cs_sync_log";
    /**
     * 同步数据日志表
     */
    public static final String CREATE_TABLE_SYNC = "create table if not exists "
            + TABLE_NAME
            + "(role_id integer not null, "
            + "account_id integer not null, "
            + "start bigint,"
            + "end bigint,"
            + "lastsync bigint,"
            + "mtype text)";

    private DB mDBUtil;

    /**
     * 析构函数
     */
    private SyncDataDB(Context context) {
        mDBUtil = DB.getInstance(context.getApplicationContext());
    }

    /**
     * 得到当前实体类
     */
    public static SyncDataDB getInstance(Context context) {
        SyncDataDB instance = new SyncDataDB(context);
        return instance;
    }

    /**
     * 创建一条同步数据
     *
     * @param info
     * @return
     */
    public void createSyncDataInfo(SyncDataInfo info) {
        createSyncDataInfo(info, true);
    }

    /**
     * 2.3 添加同步记录
     *
     * @param info
     * @param isMerge 是否自动合并可合并的记录
     */
    public void createSyncDataInfo(SyncDataInfo info, boolean isMerge) {
        if (info == null || info.getMtype() == null || info.getAccount_id() <= 0 || info.getRole_id() <= 0 || info.getLastsync() < 0 || info.getStart() <= 0) {
            throw new IllegalArgumentException("Invalid syncdatainfo: " + info);
        }

        List<SyncDataInfo> mergedSdis = null;
        List<SyncDataInfo> sdis = null;
        if (isMerge) {
            sdis = findListOfSyncDataInfo(info);
            sdis.add(info);
            mergedSdis = mergeSyncDataInfo(sdis);
        } else {
            mergedSdis = Arrays.asList(info);
        }

        synchronized (mDBUtil) {
            SQLiteDatabase db = null;
            try {
                db = mDBUtil.getWritableDatabase();
                if (isMerge && sdis.size() > 1) { // 合并前，加当前记录至少有两条记录才需要删除
                    for (SyncDataInfo sdi : sdis) {
                        removeSyncDataInfo(db, sdi);
                    }
                }
                for (SyncDataInfo sdi : mergedSdis) {
                    mDBUtil.insert(db, "cs_sync_log", creatContentValue(sdi), SQLiteDatabase.CONFLICT_ROLLBACK);
                }
            } catch (Exception e) {
            } finally {
                if (db != null) {
                    
                }
            }
        }
    }

    public static List<SyncDataInfo> mergeSyncDataInfo(List<SyncDataInfo> sdis) {
        if (sdis == null || sdis.size() <= 1) {
            return sdis;
        }
        sortSyncListByStart(sdis);
        List<SyncDataInfo> mergedSyncList = new ArrayList<SyncDataInfo>();

        Iterator<SyncDataInfo> iter = sdis.iterator();
        SyncDataInfo head = null;
        while (iter.hasNext()) {
            SyncDataInfo that = null;
            if (head == null) {
                head = iter.next();
            } else {
                that = iter.next();
            }

            if (that == null && iter.hasNext()) {
                that = iter.next();
            }

            if (that == null) {
                break;
            }

            // that not null
            if (checkMergable(head, that)) {
                head = merge(head, that);
            } else {
                mergedSyncList.add(head);
                head = that;
            }
        }
        mergedSyncList.add(head);
        return mergedSyncList;
    }

    public static void sortSyncListByStart(List<SyncDataInfo> sdis) {
        Collections.sort(sdis, new Comparator<SyncDataInfo>() {
            @Override
            public int compare(SyncDataInfo lhs, SyncDataInfo rhs) {
                return Long.valueOf(lhs.getStart()).compareTo(Long.valueOf(rhs.getStart()));
            }
        });
    }

    private static boolean checkMergable(SyncDataInfo ths, SyncDataInfo that) {
        if (ths.getMtype() == null || !ths.getMtype().equals(that.getMtype())) {
            throw new AssertionError(String.format("Illegal mtype at checkMergable %s : %s", ths.getMtype(), that.getMtype()));
        }
        return ths.getEnd() >= that.getStart();

    }

    /**
     * 合并两条记录。这两条记录必须可以合并才能调用本方法
     *
     * @param ths
     * @param that
     * @return
     */
    private static SyncDataInfo merge(SyncDataInfo ths, SyncDataInfo that) {
        SyncDataInfo sdi = new SyncDataInfo();
        sdi.setAccount_id(ths.getAccount_id());
        sdi.setRole_id(ths.getRole_id());
        sdi.setEnd(Math.max(ths.getEnd(), that.getEnd()));
        sdi.setStart(Math.min(ths.getStart(), that.getStart()));
        sdi.setMtype(ths.getMtype());
        sdi.setLastsync(Math.min(ths.getLastsync(), that.getLastsync()));
        return sdi;
    }

    public int removeSyncDataInfo(SQLiteDatabase db, SyncDataInfo sdi) {
        return mDBUtil.delete(db, "cs_sync_log",
                "lastsync=? and role_id=? and mtype=?",
                new String[]{String.valueOf(sdi.getLastsync()),
                        String.valueOf(sdi.getRole_id()),
                        sdi.getMtype()});
    }

    public List<SyncDataInfo> findListOfSyncDataInfo(SyncDataInfo info) {
        return findListOfSyncDataInfo(info.getAccount_id(), info.getRole_id(), info.getMtype());
    }


    /**
     * 用于在动态页，加载本地数据时，新增测量时间为查询条件，此时间必须大于或等于此方法返回的值
     * <p/>
     * 无记录时返回无穷大
     *
     * @return
     */
    public long getMinStartTs(long accountId, long roleId, String... mtypes) {
        synchronized (mDBUtil) {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            StringBuilder builder = new StringBuilder();
            builder.append("select start from cs_sync_log where account_id=? and role_id=?");
            if (mtypes != null) {
                builder.append(" and mtype in (");
                for (int i = 0; i < mtypes.length; i++) {
                    builder.append("'");
                    builder.append(mtypes[i]);
                    builder.append("'");
                    if (i != mtypes.length - 1) {
                        builder.append(",");
                    }
                }
                builder.append(")");
            }
            builder.append(" order by start asc limit 1");
            Cursor c = db.rawQuery(builder.toString(), new String[]{"" + accountId, "" + roleId});
            if (c.moveToNext()) {
                return c.getLong(c.getColumnIndex("start"));
            }
            return Long.MAX_VALUE;
        }
    }

    public long getMaxEndTs(long accountId, long roleId, String[] mtypes) {
        synchronized (mDBUtil) {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            StringBuilder builder = new StringBuilder();
            builder.append("select end from cs_sync_log where account_id=? and role_id=?");
            if (mtypes != null) {
                builder.append(" and mtype in (");
                for (int i = 0; i < mtypes.length; i++) {
                    builder.append("'");
                    builder.append(mtypes[i]);
                    builder.append("'");
                    if (i != mtypes.length - 1) {
                        builder.append(",");
                    }
                }
                builder.append(")");
            }
            builder.append(" order by end desc limit 1");
            Cursor c = db.rawQuery(builder.toString(), new String[]{"" + accountId, "" + roleId});
            if (c.moveToNext()) {
                return c.getLong(c.getColumnIndex("end"));
            }
            return Long.MAX_VALUE;
        }
    }

    /**
     * 按角色与类别获取未排序的同步记录列表
     *
     * @param accountId
     * @param roleId
     * @param mtype
     * @return
     */
    public List<SyncDataInfo> findListOfSyncDataInfo(long accountId, long roleId, String mtype) {
        List<SyncDataInfo> sdis = new ArrayList<SyncDataInfo>();
        Lock readLock = mDBUtil.getReadLock();
        readLock.lock();
        try {
            Cursor c = null;
            SQLiteDatabase db = mDBUtil.getReadableDatabase();
            String sql = "select * from " + "cs_sync_log" + " where " + "account_id=? and role_id=? and mtype=?";
            c = db.rawQuery(sql, new String[]{"" + accountId, "" + roleId, mtype});
            while (c.moveToNext()) {
                sdis.add(getContentValue(c));
            }
            c.close();
            
            return sdis;
        } finally {
            readLock.unlock();
        }
    }


    public void modifySyncDataInfoById(SyncDataInfo info) {
        synchronized (mDBUtil) {
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            mDBUtil.update(db, "cs_sync_log", creatContentValue(info), "account_id=? and role_id=?",
                    new String[]{String.valueOf(info.getAccount_id()), String.valueOf(info.getRole_id())});
            
        }
    }

    public long getMaxStartTs(int account_id, int role_id, String mType) {
        synchronized (mDBUtil) {
            Cursor c = null;
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            String sql =
                    "select start from cs_sync_log where account_id=? and role_id=? and mtype=? order by start desc limit 1";
            c = db.rawQuery(sql, new String[]{"" + account_id, "" + role_id, mType});
            if (c.moveToNext()) {
                return c.getLong(c.getColumnIndex("start"));
            }
            return 0;
        }
    }

    public long getMinSyncLastSync(int account_id, int role_id, String mType) {
        synchronized (mDBUtil) {
            SyncDataInfo info = null;
            Cursor c = null;
            SQLiteDatabase db = mDBUtil.getWritableDatabase();
            String sql = "select lastsync from cs_sync_log where account_id=? and role_id=? and mtype=? order by lastsync asc limit 1";
            c = db.rawQuery(sql, new String[]{"" + account_id, "" + role_id, mType});
            if (c.moveToNext()) {
                return c.getLong(c.getColumnIndex("lastsync"));
            }
            return 0;
        }
    }

    private ContentValues creatContentValue(SyncDataInfo info) {
        ContentValues values = new ContentValues();
        values.put("account_id", info.getAccount_id());
        values.put("role_id", info.getRole_id());
        values.put("start", info.getStart());
        values.put("end", info.getEnd());
        values.put("lastsync", info.getLastsync());
        values.put("mtype", info.getMtype());
        return values;
    }

    private SyncDataInfo getContentValue(Cursor c) {
        SyncDataInfo info = new SyncDataInfo();
        info.setRole_id(c.getInt(0));
        info.setAccount_id(c.getInt(1));
        info.setMtype(c.getString(c.getColumnIndex("mtype")));
        info.setStart(c.getLong(2));
        info.setEnd(c.getLong(3));
        info.setLastsync(c.getLong(4));
        return info;
    }

    public static void main(String[] args) {
        List<SyncDataInfo> syncList = Arrays.asList(
                new SyncDataInfo(132L, 232L, "weight", 100, 60, 80),
                new SyncDataInfo(132L, 232L, "weight", 89, 50, 100),
                new SyncDataInfo(132L, 232L, "weight", 92, 30, 70),
                new SyncDataInfo(132L, 232L, "weight", 32, 10, 20),
                new SyncDataInfo(132L, 232L, "weight", 120, 90, 150),
                new SyncDataInfo(132L, 232L, "weight", 180, 5, 9),
                new SyncDataInfo(132L, 232L, "weight", 130, 200, 210)
        );

        System.out.println(syncList);
        List<SyncDataInfo> mergedList = mergeSyncDataInfo(syncList);
        System.out.println("MERGED INTO");
        System.out.println(mergedList);
    }
}

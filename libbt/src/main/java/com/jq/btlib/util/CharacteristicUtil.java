package com.jq.btlib.util;

import com.jq.btlib.model.LinkedListQueue;
import com.jq.btlib.model.TaskData;

/**
 * Created by Administrator on 2016/11/23.
 */

public class CharacteristicUtil {
    private static final String TAG = "CsBtUtil_v11";
    private LinkedListQueue<TaskData> mTaskQueue=new LinkedListQueue<TaskData>();
    private boolean doing=false; //任务完成
    private CsBtUtil_v11 mCsBtUtil;

    public CharacteristicUtil(CsBtUtil_v11 btUtil_v11){
        mCsBtUtil=btUtil_v11;
    }


    public void clearTask(){
        doing=false;
        mTaskQueue.clear();
    }

    public int size(){
        return mTaskQueue.size();
    }


    public void addTask(TaskData task){
        mTaskQueue.put(task);
    }

    public void doTask(){
        doTask(true);
    }


    private void doTask(Boolean isAdd){
        if(mTaskQueue.size()==0){
            doing=false;
            return;
        }
        LogUtil.i(TAG,"queue size:" + mTaskQueue.size());

        if(doing & isAdd){return;}

        doing=true;
        final TaskData data=mTaskQueue.get();
        if(data.TaskType== TaskData.TASK_TYPE.Read){

        }else if(data.TaskType== TaskData.TASK_TYPE.Write){
            mCsBtUtil.writeCharacteristic(data, new CsBtUtil_v11.AsynBLETaskCallback() {
                @Override
                public void success(Object value) {
                    doNext();
                }

                @Override
                public void failed() {
                    doNext();
                }
            });
        }else if(data.TaskType== TaskData.TASK_TYPE.EnableNodification){
            mCsBtUtil.writeDescriptor(data.Key, new CsBtUtil_v11.AsynBLETaskCallback() {
                @Override
                public void success(Object value) {
                    doNext();
                }

                @Override
                public void failed() {
                    doNext();
                }
            });
        }

    }

    private void doNext(){
        if(mTaskQueue.size()==0){
            doing=false;
            return;
        }
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        doTask(false);
    }
}

package com.jq.btlib.model;

import com.jq.btlib.util.CsBtUtil_v11;

/**
 * Created by Administrator on 2016/11/23.
 */

public class TaskData {
    public enum TASK_TYPE{
        Read,Write,EnableNodification,
    }

    public CsBtUtil_v11.Synchronization_Task_Key Key;
    public byte[] Buffer;
    public TASK_TYPE TaskType;

    public TaskData(CsBtUtil_v11.Synchronization_Task_Key readKey){
        Key=readKey;
        TaskType=TASK_TYPE.Read;
    }

    public TaskData(CsBtUtil_v11.Synchronization_Task_Key notifyKey,boolean enableNotification){
        Key=notifyKey;
        TaskType=TASK_TYPE.EnableNodification;
    }

    public TaskData(CsBtUtil_v11.Synchronization_Task_Key writeKey,byte[] buffer){
        Key=writeKey;
        Buffer=buffer;
        TaskType=TASK_TYPE.Write;
    }
}

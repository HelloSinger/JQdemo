package com.jq.code.code.listener;


import com.jq.code.model.RoleInfo;

/**
 * Created by xulj on 2016/6/2.
 */
public interface TendParserListener {
    Object parserJson(RoleInfo roleInfo, Object data) ;
}

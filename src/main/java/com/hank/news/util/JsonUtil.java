package com.hank.news.util;

import com.alibaba.fastjson.JSON;
import com.google.gson.GsonBuilder;

/**
 * @author lxt
 * @create 2020-11-05 10:09
 */
public  class JsonUtil {

    static GsonBuilder gb=new GsonBuilder();

    static {
        /**
         * 不需要html标签转义
         */
        gb.disableHtmlEscaping();
    }

    public static String parseToJson(Object o){
        return gb.create().toJson(o);
    }

    public static <T> T parseToObject(String json, Class<T> c){
        T o = JSON.parseObject(json, c);
        return o;
    }

}

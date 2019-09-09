package com.stormkid.okhttpkt.java;

/**
 * @author ke_li
 * @date 2019/9/9
 */
public interface CallbackRule<T> {
    /**
     * 成功请求
     *
     * @entity 请求返回的json实体
     * @flag 同一个页面设置不同请求
     */
    void onSuccess(T t, String flag);

    /**
     * 请求失败
     */
    void onFailed(String error);
}

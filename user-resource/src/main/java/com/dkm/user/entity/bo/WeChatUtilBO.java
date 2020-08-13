package com.dkm.user.entity.bo;

import lombok.Data;

/**
 * @Author: HuangJie
 * @Date: 2020/3/24 9:23
 * @Version: 1.0V
 */
@Data
public class WeChatUtilBO {
    /**
     * 用户微信openid
     */
    private String weChatOpenId;
    /**
     * 用户微信昵称
     */
    private String weChatNickName;
    /**
     * 用户微信性别
     */
    private String weChatSex;
    /**
     * 用户微信所在省份
     */
    private String weChatProvince;
    /**
     * 用户微信所在市
     */
    private String weChatCity;
    /**
     * 用户微信所在国家
     */
    private String weChatCountry;
    /**
     * 用户微信头像地址
     */
    private String weChatHeadImgUrl;
    /**
     * 用户微信特权信息-JSON数组
     */
    private String weChatPrivilege;
    /**
     * 用户微信统一标识，针对一个微信开发平台账号下的应用
     */
    private String weChatUnionId;
}

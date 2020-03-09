package com.zuofei.mybatis.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;


@Data
@Accessors(fluent = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThOpCmd {
    /**
     * 事件码
     */
    private String cmd;

    /**
     * 事件说明
     */
    private String msg;

    /**
     * 请求事件码
     */
    private String req;

    /**
     * 业务失败提示信息
     */
    private String info;

    /**
     * 会话编号（房间号）
     */
    private String rid;

    /**
     * 客户编号
     */
    private String uid;

    /**
     * 邀请人号
     */
    private String iid;

    private String usertype;//用户类型0-客户 1-坐席

    /**
     * 错误码
     */
    private String error;

    /**
     * 推流地址
     */
    private String pushurl;

    /**
     * 连接编号(连接在内存中的地址)
     */
    private String opsession;

    /**
     * 操作人（连接人）
     */
    private String opuid;

    /**
     * 操作时间
     */
    private Date optime;

    /**
     * 业务线
     */
    private String opcallto;

    /**
     * 随路消息 (JSON格式)
     */
    private String data;

    /**
     * 被邀请人号列表
     */
    private String uids;

    /**
     * 拉流地址
     */
    private String pullurls;
}

package com.zuofei.mybatis.vo;

import com.zuofei.mybatis.utils.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * Websocket消息体
 * @author 胡锐锋
 * 2018年12月24日
 * 上海
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("all")
public class CMDVO<T>{
	private String cmd = "0";//事件码
	private String msg = "成功";//事件说明
	private String req = "";//请求命令
	private String info = "";//业务失败提示信息
	private T data ;//随路消息 (JSON格式)
	private String rid ;//会话编号
	private String uid ;//客户编号
	private String iid ;//邀请人号
	private String tid ;//rtc视频房间号
	private List<String> uids ;//客户编号列表
	private String error ;//错误码
	private String pushurl ;//rtmp推流地址 
	private List<String> pullurls ;//rtmp拉流地址
//	private List<UserVO> users ;//客户列表
	
	private String from ;//IM消息，来自某人的消息
	private String to ;//IM消息，发给某人的消息
	private Long did; //IM消息编号
	private Integer dtype; //IM消息类型（0:文本消息；1:JSON类型消息；2:图片；3:语音; 4:视频）
	
	@Override
	public String toString() {
		return JSONUtil.toJson(this);
	}
}
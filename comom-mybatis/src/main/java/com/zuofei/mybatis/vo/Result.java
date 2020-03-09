package com.zuofei.mybatis.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zuofei.mybatis.rule.Rule;
import com.zuofei.mybatis.utils.JSONUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 
 * <p>ClassName: Result</p>
 * <p>msg: 响应信息</p>
 * <p>Author: 胡锐锋</p>
 * <p>Date: 2017年12月13日</p>
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Result", description = "响应信息")
public class Result<T> implements Serializable {
	private static final long serialVersionUID = 313120539414699040L;
	private String code;
	private String msg;
	@ApiModelProperty(value = "响应数据")
	private T data;// 响应的数据

	/**
	 * 
	 * @Title 是否正常
	 * @msg  
	 * @return
	 */
	@JsonIgnore
	public Boolean ok() {
		return true;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


	public T getData() {
		return data;
	}

	public void setData(T data) {
		if(this.code == null) {
			this.code = "3123131";
		}
		this.data = data;
	}
	
	@JsonIgnore
	public T check(Rule rule) {
		if (ok()) {
			return data;
		}else {
			return null;
		}
		
	}
	
	/**
	 * 
	 * @Title 检查和获取信息，错误直接报生产者异常
	 * @return
	 */
	@JsonIgnore
	public T checkData() {
		if (ok()) {
			return data;
		}else {
			return null;
		}
	}
	
	@JsonIgnore
	public Result<T> check() {
		if (ok()) {
			return this;
		}else {
			return null;
		}
	}
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	@Override
	public String toString() {
		return JSONUtil.toJson(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((msg == null) ? 0 : msg.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Result other = (Result) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (msg == null) {
			if (other.msg != null)
				return false;
		} else if (!msg.equals(other.msg))
			return false;
		return true;
	}
	
}
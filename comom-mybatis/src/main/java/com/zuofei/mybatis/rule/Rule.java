package com.zuofei.mybatis.rule;

import java.io.Serializable;

/**
 * 业务规则父类接口
 * <p>ClassName: Rule</p>
 * <p>Description: 定义业务规则</p>
 * <p>Author: liuyunlong</p>
 * <p>Date: 2017年11月2日</p>
 */
public interface Rule extends Serializable {

    /**
     * <p>Description: 规则编码</p>
     * @return 规则编码
     */
	String code(); 

	/**
	 * <p>Description: 规则描述</p>
	 * @return 规则描述
	 */
	String brief();

	/**
	 * 
	 * @Title 错误码和错误信息
	 * @Description  
	 * @return
	 */
	default String codeMsg() {
		return code()+" "+brief();
	}
}
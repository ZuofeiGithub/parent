package com.zuofei.mybatis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>ClassName: PageResult</p>
 * <p>Description: 分页结果</p>
 * <p>Author: 胡锐锋</p>
 * <p>Date: 2017年11月27日</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="PageResult",description="分页结果")
public class PageResult<T> implements Serializable {
	@ApiModelProperty(value="总条数",example="1000")
	private long count;// 总条数total
	private String code;
	private String msg;
	@ApiModelProperty(value = "响应数据")
	private T data;// 响应的数据
}

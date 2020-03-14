package com.zuofei.mybatis.utils;


import org.apache.commons.lang3.StringUtils;

public class StrUtil {

	private StrUtil() {}

	/**
	 * StringUtils.isNotBlank(" null ") = false StringUtils.isNotBlank("null") =
	 * false StringUtils.isNotBlank(null) = false StringUtils.isNotBlank("") = false
	 * StringUtils.isNotBlank(" ") = false StringUtils.isNotBlank("bob") = true
	 * StringUtils.isNotBlank(" bob ") = true
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotBlank(String str) {
		return StringUtils.isNotBlank(str)
				&& !"null".equalsIgnoreCase(str.trim())
				&&!"{}".equalsIgnoreCase(str.trim());
	}
	
	public static boolean isBlank(String str) {
		return !isNotBlank(str);
	}

	/**
	 * 有空的值
	 * @param values
	 * @return
	 */
	public static boolean isAnyBlank(String... values) {
		boolean isAnyBlank = false;
		for (String val : values) {
			if(isBlank(val)) {
				isAnyBlank = true;
				break;
			}
		}
		return isAnyBlank;
	}
	
	/**
	 * 没有空
	 * @param values
	 * @return
	 */
	public static boolean isNotExistsBlank(String... values) {
		return !isAnyBlank(values);
	}
}

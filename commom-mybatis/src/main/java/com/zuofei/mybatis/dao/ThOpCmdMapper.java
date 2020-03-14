package com.zuofei.mybatis.dao;

import com.zuofei.mybatis.entity.ThOpCmd;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public interface ThOpCmdMapper extends Mapper<ThOpCmd> {

	/**
	 * 根据路由点查询所有坐席的data数据
	 * @param tableName
	 * @param callTo
	 * @param cmd
	 * @return
	 */
	@Select("SELECT t.data FROM ${tableName} t WHERE  t.cmd = #{cmd} and t.opcallto=#{callTo}")
	public List<String> queryDataListByCmd(@Param("tableName") String tableName, @Param("callTo") String callTo, @Param("cmd") Integer cmd);


	/**
	 * 查询所有坐席呼叫的用户
	 * @param tableName
	 * @param callTo
	 * @return
	 */
	@Select("SELECT *  FROM ${tableName} t WHERE  t.cmd = 1000 AND t.opcallto=#{callTo}")
	List<ThOpCmd> queryAllCallList(@Param("tableName") String tableName, @Param("callTo") String callTo);

	@Select("SELECT *  FROM ${tableName} t WHERE  t.cmd = 1000 AND t.opcallto=#{callTo} AND t.opuid = #{agentId}")
	List<ThOpCmd> queryCallListByAgentId(@Param("tableName") String tableName, @Param("callTo") String callTo, @Param("agentId") String agentId);



	/**
	 * 查询所有坐席呼叫的用户
	 * @param tableName
	 * @param callTo
	 * @return
	 */
	@Select("SELECT *  FROM ${tableName} t WHERE  t.cmd = 1000 AND t.opcallto=#{callTo} and t.opuid = #{agengId}")
	List<ThOpCmd> queryAgentCallList(@Param("tableName") String tableName, @Param("callTo") String callTo, @Param("agentId") String agengId);

	/**
	 * 查询所有坐席应答的用户
	 * @param tableName
	 * @param callTo
	 * @return
	 */
	@Select("SELECT * FROM ${tableName} t WHERE t.cmd = 1001 AND t.opcallto=#{callTo} AND usertype=2")
	List<ThOpCmd> queryAgentAnswerList(@Param("tableName") String tableName, @Param("callTo") String callTo);



	/**
	 * 查询所有挂断的用户
	 * @param tableName
	 * @param callTo
	 * @return
	 */
	@Select("SELECT * FROM ${tableName} t WHERE t.cmd = 1002 AND t.opcallto=#{callTo}")
	List<ThOpCmd> queryAllCancelCall(@Param("tableName") String tableName, @Param("callTo") String callTo);


	@Select("SELECT * FROM ${tableName} t WHERE t.cmd = 1002 AND t.opcallto=#{callTo} AND t.rid = #{rid}")
	ThOpCmd queryAgentCancelCall(@Param("tableName") String tableName, @Param("callTo") String callTo, @Param("rid") String rid);

	@Select("SELECT t.data FROM ${tableName} t WHERE  t.cmd = #{cmd} AND t.opcallto=#{callTo} AND t.usertype = 2 AND t.opuid=#{agentId}")
	List<String> queryDataListByCmdAndAgentId(@Param("tableName") String tableName, @Param("cmd") Integer cmd, @Param("callTo") String callTo, @Param("agentId") String agentId);


	/**
	 * 查询指定坐席的应答量
	 * @param tableName
	 * @param callTo
	 * @param agentId
	 * @return
	 */
	@Select("SELECT * FROM ${tableName} t WHERE t.cmd = 1001 AND t.opcallto=#{callTo} AND t.opuid =#{agentId} AND usertype=2")
	List<ThOpCmd> queryAgentAnswerListByAgent(@Param("tableName") String tableName, @Param("callTo") String callTo, @Param("agentId") String agentId);

	@Select("SELECT *  FROM `${tableName}` t WHERE  t.cmd = 1000 AND t.opcallto=#{callTo}")
	List<ThOpCmd> queryAllCallListByAgentId(@Param("tableName") String tableName, @Param("callTo") String callTo);
}
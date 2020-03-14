package com.zuofei.mybatis.service;

import com.alibaba.fastjson.JSONObject;
import com.zuofei.mybatis.dao.ThOpCmdMapper;
import com.zuofei.mybatis.entity.ThOpCmd;
import com.zuofei.mybatis.utils.JSONUtil;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zuofei.mybatis.utils.DateUtil;
import com.zuofei.mybatis.utils.ObjectUtil;

import java.util.*;


@Service
public class ThOpCmdService {
    @Autowired
    private ThOpCmdMapper thOpCmdMapper;

    /**
     * 通过cmd查询某个表中全部坐席的data字段列表
     *
     * @param tableName
     * @param cmd
     * @return
     */
    public List<String> queryDataListByCmd(String tableName, String callTo, Integer cmd) {
        return thOpCmdMapper.queryDataListByCmd(tableName, callTo, cmd);
    }

    /**
     * 统计当前表中的总呼叫量
     *
     * @param tableName
     * @return
     */
    public int queryTotalCalls(String tableName, String callTo) {
        List<String> dataList = queryDataListByCmd(tableName, callTo, 1000);

        List<String> countList = new ArrayList<>();
        for (String dataJson : dataList) {
            JSONObject dataObject = JSONObject.parseObject(dataJson);

            String callBy = dataObject.getString("callBy");
            if (callBy == null) {
                countList.add(dataJson);
            }
        }
        return dataList.size();
    }

    /**
     * 总进线量
     *
     * @param tableName
     * @return
     */
    public long queryTotalIntoLine(String tableName, String callTo, String agentId) {
        List<String> dataList = new ArrayList<>();
        if (agentId == null || agentId.isEmpty()) {
            dataList = queryDataListByCmd(tableName, callTo, 1001);
        } else {
            dataList = queryDataListByCmdAndAgent(tableName, callTo, 1001, agentId);
        }
        return dataList.size();
    }

    /**
     * 根据坐席号和路由号查询指定cmd的data数据
     *
     * @param tableName
     * @param callTo
     * @param cmd
     * @param agentId
     * @return
     */
    private List<String> queryDataListByCmdAndAgent(String tableName, String callTo, Integer cmd, String agentId) {
        return thOpCmdMapper.queryDataListByCmdAndAgentId(tableName, cmd, callTo, agentId);
    }

    /**
     * 查询总呼叫的用户
     *
     * @param thOpCmdtableName
     * @param callto
     * @return
     */
    public List<ThOpCmd> queryALlCallList(String thOpCmdtableName, String callto, String agentId) {
        List<ThOpCmd> thOpCmds = new ArrayList<ThOpCmd>();
        if (ObjectUtil.isBlank(agentId)) {
            thOpCmds = thOpCmdMapper.queryAllCallList(thOpCmdtableName, callto);
        } else {
            thOpCmds = thOpCmdMapper.queryCallListByAgentId(thOpCmdtableName, callto, agentId);
        }
        return thOpCmds;
    }

    private List<ThOpCmd> queryALlCallListByAgentId(String thOpCmdtableName, String callTo, String agentId) {
        List<ThOpCmd> opCmdList = thOpCmdMapper.queryAllCallListByAgentId(thOpCmdtableName, callTo);
        if(ObjectUtil.isBlank(agentId)){
            return opCmdList;
        }else{
            List<ThOpCmd> tempList = new ArrayList<>();
            if (opCmdList.size() > 0) {
                for (ThOpCmd thOpCmd : opCmdList) {
                    String tempAgentId = (String) JSONUtil.toMap(thOpCmd.data()).get("agentId");
                    if (tempAgentId.equals(agentId)) {
                        tempList.add(thOpCmd);
                    }
                }
            }
            return tempList;
        }
    }

    /**
     * 查询某个坐席总呼叫所有用户
     *
     * @param thOpCmdtableName
     * @param callto
     * @return
     */
    public List<ThOpCmd> queryAgentCallList(String thOpCmdtableName, String callto, String agentId) {
        List<ThOpCmd> thOpCmds = thOpCmdMapper.queryAgentCallList(thOpCmdtableName, callto, agentId);
        return thOpCmds;
    }

    /**
     * 总的应答客户
     *
     * @param thOpCmdtableName
     * @param callto
     * @return
     */
    public List<ThOpCmd> queryAgentAnswerList(String thOpCmdtableName, String callto, String agentId) {
        List<ThOpCmd> thOpCmds = new ArrayList<>();
        if (ObjectUtil.isNotBlank(agentId)) {
            thOpCmds = thOpCmdMapper.queryAgentAnswerListByAgent(thOpCmdtableName, callto, agentId);
        } else {
            thOpCmds = thOpCmdMapper.queryAgentAnswerList(thOpCmdtableName, callto);
        }
        return thOpCmds;
    }


    /**
     * 查询所有挂断的用户
     */
    public List<ThOpCmd> queryAllCancelList(String thOpCmdtableName, String callto, String agentId) {
        if (ObjectUtil.isBlank(agentId)) {
            return thOpCmdMapper.queryAllCancelCall(thOpCmdtableName, callto);
        } else {
            List<ThOpCmd> cmdList = queryAgentAnswerList(thOpCmdtableName, callto, agentId);
            List<ThOpCmd> tempList = new ArrayList<>();
            for (ThOpCmd opCmd : cmdList) {
                ThOpCmd tempCmd = thOpCmdMapper.queryAgentCancelCall(thOpCmdtableName, callto, opCmd.rid());
                tempList.add(tempCmd);
            }
            return tempList;
        }
    }

    /**
     * 总的通话时间
     *
     * @param thOpCmdtableName
     * @param callto
     * @param agentId
     * @return
     */
    public Long queryTotalLinkTime(String thOpCmdtableName, String callto, String agentId) {
        List<ThOpCmd> answerList = queryAgentAnswerList(thOpCmdtableName, callto, agentId);
        List<ThOpCmd> cancelList = queryAllCancelList(thOpCmdtableName, callto, agentId);
        Long time = 0L;
        for (ThOpCmd answer : answerList) {
            for (ThOpCmd cancel : cancelList) {
                if (answer.rid().equals(cancel.rid())) {
                    Long temptime = DateUtil.getDiffSeconds(answer.optime(), cancel.optime());
                    time += temptime;
                }
            }
        }
        return time;
    }

    /**
     * 总通话量
     *
     * @param thOpCmdtableName
     * @param callto
     * @param agentId
     * @return
     */
    public long queryTotalLinkCount(String thOpCmdtableName, String callto, String agentId) {
        List<ThOpCmd> answerList = queryAgentAnswerList(thOpCmdtableName, callto, agentId);
        List<ThOpCmd> cancelList = queryAllCancelList(thOpCmdtableName, callto, agentId);
        List<ThOpCmd> coutArray = new ArrayList<>();
        for (ThOpCmd answer : answerList) {
            for (ThOpCmd cancel : cancelList) {
                if (answer.rid().equals(cancel.rid())) {
                    coutArray.add(answer);
                }
            }
        }
        return coutArray.size();
    }

    /**
     * 总应答量
     *
     * @param thOpCmdtableName
     * @param callto
     * @return
     */
    public long queryTotalResponse(String thOpCmdtableName, String callto) {
        List<String> dataList = queryDataListByCmd(thOpCmdtableName, callto, 1001);
        return dataList.size();
    }

    /**
     * 对应坐席的总应答量
     *
     * @param thOpCmdtableName
     * @param callto
     * @return
     */
    public long queryTotalResponseByAgent(String thOpCmdtableName, String callto, String agent) {
        List<ThOpCmd> dataList = queryAgentAnswerList(thOpCmdtableName, callto, agent);
        return dataList.size();
    }

    /**
     * 10秒内的接通量
     *
     * @param thOpCmdtableName
     * @param callTo
     * @return
     */
    public long queryResponse10(String thOpCmdtableName, String callTo, String agentId) {
        List<ThOpCmd> agentCallList = queryALlCallList(thOpCmdtableName, callTo, agentId);
        List<ThOpCmd> agentResponseList = queryAgentAnswerList(thOpCmdtableName, callTo, agentId);
        List<ThOpCmd> response10Array = new ArrayList<>();
        for (ThOpCmd agentCall : agentCallList) {
            for (ThOpCmd agentResponse : agentResponseList) {
                if (agentCall.rid().equals(agentResponse.rid())) {
                    Long waitTime = DateUtil.getDiffSeconds(agentResponse.optime(), agentCall.optime());
                    if (waitTime <= 10) {
                        response10Array.add(agentCall);
                    }
                }
            }
        }
        return response10Array.size();
    }

    /**
     * 查询某个坐席10秒内的接通量
     *
     * @param thOpCmdtableName
     * @param callTo
     * @param agentId
     * @return
     */
    public long queryReponse10ByAgent(String thOpCmdtableName, String callTo, String agentId) {
        List<ThOpCmd> agentCallList = queryALlCallListByAgentId(thOpCmdtableName, callTo, agentId);
        List<ThOpCmd> agentResponseList = queryAgentAnswerList(thOpCmdtableName, callTo, agentId);
        List<ThOpCmd> response10Array = new ArrayList<>();
        for (ThOpCmd agentCall : agentCallList) {
            for (ThOpCmd agentResponse : agentResponseList) {
                if (agentCall.rid().equals(agentResponse.rid())) {
                    Long waitTime = DateUtil.getDiffSeconds(agentResponse.optime(), agentCall.optime());
                    if (waitTime <= 10) {
                        if (agentCall != null && agentCall.opuid().equals(agentId)) {
                            response10Array.add(agentCall);
                        }
                        response10Array.add(agentCall);
                    }
                }
            }
        }
        return response10Array.size();
    }


    /**
     * 20秒内的接通
     *
     * @param thOpCmdtableName
     * @param callTo
     * @return
     */
    public long queryResponse20(String thOpCmdtableName, String callTo, String agentId) {
        List<ThOpCmd> agentCallList = queryALlCallList(thOpCmdtableName, callTo, agentId);
        List<ThOpCmd> agentResponseList = queryAgentAnswerList(thOpCmdtableName, callTo, agentId);
        List<ThOpCmd> response10Array = new ArrayList<>();
        for (ThOpCmd agentCall : agentCallList) {
            for (ThOpCmd agentResponse : agentResponseList) {
                if (agentCall.rid().equals(agentResponse.rid())) {
                    Long waitTime = DateUtil.getDiffSeconds(agentResponse.optime(), agentCall.optime());
                    if (waitTime <= 20) {
                        if (agentCall != null && agentCall.opuid().equals(agentId)) {
                            response10Array.add(agentCall);
                        }
                        response10Array.add(agentCall);
                    }
                }
            }
        }
        return response10Array.size();
    }


    /**
     * 某个坐席20秒内的接通量
     *
     * @param thOpCmdtableName
     * @param callTo
     * @param agentId
     * @return
     */
    public long queryReponse20ByAgent(String thOpCmdtableName, String callTo, String agentId) {
        List<ThOpCmd> agentCallList = queryALlCallListByAgentId(thOpCmdtableName, callTo, agentId);
        List<ThOpCmd> agentResponseList = queryAgentAnswerList(thOpCmdtableName, callTo, agentId);
        List<ThOpCmd> response10Array = new ArrayList<>();
        for (ThOpCmd agentCall : agentCallList) {
            for (ThOpCmd agentResponse : agentResponseList) {
                if (agentCall.rid().equals(agentResponse.rid())) {
                    Long waitTime = DateUtil.getDiffSeconds(agentResponse.optime(), agentCall.optime());
                    if (waitTime <= 20) {
                        if (agentCall != null && agentCall.opuid().equals(agentId)) {
                            response10Array.add(agentCall);
                        }
                        response10Array.add(agentCall);
                    }
                }
            }
        }
        return response10Array.size();
    }

    /**
     * 30秒秒内的接通
     *
     * @param thOpCmdtableName
     * @param callTo
     * @return
     */
    public long queryResponse30(String thOpCmdtableName, String callTo, String agentId) {
        List<ThOpCmd> agentCallList = queryALlCallListByAgentId(thOpCmdtableName, callTo, agentId);
        List<ThOpCmd> agentResponseList = queryAgentAnswerList(thOpCmdtableName, callTo, agentId);
        List<ThOpCmd> response10Array = new ArrayList<>();
        for (ThOpCmd agentCall : agentCallList) {
            for (ThOpCmd agentResponse : agentResponseList) {
                if (agentCall.rid().equals(agentResponse.rid())) {
                    Long waitTime = DateUtil.getDiffSeconds(agentResponse.optime(), agentCall.optime());
                    if (waitTime <= 30) {
                        if (agentCall != null && agentCall.opuid().equals(agentId)) {
                            response10Array.add(agentCall);
                        }
                        response10Array.add(agentCall);
                    }
                }
            }
        }
        return response10Array.size();
    }

    /**
     * 某个坐席30秒内的接通量
     *
     * @param thOpCmdtableName
     * @param callTo
     * @param agentId
     * @return
     */
    public long queryReponse30ByAgent(String thOpCmdtableName, String callTo, String agentId) {
        List<ThOpCmd> agentCallList = queryALlCallList(thOpCmdtableName, callTo, agentId);
        List<ThOpCmd> agentResponseList = queryAgentAnswerList(thOpCmdtableName, callTo, agentId);
        List<ThOpCmd> response10Array = new ArrayList<>();
        for (ThOpCmd agentCall : agentCallList) {
            for (ThOpCmd agentResponse : agentResponseList) {
                if (agentCall.rid().equals(agentResponse.rid())) {
                    Long waitTime = DateUtil.getDiffSeconds(agentResponse.optime(), agentCall.optime());
                    if (waitTime <= 30 && agentCall.opuid().equals(agentId)) {
                        response10Array.add(agentCall);
                    }
                }
            }
        }
        return response10Array.size();
    }

    /**
     * 总的接通等待时长
     *
     * @param thOpCmdtableName
     * @param callTo
     * @return
     */
    public long queryTotalWaitTime(String thOpCmdtableName, String callTo, String agentId) {
        List<ThOpCmd> agentCallList = queryALlCallListByAgentId(thOpCmdtableName, callTo, agentId);
        List<ThOpCmd> agentResponseList = queryAgentAnswerList(thOpCmdtableName, callTo, agentId);
        Long totalWaitTime = 0L;
        for (ThOpCmd agentCall : agentCallList) {
            for (ThOpCmd agentResponse : agentResponseList) {
                if (agentCall.rid().equals(agentResponse.rid())) {
                    Long waitTime = DateUtil.getDiffSeconds(agentResponse.optime(), agentCall.optime());
                    totalWaitTime += waitTime;
                }
            }
        }
        return totalWaitTime;
    }

    /**
     * 查询等待用户量
     *
     * @param thOpCmdtableName
     * @param callTo
     * @param agentId
     * @return
     */
    public long queryTotalWaitCount(String thOpCmdtableName, String callTo, String agentId) {
        List<ThOpCmd> agentCallList = queryALlCallListByAgentId(thOpCmdtableName, callTo, agentId);
        List<ThOpCmd> agentResponseList = queryAgentAnswerList(thOpCmdtableName, callTo, agentId);
        List<ThOpCmd> countArray = new ArrayList<>();
        for (ThOpCmd agentCall : agentCallList) {
            for (ThOpCmd agentResponse : agentResponseList) {
                if (agentCall.rid().equals(agentResponse.rid())) {
                    countArray.add(agentCall);
                }
            }
        }
        return countArray.size();
    }

    /**
     * 查询所有未接通用户
     */
    List<ThOpCmd> queryNoThrough(String tableName, String callTo, String agentId) {
        List<ThOpCmd> agentCallList = queryALlCallList(tableName, callTo, agentId);
        List<ThOpCmd> agentAnswerList = queryAgentAnswerList(tableName, callTo, agentId);
        List<ThOpCmd> noThroughList = new ArrayList<>();
        for (ThOpCmd agent : agentCallList) {
            for (ThOpCmd answer : agentAnswerList) {
                if (agent.rid().equals(answer.rid())) {
                    noThroughList.add(agent);
                }
            }
        }
        noThroughList = ListUtils.subtract(agentCallList, noThroughList);
        return noThroughList;
    }

    /**
     * 5秒未接听挂断的用户数量
     *
     * @param thOpCmdtableName
     * @param callto
     * @return
     */
    public long queryTotalCallsLossed5(String thOpCmdtableName, String callto, String agentId) {
        List<ThOpCmd> agentCallList = queryALlCallList(thOpCmdtableName, callto, agentId);
        List<ThOpCmd> noThroughList = queryNoThrough(thOpCmdtableName, callto, agentId);
        List<ThOpCmd> cancelCallList = queryAllCancelList(thOpCmdtableName, callto, agentId);

        List<ThOpCmd> tempList = new ArrayList<>();
        for (ThOpCmd cancel : cancelCallList) {
            for (ThOpCmd nothrough : noThroughList) {
                if (cancel.rid().equals(nothrough.rid())) {
                    tempList.add(cancel);
                }
            }
        }
        List<ThOpCmd> coutArray = new ArrayList<>();
        for (ThOpCmd thOpCmd : agentCallList) {
            for (ThOpCmd agentOpcmd : tempList) {
                if (agentOpcmd.rid().equals(thOpCmd.rid())) {
                    Long time = DateUtil.getDiffSeconds(agentOpcmd.optime(), thOpCmd.optime());
                    if (time < 5) {
                        coutArray.add(thOpCmd);
                    }
                }
            }
        }
        return coutArray.size();
    }

    /**
     * 5秒未接听挂断的用户数量
     *
     * @param thOpCmdtableName
     * @param callto
     * @return
     */
    public long queryTotalCallsLossed(String thOpCmdtableName, String callto, String agentId) {
        List<ThOpCmd> agentCallList = queryALlCallList(thOpCmdtableName, callto, agentId);
        List<ThOpCmd> noThroughList = queryNoThrough(thOpCmdtableName, callto, agentId);
        List<ThOpCmd> cancelCallList = queryAllCancelList(thOpCmdtableName, callto, agentId);
        for (ThOpCmd noThrough : noThroughList) {
            if (cancelCallList.contains(noThrough)) {
                cancelCallList.remove(noThrough);
            }
        }
        List<ThOpCmd> coutArray = new ArrayList<>();
        for (ThOpCmd thOpCmd : cancelCallList) {
            for (ThOpCmd agentOpcmd : agentCallList) {
                if (agentOpcmd.rid().equals(thOpCmd.rid())) {
                    coutArray.add(thOpCmd);
                }
            }
        }
        return coutArray.size();
    }

    /**
     * 呼损等待时长
     *
     * @param thOpCmdtableName
     * @param callto
     * @return
     */
    public long queryTotalCallsLossedWaittime(String thOpCmdtableName, String callto, String agent) {
        List<ThOpCmd> agentCallList = queryALlCallList(thOpCmdtableName, callto, agent);
        List<ThOpCmd> noThroughList = queryNoThrough(thOpCmdtableName, callto, agent);
        List<ThOpCmd> cancelCallList = queryAllCancelList(thOpCmdtableName, callto, agent);
        for (ThOpCmd noThrough : noThroughList) {
            if (cancelCallList.contains(noThrough)) {
                cancelCallList.remove(noThrough);
            }
        }
        Long totaltime = 0L;
        for (ThOpCmd thOpCmd : cancelCallList) {
            for (ThOpCmd agentOpcmd : agentCallList) {
                if (agentOpcmd.rid().equals(thOpCmd.rid())) {
                    Long time = DateUtil.getDiffSeconds(agentOpcmd.optime(), thOpCmd.optime());
                    totaltime += time;
                }
            }
        }
        return totaltime;
    }
}

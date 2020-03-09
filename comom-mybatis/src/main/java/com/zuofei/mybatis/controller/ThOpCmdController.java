package com.zuofei.mybatis.controller;


import com.zuofei.mybatis.service.ThOpCmdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ThOpCmdController {

    @Autowired
    private ThOpCmdService thOpCmdService;


    @GetMapping("test")
    public Long test(){
        return thOpCmdService.queryTotalWaitTime("th_op_cmd20200309","32650611",null);
    }
}

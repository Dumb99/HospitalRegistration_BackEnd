package com.dcq.yygh.msm.service;

import com.dcq.yygh.vo.msm.MsmVo;

public interface MsmService {
    boolean send(String phone, String code);

    //mq使用的发送邮件接口
    boolean send(MsmVo msmVo);
}

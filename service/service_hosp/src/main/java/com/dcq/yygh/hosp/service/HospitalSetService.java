package com.dcq.yygh.hosp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dcq.yygh.model.hosp.HospitalSet;
import com.dcq.yygh.vo.order.SignInfoVo;

public interface HospitalSetService extends IService<HospitalSet> {
    String getSignKey(String hoscode);

    SignInfoVo getSignInfoVo(String hoscode);
}

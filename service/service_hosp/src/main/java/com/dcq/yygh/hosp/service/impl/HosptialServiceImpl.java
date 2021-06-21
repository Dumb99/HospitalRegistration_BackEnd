package com.dcq.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dcq.yygh.hosp.repository.HospitalRepository;
import com.dcq.yygh.hosp.service.HosptialService;
import com.dcq.yygh.model.hosp.Hospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class HosptialServiceImpl implements HosptialService {
    @Autowired
    private HospitalRepository hospitalRepository;

    @Override
    public void save(Map<String, Object> map) {
        //将 map 转换成 hosptial 对象
        String mapString = JSONObject.toJSONString(map);
        Hospital hospital = JSONObject.parseObject(mapString, Hospital.class);

        //判断是否存在
        String hoscode = hospital.getHoscode();
        Hospital hosptialExist = hospitalRepository.getHosptialByHoscode(hoscode);

        //存在，更新
        if(hosptialExist != null){
            hospital.setStatus(hosptialExist.getStatus());
            hospital.setCreateTime(hosptialExist.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);

        }
        //不存在，添加
        else{
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }
    }

    @Override
    public Hospital getByHoscode(String hoscode) {
        Hospital hospital = hospitalRepository.getHosptialByHoscode(hoscode);
        return hospital;
    }
}

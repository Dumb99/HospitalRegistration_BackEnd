package com.dcq.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dcq.yygh.cmn.client.DictFeignClient;
import org.springframework.data.domain.Page;
import com.dcq.yygh.hosp.repository.HospitalRepository;
import com.dcq.yygh.hosp.service.HosptialService;
import com.dcq.yygh.model.hosp.Hospital;
import com.dcq.yygh.vo.hosp.HospitalQueryVo;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HosptialServiceImpl implements HosptialService {
    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private DictFeignClient dictFeignClient;

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

    @Override
    public Page<Hospital> selectHostPage(int page, int limit, HospitalQueryVo hospitalQueryVon) {

        Pageable pageable = PageRequest.of(page-1,limit);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher
                        .CONTAINING).withIgnoreCase(true);
        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVon,hospital);

        Example<Hospital> example = Example.of(hospital,matcher);
        Page<Hospital> pages = hospitalRepository.findAll(example, pageable);

        pages.getContent().stream().forEach(item -> {
            this.setHospitalHosType(item);
        });

        return pages;
    }

    @Override
    public void updateStatus(String id, Integer status) {
        Hospital hospital = hospitalRepository.findById(id).get();
        hospital.setStatus(status);
        hospital.setUpdateTime(new Date());
        hospitalRepository.save(hospital);
    }

    @Override
    public Map<String, Object> getHospById(String id) {
        Map<String, Object> result = new HashMap<>();
        Hospital hospital1 = hospitalRepository.findById(id).get();
        Hospital hospital = this.setHospitalHosType(hospital1);
        result.put("hosptial",hospital);
        result.put("bookingRule",hospital.getBookingRule());
        hospital.setBookingRule(null);
        return result;
    }

    @Override
    public String getHospName(String hoscode) {
        Hospital hosptial = hospitalRepository.getHosptialByHoscode(hoscode);
        if(hosptial != null){
            return hosptial.getHosname();
        }
        return null;
    }

    private Hospital setHospitalHosType(Hospital item) {
        //根据dictCode和value获取医院等级名称
        String hostypeString = dictFeignClient.getName("Hostype", item.getHostype());
//        String provinceString = dictFeignClient.getName(item.getProvinceCode());
        String provinceString = dictFeignClient.getName(item.getProvinceCode());
        String cityString = dictFeignClient.getName(item.getCityCode());
        String districtString = dictFeignClient.getName(item.getDistrictCode());
        item.getParam().put("fullAddress",provinceString+cityString+districtString);
        item.getParam().put("hostypeString",hostypeString);
        return item;
    }
}

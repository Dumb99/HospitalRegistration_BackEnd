package com.dcq.yygh.hosp.service;

import org.springframework.data.domain.Page;
import com.dcq.yygh.model.hosp.Hospital;
import com.dcq.yygh.vo.hosp.HospitalQueryVo;

import java.util.Map;

public interface HosptialService {
    void save(Map<String, Object> map);

    Hospital getByHoscode(String hoscode);

    Page<Hospital> selectHostPage(int page, int limit, HospitalQueryVo hospitalQueryVon);

    void updateStatus(String id, Integer status);

    Map<String, Object> getHospById(String id);

    String getHospName(String hoscode);
}

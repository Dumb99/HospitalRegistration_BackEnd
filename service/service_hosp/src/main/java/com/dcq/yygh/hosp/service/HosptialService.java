package com.dcq.yygh.hosp.service;

import com.dcq.yygh.model.hosp.Hospital;

import java.util.Map;

public interface HosptialService {
    void save(Map<String, Object> map);

    Hospital getByHoscode(String hoscode);
}

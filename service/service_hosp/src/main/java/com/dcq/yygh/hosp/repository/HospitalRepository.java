package com.dcq.yygh.hosp.repository;

import com.dcq.yygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends MongoRepository<Hospital,String> {
    Hospital getHosptialByHoscode(String hoscode);
}

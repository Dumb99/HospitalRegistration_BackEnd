package com.dcq.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dcq.yygh.hosp.repository.DepartmentRepository;
import com.dcq.yygh.hosp.service.DepartmentService;
import com.dcq.yygh.model.hosp.Department;
import com.dcq.yygh.vo.hosp.DepartmentQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.Map;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public void save(Map<String, Object> paramMap) {
        String paramMapString = JSONObject.toJSONString(paramMap);
        Department department = JSONObject.parseObject(paramMapString,Department.class);

        Department departmentExist = departmentRepository.getDepartmentByHoscodeAndDepcode(department.getHoscode(), department.getDepcode());

        if(departmentExist != null){
            departmentExist.setUpdateTime(new Date());
            departmentExist.setIsDeleted(0);
            departmentRepository.save(departmentExist);
        }
        else{
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        }
    }

    @Override
    public Page<Department> findPage(int page, int limit, DepartmentQueryVo departmentQueryVo) {
        Department department = new Department();
        BeanUtils.copyProperties(departmentQueryVo,department);
        department.setIsDeleted(0);

        //创建page对象、example对象
        Pageable pageable = PageRequest.of(page-1,limit);

        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        Example<Department> example = Example.of(department,exampleMatcher);

        Page<Department> all = departmentRepository.findAll(example,pageable);
        return all;
    }

    @Override
    public void remove(String hoscode, String depcode) {
        Department departmentByHoscodeAndDepcode = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if(departmentByHoscodeAndDepcode != null){
            departmentRepository.deleteById(departmentByHoscodeAndDepcode.getId());
        }
    }
}

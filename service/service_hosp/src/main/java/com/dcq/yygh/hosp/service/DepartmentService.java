package com.dcq.yygh.hosp.service;

import com.dcq.yygh.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;
import com.dcq.yygh.model.hosp.Department;
import com.dcq.yygh.vo.hosp.DepartmentQueryVo;

import java.util.List;
import java.util.Map;

public interface DepartmentService {
    void save(Map<String, Object> paramMap);

    Page<Department> findPage(int page, int limit, DepartmentQueryVo departmentQueryVo);

    void remove(String hoscode, String depcode);

    List<DepartmentVo> findDeptTree(String hoscode);

    String getDepName(String hoscode, String depcode);
}

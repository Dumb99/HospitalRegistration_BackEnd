package com.dcq.yygh.hosp.controller;


import com.dcq.yygh.common.result.Result;
import com.dcq.yygh.hosp.service.DepartmentService;
import com.dcq.yygh.vo.hosp.DepartmentVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/hosp/department")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    //根据医院编号查询所有科室
    @ApiOperation(value = "根据医院编号查询所有科室")
    @GetMapping("getDepartment/{hoscode}")
    public Result getDepartment(@PathVariable String hoscode){
        List<DepartmentVo> list = departmentService.findDeptTree(hoscode);
        return  Result.ok(list);
    }

}

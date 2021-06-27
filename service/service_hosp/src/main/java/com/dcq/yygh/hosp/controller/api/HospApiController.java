package com.dcq.yygh.hosp.controller.api;

import com.dcq.yygh.common.result.Result;
import com.dcq.yygh.hosp.service.DepartmentService;
import com.dcq.yygh.hosp.service.HosptialService;
import com.dcq.yygh.model.hosp.Department;
import com.dcq.yygh.model.hosp.Hospital;
import com.dcq.yygh.vo.hosp.DepartmentVo;
import com.dcq.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/hosp/hospital")
public class HospApiController {
    @Autowired
    private HosptialService hosptialService;

    @Autowired
    private DepartmentService departmentService;

    @ApiOperation(value = "查询议员列表功能")
    @GetMapping("findHospList/{page}/{limit}")
    public Result findHospList(@PathVariable int page,
                               @PathVariable int limit,
                               HospitalQueryVo hospitalQueryVo){
        Page<Hospital> hospitals = hosptialService.selectHostPage(page, limit, hospitalQueryVo);
        return Result.ok(hospitals);
    }

    @ApiOperation(value = "根据医院名称进行模糊查询")
    @GetMapping("findByHosName/{hosname}")
    public Result findByHosName(@PathVariable String hosname){
        List<Hospital> list = hosptialService.findByHosName(hosname);
        return Result.ok(list);
    }

    @ApiOperation(value = "根据医院编号获取科室")
    @GetMapping("department/{hoscode}")
    public Result index(@PathVariable String hoscode){
        List<DepartmentVo> deptTree = departmentService.findDeptTree(hoscode);
        return Result.ok(deptTree);
    }

    @ApiOperation(value = "根据医院编号医院详情")
    @GetMapping("findHospDetail/{hoscode}")
    public Result findHospDetail(@PathVariable String hoscode){
        Map<String,Object> map = hosptialService.item(hoscode);
        return Result.ok(map);
    }
}

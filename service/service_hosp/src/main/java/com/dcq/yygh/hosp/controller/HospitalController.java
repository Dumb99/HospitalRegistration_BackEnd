package com.dcq.yygh.hosp.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import com.dcq.yygh.common.result.Result;
import com.dcq.yygh.hosp.service.HosptialService;
import com.dcq.yygh.model.hosp.Hospital;
import com.dcq.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/hosp/hospital")
public class HospitalController {
    @Autowired
    private HosptialService hosptialService;

    @GetMapping("/list/{page}/{limit}")
    public Result listHost(@PathVariable int page,
                           @PathVariable int limit,
                           HospitalQueryVo hospitalQueryVon){
        Page<Hospital> pageModel = hosptialService.selectHostPage(page,limit,hospitalQueryVon);
        return Result.ok(pageModel);
    }

    //更新医院上线状态
    @ApiOperation(value = "更新医院上线状态")
    @GetMapping("updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable String id,
                               @PathVariable Integer status){
        hosptialService.updateStatus(id,status);
        return Result.ok();
    }

    //医院详情信息查询
    @ApiOperation(value = "医院详情信息查询")
    @GetMapping("showHospDetail/{id}")
    public Result showHospDetails(@PathVariable String id)  {
        Map<String, Object> map = hosptialService.getHospById(id);
        return Result.ok(map);
    }


}

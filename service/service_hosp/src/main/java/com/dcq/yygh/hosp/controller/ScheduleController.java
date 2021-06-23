package com.dcq.yygh.hosp.controller;

import com.dcq.yygh.common.result.Result;
import com.dcq.yygh.hosp.service.ScheduleService;
import com.dcq.yygh.model.hosp.Schedule;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/hosp/schedule")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    //根据医院编号hoscode与科室编号depcode查询排班数据
    @ApiOperation(value = "查询排班数据")
    @GetMapping("getScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result getScheduleRule(@PathVariable int page,
                                  @PathVariable int limit,
                                  @PathVariable String hoscode,
                                  @PathVariable String depcode){
        Map<String, Object> map = scheduleService.getRuleSchedule(page,limit,hoscode,depcode);
        return Result.ok(map);
    }

    //根据医院编号hoscode、科室编号depcode、日期workDate查询排班信息
    @ApiOperation(value = "查询排班信息")
    @GetMapping("getScheduleDetail/{hoscode}/{depcode}/{workDate}")
    public Result getScheduleDetial(@PathVariable String hoscode,
                                    @PathVariable String depcode,
                                    @PathVariable String workDate){
        List<Schedule> list = scheduleService.getDetialSchedule(hoscode,depcode,workDate);
        return Result.ok(list);
    }
}

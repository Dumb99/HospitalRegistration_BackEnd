package com.dcq.yygh.hosp.controller.api;

import com.dcq.yygh.common.exception.YyghException;
import com.dcq.yygh.common.result.Result;
import com.dcq.yygh.common.result.ResultCodeEnum;
import com.dcq.yygh.common.utils.MD5;
import com.dcq.yygh.common.utils.helper.HttpRequestHelper;
import com.dcq.yygh.hosp.service.DepartmentService;
import com.dcq.yygh.hosp.service.HospitalSetService;
import com.dcq.yygh.hosp.service.HosptialService;
import com.dcq.yygh.hosp.service.ScheduleService;
import com.dcq.yygh.model.hosp.Department;
import com.dcq.yygh.model.hosp.Hospital;
import com.dcq.yygh.model.hosp.Schedule;
import com.dcq.yygh.vo.hosp.DepartmentQueryVo;
import com.dcq.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("api/hosp")
public class ApiController {

    @Autowired
    private HosptialService hosptialService;

    @Autowired
    private HospitalSetService hospitalSetService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ScheduleService scheduleService;


    //------------------------排班-------------------------------


    //删除排班
    @PostMapping("schedule/remove")
    public Result removeSchedule(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(parameterMap);

        getSign(paramMap);

        String hoscode = (String) paramMap.get("hoscode");
        String hosScheduleId = (String) paramMap.get("hosScheduleId");

        scheduleService.remove(hoscode,hosScheduleId);
        return Result.ok();
    }

    //查询排班
    @PostMapping("schedule/list")
    public Result findSchedule(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(parameterMap);

        getSign(paramMap);
        String hoscode = (String) paramMap.get("hoscode");

        int page = StringUtils.isEmpty(paramMap.get("page")) ? 1 : Integer.parseInt((String)paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit")) ? 1 : Integer.parseInt((String)paramMap.get("limit"));

        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);

        Page<Schedule> page1 = scheduleService.findPageSchedule(page,limit,scheduleQueryVo);
        return Result.ok(page1);
    }

    //上传排班
    @PostMapping("/saveSchedule")
    public Result saveSchedule(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(parameterMap);

        getSign(paramMap);

        scheduleService.save(paramMap);
        return Result.ok();
    }



    //------------------------科室-------------------------------

    //删除科室
    @PostMapping("department/remove")
    public Result removeDepartment(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(parameterMap);

        String hoscode = (String)paramMap.get("hoscode");
        String depcode = (String)paramMap.get("depcode");

        //签名校验
        getSign(paramMap);

        departmentService.remove(hoscode,depcode);
        return Result.ok();
    }

    //查询科室
    @PostMapping("department/list")
    public Result findDepartment(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(parameterMap);

        String hoscode = (String)paramMap.get("hoscode");
        int page = StringUtils.isEmpty(paramMap.get("page")) ? 1 : Integer.parseInt((String)paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit")) ? 1 : Integer.parseInt((String)paramMap.get("limit"));

        getSign(paramMap);


        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);

        Page<Department> page1 = departmentService.findPage(page,limit,departmentQueryVo);
        return Result.ok(page1);
    }

    //上传科室
    @PostMapping("saveDepartment")
    public Result saveDepartment(HttpServletRequest request){
        //传递过来的科室信息
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(parameterMap);

        //签名校验
        getSign(paramMap);

        //调用Service方法
        departmentService.save(paramMap);
        return Result.ok();

    }

    //------------------------医院-------------------------------

    //上传医院接口
    @PostMapping("saveHospital")
    public Result saveHosp(HttpServletRequest request){
        //传递过来的信息
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(parameterMap);

        String hoscode =(String) paramMap.get("hoscode");

        getSign(paramMap);

        String logoData = (String)paramMap.get("logoData");
        logoData = logoData.replaceAll(" ","+");
        paramMap.put("logoData",logoData);


        //调用Service方法
        hosptialService.save(paramMap);
        return Result.ok();
    }

    //查询医院接口
    @PostMapping("hospital/show")
    public Result getHosptial(HttpServletRequest request){
        //传递过来的信息
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(parameterMap);

        String hoscode = (String)paramMap.get("hoscode");

        getSign(paramMap);

        Hospital hospital = hosptialService.getByHoscode(hoscode);
        return Result.ok(hospital);
    }

    public void getSign(Map<String, Object> paramMap){
        String hoscode =(String) paramMap.get("hoscode");
        //获取签名
        String sign = (String) paramMap.get("sign");

        //根据签名查询数据库进行校验
        String signKey = hospitalSetService.getSignKey(hoscode);
        String signKeyMd5 = MD5.encrypt(signKey);
        if(!signKeyMd5.equals(sign)){
            System.out.println(sign);
            System.out.println(signKey);
            System.out.println(signKeyMd5);
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
    }
}

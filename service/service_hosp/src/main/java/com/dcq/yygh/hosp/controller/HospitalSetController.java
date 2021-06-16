package com.dcq.yygh.hosp.controller;

import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dcq.yygh.common.result.Result;
import com.dcq.yygh.common.utils.MD5;
import com.dcq.yygh.hosp.service.HospitalSetService;
import com.dcq.yygh.model.hosp.HospitalSet;
import com.dcq.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@Api(tags = "医院管理设置")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {

    @Autowired
    private HospitalSetService hospitalSetService;

    //查询医院设置表所有信息
    @ApiOperation(value = "获取分页列表")
    @GetMapping("findAll")
    public Result findAllHospitalSet(){
        //调用service的方法
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }

    //逻辑删除
    @ApiOperation(value = "逻辑删除医院设置")
    @DeleteMapping("{id}")
    public Result removeHospSet(@PathVariable Long id){ //@PathVariable得到上一行@DeleteMapping中的id值
        boolean flag = hospitalSetService.removeById(id);
        if(flag) return Result.ok();
        else return Result.fail();
    }

    //分页条件查询
    @ApiOperation(value = "分页条件查询医院设置")
    @PostMapping("findPageHospSet/{current}/{limit}")
    public Result findPageHospSet(@PathVariable long current,
                                  @PathVariable long limit,
                                  @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo) {
        //创建page对象，传入参数当前页，每页记录数
        Page<HospitalSet> page = new Page<>(current,limit);

        //构建条件
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        String hosname = hospitalSetQueryVo.getHosname();//医院名称
        String hoscode = hospitalSetQueryVo.getHoscode();//医院编号
        if(!StringUtils.isEmpty(hosname))
            wrapper.like("hosname",hospitalSetQueryVo.getHosname());
        if(!StringUtils.isEmpty(hoscode))
            wrapper.eq("hoscode",hospitalSetQueryVo.getHoscode());

        //调用方法实现分页查询
        Page<HospitalSet> pageHospitalSet = hospitalSetService.page(page,wrapper);

        //返回结果
        return Result.ok(pageHospitalSet);
    }


    //添加医院设置
    @ApiOperation("添加医院设置")
    @PostMapping("saveHospitalSet")
    public Result saveHospitalSet(@RequestBody(required = true) HospitalSet hospitalSet){
        //设置状态，1可用，0不可用
        hospitalSet.setStatus(1);
        //签名密钥
        Random random = new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis()+""+random.nextInt(1000)));

        //调用service
        boolean save = hospitalSetService.save(hospitalSet);

        if(save) return Result.ok();
        else return Result.fail();
    }

    //根据id获取医院设置
    @GetMapping("getHospSet/{id}")
    public Result getHostSet(@PathVariable Long id){//        try {
//            //模拟异常,自定义异常需要手动抛出
//            int a = 1/0;
//        }catch (Exception e) {
//            throw new YyghException("失败",201);
//        }
        HospitalSet hosptialSet = hospitalSetService.getById(id);
        return Result.ok(hosptialSet);
    }


    //修改医院设置
    @PostMapping("updateHospSet")
    public Result updateHospSet(@RequestBody HospitalSet hospitalSet){
        boolean flag = hospitalSetService.updateById(hospitalSet);
        if(flag) return Result.ok();
        else return Result.fail();

    }

    //批量删除医院设置
    @DeleteMapping("batchRemove")
    public Result batchRemoveHospitalSet(@RequestBody List<Long> list){
        boolean flag = hospitalSetService.removeByIds(list);
        return Result.ok();
    }

    //医院设置锁定、解锁
    @PutMapping("lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable Long id,
                                  @PathVariable Integer status){
        //根据id查询医院设置信息
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        //设置状态
        hospitalSet.setStatus(status);
        //调用方法
        boolean flag = hospitalSetService.updateById(hospitalSet);

        if(flag) return Result.ok();
        else return Result.fail();
    }

    //发送签名密钥
    @PutMapping("sendKey/{id}")
    public Result sendKey(@PathVariable Long id){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        Integer status = hospitalSet.getStatus();
        String hoscode = hospitalSet.getHoscode();
        //发送短信
        return Result.ok();
    }
}

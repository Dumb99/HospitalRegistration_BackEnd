package com.dcq.yygh.msm.controller;

import com.alibaba.excel.util.StringUtils;
import com.dcq.yygh.common.result.Result;
import com.dcq.yygh.msm.service.MsmService;
import com.dcq.yygh.msm.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/msm")
public class MsmApiController {
    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @GetMapping("send/{phone}")
    public Result phone(@PathVariable String phone){
        //从Redis中返回验证码
        //key为phone，value为code
        String code = redisTemplate.opsForValue().get(phone);
        if(!StringUtils.isEmpty(code)){
            return Result.ok();
        }
        code = RandomUtil.getSixBitRandom();

        //调用阿里云短信服务
        boolean ifSend = msmService.send(phone,code);
        if (ifSend){
            redisTemplate.opsForValue().set(phone,code,2, TimeUnit.MINUTES);
            return Result.ok();
        }
        else
            return Result.fail().message("验证码发送失败");
    }
}

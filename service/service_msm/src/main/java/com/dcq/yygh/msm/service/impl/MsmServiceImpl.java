package com.dcq.yygh.msm.service.impl;

import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.dcq.yygh.msm.service.MsmService;
import com.dcq.yygh.vo.msm.MsmVo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MsmServiceImpl implements MsmService {
    @Override
    public boolean send(String phone, String code) {
        if(StringUtils.isEmpty(phone)) return false;

        //整合邮件服务
        final ApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] {"applicationContext.xml"});
        JavaMailSender sender = (JavaMailSender) context.getBean("mailSender");

        // SimpleMailMessage只能用来发送text格式的邮件
        SimpleMailMessage mail = new SimpleMailMessage();
        try {
            mail.setTo(phone);
            mail.setFrom("hit180400304@163.com");
            mail.setSubject("【预约挂号统一平台】");
            mail.setText("尊敬的用户：\n您本次登陆的验证码为："+code+"，2分钟内有效，为了保障您的账户安全，请勿向他人泄漏验证码信息。");
            sender.send(mail);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean send(MsmVo msmVo) {
        String phone = msmVo.getPhone();
        if(phone.length() < 11) phone = phone + "@qq.com";
        if(phone.length() == 11) phone = phone + "@163.com";
        Map<String,Object> param = msmVo.getParam();
        if(StringUtils.isEmpty(phone)) return false;

        //整合邮件服务
        final ApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] {"applicationContext.xml"});
        JavaMailSender sender = (JavaMailSender) context.getBean("mailSender");

        // SimpleMailMessage只能用来发送text格式的邮件
        SimpleMailMessage mail = new SimpleMailMessage();
        try {
            mail.setTo(phone);
            mail.setFrom("hit180400304@163.com");
            mail.setSubject("【预约挂号统一平台】订单已受理");
            mail.setText(JsonTxt(JSONObject.toJSONString(param)));
            sender.send(mail);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private String JsonTxt(String json){
        String ans = "";
        json = json.substring(1,json.length()-1);
        json = json.replaceAll("\"","");
        json = json.replaceAll(":","：");
//        json = json.replaceAll(",",";\n");
        json = json.replace("name","姓名");
        json = json.replace("amount","挂号费");
        json = json.replace("title","挂号信息");
        json = json.replace("reserveDate","问诊时间");
        json = json.replace("quitTime","退号截止");
        String[] str = json.split(",");
        ans += str[1] + ";\n" + str[0] + "元;\n" + str[3] + ";\n" + str[4] + ";\n" + str[2] + ";\n";
        ans = ans + "\n【预约挂号统一平台】祝您早日康复！";
        return ans;
    }
}

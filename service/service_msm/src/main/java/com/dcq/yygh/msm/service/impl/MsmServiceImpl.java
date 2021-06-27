package com.dcq.yygh.msm.service.impl;

import com.alibaba.excel.util.StringUtils;
import com.dcq.yygh.msm.service.MsmService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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
            mail.setSubject("登录验证码");
            mail.setText("【预约挂号统一平台】尊敬的用户：您本次登陆的验证码为："+code+",2分钟内有效，为了保障您的账户安全，请勿向他人泄漏验证码信息。");
            sender.send(mail);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package com.dcq.yygh.user.config;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.dcq.yygh.user.mapper")
public class UserConfig {
}

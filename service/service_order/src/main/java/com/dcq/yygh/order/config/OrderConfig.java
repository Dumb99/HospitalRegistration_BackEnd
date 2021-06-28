package com.dcq.yygh.order.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.dcq.yygh.order.mapper")
public class OrderConfig {
}

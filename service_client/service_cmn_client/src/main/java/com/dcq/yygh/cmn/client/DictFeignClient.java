package com.dcq.yygh.cmn.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("service-cmn")
@Repository
public interface DictFeignClient {
    @GetMapping("admin/cmn/dict/getName/{dictcode}/{value}")
    public String getName(@PathVariable("dictcode") String dictcode,
                          @PathVariable("value") String value);

    @GetMapping("admin/cmn/dict/getName/{value}")
    public String getName(@PathVariable("value") String value);
}

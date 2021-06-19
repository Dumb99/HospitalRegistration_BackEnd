package com.dcq.yygh.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dcq.yygh.common.result.Result;
import com.dcq.yygh.model.cmn.Dict;
import com.dcq.yygh.model.hosp.HospitalSet;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface DictService extends IService<Dict> {
    List<Dict> findChildData(Long id);

    void exportDictData(HttpServletResponse response) throws IOException;

    Result importDictData(MultipartFile file);
}

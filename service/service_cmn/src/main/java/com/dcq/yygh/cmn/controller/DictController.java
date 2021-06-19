package com.dcq.yygh.cmn.controller;

import com.dcq.yygh.cmn.listener.DictListener;
import com.dcq.yygh.cmn.service.DictService;
import com.dcq.yygh.common.result.Result;
import com.dcq.yygh.model.cmn.Dict;
import com.dcq.yygh.vo.cmn.DictEeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Api(value = "数据字典接口")
@RestController
@RequestMapping("admin/cmn/dict")
@CrossOrigin
public class DictController {

    @Autowired
    private DictService dictService;

    //根据数据id查询子数据
    @ApiOperation("根据数据id查询子数据")
    @GetMapping("findChildData/{id}")
    public Result findChildData(@PathVariable Long id){
        List<Dict> list = dictService.findChildData(id);
        return Result.ok(list);
    }

    //导出数据字典
    @ApiOperation("导出数据字典")
    @GetMapping("exportData")
    public void exportData(HttpServletResponse response){
        try {
            dictService.exportDictData(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //导入数据字典接口
    @ApiOperation("导入数据字典")
    @PostMapping("importData")
    public Result importData(MultipartFile file){
        dictService.importDictData(file);
        return Result.ok();
    }
}

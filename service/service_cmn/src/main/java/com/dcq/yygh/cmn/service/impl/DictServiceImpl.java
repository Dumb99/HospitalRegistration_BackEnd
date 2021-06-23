package com.dcq.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dcq.yygh.cmn.listener.DictListener;
import com.dcq.yygh.cmn.mapper.DictMapper;
import com.dcq.yygh.cmn.service.DictService;
import com.dcq.yygh.common.result.Result;
import com.dcq.yygh.model.cmn.Dict;
import com.dcq.yygh.vo.cmn.DictEeVo;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {


    //根据id查询子数据
    @Cacheable(value = "dict",keyGenerator = "keyGenerator")
    @Override
    public List<Dict> findChildData(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();      //条件查询构造器
        wrapper.eq("parent_id",id);
        List<Dict> list = baseMapper.selectList(wrapper);
        for(Dict dict : list){
            Long dictId = dict.getId();
            boolean isChild = this.hasChildren(dictId);
            dict.setHasChildren(isChild);
        }
        return list;
    }

    //判断id是否又子节点，即为hasChildren赋值
    public boolean hasChildren(Long id){
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        Integer count = baseMapper.selectCount(wrapper);
        return count > 0;
    }

    public void exportDictData(HttpServletResponse response) {
        //设置下载内容
        response.setContentType("application/vnd.ms-excel");        //设置下载类型为excel
        response.setCharacterEncoding("utf-8");
        String fileName = "dict";
        response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");

        List<Dict> dictList = baseMapper.selectList(null);
        List<DictEeVo> dictEeVos = new ArrayList<>();

        //Dict --> DictEeVo
        for(Dict dict : dictList){
            DictEeVo dictEeVo = new DictEeVo();
            BeanUtils.copyProperties(dict,dictEeVo);
            dictEeVos.add(dictEeVo);
        }
        try {
            EasyExcel.write(response.getOutputStream(),DictEeVo.class)
                    .sheet("用户字典").doWrite(dictEeVos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @CacheEvict(value = "dict", allEntries=true)
    public Result importDictData(MultipartFile file){
        try {
            EasyExcel.read(file.getInputStream(),DictEeVo.class,new DictListener(baseMapper)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.ok();
    }

    @Override
    public String getDictName(String dictcode, String value) {
        if(StringUtils.isEmpty(dictcode)){

            QueryWrapper<Dict> wrapper = new QueryWrapper<>();
            wrapper.eq("value",value);
            Dict dict = baseMapper.selectOne(wrapper);

            return dict.getName();

        }
        else{
            Dict dict = getDictByDictcode(dictcode);
            Long parentId = dict.getId();
            Dict finalDict = baseMapper.selectOne(new QueryWrapper<Dict>().eq("parent_id", parentId).eq("value", value));
            return finalDict.getName();

        }
    }

    @Override
    public List<Dict> findByDictCode(String dictCode) {
        Dict dict = this.getDictByDictcode(dictCode);
        List<Dict> childData = this.findChildData(dict.getId());
        return childData;
    }

    private Dict getDictByDictcode(String dictcode){
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("dict_code",dictcode);
        Dict dict = baseMapper.selectOne(wrapper);
        return dict;
    }
}

package com.haomostudio.${pageName}.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.CaseFormat;
import com.haomostudio.${pageName}.dao.*;
import com.haomostudio.${pageName}.po.*;
import com.haomostudio.${pageName}.vo.*;
import com.haomostudio.${pageName}.service.*;
import com.haomostudio.${pageName}.service.HmUtils.MybatisExampleHelper;
import org.apache.ibatis.binding.MapperProxy;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.haomo.plugin.Page;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.stream.Collectors;

/**
* Created by hxgqh on 2016/1/7.
*/
@Service("${modelService}")
public class ${model}ServiceImpl implements ${model}Service{
    protected static final Logger LOG = LoggerFactory.getLogger(${model}ServiceImpl.class);

    // 将所有的modelMapper注入
${all_model_mapper}

    @Override
    public ${model} create(${model} item) {

         ${model_lower_camel}Mapper.insert(item);
         return item;
    }

    @Override
    public int delete(Integer id) {
        return ${model_lower_camel}Mapper.deleteByPrimaryKey(id);
    }

    @Override
    public int update(${model} item) {

        return ${model_lower_camel}Mapper.updateByPrimaryKeySelective(item);
    }

    @Override
    public ${model} get(Integer id) {
        return ${model_lower_camel}Mapper.selectByPrimaryKey(id);
    }
    @Override
     public PageInfo<${model}> getListWithPaging(Integer pageNum, Integer pageSize,
                                                  String sortItem, String sortOrder,${model} item){

        ${model}Example  example = new ${model}Example();
        ${model}Example.Criteria criteria = example.createCriteria();
        ${edit_setattr_codeimpl}

        example.setOrderByClause(Tools.humpToLine(sortItem)+" "+sortOrder);
        PageHelper.startPage(pageNum, pageSize);
        List< ${model}> list = this.${model_lower_camel}Mapper.selectByExample(example);
        PageInfo result = new PageInfo(list);

        return result;

     }

}

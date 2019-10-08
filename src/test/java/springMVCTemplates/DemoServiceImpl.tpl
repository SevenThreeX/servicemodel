package com.sidianzhong.sdz.service.impl;
import com.github.pagehelper.PageHelper;
import com.sidianzhong.sdz.mapper.*;
import com.sidianzhong.sdz.model.*;
import com.sidianzhong.sdz.service.*;
import com.sidianzhong.sdz.utils.PageInfo;
import com.sidianzhong.sdz.utils.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

/**
* Created by hxgqh on 2016/1/7.
*/
@Service
@Transactional
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

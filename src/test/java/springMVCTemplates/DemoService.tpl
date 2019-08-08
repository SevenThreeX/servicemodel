package com.haomostudio.${pageName}.service;

import com.haomostudio.${pageName}.po.${model};

import java.util.List;

/**
* Created by hxgqh on 2016/1/7.
*/
public interface ${model}Service {

    ${model} create(${model} item);

    int delete(Integer id);

    int update(${model} item);

    ${model} get(Integer id);

    PageInfo<${model}> getListWithPaging(Integer pageNum, Integer pageSize,
                                              String sortItem, String sortOrder,${model} item);
}

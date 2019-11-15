package com.sidianzhong.sdz.service;

import com.sidianzhong.sdz.model.*;
import com.sidianzhong.sdz.utils.PageInfo;
import com.alibaba.fastjson.JSONObject;

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

    PageInfo<JSONObject> getListWithObject(Integer pageNum, Integer pageSize,
                                              String sortItem, String sortOrder, ${model} item);
}

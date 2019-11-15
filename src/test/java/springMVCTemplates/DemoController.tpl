package com.sidianzhong.sdz.controller;

import com.alibaba.fastjson.JSONObject;
import com.sidianzhong.sdz.annotation.BackLoginToken;
import com.sidianzhong.sdz.annotation.UserLoginToken;
import com.sidianzhong.sdz.model.*;
import com.sidianzhong.sdz.service.*;
import com.sidianzhong.sdz.service.commond.RedisService;
import com.sidianzhong.sdz.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Created by hxg on 2016/10/06.
 */
@Api(description = "${name}")
@Controller
public class ${model}Controller {

    @Autowired
    ${model}Service ${modelService};
    @Autowired
    HttpServletRequest request;
    @Autowired
    HttpServletResponse response;

    @UserLoginToken
    @ApiOperation(value = "创建'${name}'表中一条信息")
    @RequestMapping(value = "/${model_lower_camel}/new", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object create${model}(
            HttpServletRequest request,
            @RequestHeader(value = "X-Auth-Token") String token,
            ${create_request_params}
    ) {
        ${model} item = new ${model}();
        Date date = new Date();
        ${create_setattr_code}
        ${model} iteams = ${modelService}.create(item);
        boolean result = iteams != null;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result",result);
        return new ResponseEntity<>(ResultModel.ok(jsonObject), HttpStatus.OK);
    }
    @UserLoginToken
    @ApiOperation(value = "删除'${name}'表中的某条记录")
    @RequestMapping(value = "/${model_lower_camel}/delete",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object delete${model}(
            @RequestHeader(value = "X-Auth-Token") String token,
            @RequestParam(value = "id") Integer id
    ) {
        ${model} item = ${modelService}.get(id);
        if (null == item) {
            return new ResponseEntity<>(ResultModel.error(), HttpStatus.OK);
        }
        int delete = ${modelService}.delete(id);
        boolean result = delete!=0;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result",result);
        return new ResponseEntity<>(ResultModel.ok(jsonObject), HttpStatus.OK);
    }
    @UserLoginToken
    @ApiOperation(value = "修改'${name}'表中的某条记录")
    @RequestMapping(value = "/${model_lower_camel}/edit",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object edit${model}(
            HttpServletRequest request,
            @RequestHeader(value = "X-Auth-Token") String token,
            @RequestParam(value = "id") Integer id,
            ${edit_request_params}
    ) {
        ${model} item = ${modelService}.get(id);
        if (null == item) {
           return new ResponseEntity<>(ResultModel.error(), HttpStatus.OK);
        }
        ${edit_setattr_code}
        int update =${modelService}.update(item);
        boolean result = update!=0;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result",result);
        return new ResponseEntity<>(ResultModel.ok(jsonObject), HttpStatus.OK);
    }
    @UserLoginToken
    @ApiOperation(value = "查询'${name}'表中的某条记录")
    @RequestMapping(value = "/get${model}ById",method = {RequestMethod.GET},produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object get${model}ById(
            HttpServletRequest request,
            @RequestHeader(value = "X-Auth-Token") String token,
            @RequestParam(value = "id") Integer id
    ) {
            ${model} item = ${modelService}.get(id);
            if (null == item) {
                return new ResponseEntity<>(ResultModel.error(), HttpStatus.OK);
            }
             return new ResponseEntity<>(ResultModel.ok(item), HttpStatus.OK);
    }
    @UserLoginToken
    @ApiOperation(value = "查询'${name}'表中的多条记录返回对象")
    @RequestMapping(value = "/${model_underscore_plural}",method = { RequestMethod.GET },produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object get${model_plural}(
            HttpServletRequest request,
            @RequestHeader(value = "X-Auth-Token", required = false) String token,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "sortItem", required = false, defaultValue = "id") String sortItem,
            @RequestParam(value = "sortOrder", required = false, defaultValue = "desc") String sortOrder,
            ${find_request_params}
    ) {
            ${model} item = new ${model}();
            ${get_setattr_code}
       PageInfo<${model}> list =  ${modelService}.getListWithPaging(pageNum, pageSize, sortItem, sortOrder,item);
       return new ResponseEntity<>(ResultModel.ok(list), HttpStatus.OK);
    }
    @UserLoginToken
    @ApiOperation(value = "查询'${name}'表中的多条记录返回自定义JSONObject")
    @RequestMapping(value = "/${model_underscore_plural}Object",method = { RequestMethod.GET },produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object get${model_plural}Object(
            HttpServletRequest request,
            @RequestHeader(value = "X-Auth-Token", required = false) String token,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "sortItem", required = false, defaultValue = "id") String sortItem,
            @RequestParam(value = "sortOrder", required = false, defaultValue = "desc") String sortOrder,
            ${find_request_params}
    ) {
           ${model} item = new ${model}();
           ${get_setattr_code}
           PageInfo<JSONObject> list  =  ${modelService}.getListWithObject(pageNum, pageSize, sortItem, sortOrder,item);
           List<JSONObject> list1 = list.getList();
           List<JSONObject> collect = list1.stream().map(ls -> {
            JSONObject jsonObject = new JSONObject();
            ${get_object_code}
            return jsonObject;
           }).collect(Collectors.toList());
           list.setList(collect);
           return new ResponseEntity<>(ResultModel.ok(list), HttpStatus.OK);
        }
}

package com.sidianzhong.sdz.controller;

import com.sidianzhong.sdz.model.*;
import com.sidianzhong.sdz.service.*;
import com.sidianzhong.sdz.utils.PageInfo;
import com.sidianzhong.sdz.utils.ResultModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;

/**
 * Created by hxg on 2016/10/06.
 */
@Api(description = "${name}")
@Controller
public class ${model}Controller {

    @Autowired
    ${model}Service ${modelService};

    @Autowired
    HttpServletResponse response;

    @ApiOperation(value = "创建'${name}'表中一条信息", notes = "通过post方法请求，传入表中字段的对应信息，创建一条数据。并返回给View层")
    @RequestMapping(value = "/${model_underscore_plural}/new",
                    method = RequestMethod.POST,
                    produces = "application/json;charset=UTF-8")
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

        return new ResponseEntity<>(ResultModel.ok(iteams), HttpStatus.OK);
    }

    @ApiOperation(value = "删除'${name}'表中的某条记录", notes = "根据url传入的数据id，删除整条记录。")
    @RequestMapping(value = "/${model_underscore_plural}/delete",
                    method = RequestMethod.POST,
                    produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object delete${model}(
            @RequestHeader(value = "X-Auth-Token") String token,
            @RequestParam(value = "${model_underscore}_id") Integer id
    ) {
        ${model} item = ${modelService}.get(id);
        if (null == item) {
            return new ResponseEntity<>(ResultModel.error(item), HttpStatus.OK);
        }
        ${modelService}.delete(id);
        return new ResponseEntity<>(ResultModel.ok("delete success"), HttpStatus.OK);
    }

    @ApiOperation(value = "修改'${name}'表中的某条记录", notes = "根据url传入的数据id，确定修改表中的某条记录，传入表中字段要修改的信息，不传代表不修改。并返回给View层")
    @RequestMapping(value = "/${model_underscore_plural}/edit",
                    method = RequestMethod.POST,
                    produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object edit${model}(
            HttpServletRequest request,
            @RequestHeader(value = "X-Auth-Token") String token,
            @RequestParam(value = "id") Integer id,
            ${edit_request_params}
    ) {
        ${model} item = ${modelService}.get(id);
        if (null == item) {
           return new ResponseEntity<>(ResultModel.error(item), HttpStatus.OK);
        }

        ${edit_setattr_code}
        ${modelService}.update(item);

        return ${modelService}.get(id);
    }

    @ApiOperation(value = "查询'${name}'表中的某条记录", notes = "根据url传入的数据id，查询对应的一条数据。")
    @RequestMapping(value = "/get${model}ById",
                    method = {RequestMethod.GET},
                    produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object get${model}ById(
            HttpServletRequest request,
            @RequestHeader(value = "X-Auth-Token") String token,
            @RequestParam(value = "id") Integer id
    ) {

            ${model} item = ${modelService}.get(id);
            if (null == item) {
                return new ResponseEntity<>(ResultModel.error(item), HttpStatus.OK);
            }

             return new ResponseEntity<>(ResultModel.ok(item), HttpStatus.OK);

    }

    @ApiOperation(value = "查询'${name}'表中的多条记录或者新增某条记录", notes = "get传参：根据所需的字段进行匹配查询。数据数量取决于pageNo和pageSize；数据的先后顺序取决于sortItem，sortOrder； ")
    @RequestMapping(value = "/${model_underscore_plural}",
                    method = { RequestMethod.GET },
                    produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object get${model_plural}(
            HttpServletRequest request,
            @RequestHeader(value = "X-Auth-Token", required = false) String token,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "sortItem", required = false, defaultValue = "id") String sortItem,
            @RequestParam(value = "sortOrder", required = false, defaultValue = "desc") String sortOrder,
            ${edit_request_params}
    ) {
        ${model} item = new ${model}();
        ${edit_setattr_code}
       PageInfo<${model}> list =  ${modelService}.getListWithPaging(pageNum, pageSize, sortItem, sortOrder,item);
       return new ResponseEntity<>(ResultModel.ok(list), HttpStatus.OK);

    }
}

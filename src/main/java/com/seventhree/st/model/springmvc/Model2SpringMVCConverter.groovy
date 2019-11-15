package com.seventhree.st.model.springmvc

import com.seventhree.st.Utils
import groovy.text.GStringTemplateEngine

/**
 * Created by hm20160509 on 16/10/21.
 */
class Model2SpringMVCConverter {
    def pageName
    def controller_tpl
    def service_tpl
    def service_impl_tpl
    def model_controller_file_dict = [:]
    def model_service_file_dict = [:]
    def model_service_impl_file_dict = [:]
    def model_vo_file_dict = [:]
    def targetDir
    def table_column_infos

    def convert_pdm_type_to_java_type(pdmType){
        pdmType.toLowerCase();
        if (pdmType.contains("varchar") || pdmType.contains("lob")) {
            return 'String'
        }

        if (pdmType.contains('int') || pdmType.contains('number')) {
            return 'Integer'
        } else if(pdmType.contains('number')){
            return 'Long'
        }else if (pdmType.contains('date') || pdmType.contains('timestamp')){
            return 'String'
        }else if (pdmType.contains('decimal')) {
            return 'BigDecimal'
        }else if (pdmType.contains('longtext')) {
            return 'String'
        }else if(pdmType.contains('double')){
            return 'Double'
        }else if(pdmType.contains('text')){
            return 'String'
        }else if(pdmType.contains('float')){
            return 'float'
        }else if(pdmType.contains('char')){
            return 'String'
        }else if(pdmType.contains('longtext')){
            return 'String'
        }


        println "unknown pdm type: " + pdmType
    }

    def convert_object_type_to_java_type(pdmType){
        pdmType.toLowerCase();
        if (pdmType.contains("varchar") || pdmType.contains("lob")) {
            return 'String'
        }

        if (pdmType.contains('int') || pdmType.contains('number')) {
            return 'Integer'
        } else if(pdmType.contains('number')){
            return 'Long'
        }else if (pdmType.contains('date') || pdmType.contains('timestamp')){
            return 'Date'
        }else if (pdmType.contains('decimal')) {
            return 'BigDecimal'
        }else if (pdmType.contains('longtext')) {
            return 'String'
        }else if(pdmType.contains('double')){
            return 'Double'
        }else if(pdmType.contains('text')){
            return 'String'
        }else if(pdmType.contains('float')){
            return 'float'
        }else if(pdmType.contains('char')){
            return 'String'
        }else if(pdmType.contains('longtext')){
            return 'String'
        }


        println "unknown pdm type: " + pdmType
    }

    /**
     * 生成/tables/create的接口参数
     * @param model - 表名，例如user
     * @param columns - 字段列表，格式为：
     *  {
     *      column: {
     *          name: "column",
     *          type: "varchar(32)",
     *          required: false,
     *          description: ""
     *      }
     *  }
     * @return
     */
    def generate_create_param_code(model, columns) {
        def params = []

        columns.each { name, column ->
            if (column['name'] == 'id') {
                return
            }
            params.add(
                    '@RequestParam(value = "' +
                            column['name'] +
                            '", required = ' +
                            column['required'] + ') ' +
                            this.convert_pdm_type_to_java_type(column['type']) +
                            ' ' + column['name']
            )
        }

        return params.join(',\n            ')
    }


    def generate_create_setattr_code(model, columns, join) {
        def params = []

        columns.each { name, column ->
            if (column['name'] == 'id') {
                return
            }

            if ((column['name']=="createTime")){
                if (column['type'] == 'datetime') {
                    params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name'])
                            + '(new Date());')
                } else if (column['type'] == 'date') {
                    params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name'])
                            + '(new Date());')
                } else if (column['type'] == 'timestamp') {
                    params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name'])
                            + '(new Date());')
                } }
            else if ((column['name']=="lastUpdateTime")) {
                if (column['type'] == 'datetime') {
                    params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name'])
                            + '(new Date());')
                } else if (column['type'] == 'date') {
                    params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name'])
                            + '(new Date());')
                }
            }

             else if (column['type'] == 'datetime') {
                params.add('if( ' + column['name'] + ' != ' + 'null ){')
                params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name'])+ '(date);')
                params.add('}')
            } else if (column['type'] == 'date') {
                params.add('if( ' + column['name'] + ' != ' + 'null ){')
                params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name'])+ '(date);')
                params.add('}')
            } else if (column['type'] == 'timestamp') {
                params.add('if( ' + column['name'] + ' != ' + 'null ){')
                params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name'])+ '(date);')
                params.add('}')
            } else {
                params.add('if( ' + column['name'] + ' != ' + 'null ){');
                params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name']) + '(' + column['name'] + ');')
                 params.add('}')
            }

        }

        return params.join(join)
    }



    def generate_create_setattr_codes(model, columns, join) {
        def params = []

        columns.each { name, column ->
            if (column['name'] == 'id') {
                return
            }

            if ((column['name']=="createTime")){
                if (column['type'] == 'datetime') {
                    params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name'])
                            + '(new Date());')
                } else if (column['type'] == 'date') {
                    params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name'])
                            + '(new Date());')
                } else if (column['type'] == 'timestamp') {
                    params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name'])
                            + '(new Date());')
                } }
            else if ((column['name']=="lastUpdateTime")){
                if (column['type'] == 'datetime'){
                    params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name'])
                            + '(new Date());')
                } else if (column['type'] == 'date') {
                    params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name'])
                            + '(new Date());')
                }
                else if (column['type'] == 'timestamp') {
                    params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name'])
                            + '(new Date());')
                }
            } else if (column['type'] == 'datetime') {
                params.add('if(jsonObject.containsKey("' + column['name'] + '")   ){')
                params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name'])
                        + '(Tools.convertStringToDate('
                        + 'jsonObject.get("' + column['name'] + '").toString()'
                        + ', "yyyy-MM-dd HH:mm:ss"));')
                params.add('}')
            } else if (column['type'] == 'date') {
                params.add('if(jsonObject.containsKey("' + column['name'] + '")   ){')
                params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name'])
                        + '(Tools.convertStringToDate('
                        + 'jsonObject.get("' + column['name'] + '").toString()'
                        + ', "yyyy-MM-dd HH:mm:ss"));')
                params.add('}')
            } else if (column['type'] == 'timestamp') {
                params.add('if(jsonObject.containsKey("' + column['name'] + '")   ){')
                params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name'])
                        + '(Tools.convertStringToDate('
                        + 'jsonObject.get("' + column['name'] + '").toString()'
                        + ', "yyyy-MM-dd HH:mm:ss"));')
                params.add('}')
            } else if(column['type'] == 'double') {
                params.add('if(jsonObject.containsKey("' + column['name'] + '")   ){')
                params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name']) + '(new Double(jsonObject.get("' + column['name'] + '").toString()));')
                params.add('}')
            }else if(column['type'] == 'int') {
                params.add('if(jsonObject.containsKey("' + column['name'] + '")   ){')
                params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name']) + '(new Integer(jsonObject.get("' + column['name'] + '").toString()));')
                params.add('}')
            } else {
                params.add('if(jsonObject.containsKey("' + column['name'] + '")   ){')
                params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name']) + '(jsonObject.get("' + column['name'] + '").toString());')
                params.add('}')
            }

        }

        return params.join(join)
    }

    def generate_edit_param_code(model, columns) {
        def params = []

        columns.each { name, column ->
            if (column['name'] == 'id') {
                return
            }
            if(column['name']=='createTime'){return }
            if(column['name']=='lastUpdateTime'){return }

            params.add(
                    '@RequestParam(value = "' +
                            column['name'] + '", required = ' +
                            column['required'] +
                            ') ' + this.convert_pdm_type_to_java_type(column['type']) + ' ' +
                            column['name']
            )
        }

        return params.join(',\n            ')
    }

    def generate_find_param_code(model, columns) {
        def params = []

        columns.each { name, column ->
            if (column['name'] == 'id') {
                return
            }
            params.add(
                    '@RequestParam(value = "' +
                            column['name'] + '", required = ' +
                            column['required'] +
                            ') ' + this.convert_pdm_type_to_java_type(column['type']) + ' ' +
                            column['name']
            )
        }

        return params.join(',\n            ')
    }

    def generate_getObject_setattr_code(model, columns, join) {
        def params = []

        columns.each { name, column ->
            if (column['name'] == 'id') {
                return
            }
//            if(column['name']=='createTime'){return }


            params.add("jsonObject.put(\""+column['name']+"\",ls.get"+this.convert_object_type_to_java_type(column['type'])+"(\""+column['name']+"\"));")
//            jsonObject.put("+column['name']+","ls.get"+this.convert_pdm_type_to_java_type(column['type'])+("+column['name']+"));
//
//            println(column['type'])
//
////            if (column['name']=="lastUpdateTime") {
////                if (column['type'] == 'datetime') {
////                    params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name'])
////                            + '(new Date());')
////                } else if (column['type'] == 'date') {
////                    params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name'])
////                            + '(new Date());')
////                } else if (column['type'] == 'timestamp') {
////                    params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name'])
////                            + '(new Date());')
////                }} else
//            if(column['type'] == 'datetime') {
//                params.add('if( ' + column['name'] + ' != ' + 'null ){')
//                params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name'])
//                        + '(Tools.convertStringToDate('
//                        + column['name']
//                        + ', "yyyy-MM-dd HH:mm:ss"));')
//                params.add('}')
//            } else if (column['type'] == 'date') {
//                params.add('if( ' + column['name'] + ' != ' + 'null ){')
//                params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name'])
//                        + '(Tools.convertStringToDate('
//                        + column['name']
//                        + ', "yyyy-MM-dd"));')
//                params.add('}')
//            } else if (column['type'] == 'timestamp') {
//                params.add('if( ' + column['name'] + ' != ' + 'null ){')
//                params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name'])
//                        + '(Tools.convertStringToDate('
//                        + column['name']
//                        + ', "yyyy-MM-dd HH:mm:ss"));')
//                params.add('}')
//            }else{
//                params.add('if( ' + column['name'] + ' != ' + 'null ){')
//                params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name']) + '(' + column['name'] + ');')
//                params.add('}')
//            }
//
        }

        return params.join(join)
    }

    def generate_edit_setattr_code(model, columns, join) {
        def params = []

        columns.each { name, column ->
                if (column['name'] == 'id') {
                    return
                }
            if(column['name']=='createTime'){return }

                println(column['type'])

            if (column['name']=="lastUpdateTime") {
                if (column['type'] == 'datetime') {
                    params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name'])
                            + '(new Date());')
                } else if (column['type'] == 'date') {
                    params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name'])
                            + '(new Date());')
                } else if (column['type'] == 'timestamp') {
                    params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name'])
                            + '(new Date());')
                }} else
            if(column['type'] == 'datetime') {
                params.add('if( ' + column['name'] + ' != ' + 'null ){')
                params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name'])
                        + '(Tools.convertStringToDate('
                        + column['name']
                        + ', "yyyy-MM-dd HH:mm:ss"));')
                params.add('}')
            } else if (column['type'] == 'date') {
                params.add('if( ' + column['name'] + ' != ' + 'null ){')
                params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name'])
                        + '(Tools.convertStringToDate('
                        + column['name']
                        + ', "yyyy-MM-dd"));')
                params.add('}')
            } else if (column['type'] == 'timestamp') {
                params.add('if( ' + column['name'] + ' != ' + 'null ){')
                params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name'])
                        + '(Tools.convertStringToDate('
                        + column['name']
                        + ', "yyyy-MM-dd HH:mm:ss"));')
                params.add('}')
            }else{
                params.add('if( ' + column['name'] + ' != ' + 'null ){')
                params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name']) + '(' + column['name'] + ');')
                params.add('}')
            }

        }

        return params.join(join)
    }

    def generate_get_setattr_code(model, columns, join) {
        def params = []

        columns.each { name, column ->
            if (column['name'] == 'id') {
                return
            }
//            if(column['name']=='createTime'){return }

            println(column['type'])

//            if (column['name']=="lastUpdateTime") {
//                if (column['type'] == 'datetime') {
//                    params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name'])
//                            + '(new Date());')
//                } else if (column['type'] == 'date') {
//                    params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name'])
//                            + '(new Date());')
//                } else if (column['type'] == 'timestamp') {
//                    params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name'])
//                            + '(new Date());')
//                }} else
            if(column['type'] == 'datetime') {
                params.add('if( ' + column['name'] + ' != ' + 'null ){')
                params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name'])
                        + '(Tools.convertStringToDate('
                        + column['name']
                        + ', "yyyy-MM-dd HH:mm:ss"));')
                params.add('}')
            } else if (column['type'] == 'date') {
                params.add('if( ' + column['name'] + ' != ' + 'null ){')
                params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name'])
                        + '(Tools.convertStringToDate('
                        + column['name']
                        + ', "yyyy-MM-dd"));')
                params.add('}')
            } else if (column['type'] == 'timestamp') {
                params.add('if( ' + column['name'] + ' != ' + 'null ){')
                params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name'])
                        + '(Tools.convertStringToDate('
                        + column['name']
                        + ', "yyyy-MM-dd HH:mm:ss"));')
                params.add('}')
            }else{
                params.add('if( ' + column['name'] + ' != ' + 'null ){')
                params.add('    item.set' + new Utils().underscore2UpperCamelCase(column['name']) + '(' + column['name'] + ');')
                params.add('}')
            }

        }

        return params.join(join)
    }

    def generate_edit_setattr_codeimpl(model, columns, join) {
        def params = []

        columns.each { name, column ->
            if (column['name'] == 'id') {
                return
            }
//            if(column['name']=='createTime'){return }

            println(column['type'])

//            if (column['name']=="lastUpdateTime") {
//                if (column['type'] == 'datetime') {
//                    params.add('    criteria.and' + new Utils().underscore2UpperCamelCase(column['name'])
//                            + 'EqualTo(new Date());')
//                } else if (column['type'] == 'date') {
//                    params.add('    criteria.and' + new Utils().underscore2UpperCamelCase(column['name'])
//                            + 'EqualTo(new Date());')
//                } else if (column['type'] == 'timestamp') {
//                    params.add('    criteria.and' + new Utils().underscore2UpperCamelCase(column['name'])
//                            + 'EqualTo(new Date());')
//                }
//            } else
//            if(column['type'] == 'datetime') {
//                params.add('if( item.get' +new Utils().underscore2UpperCamelCase(column['name']) + '() != ' + 'null ){')
//                params.add('    criteria.and' + new Utils().underscore2UpperCamelCase(column['name'])
//                        + 'EqualTo(Tools.convertStringToDate(item.get'
//                        + new Utils().underscore2UpperCamelCase(column['name'])
//                        + '(), "yyyy-MM-dd HH:mm:ss"));')
//                params.add('}')
//            } else if (column['type'] == 'date') {
//                params.add('if(item.get ' + new Utils().underscore2UpperCamelCase(column['name']) + '() != ' + 'null ){')
//                params.add('    criteria.and' + new Utils().underscore2UpperCamelCase(column['name'])
//                        + 'EqualTo(Tools.convertStringToDate(item.get'
//                        + new Utils().underscore2UpperCamelCase(column['name'])
//                        + '(), "yyyy-MM-dd"));')
//                params.add('}')
//            } else if (column['type'] == 'timestamp') {
//                params.add('if( item.get' +new Utils().underscore2UpperCamelCase(column['name']) + '() != ' + 'null ){')
//                params.add('    criteria.and' + new Utils().underscore2UpperCamelCase(column['name'])
//                        + 'EqualTo(Tools.convertStringToDate(item.get'
//                        + new Utils().underscore2UpperCamelCase(column['name'])
//                        + '(), "yyyy-MM-dd HH:mm:ss"));')
//                params.add('}')
//            }else{
                params.add('if(item.get' + new Utils().underscore2UpperCamelCase(column['name']) + '() != ' + 'null ){')
                params.add('    criteria.and' + new Utils().underscore2UpperCamelCase(column['name']) +'EqualTo'+'(item.get' + new Utils().underscore2UpperCamelCase(column['name']) + '());')
                params.add('}')
//            }

        }

        return params.join(join)
    }

    def generate_all_model_mapper(model){
        def mapper = []
//        this.table_column_infos.each { k, v ->
//            mapper.add("    @Autowired\n    "+k+"Mapper "+new Utils().UpperCamelCase2camelCase(k)+"Mapper;\n");
//        }
        mapper.add("    @Autowired\n    private "+model+"Mapper "+new Utils().UpperCamelCase2camelCase(model)+"Mapper;\n");
        return mapper.join("\n")

    }

    def generate_controller(model, columns,name,description) {
        def binding = [
                pageName               : pageName,
                model                  : model,
                name                   : name,
                description            : description,
                modelService           : new Utils().UpperCamelCase2camelCase(model) + 'Service',
                model_underscore       : new Utils().UpperCamelCase2underscore(model),
                model_lower_camel      : new Utils().UpperCamelCase2camelCase(model),
                model_underscore_plural: org.atteo.evo.inflector.English.plural(new Utils().UpperCamelCase2camelCase(model), 2),
                model_plural           : org.atteo.evo.inflector.English.plural(model, 2),
                create_request_params  : this.generate_create_param_code(model, columns),
                create_setattr_code    : this.generate_create_setattr_code(model, columns, '\n        '),
                create_setattr   : this.generate_create_setattr_codes(model, columns, '\n        '),
                tables_setattr_code    : this.generate_create_setattr_code(model, columns, '\n            '),
                edit_request_params    : this.generate_edit_param_code(model, columns),
                find_request_params    : this.generate_find_param_code(model, columns),
                edit_setattr_code      : this.generate_edit_setattr_code(model, columns, '\n        '),
                get_setattr_code       : this.generate_get_setattr_code(model, columns, '\n            '),
                get_object_code       : this.generate_getObject_setattr_code(model, columns, '\n            ')


        ]

        def f = new File(this.controller_tpl)
        def engine = new GStringTemplateEngine()
        def template = engine.createTemplate(f).make(binding)
        this.model_controller_file_dict[model] = template.toString()
    }

    def generate_service(model, columns){
        def binding = [
                pageName               : pageName,
                model                  : model,
                modelCamel             : new Utils().UpperCamelCase2camelCase(model),
                modelService           : new Utils().UpperCamelCase2camelCase(model) + 'Service',
                model_underscore       : new Utils().UpperCamelCase2underscore(model),
                model_underscore_plural: org.atteo.evo.inflector.English.plural(new Utils().UpperCamelCase2underscore(model), 2),
                model_plural           : org.atteo.evo.inflector.English.plural(model, 2)
        ]

        def f = new File(this.service_tpl)
        def engine = new GStringTemplateEngine()
        def template = engine.createTemplate(f).make(binding)
        this.model_service_file_dict[model] = template.toString()
    }

    def generate_service_impl(model, columns){
        def binding = [
                pageName               : pageName,
                model                  : model,
                model_lower_camel      : new Utils().UpperCamelCase2camelCase(model),
                modelCamel             : new Utils().UpperCamelCase2camelCase(model),
                modelService           : new Utils().UpperCamelCase2camelCase(model) + 'Service',
                model_underscore       : new Utils().UpperCamelCase2underscore(model),
                model_underscore_plural: org.atteo.evo.inflector.English.plural(new Utils().UpperCamelCase2underscore(model), 2),
                model_plural           : org.atteo.evo.inflector.English.plural(model, 2),
                edit_setattr_codeimpl  : this.generate_edit_setattr_codeimpl(model, columns, '\n        '),
                all_model_mapper       : this.generate_all_model_mapper(model)
        ]

        def f = new File(this.service_impl_tpl)
        def engine = new GStringTemplateEngine()
        def template = engine.createTemplate(f).make(binding)
        this.model_service_impl_file_dict[model] = template.toString()
    }


    /**
     * 保存controller
     */
    def saveController(targetDir){
//         def mkdir_controller = "mkdir " + targetDir + "/controller"
//         println mkdir_controller.execute().text

        this.model_controller_file_dict.each { model, code ->
            def new_file = new File(targetDir + '/controller/' + model + 'Controller.java')
            new_file.write(code)
        }
    }

    /**
     * 保存Service
     */
    def saveService(targetDir){
//         def mkdir_controller = "mkdir " + targetDir + "/service"
//         println mkdir_controller.execute().text

        this.model_service_file_dict.each { model, code ->
            def new_file = new File(targetDir + '/service/' + model + 'Service.java')
            new_file.write(code)
        }
    }

    /**
     * 保存ServiceImpl
     */
    def saveServiceImpl(targetDir){
//          def mkdir_controller = "mkdir " + targetDir + "/service/impl"
//          println mkdir_controller.execute().text

        this.model_service_impl_file_dict.each { model, code ->
            def new_file = new File(targetDir + '/service/impl/' + model + 'ServiceImpl.java')
            new_file.write(code)
        }
    }

    /**
     * 保存VO
     */
//    def saveVO(targetDir){
////          def mkdir_controller = "mkdir " + targetDir + "/vo"
////        println mkdir_controller.execute().text
//
//        this.model_vo_file_dict.each { model, code ->
//            def new_file = new File(targetDir + '/vo/' + model + 'VO.java')
//            new_file.write(code)
//        }
//    }

    /**
     * 执行转换的入口。table_column_infos为parser里的一个Property
     * @param table_column_infos
     */
    def run(table_column_infos){
        this.table_column_infos = table_column_infos
        table_column_infos.each { k, v ->
            this.generate_controller(k, v["columns"],v["name"],v["description"])
            this.generate_service(k, v["columns"])
            this.generate_service_impl(k, v["columns"])
//            this.generate_vo_impl(k, v["columns"])
        }

        this.saveController(this.targetDir)
        this.saveService(this.targetDir)
        this.saveServiceImpl(this.targetDir)
//        this.saveVO(this.targetDir)
    }
}

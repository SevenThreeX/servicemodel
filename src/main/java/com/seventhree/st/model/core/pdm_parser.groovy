package com.seventhree.st.model.core

import com.seventhree.st.Utils as utils

import groovy.json.JsonOutput

import java.security.MessageDigest
import java.util.regex.Matcher
import java.util.regex.Pattern

class PdmParser {
    String file  // pdm文件路径
    def parser
    def definitions = [:]    // 所有生成的definitions
    def table_column_infos = [:]    // 结构类似definitions，但是包含很多swagger definitions不支持的属性
    def paths = [:]          //
    def id_definition_map = [:]  // 记录pdm中的Id与definition的映射，例如{'o10': 'MyTable'}
    def table_tags = [:]     // 记录表格被哪些Diagram包含了。每个Diagram生成一个tag

    def tables
    def references
    def diagrams

    private static Pattern linePattern = Pattern.compile("_(\\w)");
    /**下划线转驼峰*/
    public static String lineToHump(String str){
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while(matcher.find()){
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    def generate_definitions(tables){
        println 'Generate definitions'
        tables.children().each { table ->
            def model_name = new utils().capitalizeTableName(table['a:Code'].text())    // 表格英文名

            // Table相关信息
            this.definitions[model_name] = [
                    "type": "object",
                    "description": this.get_table_description(table),
                    "properties": [:]
            ]

            // 字段相关信息
            table['c:Columns'].each{col ->
                col.children().each(){
                    this.definitions[model_name]['properties'][it['a:Code'].text()] = [
                            "type": new utils().get_datatype(it['a:DataType'].text()),
                            "description": this.get_column_description(it),
                    ]
                }
            }
        }
        return this.definitions
    }

    def get_column_description(node_column){
        return "<br/>\n" + node_column['a:Name'].text() + "。<br/>\n" + node_column['a:Comment'].text() + "<br/>\n"
    }

    def get_table_description(node_table){
        return '<br/>\n' + node_table['a:Name'].text() + '。<br/>\n' + node_table['a:Comment'].text() + "<br/>\n"
    }

    def get_strike_case(CamelCase){
        return CamelCase.replaceAll(/\B[A-Z]/) { '_' + it }.toLowerCase()
    }

    def get_underscore_case(CamelCase){
        return CamelCase.replaceAll(/\B[A-Z]/) { '_' + it }.toLowerCase()
    }

    /**
     * 保存更多的Table和Column的属性
     * */
    def generate_table_column_infos(tables) {
        println 'Generate table_column_infos'
        tables.children().each { table ->
            def model_name = new utils().capitalizeTableName(table['a:Code'].text().toLowerCase())    // 表格英文名code
            def name = new utils().capitalizeTableName(table['a:Name'].text())    // 表格name
            // Table相关信息
            this.table_column_infos[model_name] = [
                    "type"      : "object",
                    "description": this.get_table_description(table),
                    "columns": [:],
                    "name":name,
                    "id": table.@Id
            ]

            // 字段相关信息
            table['c:Columns'].each { col ->
                col.children().each() {
                    def required = false
                    if(it['a:Mandatory']){
                        required = true
                    }
                    this.table_column_infos[model_name]['columns'][it['a:Code'].text().toLowerCase()] = [
                            "type": it['a:DataType'].text().toLowerCase(),
                            "description": this.get_column_description(it),
                            //"code": it['a:Code'].text().toLowerCase(),
                            //"name": it['a:Name'].text(),
                            "name": lineToHump(it['a:Code'].text().toLowerCase()),
                            "required": required,
                            "id": it.@Id
                    ]
                }
            }
        }
    }

    def get_id_definition_map(){
        this.tables.each { table ->
            def model_name = new utils().capitalizeTableName(table['a:Code'].text())    // 表格英文名
            def table_id = table.@Id
            this.id_definition_map.put(table_id, model_name)
        }
    }

    def generate_table_tags(){
        this.diagrams.each{diagram ->
            def diagram_name = diagram['a:Name'].text()
            def symbols = diagram['c:Symbols'][0]

            symbols.each{symbol ->
//                println symbol.name()
                if(symbol.name().toString() == '{object}TableSymbol'){
                    symbol['c:Object'][0].each{refTable ->
                        if(! this.table_tags.containsKey(this.id_definition_map[refTable.@Ref])){
                            this.table_tags[this.id_definition_map[refTable.@Ref]] = [diagram_name]
                        }
                        else{
                            this.table_tags[this.id_definition_map[refTable.@Ref]].add(diagram_name)
                        }
                    }
                }
            }
        }
    }

    /**
     * 解析文件，生成解析后的Swagger JSON格式
     * */
    def parse() {
        println "parse file: " + this.file
        this.parser = new XmlParser().parse(this.file)
        def model = this.parser.children()[0].children()[0].children()[0]

        model.each{
            def name = it.name().toString()
            if(name == '{collection}Tables'){
                this.tables = it
            }

            if(name == '{collection}References'){
                this.references = it
            }

            if(name == '{collection}PhysicalDiagrams'){
                this.diagrams = it
            }
        }

        this.get_id_definition_map()
//        this.definitions = this.generate_definitions(this.tables)
        this.generate_table_column_infos(this.tables)
        this.generate_table_tags()
    }

    def get_column_infos(tables, table_columns) {
        println 'Generate table_column_infos'
        tables.children().each { table ->
            // Table相关信息
            table_columns[table['a:Code'].text()] = [
                    "display_name": table['a:Name'].text(),
                    "name": table['a:Code'].text(),
                    "columns": [:],
            ]

            // 字段相关信息
            table['c:Columns'].each { col ->
                col.children().each() {
                    table_columns[table['a:Code'].text()]['columns'][it['a:Code'].text()] = [
                            "display_name": it['a:Name'].text(),
                            "name": it['a:Code'].text(),
                    ]
                }
            }
        }

        return table_columns
    }
}



def MD5(String s){
    md5 = MessageDigest.getInstance("MD5").digest(s.bytes).encodeHex().toString()
    md5[0..8] + '-' + md5[8..12] + '-' + md5[12..16] + '-' + md5[16..31]
}

//def file = '/Users/hm20160509/Desktop/pt/1-working/20160929证件防伪/5.workspaces/design/数据库-20161011/证件防伪15.pdm'
def file = '/Users/hxgqh/GitBook/Library/hxgqh/seektruth/files/证件防伪.pdm'
def p = new PdmParser(file: file)
p.parse()
def json = JsonOutput.toJson(p.table_column_infos)
println JsonOutput.prettyPrint(json)

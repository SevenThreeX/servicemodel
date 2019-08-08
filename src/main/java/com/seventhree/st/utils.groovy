package com.seventhree.st

class Utils {
    def capitalizeTableName(String name){
        def new_name = ''
        name.split('_').each{
            new_name += it.capitalize()
        }
        return new_name
    }

    def get_datatype(String db_datatype){
        if(db_datatype.toUpperCase().contains('INT')){
            return 'integer'
        }
        else{
            return 'string'
        }
    }

    def underscore2camelCase(String name){
        def new_name = ''
        name.split('_').each{
            new_name += it.capitalize()
        }
        return new_name
    }

    def underscore2UpperCamelCase(String name){
        this.underscore2camelCase(name).capitalize()
    }

    def UpperCamelCase2underscore(String UpperCamelCase){
        return UpperCamelCase.replaceAll(/\B[A-Z]/) { '_' + it }.toLowerCase()
    }

    def UpperCamelCase2camelCase(String UpperCamelCase){
        def tmp = UpperCamelCase.toCharArray()
        tmp[0] = tmp[0].toLowerCase()
        return tmp.join('')
    }
}

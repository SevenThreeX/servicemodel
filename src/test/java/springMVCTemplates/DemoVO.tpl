package com.haomostudio.${pageName}.vo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.haomostudio.${pageName}.po.${model};

import java.util.Date;

public class ${model}VO{
    @JsonProperty("includes")
    private Map<String, Object> includes;

    @JsonProperty("refers")
    private Map<String, List<Object>> refers;

    @JsonProperty("relates")
    private Map<String, List<Object>> relates;

    private ${model} superior;

    public ${model} getSuperior() {
        return superior;
    }

    public ${model}VO(${model} superior){
        this.superior = superior;
        this.includes = new HashMap<>();
        this.refers = new HashMap<>();
        this.relates = new HashMap<>();
    }

    public void setSuperior(${model} superior) {
        this.superior = superior;
    }

    public void setIncludes(Map<String, Object>includes){
        this.includes = includes;
    }

    public Map<String, Object> getIncludes(){
        return this.includes;
    }

    public void setRefers(Map<String, List<Object>> refers){
        this.refers = refers;
    }

    public Map<String, List<Object>> getRefers(){
        return this.refers;
    }

    public void setRelates(Map<String, List<Object>> relates){
        this.relates = relates;
    }

    public Map<String, List<Object>> getRelates(){
        return this.relates;
    }
}

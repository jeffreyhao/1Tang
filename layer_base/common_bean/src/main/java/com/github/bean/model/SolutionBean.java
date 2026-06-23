package com.github.bean.model;

public class SolutionBean {
    private int solutionId;
    private Integer sort;
    private String id;
    private String code;
    private String name;
    private String intro;
    private String description;
    private int type;

    private int is_vip;

    public SolutionBean(){

    }

    public SolutionBean(int solutionId, Integer sort, String id, String code, String name,
                        String intro, String description, int type) {
        this.solutionId = solutionId;
        this.sort = sort;
        this.id = id;
        this.code = code;
        this.name = name;
        this.intro = intro;
        this.description = description;
        this.type = type;
    }

    public SolutionBean(int solutionId, Integer sort, String id, String code, String name,
                        String intro, String description, int type, int is_vip) {
        this.solutionId = solutionId;
        this.sort = sort;
        this.id = id;
        this.code = code;
        this.name = name;
        this.intro = intro;
        this.description = description;
        this.type = type;
        this.is_vip = is_vip;
    }

    public int getSolutionId() {
        return solutionId;
    }

    public void setSolutionId(int solutionId) {
        this.solutionId = solutionId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public int getIs_vip() {
        return is_vip;
    }

    public void setIs_vip(int is_vip) {
        this.is_vip = is_vip;
    }

    public int is_vip(){
        return is_vip;
    }
}

package cn.com.wishtoday.model;

import java.io.Serializable;

public class StudentModel implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;

    private String code;

    private String name;

    private Integer sex;

    private Integer age;

    private String political;

    private String origin;

    private String professional;



    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSex() {
        return this.sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return this.age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPolitical() {
        return this.political;
    }

    public void setPolitical(String political) {
        this.political = political;
    }

    public String getOrigin() {
        return this.origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getProfessional() {
        return this.professional;
    }

    public void setProfessional(String professional) {
        this.professional = professional;
    }

}
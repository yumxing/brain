package cn.com.wishtoday.model;

public class CollegeModel {
    private int id;
    private String collegeCode;
    private String collegeName;
    private int collegePublic;
    private String collegeCity;
    private String collegeCategory;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCollegeCode() {
        return collegeCode;
    }

    public void setCollegeCode(String collegeCode) {
        this.collegeCode = collegeCode;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public int getCollegePublic() {
        return collegePublic;
    }

    public void setCollegePublic(int collegePublic) {
        this.collegePublic = collegePublic;
    }

    public String getCollegeCity() {
        return collegeCity;
    }

    public void setCollegeCity(String collegeCity) {
        this.collegeCity = collegeCity;
    }

    public String getCollegeCategory() {
        return collegeCategory;
    }

    public void setCollegeCategory(String collegeCategory) {
        this.collegeCategory = collegeCategory;
    }
}

package cn.com.wishtoday.model;

public class CollegeModel {
    private Integer id;
    private String collegeCode;
    private String collegeName;
    private Integer collegePublic;
    private String collegeCity;
    private String collegeCategory;

    public CollegeModel() {
    }

    @Override
    public String toString() {
        return "CollegeModel{" +
                "id=" + id +
                ", collegeCode='" + collegeCode + '\'' +
                ", collegeName='" + collegeName + '\'' +
                ", collegePublic=" + collegePublic +
                ", collegeCity='" + collegeCity + '\'' +
                ", collegeCategory='" + collegeCategory + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Integer getCollegePublic() {
        return collegePublic;
    }

    public void setCollegePublic(Integer collegePublic) {
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

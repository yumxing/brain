package cn.com.wishtoday.model;

public class PersonModel {
    private String appUsername;
    private int age;
    private String address;

    // getters and setters...

    public String getAppUsername() {
        return appUsername;
    }

    public void setAppUsername(String appUsername) {
        this.appUsername = appUsername;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Person{" +
                "appUsername='" + appUsername + '\'' +
                ", age=" + age +
                ", address='" + address + '\'' +
                '}';
    }
}

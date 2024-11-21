package cn.com.wishtoday.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * sql排序字段类
 */
public class SortersModel {
    @JsonProperty("property")
    private String property;
    @JsonProperty("direction")
    private String direction;

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}

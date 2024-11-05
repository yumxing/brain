package cn.com.wishtoday.model;

import java.io.Serializable;
import java.util.List;

public class Page implements Serializable {
    private static final long serialVersionUID = 1L;

    private int totalCount;
    private List<?> result;

    public Page(int totalCount, List<?> result) {
        this.totalCount = totalCount;
        this.result = result;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<?> getResult() {
        return result;
    }

    public void setResult(List<?> result) {
        this.result = result;
    }
}

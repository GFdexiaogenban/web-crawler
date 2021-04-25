package house.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class HouseTrendInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;// 编号
    private String houseName;
    private String trendTitle;
    private String trendTime;
    private String contents;
    private String directory;
    private String trendUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public String getTrendTitle() {
        return trendTitle;
    }

    public void setTrendTitle(String trendTitle) {
        this.trendTitle = trendTitle;
    }

    public String getTrendTime() {
        return trendTime;
    }

    public void setTrendTime(String trendTime) {
        this.trendTime = trendTime;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getTrendUrl() {
        return trendUrl;
    }

    public void setTrendUrl(String trendUrl) {
        this.trendUrl = trendUrl;
    }
}

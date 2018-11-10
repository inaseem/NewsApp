package ali.naseem.newsapp.models;

public class News {
    private String sectionName;
    private String webUrl;
    private String webPublicationDate;
    private String webTitle;

    public News(String webTitle, String sectionName, String webUrl, String webPublicationDate) {
        this.sectionName = sectionName;
        this.webUrl = webUrl;
        this.webPublicationDate = webPublicationDate;
        this.webTitle = webTitle;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getWebPublicationDate() {
        return webPublicationDate;
    }

    public String getWebTitle() {
        return webTitle;
    }
}

package ali.naseem.newsapp.models;

public class News {
    private String sectionName;
    private String webUrl;
    private String webPublicationDate;
    private String webTitle;
    private String author;

    public News(String webTitle, String sectionName, String webUrl, String webPublicationDate, String author) {
        this.sectionName = sectionName;
        this.webUrl = webUrl;
        this.webPublicationDate = webPublicationDate;
        this.webTitle = webTitle;
        this.author = author;
    }

    public String getAuthor() {
        return author;
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
